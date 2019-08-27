/*
 * Copyright (c) 2019, Hermetism <https://github.com/Hermetism>
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
package net.runelite.client.plugins.ourania;

import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.KeyListener;
import net.runelite.client.plugins.helpers.HelperWidget;
import net.runelite.client.plugins.xptracker.XpTrackerService;

import javax.inject.Inject;
import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.VK_HOME;

public class OuraniaInputListener implements KeyListener {

    private final ConfigManager configManager;
    private final OuraniaConfig config;
    private final XpTrackerService xpTrackerService;
    private final OuraniaPlugin plugin;
    private final Client client;

    @Inject
    public OuraniaInputListener(OuraniaConfig ouraniaConfig, ConfigManager configManager, XpTrackerService xpTrackerService, OuraniaPlugin ouraniaPlugin, Client client) {
        this.xpTrackerService = xpTrackerService;
        this.config = ouraniaConfig;
        this.configManager = configManager;
        this.plugin = ouraniaPlugin;
        this.client = client;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_END) {
            configManager.setConfiguration("ourania", "OuraniaAltarBuiltIn", !config.RunAltarBuiltIn());
            HelperWidget.shouldStop = !config.RunAltarBuiltIn();

            if (!config.RunAltarBuiltIn()) {

                plugin.Reset();
            }
        }
        if (e.getKeyCode() == VK_HOME) {
            config.laps(0);
            xpTrackerService.resetSkillState(Skill.RUNECRAFT);
            OuraniaOverlay.actions = 0;
            plugin.Reset();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}

