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
package net.runelite.client.plugins.ourania;


import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.Varbits;
import net.runelite.client.plugins.helpers.HelperRegion;
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
import static net.runelite.client.plugins.helpers.HelperRegion.regionString;
import static net.runelite.client.plugins.helpers.HelperThread.isBusy;
import static net.runelite.client.plugins.helpers.HelperWidget.bankIsOpen;
import static net.runelite.client.plugins.helpers.HelperWidget.shouldStop;
import static net.runelite.client.plugins.ourania.OuraniaItemsBank.OuraniaBankItemsList;
import static net.runelite.client.plugins.ourania.OuraniaOverlay.*;
import static net.runelite.client.util.StackFormatter.quantityToRSDecimalStack;


class OuraniaOverlayMenu extends Overlay {
    private final OuraniaConfig config;
    private final Client client;
    private final XpTrackerService xpTrackerService;
    private final PanelComponent panelComponent = new PanelComponent();

    static String currentStatus = "";

    @Inject
    public OuraniaOverlayMenu(Client client, OuraniaConfig config, XpTrackerService xpTrackerService) {
        setPosition(OverlayPosition.TOP_LEFT);
        this.client = client;
        this.config = config;
        this.xpTrackerService = xpTrackerService;
    }

    @Override
    public Dimension render(Graphics2D graphics) {


        panelComponent.getChildren().clear();

        StringBuilder sbh = new StringBuilder();
        sbh.append("Hitpoints: ").append((Hpoints * 100 / client.getRealSkillLevel(Skill.HITPOINTS))).append("%");
        if (Hpoints * 100 / client.getRealSkillLevel(Skill.HITPOINTS) < config.teleAction()) {
            if (config.RunAltarAutoTele()) {

                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("Teleporting!")
                        .color(cRed)
                        .build());
            } else {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text(sbh.toString())
                        .color(cRed)
                        .build());
            }

        } else if (Hpoints * 100 / client.getRealSkillLevel(Skill.HITPOINTS) < 30) {

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

        StringBuilder sb = new StringBuilder();
        sb.append("Energy: ").append(client.getEnergy()).append("%");
        if (energy < 15) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(sb.toString())
                    .color(cRed)
                    .build());
        } else if (energy < 50) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(sb.toString())
                    .color(cDarkOrange)
                    .build());

        } else if (energy < 75) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(sb.toString())
                    .color(cYellow)
                    .build());

        } else {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(sb.toString())
                    .color(cBlue)
                    .build());

        }

        int p = prayer * 100 / client.getRealSkillLevel(Skill.PRAYER);
        StringBuilder sbp = new StringBuilder();
        sbp.append("Prayer: ").append((p)).append("%");
        if (p < 10) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(sbp.toString())
                    .color(cRed)
                    .build());
        } else if (p < 25) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(sbp.toString())
                    .color(cYellow)
                    .build());

        } else {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(sbp.toString())
                    .color(cBlue)
                    .build());
        }


        StringBuilder stamPots = new StringBuilder();
        stamPots.append("Stamina Doses: ").append((StamPotQuan));
        if (StamThree > 0 || StamFour > 0 || StamTwo > 0) {

            if (StamPotQuan > 100) {

                panelComponent.getChildren().add(TitleComponent.builder()
                        .text(stamPots.toString())
                        .color(cBlue)
                        .build());
            } else if (StamPotQuan > 50) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text(stamPots.toString())
                        .color(cGreen)
                        .build());

            } else if (StamPotQuan > 30) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text(stamPots.toString())
                        .color(cYellow)
                        .build());

            } else if (StamPotQuan > 5) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text(stamPots.toString())
                        .color(cDarkOrange)
                        .build());

            } else if (StamPotQuan < 5 && config.ShowPotWarning()) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("Need Stamina Pots!")
                        .color(cRed)
                        .build());
            }
        } else {
            if (config.ShowPotWarning()) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("Need Stamina Pots!")
                        .color(cRed)
                        .build());
            }
        }

        StringBuilder energyPots = new StringBuilder();
        energyPots.append("Energy Doses: ").append(EnergyPotQuan);

        if (EnergyThree > 0 || EnergyFour > 0 || EnergyTwo > 0) {

            if (EnergyPotQuan > 100) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text(energyPots.toString())
                        .color(cBlue)
                        .build());
            } else if (EnergyPotQuan > 50) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text(energyPots.toString())
                        .color(cGreen)
                        .build());
            } else if (EnergyPotQuan > 30) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text(energyPots.toString())
                        .color(cYellow)
                        .build());
            } else if (config.ShowPotWarning() && EnergyPotQuan > 5) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text(energyPots.toString())
                        .color(cDarkOrange)
                        .build());
            } else if (config.ShowPotWarning() && EnergyPotQuan < 5) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("Need Energy Pots!")
                        .color(cRed)
                        .build());
            }
        } else {
            if (config.ShowPotWarning()) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("Need Energy Pots!")
                        .color(cRed)
                        .build());
            }
        }

        if (config.ShowDegradeWarning()) {
            if (GiantSack) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("Giant Sack")
                        .color(cGreen)
                        .build());

            } else if (Degraded && !Cosmic) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("No Cosmics!")
                        .color(cOrange)
                        .build());
            } else if (Degraded) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("Degraded Sack")
                        .color(cRed)
                        .build());
            } else {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("No Sack")
                        .color(cRed)
                        .build());
            }
        }

        if (client.getVar(Varbits.RUN_SLOWED_DEPLETION_ACTIVE) == 0 && config.ShowStamBuffWarning()) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Need Stam Buff!")
                    .color(cYellow)
                    .build());
        }
        if (Hpoints * 100 / client.getRealSkillLevel(Skill.HITPOINTS) < config.foodAction() && config.ShowFoodWarning()) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Need to Eat!")
                    .color(cRed)
                    .build());

        }


        String progressString = "";
        if (Progress == 0) {
            progressString = "First Step";
        }

        if (Progress == 1) {
            progressString = "Second Step";
        }

        if (Progress == 2) {
            progressString = "Third Step";
        }

        if (regionString.equalsIgnoreCase("Inside ZMI")) {
            if (OuraniaOverlay.banker) {

                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("Next to Banker")
                        .color(cBlue)
                        .build());

            }
            if (!altar && !banker && !secondTileClick) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("On Altar Path")
                        .color(cBlue)
                        .build());
            }
            if (secondTileClick && !banker && !altar) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("Nearing Corner")
                        .color(cBlue)
                        .build());
            }
            if (altar && !altarClose) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("Nearing Altar")
                        .color(cBlue)
                        .build());
            }
            if (altarClose) {
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Altar")
                        .right(progressString)
                        .rightColor(cBlue)
                        .build());
            }
        } else if (regionString.equalsIgnoreCase("Outside ZMI")) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(regionString)
                    .color(cYellow)
                    .build());

        } else {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(regionString + " : " + HelperRegion.currentRegionID)
                    .color(cRed)
                    .build());
        }

        if (shouldStop) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Stopped!")
                    .color(cDarkOrange)
                    .build());
        } else if (!currentStatus.equalsIgnoreCase("")) {
            String[] parts = currentStatus.split(",");
            if (parts.length == 1) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text(currentStatus)
                        .color(cBlue)
                        .build());
            } else {
                panelComponent.getChildren().add(LineComponent.builder()
                        .left(parts[0])
                        .right(parts[1])
                        .rightColor(cBlue)
                        .build());
            }
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

        // Render Items
        Items();

        // XP Tracker
        xpTracker();

        return panelComponent.render(graphics);
    }

    private void xpTracker() {

        int lapsFromConfig = config.laps();

        if (HelperWidget.totalBankValue > 0 && actions > 0 && lapsFromConfig > 0) {

            int lapsHr = xpTrackerService.getActionsHr(Skill.RUNECRAFT) / 3;
            int vtt = HelperWidget.totalBankValue;
            StringBuilder vaa = new StringBuilder();
            int va = vtt / lapsFromConfig * lapsHr;
            String vaaa = quantityToRSDecimalStack(va);
            vaa.append((vaaa));

            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Est GP/hr")
                    .right((vaa.toString()))
                    .rightColor(cBlue)
                    .build());
        }

        if (HelperWidget.dialogOpen) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Talking to NPC!")
                    .color(Color.RED)
                    .build());
        }

        if (lapsFromConfig > 0) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Current Laps")
                    .right(Integer.toString(lapsFromConfig))
                    .rightColor(cBlue)
                    .build());
        }

        if (actions > 0) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Est Laps/hr")
                    .right(Integer.toString(xpTrackerService.getActionsHr(Skill.RUNECRAFT) / 3))
                    .rightColor(cBlue)
                    .build());
            int ExpPerHour = xpTrackerService.getXpHr(Skill.RUNECRAFT);
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("XP Per Hour")
                    .right(quantityToRSDecimalStack(ExpPerHour))
                    .rightColor(cBlue)
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Level in ")
                    .leftColor(cBlue)
                    .right(xpTrackerService.getTimeRem(Skill.RUNECRAFT))
                    .rightColor(cBlue)
                    .build());
        }
    }

    private void Items() {
        HelperWidget.totalBankValue = 0;
        if (config.ShowValueBankOnly() && !bankIsOpen) {
            return;
        }

        //TODO: CHANGE TO 0 IF REUSED
        int i3 = 19;
        while (i3 < OuraniaBankItemsList.size()) {
            HelperWidget.totalBankValue = HelperWidget.totalBankValue + OuraniaBankItemsList.get(i3).getFourth();
            i3++;
        }

        int i2 = 19;
        while (i2 < OuraniaBankItemsList.size()) {


            if (HelperWidget.totalBankValue > 0) {

                int totalBank = OuraniaBankItemsList.get(i2).getFourth();
                String itemName = OuraniaBankItemsList.get(i2).getFirst();
                String totalValue = quantityToRSDecimalStack(totalBank);

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

        if (HelperWidget.totalBankValue > 0) {
            String totalTotal = quantityToRSDecimalStack(HelperWidget.totalBankValue);
            if (HelperWidget.totalBankValue > 0) {
                panelComponent.getChildren()
                        .add(LineComponent.builder()
                                .left("Total Value: ")
                                .right(totalTotal)
                                .rightColor(cBlue)
                                .build());
            }
        }
    }

}


