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
package net.runelite.client.plugins.woodcutting;


import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.ItemID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.helpers.HelperInput;
import net.runelite.client.plugins.helpers.HelperRegion;
import net.runelite.client.plugins.helpers.HelperTransform;
import net.runelite.client.plugins.helpers.HelperWidget;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Set;

import static net.runelite.client.plugins.helpers.HelperDelay.*;
import static net.runelite.client.plugins.helpers.HelperFind.FindWidgetItemAndClick;
import static net.runelite.client.plugins.helpers.HelperInput.Click;
import static net.runelite.client.plugins.helpers.HelperInput.Delay;
import static net.runelite.client.plugins.helpers.HelperWidget.inventory;


class aTasks implements Runnable {

    private final Client client;
    private final WoodcuttingConfig config;
    private String nameOfTask;
    private WoodcuttingPlugin plugin;
    private Set<GameObject> gameObjects;

    /**
     * Tasks to be completed by this plugin
     *
     * @param client Instance of Client
     */
    aTasks(Client client, WoodcuttingConfig config, String nameOfTask, WoodcuttingPlugin plugin, Set<GameObject> gameObjects) {
        this.client = client;
        this.config = config;
        this.nameOfTask = nameOfTask;
        this.plugin = plugin;
        this.gameObjects = gameObjects;
    }

    public void run() {
        if (HelperWidget.shouldStop) {
            return;
        }

        Delay(rand900to1100);

        switch (nameOfTask) {

            case "empty": {

                // Open Inventory If It is Closed
                if (HelperRegion.currentInventoryTab != 3) {
                    HelperInput.PressKeyRandom(KeyEvent.VK_ESCAPE);
                }

                Delay(rand215to280);

                switch (HelperTransform.getRandomNumberInRange(1, 5)) {

                    case 1: {
                        // Clean Herbs
                        for (WidgetItem item : inventory.getWidgetItems()) {

                            int id = item.getId();
                            if (id == ItemID.TEAK_LOGS) {
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


                        Delay(rand700to900);
                    }
                    break;
                }

            }
            break;

            case "find": {

                Delay(rand400to1700);
                int[] inc = new int[]{2, 3, 5, 6, 9, 10, 12};
                for (int i2 : inc) {
                    for (GameObject treeObject : gameObjects) {
                        int[] ids = config.treeToUse().getTreeIds();
                        for (int i : ids) {
                            if (i == treeObject.getId()) {
                                if (treeObject.getWorldLocation().distanceTo2D(client.getLocalPlayer().getWorldLocation()) <= i2) {
                                    if (!HelperRegion.isMoving && plugin.getAxe().getAnimId() != client.getLocalPlayer().getAnimation()) {
                                        Polygon trees = treeObject.getConvexHull();
                                        int[] tree = HelperTransform.xyFromEdge(trees, client);
                                        if (tree[0] != 0) {
                                            Click(tree[0], tree[1]);
                                            Delay(rand1500to1700);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Delay(rand700to900);

            }
            break;

        }

    }

    private void widgetItem(Widget widget, int index) {
        WidgetItem item = widget.getWidgetItem(index);
        if (item.getId() == ItemID.TEAK_LOGS) {
            FindWidgetItemAndClick(item, client,false);
        }
    }

}