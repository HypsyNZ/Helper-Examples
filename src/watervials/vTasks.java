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
package net.runelite.client.plugins.watervials;


import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.helpers.HelperInput;
import net.runelite.client.plugins.helpers.HelperRegion;
import net.runelite.client.plugins.helpers.HelperTransform;
import net.runelite.client.plugins.helpers.HelperWidget;

import java.awt.*;
import java.awt.event.KeyEvent;

import static net.runelite.client.plugins.helpers.HelperDelay.*;
import static net.runelite.client.plugins.helpers.HelperFind.FindWidgetItemAndClick;
import static net.runelite.client.plugins.helpers.HelperInput.*;
import static net.runelite.client.plugins.helpers.HelperRegion.jatix;
import static net.runelite.client.plugins.helpers.HelperRegion.vialOfWaterInt;
import static net.runelite.client.plugins.helpers.HelperWidget.*;


class vTasks implements Runnable {

    private final Client client;
    private final vConfig config;

    /**
     * Tasks to be completed by this plugin
     *
     * @param client Instance of Client
     */
    vTasks(Client client, vConfig config) {
        this.client = client;
        this.config = config;
    }


    private void widgetItem(Widget widget, int index) {
        WidgetItem item = widget.getWidgetItem(index);
        if (item.getId() == 11879) {
            FindWidgetItemAndClick(item, client,true);
        }
    }

