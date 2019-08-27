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
package net.runelite.client.plugins.herblore;


import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.helpers.*;

import java.awt.event.KeyEvent;

import static net.runelite.client.plugins.helpers.HelperDelay.*;
import static net.runelite.client.plugins.helpers.HelperFind.FindWidgetItemAndClick;
import static net.runelite.client.plugins.helpers.HelperInput.*;
import static net.runelite.client.plugins.helpers.HelperRegion.WaitForBankClose;
import static net.runelite.client.plugins.helpers.HelperWidget.*;
import static net.runelite.client.plugins.herblore.hItems.hItemsListBank;


class hTasks implements Runnable {

    private final Client client;
    private final String nameOfTask;
    private final hConfig config;

    /**
     * Tasks to be completed by this plugin
     *
     * @param client     Instance of Client
     * @param nameOfTask Name of the Task
     * @param config     Instance of Config
     */
    hTasks(Client client, String nameOfTask, hConfig config) {
        this.client = client;
        this.nameOfTask = nameOfTask;
        this.config = config;
    }


    private void widgetItem(Widget widget, int index) {
        WidgetItem item = widget.getWidgetItem(index);
        if (item.getId() == config.herbs().getDirty()) {
            FindWidgetItemAndClick(item, client,true);
        }
    }

    private void widgetItemS(Widget widget, int index) {
        WidgetItem item = widget.getWidgetItem(index);
        if (item.getId() == config.herbs().getDirty()) {
            FindWidgetItemAndClick(item, client,false);
        }
    }

