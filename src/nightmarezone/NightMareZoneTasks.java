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
package net.runelite.client.plugins.nightmarezone;


import net.runelite.api.Client;
import net.runelite.api.VarPlayer;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.helpers.HelperInput;
import net.runelite.client.plugins.helpers.HelperRegion;
import net.runelite.client.plugins.helpers.HelperWidget;

import java.awt.event.KeyEvent;

import static net.runelite.api.ItemID.*;
import static net.runelite.client.plugins.helpers.HelperDelay.*;
import static net.runelite.client.plugins.helpers.HelperFind.FindWidgetItemAndClick;
import static net.runelite.client.plugins.helpers.HelperInput.Click;
import static net.runelite.client.plugins.helpers.HelperInput.Delay;
import static net.runelite.client.plugins.helpers.HelperWidget.*;


class NightMareZoneTasks implements Runnable {

    private final Client client;
    private final String nameOfTask;
    private final NightmareZoneConfig config;

    /**
     * Tasks to be completed by this plugin
     *
     * @param client     Instance of Client
     * @param nameOfTask Name of the Task
     * @param config     Instance of Config
     */
    NightMareZoneTasks(Client client, String nameOfTask, NightmareZoneConfig config) {
        this.client = client;
        this.nameOfTask = nameOfTask;
        this.config = config;
    }

    public void run() {
        if (HelperWidget.shouldStop) {
            return;
        }

        Delay(rand175to300);
        Delay(rand1to100);

        Widget inventoryWidget = HelperWidget.inventory;
        if (inventoryWidget != null) {

            switch (nameOfTask) {

                case "absorb": {

                    int doses = config.absorbDosesToDrink();
                    int di = 0;

                    for (WidgetItem item : inventory.getWidgetItems()) {
                        int id = item.getId();
                        if (id == ABSORPTION_4 || id == ABSORPTION_3 || id == ABSORPTION_2 || id == ABSORPTION_1) {
                            while (di < doses && id > 0) {
                                FindWidgetItemAndClick(item, client,true);
                                id = item.getId();
                                di++;
                                Delay(rand900to1100);
                                Delay(rand1to100);

                            }

                        }


//                        int a4 = 0;
//                        int a3 = 0;
//                        int a2 = 0;
//                        int a1 = 0;
//
//                        if (id == ABSORPTION_4) {
//                            while (a4 < 4 && di < doses) {
//                                FindWidgetItemAndClick(item, client);
//                                di++;
//                                a4++;
//                                Delay(rand900to1100);
//                                Delay(rand1to100);
//                            }
//
//                        } else if (id == ABSORPTION_3) {
//                            while (a3 < 3 && di < doses) {
//                                FindWidgetItemAndClick(item, client);
//                                di++;
//                                a3++;
//                                Delay(rand900to1100);
//                                Delay(rand1to100);
//                            }
//
//                        } else if (id == ABSORPTION_2) {
//                            while (a2 < 2 && di < doses) {
//                                FindWidgetItemAndClick(item, client);
//                                di++;
//                                a2++;
//                                Delay(rand900to1100);
//                                Delay(rand1to100);
//                            }
//
//                        } else if (id == ABSORPTION_1) {
//                            while (a1 < 1 && di < doses) {
//                                FindWidgetItemAndClick(item, client);
//                                di++;
//                                a1++;
//                                Delay(rand900to1100);
//                                Delay(rand1to100);
//                            }
//                        }
                    }
                }
                break;


                case "rockcake": {

                    Delay(rand400to1700);

                    while (NightmareZoneTaskMan.map.get(nameOfTask) && !HelperRegion.isMoving) {

                        for (WidgetItem item : inventoryWidget.getWidgetItems()) {

                            if (!HelperRegion.isMoving) {
                                int id = item.getId();
                                if (id == DWARVEN_ROCK_CAKE_7510 || id == DWARVEN_ROCK_CAKE) {

                                    // Open Inventory If It is Closed
                                    if (HelperRegion.currentInventoryTab != 3) {
                                        HelperInput.PressKeyRandom(KeyEvent.VK_ESCAPE);
                                    }

                                    // Click on Rock Cake
                                    FindWidgetItemAndClick(item, client,true);
                                    Delay(rand175to300);
                                    Delay(200);
                                    Delay(rand1to100);
                                }

                            }
                        }
                    }

                }
                break;

                case "spec": {

                    if (client.getVar(VarPlayer.SPECIAL_ATTACK_PERCENT) >= 50) {
                        LocalPoint me = client.getLocalPlayer().getLocalLocation();
                        LocalPoint opp = opponentPoint;

                        while (true) {

                            boolean click = false;

                            if (me.getX() - opp.getX() <= 2 && me.getX() - opp.getX() > 0) {
                                click = true;
                            }

                            if (opp.getX() - me.getX() <= 2 && opp.getX() - me.getX() > 0) {
                                click = true;
                            }

                            if (click) {
                                Delay(rand400to1700);
                                FindWidgetItemAndClick(specialAttackToggle,client,true);
                                Delay(rand900to1100);
                                break;
                            }
                        }
                    }

                }
                break;
            }
        }


    }

}