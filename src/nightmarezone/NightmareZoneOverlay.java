/*
 * Copyright (c) 2019, Hermetism <https://github.com/Hermetism>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.nightmarezone;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.Skill;
import net.runelite.api.Varbits;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.StackFormatter;

import javax.inject.Inject;
import java.awt.*;

import static net.runelite.client.plugins.helpers.HelperColor.*;
import static net.runelite.client.plugins.helpers.HelperRegion.isMoving;
import static net.runelite.client.plugins.helpers.HelperThread.isBusy;
import static net.runelite.client.plugins.helpers.HelperWidget.shouldStop;
import static net.runelite.client.plugins.nightmarezone.NightmareZonePlugin.*;

class NightmareZoneOverlay extends Overlay {

    private final Client client;
    private final NightmareZoneConfig config;
    private final NightmareZonePlugin plugin;
    private final InfoBoxManager infoBoxManager;
    private final ItemManager itemManager;

    private AbsorptionCounter absorptionCounter;
    private AbsorptionCounter hitpointsCounter;

    private final PanelComponent panelComponent = new PanelComponent();

    static int hitpoints;

    @Inject
    NightmareZoneOverlay(Client client, NightmareZoneConfig config, NightmareZonePlugin plugin, InfoBoxManager infoBoxManager, ItemManager itemManager) {
        setPosition(OverlayPosition.TOP_LEFT);
        setPriority(OverlayPriority.LOW);
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        this.infoBoxManager = infoBoxManager;
        this.itemManager = itemManager;
    }

    @Override
    public Dimension render(Graphics2D graphics) {

        Widget nmzWidget = client.getWidget(WidgetInfo.NIGHTMARE_ZONE);
        if (nmzWidget != null) {
            nmzWidget.setHidden(true);
        }
        Color c;

        hitpoints = client.getBoostedSkillLevel(Skill.HITPOINTS);

        panelComponent.getChildren().clear();
        if (isInRenderArea(client)) {

            /* Rock Cake HP Counter  */
            renderHitpointsCounter();

            if (TickWeStartedOn != -1 && isInNightmareZone(client) && TickWeStartedOn + 6 < client.getTickCount()) {

                /* Run Tasks */
                NightmareZoneTaskMan.runTasks(client, config);

                /* Absorption Pot Counter */
                renderAbsorptionCounter();

                if (NightmareZoneTaskMan.map.get("rockcake")) {
                    c = cRed;
                } else {
                    c = cBlue;
                }
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("Rock Cake").color(c)
                        .build());


                if (NightmareZoneTaskMan.map.get("absorb")) {
                    c = cRed;
                } else {
                    c = cBlue;
                }
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("Absorption").color(c)
                        .build());

            }


            c = cBlue;
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Points: ").leftColor(c)
                    .right(StackFormatter.formatNumber(client.getVar(Varbits.NMZ_POINTS)))
                    .build());


            if (isBusy()) {
                c = cRed;
            } else {
                c = cBlue;
            }
            if (shouldStop) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("Stopped!")
                        .color(cDarkOrange)
                        .build());
            } else if (isBusy()) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("Busy!")
                        .color(c)
                        .build());
            } else {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("Not Busy..")
                        .color(c)
                        .build());
            }


            if (isMoving) {
                c = cRed;
            } else {
                c = cBlue;
            }
            if (isMoving) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("Moving").color(c)
                        .build());
            }
        }
        return panelComponent.render(graphics);
    }

    private void renderAbsorptionCounter() {
        int absorptionPoints = client.getVar(Varbits.NMZ_ABSORPTION);
        if (absorptionPoints == 0) {
            if (absorptionCounter != null) {
                removeAbsorptionCounter();
                absorptionCounter = null;
            }
        } else {
            if (absorptionCounter == null) {
                addAbsorptionCounter(absorptionPoints);
            } else {
                absorptionCounter.setCount(absorptionPoints);
            }
        }
    }

    private void renderHitpointsCounter() {
        if (hitpoints != 0 && hitpointsCounter == null) {
            addHitPointsCounter();
        } else {
            hitpointsCounter.setCount(hitpoints);
        }
    }


    private void addAbsorptionCounter(int startValue) {
        absorptionCounter = new AbsorptionCounter(itemManager.getImage(ItemID.ABSORPTION_4), plugin, startValue, config.absorptionThreshold(), "Absorption: ");
        absorptionCounter.setAboveThresholdColor(config.absorptionColorAboveThreshold());
        absorptionCounter.setBelowThresholdColor(config.absorptionColorBelowThreshold());
        infoBoxManager.addInfoBox(absorptionCounter);
    }

    private void addHitPointsCounter() {
        hitpointsCounter = new AbsorptionCounter(itemManager.getImage(ItemID.DWARVEN_ROCK_CAKE), plugin, hitpoints, config.hitpointsThreshold(), "Hitpoints: ");
        hitpointsCounter.setAboveThresholdColor(config.absorptionColorBelowThreshold());
        hitpointsCounter.setBelowThresholdColor(config.absorptionColorAboveThreshold());
        infoBoxManager.addInfoBox(hitpointsCounter);
    }

    void removeHitPointsCounter() {
        infoBoxManager.removeInfoBox(hitpointsCounter);
        hitpointsCounter = null;
    }

    void removeAbsorptionCounter() {
        infoBoxManager.removeInfoBox(absorptionCounter);
        absorptionCounter = null;
    }

    public void updateConfig() {
        if (absorptionCounter != null) {
            absorptionCounter.setAboveThresholdColor(config.absorptionColorAboveThreshold());
            absorptionCounter.setBelowThresholdColor(config.absorptionColorBelowThreshold());
            absorptionCounter.setThreshold(config.absorptionThreshold());

            hitpointsCounter.setAboveThresholdColor(config.absorptionColorAboveThreshold());
            hitpointsCounter.setBelowThresholdColor(config.absorptionColorBelowThreshold());
            hitpointsCounter.setThreshold(config.absorptionThreshold());
        }
    }
}
