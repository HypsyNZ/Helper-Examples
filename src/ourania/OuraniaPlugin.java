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

import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
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
import java.time.Instant;

@PluginDescriptor(
        name = "∞ Ourania Altar",
        description = "Runs the Ourania ZMI Altar",
        tags = {"npcs", "players", "projectiles"},
        enabledByDefault = false
)
@PluginDependency(XpTrackerPlugin.class)
public class OuraniaPlugin extends Plugin {

    @Inject
    private Client client;
    @Inject
    private OuraniaConfig config;
    @Inject
    private KeyManager keyManager;
    @Inject
    private OuraniaOverlay overlay;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private ConfigManager configManager;
    @Inject
    private OuraniaInputListener ouraniaInputListener;
    @Inject
    private OuraniaOverlayMenu overlayMenu;


    @Getter(AccessLevel.PACKAGE)
    private Instant lastTickUpdate;
    @Getter(AccessLevel.PACKAGE)
    private WorldPoint lastPlayerLocation;

    public OuraniaPlugin() {
    }

    @Provides
    OuraniaConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(OuraniaConfig.class);
    }

    @Subscribe
    public void onGameTick(GameTick event) {
        lastTickUpdate = Instant.now();
        lastPlayerLocation = client.getLocalPlayer().getWorldLocation();
    }

    @Override
    protected void startUp() {
        OuraniaItemsBank.initItems();
        OuraniaItemsInv.initItems();
        overlayManager.add(overlay);
        overlayManager.add(overlayMenu);
        keyManager.registerKeyListener(ouraniaInputListener);
        configManager.setConfiguration("ourania", "OuraniaAltar", false);
        configManager.setConfiguration("ourania", "OuraniaAltarBuiltIn", false);
        client.setIsHidingEntities(true);
        updateConfig();

        HelperWidget.enableAllHelperWidgets();
        HelperWidget.WidgetCombatEnabledFalse();
    }

    @Override
    protected void shutDown() throws Exception {

        HelperWidget.enableAllHelperWidgets();
        HelperWidget.shouldStop = true;

        keyManager.unregisterKeyListener(ouraniaInputListener);

        configManager.setConfiguration("ourania", "OuraniaAltarBuiltIn", false);

        overlayManager.remove(overlay);
        overlayManager.remove(overlayMenu);

        Reset();

        client.setIsHidingEntities(false);

        client.setPlayersHidden(false);
        client.setPlayersHidden2D(false);

        client.setFriendsHidden(false);
        client.setClanMatesHidden(false);

        client.setLocalPlayerHidden(false);
        client.setLocalPlayerHidden2D(false);

        client.setNPCsHidden(false);
        client.setNPCsHidden2D(false);

        client.setAttackersHidden(false);

        client.setProjectilesHidden(false);

    }

    public void Reset() {
        OuraniaOverlay.NextToBanker = false;
        OuraniaOverlay.NeedLadder = false;
        OuraniaOverlay.NeedLadderPray = false;
        OuraniaOverlay.NeedDarkMage = false;
        OuraniaOverlay.NeedStam = false;
        OuraniaOverlay.NeedEnergy = false;
        OuraniaOverlay.NeedFood = false;
        OuraniaOverlay.NeedEss = false;
        OuraniaOverlay.DepositBank = false;
        OuraniaOverlay.altarClose = false;
        OuraniaOverlay.StepTwo = false;
        OuraniaOverlay.StepThree = false;
        OuraniaOverlay.Teleport = false;
        OuraniaOverlay.AltarPath = false;
        OuraniaOverlay.AltarStep = false;
        OuraniaOverlay.secondTileClick = false;
        OuraniaOverlay.thirdTileClicked = false;
        updateConfig();
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged configChanged) {
        if (!configChanged.getGroup().equals("ourania")) {
            return;
        }

        if (config.RunAltarBuiltIn()) {
            updateConfig();
        }

        if (!config.RunAltarBuiltIn()) {
            Reset();
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN) {
            client.setIsHidingEntities(isPlayerRegionAllowed());
        }

        if (event.getGameState() == GameState.LOGIN_SCREEN ||
                event.getGameState() == GameState.HOPPING) {
            lastPlayerLocation = null;

        }

    }

    private void updateConfig() {
        client.setIsHidingEntities(isPlayerRegionAllowed());

        client.setLocalPlayerHidden2D(config.hideAll2DEntities());
        client.setPlayersHidden2D(config.hideAll2DEntities());
        client.setNPCsHidden2D(config.hideAll2DEntities());

        client.setFriendsHidden(config.hideFriendsAndLocal());
        client.setClanMatesHidden(config.hideFriendsAndLocal());
        client.setLocalPlayerHidden(config.hideFriendsAndLocal());

        client.setAttackersHidden(config.hideAttackers());
        client.setProjectilesHidden(config.hideAttackers());
    }

    private boolean isPlayerRegionAllowed() {
        final Player localPlayer = client.getLocalPlayer();

        if (localPlayer == null) {
            return true;
        }

        final int playerRegionID = WorldPoint.fromLocalInstance(client, localPlayer.getLocalLocation()).getRegionID();

        // 9520 = Castle Wars
        return playerRegionID != 9520;
    }

}
