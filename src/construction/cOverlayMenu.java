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
package net.runelite.client.plugins.construction;

import net.runelite.api.Skill;
import net.runelite.client.plugins.helpers.HelperColor;
import net.runelite.client.plugins.helpers.HelperInventory;
import net.runelite.client.plugins.helpers.HelperThread;
import net.runelite.client.plugins.helpers.HelperWidget;
import net.runelite.client.plugins.xptracker.XpTrackerService;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

import static net.runelite.client.plugins.construction.cItems.cItemsList;
import static net.runelite.client.plugins.helpers.HelperColor.cBlue;
import static net.runelite.client.plugins.helpers.HelperColor.cDarkOrange;
import static net.runelite.client.util.StackFormatter.quantityToRSDecimalStack;

class cOverlayMenu extends Overlay {

    private final cConfig config;
    private final PanelComponent panelComponent = new PanelComponent();
    private XpTrackerService xpTrackerService;
    private HelperInventory helperInventory;

    @Inject
    public cOverlayMenu(cConfig config, XpTrackerService xpTrackerService, HelperInventory helperInventory) {
        setPosition(OverlayPosition.TOP_LEFT);
        this.config = config;
        this.xpTrackerService = xpTrackerService;
        this.helperInventory = helperInventory;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        Panel();

        cItemsList = helperInventory.CheckItemAvailableInInventory(cItemsList);

        Items();

        if (config.showExp()) {

            xpTracker();

        }


        if (config.showStats()) {

            runningStatus();

        }


        return panelComponent.render(graphics);
    }

    private void Panel() {
        panelComponent.getChildren().clear();
        panelComponent.setBorder(new Rectangle(2, 2, 2, 2));
        panelComponent.setGap(new Point(0, 2));
    }

    private void Items() {
        int[] logLocation = HelperInventory.InventoryItemXYbyID(8778, cItemsList);
        if (logLocation[0] == 0) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("No Planks!")
                    .color(cDarkOrange)
                    .build());
        }
    }

    private void runningStatus() {
        if (HelperWidget.furniture != null) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Menu")
                    .color(HelperColor.cGreen)
                    .build());
        }

        if (HelperWidget.shouldStop) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Stopped!")
                    .color(HelperColor.cDarkOrange)
                    .build());
        } else if (HelperThread.isBusy()) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Busy!")
                    .color(HelperColor.cGreen)
                    .build());
        } else {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Not Busy..")
                    .color(HelperColor.cYellow)
                    .build());
        }
    }


    int actionsMin;

    private void xpTracker() {
        int actions = xpTrackerService.getActions(Skill.CONSTRUCTION);

        if (actions > 0) {
            actionsMin = xpTrackerService.getActionsHr(Skill.CONSTRUCTION) / 60;
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("     ")
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Larders/Hr ")
                    .leftColor(cBlue)
                    .right(Integer.toString(xpTrackerService.getActionsHr(Skill.CONSTRUCTION)))
                    .rightColor(HelperColor.cGreen)
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Larders/Min ")
                    .leftColor(cBlue)
                    .right(Integer.toString(actionsMin))
                    .rightColor(HelperColor.cGreen)
                    .build());
            int ExpPerHour = xpTrackerService.getXpHr(Skill.CONSTRUCTION);
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Con XP/hr ")
                    .leftColor(cBlue)
                    .right(quantityToRSDecimalStack(ExpPerHour))
                    .rightColor(HelperColor.cGreen)
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("TTL Con ")
                    .leftColor(cBlue)
                    .right(xpTrackerService.getTimeRem(Skill.CONSTRUCTION))
                    .rightColor(HelperColor.cGreen)
                    .build());
        }
    }
}

