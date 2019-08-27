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

import com.google.inject.Provides;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.helpers.HelperTransform;
import net.runelite.client.plugins.helpers.HelperWidget;
import net.runelite.client.plugins.xptracker.XpTrackerPlugin;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@PluginDescriptor(
        name = "∞ Woodcutting",
        description = "Show woodcutting statistics and/or bird nest notifications",
        tags = {"birds", "nest", "notifications", "overlay", "skilling", "wc"}
)
@PluginDependency(XpTrackerPlugin.class)
public class WoodcuttingPlugin extends Plugin {
    @Inject
    private Notifier notifier;

    @Inject
    private KeyManager keyManager;

    @Inject
    private Client client;

    @Inject
    private aInputListener listener;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private WoodcuttingOverlay overlay;

    @Inject
    private WoodcuttingTreesOverlay treesOverlay;

    @Inject
    private WoodcuttingConfig config;

    @Getter
    private WoodcuttingSession session;

    @Inject
    private ConfigManager configManager;

    @Getter
    private Axe axe;

    @Getter
    @Setter
    private final Set<GameObject> treeObjects = new HashSet<>();

    @Provides
    WoodcuttingConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(WoodcuttingConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        keyManager.registerKeyListener(listener);
        overlayManager.add(overlay);
        overlayManager.add(treesOverlay);
        configManager.setConfiguration("woodcutting", "aAltarBuiltIn", false);
        HelperWidget.shouldStop = true;
    }

    @Override
    protected void shutDown() throws Exception {
        HelperWidget.shouldStop = true;
        configManager.setConfiguration("woodcutting", "aAltarBuiltIn", false);
        keyManager.registerKeyListener(listener);
        overlayManager.remove(overlay);
        overlayManager.remove(treesOverlay);
        treeObjects.clear();
        session = null;
        axe = null;
    }

    @Subscribe
    public void onGameTick(GameTick gameTick) {
        if (session == null || session.getLastLogCut() == null) {
            return;
        }

        Duration statTimeout = Duration.ofMinutes(config.statTimeout());
        Duration sinceCut = Duration.between(session.getLastLogCut(), Instant.now());

        if (sinceCut.compareTo(statTimeout) >= 0) {
            session = null;
            axe = null;
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (!event.getGroup().equals("woodcutting")) {
            return;
        }

    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if (event.getType() == ChatMessageType.SPAM || event.getType() == ChatMessageType.GAMEMESSAGE) {
            if (event.getMessage().startsWith("You get some") && (event.getMessage().endsWith("logs.") || event.getMessage().endsWith("mushrooms."))) {
                if (session == null) {
                    session = new WoodcuttingSession();
                }

                session.setLastLogCut();
            }

            if (event.getMessage().contains("A bird's nest falls out of the tree") && config.showNestNotification()) {
                notifier.notify("A bird nest has spawned!");
            }
        }
    }

    @Subscribe
    public void onGameObjectSpawned(final GameObjectSpawned event) {
        GameObject gameObject = event.getGameObject();

        Tree tree = Tree.findTree(gameObject.getId());

        if (tree != null) {
            if (config.treeToUse().getTreeIds() == tree.getTreeIds()) {
                treeObjects.add(gameObject);
            }
        }
    }

    @Subscribe
    public void onGameObjectDespawned(final GameObjectDespawned event) {
        treeObjects.remove(event.getGameObject());
    }

    @Subscribe
    public void onGameObjectChanged(final GameObjectChanged event) {
        treeObjects.remove(event.getGameObject());
    }

    @Subscribe
    public void onGameStateChanged(final GameStateChanged event) {
        if (event.getGameState() != GameState.LOGGED_IN) {
            treeObjects.clear();
        }
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {

        if (client.getGameState() != GameState.LOGGED_IN) {
            return;
        }

        MenuEntry[] entries = client.getMenuEntries();
        client.getMenuEntries();

        if (config.dropLogs()) {
            swapWalk(entries, "use", "drop");
        }
    }

    @Subscribe
    public void onAnimationChanged(final AnimationChanged event) {
        Player local = client.getLocalPlayer();

        if (event.getActor() != local) {
            return;
        }

        int animId = local.getAnimation();
        Axe axe = Axe.findAxeByAnimId(animId);
        if (axe != null) {
            this.axe = axe;
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
}