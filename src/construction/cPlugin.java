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
import net.runelite.client.plugins.helpers.*;
import net.runelite.client.plugins.xptracker.XpTrackerPlugin;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

@PluginDescriptor(
        name = "∞ Larder Maker",
        description = "A Larder Maker",
        tags = {"npcs", "players", "projectiles"},
        enabledByDefault = false
)
@PluginDependency(XpTrackerPlugin.class)
public class cPlugin extends Plugin {

    @Inject
    private KeyManager keyManager;

    @Inject
    private cMain gMain;

    @Inject
    private cOverlayMenu gOverlayMenu;

    @Inject
    private HelperWidget overlaytwo;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private ConfigManager configManager;

    @Inject
    private cInputListener listener;

    @Inject
    private HelperTransform helperTransform;
    @Inject
    private HelperThread helperThread;
    @Inject
    private HelperWidget helperWidget;
    @Inject
    private HelperInput helperInput;
    @Inject
    private HelperFind helperFind;
    @Inject
    private HelperDelay helperDelay;
    @Inject
    private HelperColor helperColor;
    @Inject
    private HelperBank helperBank;
    @Inject
    private HelperRegion helperRegion;
    @Inject
    private HelperInventory helperInventory;

    @Getter(AccessLevel.PACKAGE)
    private WorldPoint lastPlayerLocation;

    @Provides
    cConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(cConfig.class);
    }

    @Override
    protected void startUp() {
        keyManager.registerKeyListener(listener);
        overlayManager.add(overlaytwo);
        overlayManager.add(gMain);
        overlayManager.add(gOverlayMenu);
        configManager.setConfiguration("c", "cAltarBuiltIn", false);
        net.runelite.client.plugins.construction.cItems.initItems();
        HelperWidget.shouldStop = true;
    }

    @Override
    protected void shutDown() throws Exception {
        HelperWidget.shouldStop = true;


        configManager.setConfiguration("c", "cAltarBuiltIn", false);
        keyManager.unregisterKeyListener(listener);

        overlayManager.remove(gMain);
        overlayManager.remove(overlaytwo);
        overlayManager.remove(gOverlayMenu);
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {

        if (event.getGameState() == GameState.LOGIN_SCREEN ||
                event.getGameState() == GameState.HOPPING) {
            lastPlayerLocation = null;
        }

    }

}
