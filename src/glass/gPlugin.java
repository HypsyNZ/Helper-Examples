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

import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
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

@PluginDescriptor(
        name = "∞ Glass Plugin",
        description = "A Glass Plugin",
        tags = {"npcs", "players", "projectiles"},
        enabledByDefault = false
)
@PluginDependency(XpTrackerPlugin.class)
public class gPlugin extends Plugin {

    @Inject
    private gItems gItems;
    @Inject
    private KeyManager keyManager;
    @Inject
    private gMain gMain;
    @Inject
    private gOverlayMenu gOverlayMenu;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private ConfigManager configManager;
    @Inject
    private gInputListener listener;
    @Inject
    private gConfig gConfig;

    @Getter(AccessLevel.PACKAGE)
    private WorldPoint lastPlayerLocation;

    @Provides
    gConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(gConfig.class);
    }

    @Override
    protected void startUp() {

        keyManager.registerKeyListener(listener);
        overlayManager.add(gMain);
        overlayManager.add(gOverlayMenu);
        configManager.setConfiguration("g", "gAltarBuiltIn", false);
        net.runelite.client.plugins.glass.gItems.initItems();

        HelperWidget.disableAllHelperWidgets();
        HelperWidget.shouldStop = true;
        HelperWidget.WidgetBankingEnabled = true;
    }

    @Override
    protected void shutDown() throws Exception {

        HelperWidget.shouldStop = true;
        HelperWidget.enableAllHelperWidgets();

        configManager.setConfiguration("g", "gAltarBuiltIn", false);
        keyManager.unregisterKeyListener(listener);

        overlayManager.remove(gMain);
        overlayManager.remove(gOverlayMenu);

    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {

        if (event != null && (event.getGameState() == GameState.LOGIN_SCREEN ||
                event.getGameState() == GameState.HOPPING)) {

            lastPlayerLocation = null;
        }

    }


}
