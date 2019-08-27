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
package net.runelite.client.plugins.masterfarmer;


import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.plugins.helpers.HelperWidget;
import net.runelite.client.plugins.xptracker.XpTrackerService;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

import static net.runelite.client.plugins.helpers.HelperColor.*;
import static net.runelite.client.plugins.helpers.HelperThread.isBusy;
import static net.runelite.client.plugins.helpers.HelperTransform.getValueOrDefault;
import static net.runelite.client.plugins.masterfarmer.MasterFarmOverlay.*;
import static net.runelite.client.plugins.masterfarmer.xoItems.*;
import static net.runelite.client.plugins.masterfarmer.xoWidget.fightingNPC;
import static net.runelite.client.plugins.masterfarmer.xoWidget.opponentName;
import static net.runelite.client.util.StackFormatter.quantityToRSDecimalStack;


class MasterFarmOverlayTwo extends Overlay {

    private final MasterFarmConfig config;
    private final Client client;
    private final PanelComponent panelComponent = new PanelComponent();
    private XpTrackerService xpTrackerService;

    @Inject
    public MasterFarmOverlayTwo(Client client, MasterFarmConfig config, XpTrackerService xpTrackerService) {
        setPosition(OverlayPosition.TOP_LEFT);
        this.client = client;
        this.config = config;
        this.xpTrackerService = xpTrackerService;
    }

    @Override
    public Dimension render(Graphics2D graphics) {

        // Render Panel
        int Hpoints = client.getBoostedSkillLevel(Skill.HITPOINTS);
        Panel(Hpoints);

        // Render Running Status'
        if (config.showStats()) {
            runningStatus();
        }

        // Render Items
        if (config.showItemsPanel()) {
            Items();
        }

        // Render XP Tracker
        if (config.showXPPanel()) {
            xpTracker();
        }

        return panelComponent.render(graphics);
    }

    private void Panel(int Hpoints) {
        panelComponent.getChildren().clear();
        panelComponent.setBorder(new Rectangle(2, 2, 2, 2));
        panelComponent.setGap(new Point(0, 2));

        panelComponent.getChildren().add(TitleComponent.builder()
                .text("     ")
                .color(cDarkOrange)
                .build());

        StringBuilder sbh = new StringBuilder();
        sbh.append("Hitpoints: ").append((Hpoints * 100 / client.getRealSkillLevel(Skill.HITPOINTS))).append("%");

        if (Hpoints * 100 / client.getRealSkillLevel(Skill.HITPOINTS) < 30) {

            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(sbh.toString())
                    .color(cDarkOrange)
                    .build());
        } else if (Hpoints * 100 / client.getRealSkillLevel(Skill.HITPOINTS) < 50) {

            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(sbh.toString())
                    .color(cOrange)
                    .build());
        } else if (Hpoints * 100 / client.getRealSkillLevel(Skill.HITPOINTS) < 65) {

            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(sbh.toString())
                    .color(cYellow)
                    .build());
        } else if (Hpoints * 100 / client.getRealSkillLevel(Skill.HITPOINTS) < 75) {

            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(sbh.toString())
                    .color(cGreen)
                    .build());
        } else {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(sbh.toString())
                    .color(cBlue)
                    .build());

        }

    }

    private void runningStatus() {
        StringBuilder cc = new StringBuilder();
        cc.append("H: ").append(healthSeedCurrent).append(" || D: ").append(discardSeedCurrent);
        panelComponent.getChildren().add(TitleComponent.builder()
                .text(cc.toString())
                .color(cGreen)
                .build());

        if (HelperWidget.shouldStop) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Stopped!")
                    .color(cDarkOrange)
                    .build());
        } else if (isBusy()) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Busy!")
                    .color(cGreen)
                    .build());
        } else {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Not Busy..")
                    .color(cYellow)
                    .build());
        }

        if (whyJavaStunned) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Stunned!")
                    .color(cRed)
                    .build());
        } else {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Not Stunned")
                    .color(cGreen)
                    .build());

        }

        if (fightingNPC) {
            String result = getValueOrDefault(opponentName, "Dead!");
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(result)
                    .color(cRed)
                    .build());
        } else {
            String result = getValueOrDefault(opponentName, "Not Fighting");
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(result)
                    .color(cGreen)
                    .build());
        }


        if (HelperWidget.bankDepositInventory.isHidden() || xoWidget.tab2x == 0 || xoWidget.MonkfishX == 0) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Bank Setup")
                    .color(cRed)
                    .build());
        }

    }

    private void Items() {
        int i2 = 0;
        while (i2 < ItemsList.size()) {

            int totalBank = ItemsList.get(i2).getFourth() <= 0 ? ItemsList.get(i2).getThird() : ItemsList.get(i2).getThird() - ItemsList.get(i2).getFourth();
            int totalR = ItemsList.get(i2).getFifth();

            if (totalBank > 0 || totalR > 0) {

                int total = totalR + totalBank;

                String itemName = ItemsList.get(i2).getFirst();
                String totalValue = quantityToRSDecimalStack(total);

                panelComponent.getChildren()
                        .add(LineComponent.builder()
                                .left(itemName)
                                .leftColor(cDarkOrange)
                                .right(totalValue)
                                .rightColor(cDarkOrange)
                                .build());
            }
            i2++;
        }

        if (valueTotalBank > 0 || valueTotalInventory > 0) {
            String totalTotal = quantityToRSDecimalStack(valueTotalBank + valueTotalInventory);
            if (valueTotalBank + valueTotalInventory > 0) {
                panelComponent.getChildren()
                        .add(LineComponent.builder()
                                .left("Total Value: ")
                                .right(totalTotal)
                                .rightColor(cBlue)
                                .build());
            }
        }
    }

    private void xpTracker() {
        int actions = xpTrackerService.getActions(Skill.THIEVING);
        if (actions > 0) {

            int actionsMin = xpTrackerService.getActionsHr(Skill.THIEVING) / 60;
            int actions4Min = actionsMin * 4;
            StringBuilder vaa = new StringBuilder();

            if (valueTotalBank > 0 || valueTotalInventory > 0) {
                int va = ((valueTotalBank + valueTotalInventory) * actions4Min / actions) * (xpTrackerService.getActionsHr(Skill.THIEVING) / actions4Min);
                String vaaa = quantityToRSDecimalStack(va);
                vaa.append((vaaa));

                if (va > 1) {
                    panelComponent.getChildren().add(LineComponent.builder()
                            .left("Est GP Per Hour")
                            .leftColor(cBlue)
                            .right((vaa.toString()))
                            .rightColor(cBlue)
                            .build());
                }
            }


            panelComponent.getChildren().add(LineComponent.builder()
                    .left("     ")
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Pockets/Hr")
                    .leftColor(cBlue)
                    .right(Integer.toString(xpTrackerService.getActionsHr(Skill.THIEVING)))
                    .rightColor(cBlue)
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Pockets/Min")
                    .leftColor(cBlue)
                    .right(Integer.toString(actionsMin))
                    .rightColor(cBlue)
                    .build());
            int ExpPerHour = xpTrackerService.getXpHr(Skill.THIEVING);
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("XP Per Hour")
                    .leftColor(cBlue)
                    .right(quantityToRSDecimalStack(ExpPerHour))
                    .rightColor(cBlue)
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Time to Level")
                    .leftColor(cBlue)
                    .right(xpTrackerService.getTimeRem(Skill.THIEVING))
                    .rightColor(cBlue)
                    .build());
        }
    }
}

