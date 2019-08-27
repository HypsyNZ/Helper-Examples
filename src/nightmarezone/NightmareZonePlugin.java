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
package net.runelite.client.plugins.nightmarezone;

import com.google.inject.Provides;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.helpers.HelperTransform;
import net.runelite.client.plugins.helpers.HelperWidget;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.util.Arrays;

import static net.runelite.client.plugins.helpers.HelperRegion.currentRegionID;
import static net.runelite.client.plugins.nightmarezone.NightmareZoneOverlay.hitpoints;

@PluginDescriptor(
        name = "∞ Nightmare Zone",
        description = "NMZ Plugin",
        tags = {"combat", "nmz", "minigame", "notifications"}
)

public class NightmareZonePlugin extends Plugin {

    @Inject
    private Notifier notifier;

    @Inject
    private Client client;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private NightmareZoneConfig config;

    @Inject
    private NightmareZoneOverlay overlay;

    private boolean absorptionNotificationSend = true;
    private boolean hpNotificationSend = true;

    @Override
    protected void startUp() throws Exception {
        overlayManager.add(overlay);

        overlay.removeAbsorptionCounter();

        HelperWidget.disableAllHelperWidgets();
        HelperWidget.WidgetCombatEnabledTrue();
        HelperWidget.shouldStop = false;
    }

    @Override
    protected void shutDown() throws Exception {
        overlayManager.remove(overlay);

        overlay.removeAbsorptionCounter();
        overlay.removeHitPointsCounter();

        HelperWidget.enableAllHelperWidgets();
        HelperWidget.shouldStop = true;

        Widget nmzWidget = client.getWidget(WidgetInfo.NIGHTMARE_ZONE);
        if (nmzWidget != null) {
            nmzWidget.setHidden(false);
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        overlay.updateConfig();
    }

    @Provides
    NightmareZoneConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(NightmareZoneConfig.class);
    }

    static int TickWeStartedOn = -1;
    private boolean so = false;

    @Subscribe
    public void onGameTick(GameTick event) {

        if (isInNightmareZone(client) && !so) {
            so = true;
            TickWeStartedOn = client.getTickCount();
        }
        if (!isInNightmareZone(client) && so) {
            so = false;
            TickWeStartedOn = -1;
        }

        if (!absorptionNotificationSend) {
            absorptionNotificationSend = true;
        }
        if (!hpNotificationSend) {
            hpNotificationSend = true;
        }

        if (isInNightmareZone(client)) {

            if (config.absorptionNotification()) {
                checkAbsorption();
            }
            if (config.hitpointNotification()) {
                checkHitpoints();
            }

        }

    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {

        if (client.getGameState() != GameState.LOGGED_IN) {
            return;
        }

        MenuEntry[] entries = client.getMenuEntries();
        client.getMenuEntries();

        if (config.swapGuzzle()) {
            swapWalk(entries, "eat", "guzzle");
        }

    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if (event.getType() != ChatMessageType.GAMEMESSAGE
                || !isInNightmareZone(client)) {
            return;
        }

        String msg = Text.removeTags(event.getMessage()); //remove color
        if (msg.contains("The effects of overload have worn off, and you feel normal again.")) {

            if (config.overloadNotification()) {
                notifier.notify("Your overload has worn off");
            }

        } else if (msg.contains("A power-up has spawned:")) {

            if (msg.contains("Power surge")) {

                if (config.powerSurgeNotification()) {
                    notifier.notify(msg);
                }

            } else if (msg.contains("Recurrent damage")) {

                if (config.recurrentDamageNotification()) {
                    notifier.notify(msg);
                }

            } else if (msg.contains("Zapper")) {

                if (config.zapperNotification()) {
                    notifier.notify(msg);
                }

            } else if (msg.contains("Ultimate force")) {

                if (config.ultimateForceNotification()) {
                    notifier.notify(msg);
                }
            }
        }
    }

    private void checkAbsorption() {
        if (isInNightmareZone(client)) {

            int absorptionPoints = client.getVar(Varbits.NMZ_ABSORPTION);

            if (!absorptionNotificationSend) {
                if (absorptionPoints < config.absorptionThreshold()) {
                    notifier.notify("Absorption points below: " + config.absorptionThreshold());
                    absorptionNotificationSend = true;
                }
            } else {
                if (absorptionPoints > config.absorptionThreshold()) {
                    absorptionNotificationSend = false;
                }
            }

        }
    }

    private void checkHitpoints() {

        if (!hpNotificationSend) {
            if (hitpoints >= config.hitpointsThreshold()) {
                notifier.notify("Hit points at threshold: " + config.hitpointsThreshold());
                hpNotificationSend = true;

            }
        } else {
            if (hitpoints < config.hitpointsThreshold()) {
                hpNotificationSend = false;
            }
        }

    }


    private void swapWalk(MenuEntry[] entries, String optionA, String optionB) {

        int idxA = HelperTransform.searchIndex(entries, optionA);
        int idxB = HelperTransform.searchIndex(entries, optionB);

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


    private static final int[] NMZ_MAP_REGION = {9033};

    public static boolean isInNightmareZone(Client client) {
        return Arrays.equals(client.getMapRegions(), NMZ_MAP_REGION);
    }

    public static boolean isInRenderArea(Client client) {

        if (Arrays.equals(client.getMapRegions(), NMZ_MAP_REGION)) {
            return true;
        }

        return currentRegionID == 10288;
    }

}
