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
package net.runelite.client.plugins.glass;

import net.runelite.api.Skill;
import net.runelite.client.plugins.helpers.*;
import net.runelite.client.plugins.xptracker.XpTrackerService;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static net.runelite.client.plugins.glass.gItems.gItemsList;
import static net.runelite.client.plugins.helpers.HelperColor.cBlue;
import static net.runelite.client.plugins.helpers.HelperColor.cDarkOrange;
import static net.runelite.client.plugins.runepouch.RunepouchOverlay.AstralAmount;
import static net.runelite.client.util.StackFormatter.quantityToRSDecimalStack;

class gOverlayMenu extends Overlay {

    private final gConfig config;
    private final PanelComponent panelComponent = new PanelComponent();
    private XpTrackerService xpTrackerService;
    private HelperBank helperBank;

    @Inject
    public gOverlayMenu(gConfig config, XpTrackerService xpTrackerService, HelperBank helperBank) {
        setPosition(OverlayPosition.TOP_LEFT);
        this.config = config;
        this.xpTrackerService = xpTrackerService;
        this.helperBank = helperBank;
    }

    static long startTime = 0;
    static boolean started = false;

    @Override
    public Dimension render(Graphics2D graphics) {

        if (config.RunAltarBuiltIn() && !started) {
            started = true;
            startTime = System.currentTimeMillis();
        }

        if (!config.RunAltarBuiltIn() && started) {
            started = false;
            startTime = 0;
        }

        panelComponent.getChildren().clear();
        panelComponent.setBorder(new Rectangle(2, 2, 2, 2));
        panelComponent.setGap(new Point(0, 2));

        gItemsList = helperBank.CheckItemAvailableInBank(gItemsList);

        Items();

        if (config.showExp()) {
            xpTracker();
        }

        if (config.showStats()) {
            runningStatus();
        }

        return panelComponent.render(graphics);
    }

    private static int profit = 0;

    private void Items() {
        if (HelperRegion.regionString != null) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Region ")
                    .leftColor(cBlue)
                    .right(HelperRegion.regionString)
                    .rightColor(HelperColor.cGreen)
                    .build());
        }

        if (config.showGlassValue()) {
            int iv = HelperBank.bValueByID(1775, gItemsList);
            if (iv > 0) {
                String o = quantityToRSDecimalStack(HelperBank.bValueByID(1775, gItemsList), true);
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Glass Value ")
                        .leftColor(cBlue)
                        .right(o)
                        .rightColor(HelperColor.cGreen)
                        .build());
            }
        }

        if (actionsMin > 0 && config.showMatsRemaining()) {
            int sandDiv = HelperBank.bQuanByID(1783, gItemsList) / 13;
            long resourceTimeRemaining = sandDiv / actionsMin;
            String f = HelperTransform.convertTime(resourceTimeRemaining);
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Mat Left ")
                    .leftColor(cBlue)
                    .right(f)
                    .rightColor(HelperColor.cGreen)
                    .build());
        }

        if (config.showSessionTime()) {
            long elapsedTime;
            elapsedTime = System.currentTimeMillis() - startTime;
            DateFormat simple = new SimpleDateFormat("HH:mm:ss");
            simple.setTimeZone(TimeZone.getTimeZone("UTC"));

            if (started && elapsedTime > 0) {
                String f = simple.format(elapsedTime);
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Elapsed : ")
                        .leftColor(cBlue)
                        .right(f)
                        .rightColor(HelperColor.cGreen)
                        .build());

            }
        }


        if (config.useManualGuessProfit()) {

            double multi = 16.9 * config.moltenPrice();
            int astralcost = config.astralCost() * 2;
            int seaweedcost = config.seaweedCost() * 13;
            int sandcost = config.sandCost() * 13;

            profit = (int) multi - astralcost - seaweedcost - sandcost;

        }


        if (config.showProfitInPanel()) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Profit Per Cast ")
                    .leftColor(cBlue)
                    .right(Integer.toString(profit))
                    .rightColor(HelperColor.cGreen)
                    .build());
        }

        if (config.useBankFillers() && AstralAmount != 0 && config.showAstralsLeftInPouch()) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Astrals Pouch: ")
                    .leftColor(cBlue)
                    .right(Integer.toString(AstralAmount))
                    .rightColor(HelperColor.cGreen)
                    .build());

        }
    }

    private void runningStatus() {

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

        if (config.useBankFillers() && AstralAmount == 0) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Rune Pouch!")
                    .color(cDarkOrange)
                    .build());
        }

    }


    int actionsMin;

    private void xpTracker() {

        int actionsHr = xpTrackerService.getActionsHr(Skill.CRAFTING);
        StringBuilder vaa = new StringBuilder();
        int va;

        if (config.useManualGuessProfit()) {
            va = profit * actionsHr;
        } else {
            va = config.profitPerCast() * actionsHr;
        }

        String vaaa = quantityToRSDecimalStack(va);
        vaa.append((vaaa));

        if (va > 1) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("GP Per Hour ")
                    .leftColor(cBlue)
                    .right((vaa.toString()))
                    .rightColor(HelperColor.cGreen)
                    .build());
        }

        int actions = xpTrackerService.getActions(Skill.CRAFTING);

        if (actions > 0) {
            actionsMin = xpTrackerService.getActionsHr(Skill.CRAFTING) / 60;
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("     ")
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Casts/Hr ")
                    .leftColor(cBlue)
                    .right(Integer.toString(xpTrackerService.getActionsHr(Skill.CRAFTING)))
                    .rightColor(HelperColor.cGreen)
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Casts/Min ")
                    .leftColor(cBlue)
                    .right(Integer.toString(actionsMin))
                    .rightColor(HelperColor.cGreen)
                    .build());
            int ExpPerHour = xpTrackerService.getXpHr(Skill.CRAFTING);
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Crafting XP/hr ")
                    .leftColor(cBlue)
                    .right(quantityToRSDecimalStack(ExpPerHour))
                    .rightColor(HelperColor.cGreen)
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Level in ")
                    .leftColor(cBlue)
                    .right(xpTrackerService.getTimeRem(Skill.CRAFTING))
                    .rightColor(HelperColor.cGreen)
                    .build());
            int ExpPerHourM = xpTrackerService.getXpHr(Skill.MAGIC);
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Magic XP/hr ")
                    .leftColor(cBlue)
                    .right(quantityToRSDecimalStack(ExpPerHourM))
                    .rightColor(HelperColor.cGreen)
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Level in ")
                    .leftColor(cBlue)
                    .right(xpTrackerService.getTimeRem(Skill.MAGIC))
                    .rightColor(HelperColor.cGreen)
                    .build());
        }
    }
}

