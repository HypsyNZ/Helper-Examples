/*
 * Copyright (c) 2018, Tomas Slusny <slusnucky@gmail.com>
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
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
import net.runelite.api.TileObject;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;

class WoodcuttingTreesOverlay extends Overlay {
    private final Client client;
    private final WoodcuttingConfig config;
    private final ItemManager itemManager;
    private final WoodcuttingPlugin plugin;

    @Inject
    private SkillIconManager skillIconManager;

    @Inject
    private WoodcuttingTreesOverlay(final Client client, final WoodcuttingConfig config, final ItemManager itemManager, final WoodcuttingPlugin plugin, final SkillIconManager skillIconManager) {
        this.client = client;
        this.config = config;
        this.itemManager = itemManager;
        this.plugin = plugin;
        this.skillIconManager = skillIconManager;
        setPosition(OverlayPosition.DYNAMIC);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!config.showTrees()) {
            return null;
        }

        Axe axe = plugin.getAxe();
        BufferedImage treeImage;

        if (axe == null) {
            treeImage = skillIconManager.getSkillImage(Skill.WOODCUTTING);
        } else {
            treeImage = itemManager.getImage(axe.getItemId());
        }


        for (TileObject treeObject : plugin.getTreeObjects()) {

            int[] ids = config.treeToUse().getTreeIds();
            for (int i : ids) {
                if (i == treeObject.getId()) {
                    if (treeObject.getWorldLocation().distanceTo2D(client.getLocalPlayer().getWorldLocation()) <= 12) {
                        OverlayUtil.renderImageLocation(client, graphics, treeObject.getLocalLocation(), treeImage, 120);
                    }
                }
            }
        }

        return null;
    }
}
