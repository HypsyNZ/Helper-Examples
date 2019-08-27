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
package net.runelite.client.plugins.glass;

import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.helpers.*;

import java.awt.event.KeyEvent;

import static net.runelite.client.plugins.helpers.HelperBank.*;
import static net.runelite.client.plugins.helpers.HelperDelay.rand1to100;
import static net.runelite.client.plugins.helpers.HelperFind.FindWidgetItemAndClick;
import static net.runelite.client.plugins.helpers.HelperInput.Delay;
import static net.runelite.client.plugins.helpers.HelperWidget.*;
import static net.runelite.client.plugins.runepouch.RunepouchOverlay.AstralAmount;

class gActions implements Runnable {


    private final Client client;
    private final gConfig gConfig;


    gActions(Client client, gConfig gConfig) {
        this.client = client;
        this.gConfig = gConfig;
    }


    public void run() {

        if (HelperWidget.shouldStop) {
            return;
        }

        Widget sand = bWidgetByID(1783,client);
        Widget seaweed = bWidgetByID(401,client);
        Widget astral = bWidgetByID(9075,client);

        int seaWeedQuantity = bQuanByWidget(seaweed);
        int sandQuantity = bQuanByWidget(sand);
        int astralQuantity = bQuanByWidget(astral);

        int astralAmountFromPlugin = AstralAmount;

        Delay(rand1to100);
        if (gConfig.useBankFillers()) {
            if (sandQuantity >= 13 && seaWeedQuantity >= 13 && astralAmountFromPlugin >= 2) {

                FindWidgetItemAndClick(sand,client,true);
                Delay(HelperDelay.rand35to70);
                FindWidgetItemAndClick(seaweed,client,true);

            } else {
                HelperWidget.shouldStop = true;
                return;
            }
        } else {
            if (sandQuantity >= 13 && seaWeedQuantity >= 13 && astralQuantity >= 2) {

                FindWidgetItemAndClick(sand,client,true);
                Delay(HelperDelay.rand35to70);
                FindWidgetItemAndClick(seaweed,client,true);
                Delay(HelperDelay.rand35to70);
                FindWidgetItemAndClick(astral,client,true);

            } else {
                HelperWidget.shouldStop = true;
                return;
            }
        }
        Delay(HelperDelay.rand175to300);

        // Exit Bank
        HelperInput.PressKeyRandom(KeyEvent.VK_ESCAPE);

        // Wait For Bank Close
        HelperRegion.WaitForBankClose();

        // Magic Tab
        if (HelperRegion.currentInventoryTab != 6) {

            HelperInput.PressKeyRandom(KeyEvent.VK_F6);
            Delay(HelperDelay.rand125to200);

        }

        // Click on Glass Make Spell
        FindWidgetItemAndClick(superGlassMake,client,false);
        Delay(HelperDelay.rand1500to1700);
        Delay(HelperDelay.rand700to900);
        Delay(HelperDelay.rand100to500);

        // Open Bank
        HelperRegion.WaitForBankOpen(client);
        Delay(rand1to100);

        // Deposit Glass
        FindWidgetItemAndClick(bankDepositInventory,client,false);
        Delay(rand1to100);
    }
}


