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

import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;

import java.awt.*;

import static net.runelite.client.plugins.helpers.HelperDraw.drawSquare;
import static net.runelite.client.plugins.helpers.HelperTransform.getRandomNumberInRange;

class xoFindTiles {

    // Click an area with a tiles WorldPoint at the center // 1 = 3x3 // 2 = 5x5 // 3 = 7x7
    int[] ClickTileLargeArea(int WorldCenterX, int WorldCenterY, Graphics2D graphics, int Size, Client client) {
        Scene scene = client.getScene();
        Tile[][][] tiles = scene.getTiles();
        int z = client.getPlane();
        int xv = 0;
        int yv = 0;
        int randx = 0;
        int randy = 0;

        int WorldCenterXx = getRandomNumberInRange((WorldCenterX - Size), (WorldCenterX + Size));
        int WorldCenterYx = getRandomNumberInRange((WorldCenterY - Size), (WorldCenterY + Size));

        for (int x = 0; x < Constants.SCENE_SIZE; ++x) {
            for (int y = 0; y < Constants.SCENE_SIZE; ++y) {
                Tile tile = tiles[z][x][y];
                if (tile == null) {
                    continue;
                }

                WorldPoint twp = tile.getWorldLocation();
                final int canvasHeight = client.getCanvasHeight();
                final int canvasWidth = client.getCanvasWidth();
                Dimension dim = client.getStretchedDimensions();

                double scaleFactorY = dim.getHeight() / canvasHeight;
                double scaleFactorX = dim.getWidth() / canvasWidth;


                Polygon poly = Perspective.getCanvasTilePoly(client, tile.getLocalLocation());

                //	int randside = getRandomNumberInRange(1, 4);

                if (twp.getX() == WorldCenterXx && twp.getY() == WorldCenterYx) {
                    if (poly != null) {
                        xv = (int) Math.ceil(scaleFactorX * (poly.xpoints[1])) + 2;
                        yv = (int) Math.ceil(scaleFactorY * (poly.ypoints[1])) + 2;
                        randx = getRandomNumberInRange(xv + 3, xv + 10);
                        randy = getRandomNumberInRange(yv + 15, yv + 25);
                        drawSquare(poly, graphics);
                        return new int[]{randx, randy};
                    }
                }
            }

        }
        return null;
    }

}
