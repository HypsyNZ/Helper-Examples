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

import net.runelite.api.Client;
import net.runelite.api.NPC;

import java.awt.*;

import static net.runelite.client.plugins.helpers.HelperDraw.drawCircle;
import static net.runelite.client.plugins.helpers.HelperDraw.renderPoly;

public class xoFindNPC {

    // Find an NPC by their ID
    int[] FindNPCLocationByID(int id, Client client, Graphics2D graphics) {

        int xv = 0;
        int yv = 0;
        for (NPC npc : client.getNpcs()) {
            final int npcID = npc.getId();

            npc.setModelHeight(1);

            if (npcID == id) {

                final int canvasHeight = client.getCanvasHeight();
                final int canvasWidth = client.getCanvasWidth();
                Dimension dim = client.getStretchedDimensions();

                double scaleFactorY = dim.getHeight() / canvasHeight;
                double scaleFactorX = dim.getWidth() / canvasWidth;

                Polygon k = npc.getConvexHull();

                // +0x +20y Center || -8x, +12y LeftOfCenter
                xv = (int) Math.ceil(scaleFactorX * k.getBounds().getCenterX()) - 8;
                yv = (int) Math.ceil(scaleFactorY * k.getBounds().getCenterY()) + 12;

                renderPoly(graphics, Color.orange, k);
                drawCircle(graphics, (int) k.getBounds().getCenterX() - 1,
                        (int) k.getBounds().getCenterY() - 1, 1, Color.MAGENTA);

            }
            // Random Click around center point
//            int randx = getRandomNumberInRange(xv + 1, xv + 13);
//            int randy = getRandomNumberInRange(yv + 1, yv + 15);
        }

        return new int[]{xv, yv};
    }

}
