/*
 * Copyright (c) 2017, Seth <Sethtroll3@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.woodcutting;

import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.widgets.Widget;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.helpers.*;
import net.runelite.client.plugins.xptracker.XpTrackerService;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static net.runelite.client.plugins.helpers.HelperColor.cBlue;
import static net.runelite.client.util.StackFormatter.quantityToRSDecimalStack;

class WoodcuttingOverlay extends Overlay {
    private final Client client;
    private final WoodcuttingPlugin plugin;
    private final WoodcuttingConfig config;
    private final ConfigManager configManager;
    private final XpTrackerService xpTrackerService;
    private final PanelComponent panelComponent = new PanelComponent();
    private HelperInventory helperInventory;

    private Notifier notifier;

    @Inject
    private WoodcuttingOverlay(Client client, WoodcuttingPlugin plugin, WoodcuttingConfig config, XpTrackerService xpTrackerService, ConfigManager configManager, HelperInventory helperInventory, Notifier notifier) {
        super(plugin);
        setPosition(OverlayPosition.TOP_LEFT);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        this.configManager = configManager;
        this.xpTrackerService = xpTrackerService;
        this.helperInventory = helperInventory;
        this.notifier = notifier;
    }

    static List<Octagon<String, Integer, Integer, Integer, Widget, Integer, Integer, Integer>> emptyList = new ArrayList<>();

    @Override
    public Dimension render(Graphics2D graphics) {
        HelperInput.HoldKeyListener(KeyEvent.VK_SHIFT);

        helperInventory.CheckItemAvailableInInventory(emptyList);

        panelComponent.getChildren().clear();

        if (HelperWidget.shouldStop) {
            configManager.setConfiguration("woodcutting", "aAltarBuiltIn", false);
        }

        if (config.RunAltarBuiltIn()) {

            /* Run Tasks */
            aTaskMan.runTasks(client, config, notifier, plugin);

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

        if (!config.showWoodcuttingStats()) {
            return null;
        }

        WoodcuttingSession session = plugin.getSession();
        if (session == null) {
            return null;
        }

        Axe axe = plugin.getAxe();
        if (axe != null && axe.getAnimId() == client.getLocalPlayer().getAnimation()) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Woodcutting")
                    .color(Color.GREEN)
                    .build());
        } else {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("NOT woodcutting")
                    .color(Color.RED)
                    .build());
        }

        int actions = xpTrackerService.getActions(Skill.WOODCUTTING);
        if (actions > 0) {
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Logs cut:")
                    .right(Integer.toString(actions))
                    .build());

            if (actions > 2) {
                int actionsMin = xpTrackerService.getActionsHr(Skill.WOODCUTTING) / 60;
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Logs/Hr ")
                        .leftColor(cBlue)
                        .right(Integer.toString(xpTrackerService.getActionsHr(Skill.WOODCUTTING)))
                        .rightColor(HelperColor.cGreen)
                        .build());
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Logs/Min ")
                        .leftColor(cBlue)
                        .right(Integer.toString(actionsMin))
                        .rightColor(HelperColor.cGreen)
                        .build());
                int ExpPerHour = xpTrackerService.getXpHr(Skill.WOODCUTTING);
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Woodcutting XP/hr ")
                        .leftColor(cBlue)
                        .right(quantityToRSDecimalStack(ExpPerHour))
                        .rightColor(HelperColor.cGreen)
                        .build());
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Level in ")
                        .leftColor(cBlue)
                        .right(xpTrackerService.getTimeRem(Skill.WOODCUTTING))
                        .rightColor(HelperColor.cGreen)
                        .build());
            }
        }

        return panelComponent.render(graphics);
    }

}