    public void run() {
        if (HelperWidget.shouldStop) {
            return;
        }

        Widget inventoryWidget = HelperWidget.inventory;
        if (inventoryWidget != null) {

            switch (nameOfTask) {

                case "clean": {

                    if (HelperRegion.currentInventoryTab != 3) {
                        HelperInput.PressKeyRandom(KeyEvent.VK_ESCAPE);
                    }

                    while (inventory.getWidgetItem(0).getId() != config.herbs().getDirty()) {
                        if (shouldStop) {
                            break;
                        }
                        Delay(rand75to100);
                    }

                    Delay(rand75to100);

//                    switch (HelperTransform.getRandomNumberInRange(1, 2)) {
//
//                        case 1: {
//                            for (WidgetItem item : inventory.getWidgetItems()) {
//
//                                int id = item.getId();
//                                if (id == config.herbs().getDirty()) {
//
//                                    FindWidgetItemAndClick(item, client,true);
//
//                                }
//                            }
//                        }
//                        break;
//
//                        case 2: {
                            widgetItemS(inventory, 0);
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

                            widgetItem(inventory, 27);
                            widgetItem(inventory, 26);
                            widgetItem(inventory, 23);
                            widgetItem(inventory, 22);
                            widgetItem(inventory, 19);
                            widgetItem(inventory, 18);
                            widgetItem(inventory, 15);
                            widgetItem(inventory, 14);
                            widgetItem(inventory, 11);
                            widgetItem(inventory, 10);
                            widgetItem(inventory, 7);
                            widgetItem(inventory, 6);
                            widgetItem(inventory, 3);
                            widgetItem(inventory, 2);

//                        }
//                        break;
//
//                    }

                    Delay(rand75to100);

                    // Wait for Bank to Open
                    HelperRegion.WaitForBankOpen(client);

                    Delay(rand75to100);

                    FindWidgetItemAndClick(bankDepositInventory,client,false);

                }
                break;


                case "withdraw": {


                    if (config.modeToUse() == Mode.CLEAN) {

                        // Withdraw Grimy
                        Widget herbToClean = HelperBank.bWidgetByID(config.herbs().getDirty(), client);

                        if (herbToClean != null) {
                            FindWidgetItemAndClick(herbToClean,client,false);
                        } else {
                            shouldStop = true;
                            break;
                        }

                    }

                    if (config.modeToUse() == Mode.MIX) {

                        Widget herbToMix = HelperBank.bWidgetByID(config.herbs().getClean(), client);
                        Widget vialOfWater = HelperBank.bWidgetByID(ItemID.VIAL_OF_WATER, client);

                        if (herbToMix != null) {
                            FindWidgetItemAndClick(herbToMix,client,false);
                        } else {
                            shouldStop = true;
                            break;
                        }

                        Delay(rand45to75);

                        if (vialOfWater != null) {
                            FindWidgetItemAndClick(vialOfWater, client,true);
                        } else {
                            shouldStop = true;
                            break;
                        }


                    }

                    // Exit Bank
                    PressKeyRandom(KeyEvent.VK_ESCAPE);
                    WaitForBankClose();

                    Delay(rand100to175);

                }
                break;


                case "mix": {

                    // Open Inventory If It is Closed
                    if (HelperRegion.currentInventoryTab != 3) {
                        HelperInput.PressKeyRandom(KeyEvent.VK_ESCAPE);
                    } else {
                        Delay(rand45to75);
                    }


                    WidgetItem item1 = inventory.getWidgetItem(0);
                    WidgetItem item14 = inventory.getWidgetItem(13);
                    WidgetItem item15 = inventory.getWidgetItem(14);


                    if (item14.getId() == config.herbs().getClean() && item15.getId() == ItemID.VIAL_OF_WATER) {

                        FindWidgetItemAndClick(item14, client,false);
                        Delay(rand45to75);
                        FindWidgetItemAndClick(item15, client,true);

                        long f1 = System.currentTimeMillis();
                        while (true) {
                            long f2 = System.currentTimeMillis();
                            boolean f = HelperWidget.dialogOpen;

                            if (shouldStop || f) {
                                break;
                            }

                            if (f1 + 1250 < f2) {
                                f1 = System.currentTimeMillis();
                                if (item14.getId() == config.herbs().getClean() && item15.getId() == ItemID.VIAL_OF_WATER) {

                                    FindWidgetItemAndClick(item14, client,false);
                                    Delay(rand45to75);
                                    FindWidgetItemAndClick(item15, client,true);
                                }
                            }
                            Delay(rand45to75);
                        }


                    } else if (item1.getId() == config.herbs().getClean() && item15.getId() == ItemID.VIAL_OF_WATER) {
                        FindWidgetItemAndClick(item1, client,false);
                        Delay(rand45to75);
                        FindWidgetItemAndClick(item15, client,true);

                        long f1 = System.currentTimeMillis();
                        while (true) {
                            long f2 = System.currentTimeMillis();
                            boolean f = HelperWidget.dialogOpen;

                            if (shouldStop || f) {
                                break;
                            }

                            if (f1 + 1250 < f2) {
                                f1 = System.currentTimeMillis();
                                if (item14.getId() == config.herbs().getClean() && item15.getId() == ItemID.VIAL_OF_WATER) {

                                    FindWidgetItemAndClick(item1, client,false);
                                    Delay(rand45to75);
                                    FindWidgetItemAndClick(item15, client,true);
                                }
                            }
                            Delay(rand45to75);
                        }


                    } else {
                        break;
                    }

                    while (!chatCraftOptionOne) {
                        if (shouldStop) {
                            break;
                        }
                        Delay(rand20to40);

                    }

                    // Space
                    Delay(rand215to280);
                    PressKeyRandom(KeyEvent.VK_SPACE);

                    // Check for Space
                    long s = System.currentTimeMillis();
                    int len = HelperInventory.CurrentInventoryLen;
                    while (len == 28) {
                        long s1 = System.currentTimeMillis();
                        if (shouldStop) {
                            break;
                        }
                        len = HelperInventory.CurrentInventoryLen;
                        if (s + rand900to1100 < s1) {
                            s = System.currentTimeMillis();
                            PressKeyRandom(KeyEvent.VK_SPACE);
                        }
                        Delay(rand35to70);
                    }


                    // Wait for pots
                    while (HelperInventory.CurrentInventoryLen > 14) {
                        if (shouldStop) {
                            break;
                        }
                        Delay(rand35to70);
                    }

                    Delay(rand35to70);

                    // Bank
                    HelperRegion.WaitForBankOpen(client);

                    Delay(rand35to70);

                    // Deposit
                    FindWidgetItemAndClick(bankDepositInventory,client,false);


                    Delay(rand35to70);

                }
                break;

            }
        }


    }

}