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

import net.runelite.api.Client;
import net.runelite.client.plugins.helpers.HelperFind;
import net.runelite.client.plugins.helpers.HelperInventory;
import net.runelite.client.plugins.helpers.HelperTransform;
import net.runelite.client.plugins.helpers.HelperWidget;

import java.awt.*;
import java.awt.event.KeyEvent;

import static net.runelite.client.plugins.construction.cItems.cItemsList;
import static net.runelite.client.plugins.helpers.HelperDelay.*;
import static net.runelite.client.plugins.helpers.HelperInput.*;
import static net.runelite.client.plugins.helpers.HelperWidget.shouldStop;

class cActions implements Runnable {


    private Client client;
    private Graphics2D graphics;

    cActions(Client client, Graphics2D graphics) {
        this.client = client;
        this.graphics = graphics;
    }


    public void run() {

        int[] logLocation = HelperInventory.InventoryItemXYbyID(8778, cItemsList);
        if (logLocation[0] == 0) {
            return;
        }

        Delay(rand700to900);

        // Find Larder Build
        int[] larderb = HelperFind.FindGameObjectOdd(client, graphics, 15403, 3);
        while (larderb[0] == 0) {
            larderb = HelperFind.FindGameObjectOdd(client, graphics, 15403, 3);
            Delay(rand175to300);
            if (shouldStop) {
                break;
            }
        }
        if (shouldStop) {
            return;
        }

        Delay(rand700to900);
        ClickRight(larderb[0], larderb[1]);
        Delay(rand175to300);
        int x = HelperTransform.getRandomNumberInRange(larderb[0] - 30, larderb[0] + 30);
        int y = HelperTransform.getRandomNumberInRange(larderb[1] - 2, larderb[1] + 2);
        Click(x, y + 55);
        Delay(rand175to300);

        // Wait for larder widget
        while (HelperWidget.furniture == null) {
            Delay(rand45to75);
            if (shouldStop) {
                break;
            }
        }

        if (shouldStop) {
            return;
        }

        Delay(rand175to300);
        PressKeyRandom(KeyEvent.VK_2);
        Delay(rand700to900);


        int[] larderr = HelperFind.FindGameObjectOdd(client, graphics, 13566, 3);
        while (larderr[0] == 0) {
            larderr = HelperFind.FindGameObjectOdd(client, graphics, 13566, 3);
            Delay(rand175to300);
            if (shouldStop) {
                break;
            }
        }
        if (shouldStop) {
            return;
        }

        Delay(rand700to900);
        ClickRight(larderr[0], larderr[1]);
        Delay(rand175to300);

        // Left Click + 80 pxy
        int xr = HelperTransform.getRandomNumberInRange(larderr[0] - 30, larderr[0] + 30);
        int y1 = HelperTransform.getRandomNumberInRange(larderr[1] - 2, larderr[1] + 2);
        Click(xr, y1 + 75);

        Delay(rand175to300);

        while (!HelperWidget.dialogOpen) {
            Delay(rand175to300);
            if (shouldStop) {
                break;
            }
        }

        Delay(rand175to300);
        PressKeyRandom(KeyEvent.VK_1);
        Delay(rand900to1100);


        // 1

        Delay(rand1to300);

        // Find Larder Build
        int[] larderb2 = HelperFind.FindGameObjectOdd(client, graphics, 15403, 3);
        while (larderb2[0] == 0) {
            larderb2 = HelperFind.FindGameObjectOdd(client, graphics, 15403, 3);
            Delay(rand175to300);
            if (shouldStop) {
                break;
            }
        }
        if (shouldStop) {
            return;
        }

        Delay(rand700to900);
        ClickRight(larderb2[0], larderb2[1]);
        Delay(rand175to300);
        int x2 = HelperTransform.getRandomNumberInRange(larderb2[0] - 30, larderb2[0] + 30);
        int y2 = HelperTransform.getRandomNumberInRange(larderb2[1] - 2, larderb2[1] + 2);
        Click(x2, y2 + 55);
        Delay(rand175to300);

        // Wait for larder widget
        while (HelperWidget.furniture == null) {
            Delay(rand175to300);
            if (shouldStop) {
                break;
            }
        }

        if (shouldStop) {
            return;
        }

        Delay(rand175to300);
        PressKeyRandom(KeyEvent.VK_2);
        Delay(rand700to900);


        int[] larderr2 = HelperFind.FindGameObjectOdd(client, graphics, 13566, 3);
        while (larderr2[0] == 0) {
            larderr2 = HelperFind.FindGameObjectOdd(client, graphics, 13566, 3);
            Delay(rand175to300);
            if (shouldStop) {
                break;
            }
        }
        if (shouldStop) {
            return;
        }

        Delay(rand700to900);
        ClickRight(larderr2[0], larderr2[1]);
        Delay(rand175to300);

        // Left Click + 80 pxy
        int xr2 = HelperTransform.getRandomNumberInRange(larderr2[0] - 30, larderr2[0] + 30);
        int yr1 = HelperTransform.getRandomNumberInRange(larderr2[1] - 2, larderr2[1] + 2);
        Click(xr2, yr1 + 75);

        Delay(rand1to300);

        while (!HelperWidget.dialogOpen) {
            Delay(rand175to300);
            if (shouldStop) {
                break;
            }
        }

        Delay(rand175to300);
        PressKeyRandom(KeyEvent.VK_1);
        Delay(rand900to1100);

        Delay(rand1to300);
        // Check if demon butler next to me?
        // --> Summon if not?

        int[] demon = HelperFind.FindNPCLocationByID(229, client, graphics);
        Click(demon[0], demon[1]);

        while (!HelperWidget.dialogOpen) {
            Delay(rand175to300);
            if (shouldStop) {
                break;
            }
        }

        Delay(rand175to300);
        PressKeyRandom(KeyEvent.VK_1);
        Delay(rand175to300);


        Delay(rand1to300);

        // Find Larder Build
        int[] larderb3 = HelperFind.FindGameObjectOdd(client, graphics, 15403, 3);
        while (larderb3[0] == 0) {
            larderb3 = HelperFind.FindGameObjectOdd(client, graphics, 15403, 3);
            Delay(rand175to300);
            if (shouldStop) {
                break;
            }
        }
        if (shouldStop) {
            return;
        }

        Delay(rand175to300);
        ClickRight(larderb3[0], larderb3[1]);
        Delay(rand175to300);
        int x3 = HelperTransform.getRandomNumberInRange(larderb3[0] - 30, larderb3[0] + 30);
        int yr3 = HelperTransform.getRandomNumberInRange(larderb3[1] - 2, larderb3[1] + 2);
        Click(x3, yr3 + 55);
        Delay(rand175to300);

        // Wait for larder widget
        while (HelperWidget.furniture == null) {
            Delay(rand175to300);
            if (shouldStop) {
                break;
            }
        }

        if (shouldStop) {
            return;
        }

        Delay(rand175to300);
        PressKeyRandom(KeyEvent.VK_2);
        Delay(rand175to300);


        int[] larderr3 = HelperFind.FindGameObjectOdd(client, graphics, 13566, 3);
        while (larderr3[0] == 0) {
            larderr3 = HelperFind.FindGameObjectOdd(client, graphics, 13566, 3);
            Delay(rand175to300);
            if (shouldStop) {
                break;
            }
        }
        if (shouldStop) {
            return;
        }

        Delay(rand175to300);
        ClickRight(larderr3[0], larderr3[1]);
        Delay(rand125to200);

        // Left Click + 80 pxy
        int xr3 = HelperTransform.getRandomNumberInRange(larderr3[0] - 30, larderr3[0] + 30);
        int yr4 = HelperTransform.getRandomNumberInRange(larderr3[1] - 2, larderr3[1] + 2);
        Click(xr3, yr4 + 75);

        Delay(rand125to200);

        while (!HelperWidget.dialogOpen) {
            Delay(rand125to200);
            if (shouldStop) {
                break;
            }
        }

        Delay(rand125to200);
        PressKeyRandom(KeyEvent.VK_1);
        Delay(rand175to300);


        // Click on butler

        // Find first option and click
        Delay(rand1to300);

    }
}


