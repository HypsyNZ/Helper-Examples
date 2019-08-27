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
package net.runelite.client.plugins.watervials;

import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.helpers.HelperWidget;
import net.runelite.client.plugins.xptracker.XpTrackerPlugin;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

import static net.runelite.client.plugins.watervials.vItems.initItems;

@PluginDescriptor(
        name = "∞ VialPack Plugin",
        description = "A VialPack Plugin",
        tags = {"npcs", "players", "projectiles"},
        enabledByDefault = false
)
@PluginDependency(XpTrackerPlugin.class)
public class vPlugin extends Plugin {

    @Inject
    private vItems vItems;
    @Inject
    private Client client;
    @Inject
    private KeyManager keyManager;
    @Inject
    private vMain vMain;
    @Inject
    private vOverlayMenu vOverlayMenu;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private ConfigManager configManager;
    @Inject
    private vInputListener listener;
    @Inject
    private vConfig vConfig;

    @Getter(AccessLevel.PACKAGE)
    private WorldPoint lastPlayerLocation;

    @Provides
    vConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(vConfig.class);
    }

    @Override
    protected void startUp() {

        keyManager.registerKeyListener(listener);
        overlayManager.add(vMain);
        overlayManager.add(vOverlayMenu);
        configManager.setConfiguration("v", "vAltarBuiltIn", false);

        initItems();
        HelperWidget.WidgetCombatEnabledFalse();
        HelperWidget.shouldStop = true;
    }


    @Override
    protected void shutDown() throws Exception {

        HelperWidget.shouldStop = true;
        HelperWidget.enableAllHelperWidgets();

        configManager.setConfiguration("v", "vAltarBuiltIn", false);
        keyManager.unregisterKeyListener(listener);

        overlayManager.remove(vMain);
        overlayManager.remove(vOverlayMenu);

    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {

        if (event.getGameState() == GameState.LOGIN_SCREEN ||
                event.getGameState() == GameState.HOPPING) {


            lastPlayerLocation = null;
        }

    }


}
