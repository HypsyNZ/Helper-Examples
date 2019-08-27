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

import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuEntry;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.callback.ClientThread;
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
import java.time.Duration;
import java.time.Instant;

import static net.runelite.client.plugins.helpers.HelperTransform.searchIndex;

@PluginDescriptor(
        name = "∞ Master Farmer",
        description = "Master Farmer",
        tags = {"npcs", "players", "projectiles"},
        enabledByDefault = false
)
@PluginDependency(XpTrackerPlugin.class)
public class MasterFarmPlugin extends Plugin {
    private static final Duration WAIT = Duration.ofSeconds(5);
    private static final int MAX_ACTOR_VIEW_RANGE = 20;
    static boolean masterFarmer = false;

    @Inject
    private Client client;

    @Inject
    private MasterFarmConfig config;

    @Inject
    private KeyManager keyManager;

    @Inject
    private MasterFarmOverlay overlay;

    @Inject
    private MasterFarmOverlayTwo overlayTwo;

    @Inject
    private xoWidget overlaytwo;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private ConfigManager configManager;

    @Inject
    private ClientThread clientThread;

    @Inject
    private MasterFarmInputListener listener;

    @Inject
    private xoFindNPC masterFarmFindNPC;

    @Inject
    private xoFindTiles masterFarmFindTiles;

    @Inject
    private xoBank masterFarmBank;

    @Inject
    private xoInventory masterFarmInventory;

    @Getter(AccessLevel.PACKAGE)
    private Actor lastOpponent;

    private Instant lastTime;

    /**
     * The time when the last game tick event ran.
     */
    @Getter(AccessLevel.PACKAGE)
    private Instant lastTickUpdate;
    /**
     * The players location on the last game tick.
     */
    @Getter(AccessLevel.PACKAGE)
    private WorldPoint lastPlayerLocation;

    @Provides
    MasterFarmConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(MasterFarmConfig.class);
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        lastTickUpdate = Instant.now();
        lastPlayerLocation = client.getLocalPlayer().getWorldLocation();

        if (lastOpponent != null
                && lastTime != null
                && client.getLocalPlayer().getInteracting() == null) {
            if (Duration.between(lastTime, Instant.now()).compareTo(WAIT) > 0) {
                lastOpponent = null;
            }
        }
    }

    @Subscribe
    public void onInteractingChanged(InteractingChanged event) {

        if (event.getSource() != client.getLocalPlayer()) {
            return;
        }

        Actor opponent = event.getTarget();

        if (opponent == null) {
            lastTime = Instant.now();
            return;
        }

        lastOpponent = opponent;

    }

    static MenuEntryAdded eventMenu;

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {


        if (client.getGameState() != GameState.LOGGED_IN) {
            return;
        }

        eventMenu = event;
        MenuEntry[] entries = client.getMenuEntries();
        client.getMenuEntries();

        swapWalk(entries, "trade");
        swapWalk(entries, "talk");
        swapWalk(entries, "steal-from");
        swapWalk(entries, "steal from");
    }


    private void swapWalk(MenuEntry[] entries, String optionA) {

        int idxA = searchIndex(entries, optionA);
        int idxB = searchIndex(entries, "walk");

        if (idxA == -1 || idxB == -1) {
            return;
        }

        for (int i = entries.length - 1; i >= 0; i--) {

            MenuEntry entry = entries[idxA];
            entries[idxA] = entries[idxB];
            entries[idxB] = entry;
        }

        client.setMenuEntries(entries);


    }


    @Override
    protected void startUp() {
        keyManager.registerKeyListener(listener);
        overlayManager.add(overlaytwo);
        overlayManager.add(overlay);
        overlayManager.add(overlayTwo);
        configManager.setConfiguration("MasterFarm", "MasterFarmAltarBuiltIn", false);

        xoItems.initItems();
        HelperWidget.shouldStop = true;

        updateConfig();
    }

    @Override
    protected void shutDown() throws Exception {
        HelperWidget.shouldStop = true;

        lastOpponent = null;
        lastTime = null;
        configManager.setConfiguration("MasterFarm", "MasterFarmAltarBuiltIn", false);
        keyManager.unregisterKeyListener(listener);

        overlay.ToggleNPCoff = false;
        overlay.NeedFood = false;
        overlay.DepositBank = false;
        overlay.DoneBanking = false;
        overlay.stunned = false;
        overlay.discardSeeds = false;

        overlayManager.remove(overlay);
        overlayManager.remove(overlaytwo);
        overlayManager.remove(overlayTwo);

    }

    @Subscribe
    public void onConfigChanged(ConfigChanged configChanged) {
        if (!configChanged.getGroup().equals("MasterFarm")) {
            return;
        }

        updateConfig();

    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {

        if (event.getGameState() == GameState.LOGIN_SCREEN ||
                event.getGameState() == GameState.HOPPING) {


            lastPlayerLocation = null;
        }

    }

    private void updateConfig() {
        if (!config.RunAltarBuiltIn()) {

            // Overlay
            overlay.ToggleNPCoff = false;
            overlay.NeedFood = false;
            overlay.DepositBank = false;
            overlay.DoneBanking = false;
            overlay.stunned = false;
            overlay.discardSeeds = false;

//			masterFarmBank.ResetSession();
//			masterFarmInventory.ResetInventory();

        }
    }

}
