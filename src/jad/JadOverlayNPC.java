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
package net.runelite.client.plugins.jad;

import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.awt.*;
import java.util.List;

public class JadOverlayNPC extends Overlay {
    private final Client client;
    static boolean jadPrayer = false;
    static boolean jad = false;

    @Inject
    JadOverlayNPC(Client client) {
        this.client = client;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        final List<NPC> npcs = client.getNpcs();
        for (NPC npc : npcs) {


            jad = false;
            if (npc.getId() == 3127) {
                jad = true;
                int animation = npc.getAnimation();

                // Range
                if (animation == 2652) {
                    jadPrayer = true;
                }
                // Mage
                if (animation == 2656) {
                    jadPrayer = false;
                }

                if (jadPrayer) {
                    Polygon opPoly = npc.getConvexHull();
                    renderPoly(graphics, Color.GREEN, opPoly);
                    String npcName = Text.removeTags(npc.getName());
                    Point textLocation = npc.getCanvasTextLocation(graphics, npcName, npc.getLogicalHeight() + 40);
                    if (textLocation != null) {
                        OverlayUtil.renderTextLocation(graphics, textLocation, npcName + " PRAY RANGE", Color.GREEN);
                    }
                } else {
                    Polygon opPoly = npc.getConvexHull();
                    renderPoly(graphics, Color.BLUE, opPoly);
                    String npcName = Text.removeTags(npc.getName());
                    Point textLocation = npc.getCanvasTextLocation(graphics, npcName, npc.getLogicalHeight() + 40);
                    if (textLocation != null) {
                        OverlayUtil.renderTextLocation(graphics, textLocation, npcName + " PRAY MAGE", Color.BLUE);
                    }
                }


            } else {


                if (npc.isDead()) {
                    String npcName = Text.removeTags(npc.getName());
                    Point textLocation = npc.getCanvasTextLocation(graphics, npcName, npc.getLogicalHeight() + 40);

                    if (textLocation != null) {
                        OverlayUtil.renderTextLocation(graphics, textLocation, "Dead", Color.RED);
                    }
                } else {
                    String npcName = Text.removeTags(npc.getName());
                    Point textLocation = npc.getCanvasTextLocation(graphics, npcName, npc.getLogicalHeight() + 40);
                    Polygon opPoly = npc.getConvexHull();
                    renderPoly(graphics, Color.WHITE, opPoly);
                    if (textLocation != null) {
                        OverlayUtil.renderTextLocation(graphics, textLocation, npcName, Color.WHITE);
                    }
                }

            }


        }

        return null;
    }

    private void renderPoly(Graphics2D graphics, Color color, Polygon polygon) {
        if (polygon != null) {
            graphics.setColor(color);
            graphics.setStroke(new BasicStroke(2));
            graphics.draw(polygon);
            graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
            graphics.fill(polygon);
        }
    }
}