    public void run() {
        if (HelperWidget.shouldStop) {
            return;
        }

        Delay(rand900to1100);

        int runsTwice = 0;
        while (runsTwice < 2) {

            // OPEN SHOP
            HelperInput.Click(jatix[0], jatix[1]);
            while (!TalkingToShopOwner) {
                if (shouldStop) {
                    break;
                }
                Delay(rand175to300);
            }

            if (vialOfWaterInt[0] == 0) {
                shouldStop = true;
                return;
            }

            // BUY VIALS
            Delay(rand700to900);
            ClickRight(vialOfWaterInt[0], vialOfWaterInt[1]);
            Delay(rand175to300);
            int x = HelperTransform.getRandomNumberInRange(vialOfWaterInt[0] - 50, vialOfWaterInt[0] + 50);
            int y = HelperTransform.getRandomNumberInRange(vialOfWaterInt[1] - 2, vialOfWaterInt[1] + 2);
            Click(x, y + 86);
            Delay(rand175to300);


            // Exit Shop
            HelperInput.PressKeyRandom(KeyEvent.VK_ESCAPE);
            Delay(rand175to300);


            // Open Inventory If It is Closed
            if (HelperRegion.currentInventoryTab != 3) {
                HelperInput.PressKeyRandom(KeyEvent.VK_ESCAPE);
            }


            Delay(rand1500to1700);
            switch (HelperTransform.getRandomNumberInRange(1, 5)) {

                case 1: {
                    // Clean Herbs
                    for (WidgetItem item : inventory.getWidgetItems()) {

                        int id = item.getId();
                        if (id == 11879) {
                            FindWidgetItemAndClick(item, client,true);
                        }
                    }
                }
                break;


                case 2: {
                    widgetItem(inventory, 0);
                    widgetItem(inventory, 4);
                    widgetItem(inventory, 8);
                    widgetItem(inventory, 12);
                    widgetItem(inventory, 16);
                    widgetItem(inventory, 20);
                    widgetItem(inventory, 24);

                    widgetItem(inventory, 25);
                    widgetItem(inventory, 21);
                    widgetItem(inventory, 17);
                    widgetItem(inventory, 13);
                    widgetItem(inventory, 9);
                    widgetItem(inventory, 5);
                    widgetItem(inventory, 1);


                    widgetItem(inventory, 2);
                    widgetItem(inventory, 6);
                    widgetItem(inventory, 10);
                    widgetItem(inventory, 14);
                    widgetItem(inventory, 18);
                    widgetItem(inventory, 22);
                    widgetItem(inventory, 26);

                    widgetItem(inventory, 27);
                    widgetItem(inventory, 23);
                    widgetItem(inventory, 19);
                    widgetItem(inventory, 15);
                    widgetItem(inventory, 11);
                    widgetItem(inventory, 7);
                    widgetItem(inventory, 3);
                }
                break;


                case 3: {
                    widgetItem(inventory, 0);
                    widgetItem(inventory, 1);

                    widgetItem(inventory, 4);
                    widgetItem(inventory, 5);

                    widgetItem(inventory, 8);
                    widgetItem(inventory, 9);

                    widgetItem(inventory, 12);
                    widgetItem(inventory, 13);

                    widgetItem(inventory, 16);
                    widgetItem(inventory, 17);

                    widgetItem(inventory, 20);
                    widgetItem(inventory, 21);

                    widgetItem(inventory, 24);
                    widgetItem(inventory, 25);

                    widgetItem(inventory, 2);
                    widgetItem(inventory, 3);

                    widgetItem(inventory, 6);
                    widgetItem(inventory, 7);

                    widgetItem(inventory, 10);
                    widgetItem(inventory, 11);

                    widgetItem(inventory, 14);
                    widgetItem(inventory, 15);

                    widgetItem(inventory, 18);
                    widgetItem(inventory, 19);

                    widgetItem(inventory, 22);
                    widgetItem(inventory, 23);

                    widgetItem(inventory, 26);
                    widgetItem(inventory, 27);
                }
                break;


                case 4: {
                    widgetItem(inventory, 0);
                    widgetItem(inventory, 4);

                    widgetItem(inventory, 1);
                    widgetItem(inventory, 2);

                    widgetItem(inventory, 5);
                    widgetItem(inventory, 8);

                    widgetItem(inventory, 12);
                    widgetItem(inventory, 9);

                    widgetItem(inventory, 6);
                    widgetItem(inventory, 3);

                    widgetItem(inventory, 7);
                    widgetItem(inventory, 10);

                    widgetItem(inventory, 13);
                    widgetItem(inventory, 16);

                    widgetItem(inventory, 20);
                    widgetItem(inventory, 17);

                    widgetItem(inventory, 14);
                    widgetItem(inventory, 11);

                    widgetItem(inventory, 15);
                    widgetItem(inventory, 18);

                    widgetItem(inventory, 21);
                    widgetItem(inventory, 24);

                    widgetItem(inventory, 25);
                    widgetItem(inventory, 22);

                    widgetItem(inventory, 19);
                    widgetItem(inventory, 23);

                    widgetItem(inventory, 26);
                    widgetItem(inventory, 27);
                }
                break;


                case 5: {
                    widgetItem(inventory, 0);
                    widgetItem(inventory, 4);

                    widgetItem(inventory, 1);
                    widgetItem(inventory, 5);

                    widgetItem(inventory, 2);
                    widgetItem(inventory, 6);

                    widgetItem(inventory, 3);
                    widgetItem(inventory, 7);

                    widgetItem(inventory, 11);
                    widgetItem(inventory, 15);

                    widgetItem(inventory, 10);
                    widgetItem(inventory, 14);

                    widgetItem(inventory, 9);
                    widgetItem(inventory, 13);

                    widgetItem(inventory, 8);
                    widgetItem(inventory, 12);

                    widgetItem(inventory, 16);
                    widgetItem(inventory, 20);

                    widgetItem(inventory, 17);
                    widgetItem(inventory, 21);

                    widgetItem(inventory, 18);
                    widgetItem(inventory, 22);

                    widgetItem(inventory, 19);
                    widgetItem(inventory, 23);

                    widgetItem(inventory, 27);
                    widgetItem(inventory, 26);

                    widgetItem(inventory, 25);
                    widgetItem(inventory, 24);
                }
                break;
            }

            Delay(rand700to900);
            runsTwice++;

        }

        Delay(rand1500to1700);
        HelperInput.PressKeyRandom(config.keyForHop().getValue());

        Delay(rand1500to1700);
        Delay(rand1500to1700);

        while (client.getGameState() == GameState.HOPPING) {
            if (shouldStop) {
                break;
            }
            Delay(rand175to300);
        }

    }

}