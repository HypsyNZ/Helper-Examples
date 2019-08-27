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

import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.helpers.HelperTransform;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;

import javax.inject.Inject;
import java.awt.*;
import java.util.List;

import static net.runelite.client.plugins.helpers.HelperDraw.renderPoly;


class xoWidget extends Overlay {

    static boolean fightingNPC = false;
    static String opponentName = null;
    private final Client client;
    private final PanelComponent panelComponent = new PanelComponent();
    private MasterFarmPlugin masterFarmPlugin;

    // Monkfish
    static int MonkfishX = 0;
    static int MonkfishY = 0;

    // Tab2
    static int tab2x = 0;
    static int tab2y = 0;

    @Inject
    public xoWidget(Client client, MasterFarmPlugin masterFarmPlugin) {
        setPosition(OverlayPosition.DYNAMIC);
        this.client = client;
        this.masterFarmPlugin = masterFarmPlugin;
    }

    @Override
    public Dimension render(Graphics2D graphics) {

        // Booleans
        fightingNPC = false;

        // Widgets
        Widget bank = client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
        Widget bcc = client.getWidget(WidgetInfo.BANK_CONTENT_CONTAINER);

        // Find Opponent
        final Actor opponent = masterFarmPlugin.getLastOpponent();
        if (opponent != null) {
            if (opponent instanceof NPC) {
                opponentName = opponent.getName();
                Polygon opPoly = opponent.getConvexHull();

                if (((NPC) opponent).getId() == 2120 || ((NPC) opponent).getId() == 3259 || ((NPC) opponent).getId() == 3949) {
                    fightingNPC = true;
                    renderPoly(graphics, Color.RED, opPoly);
                }
            }
        }

        // Find NPCs that are targeting local player
        final List<NPC> npcs = client.getNpcs();
        for (NPC npc : npcs) {
            if (npc.getId() == 2120 || npc.getId() == 3259 || npc.getId() == 3949) {
                if (npc.getInteracting() == client.getLocalPlayer()) {
                    Polygon opPoly = npc.getConvexHull();
                    fightingNPC = true;
                    renderPoly(graphics, Color.RED, opPoly);
                }

            }
        }

        // BANK_CONTENT_CONTAINER
        if (bcc != null) {
            Widget[] bc = bcc.getDynamicChildren();
            for (Widget ss : bc) {
                if (ss.getIndex() == 2) {

                    int[] xy = HelperTransform.xyFromWidget(ss, client);
                    if (xy[0] != 0) {
                        tab2x = xy[0];
                        tab2y = xy[1];
                    }
                }
            }
        }

        // BANK_ITEM_CONTAINER
        if (bank != null) {
            Widget[] sb = bank.getDynamicChildren();
            for (Widget ss : sb) {
                if (ss.getItemId() == 7946) {
                    int[] xy = HelperTransform.xyFromWidget(ss, client);
                    if (xy[0] != 0) {
                        MonkfishX = xy[0];
                        MonkfishY = xy[1];
                    }
                }
            }
        }
        return panelComponent.render(graphics);
    }
}


