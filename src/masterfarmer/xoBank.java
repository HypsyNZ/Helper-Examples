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

import com.google.common.collect.ImmutableList;
import net.runelite.api.*;
import net.runelite.client.game.ItemManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.runelite.client.plugins.masterfarmer.xoItems.valueTotalBank;
import static net.runelite.client.plugins.masterfarmer.xoItems.valueTotalMinusValue;

class xoBank {

    private static final ImmutableList<Varbits> TAB_VARBITS = ImmutableList.of(
            Varbits.BANK_TAB_ONE_COUNT,
            Varbits.BANK_TAB_TWO_COUNT,
            Varbits.BANK_TAB_THREE_COUNT,
            Varbits.BANK_TAB_FOUR_COUNT,
            Varbits.BANK_TAB_FIVE_COUNT,
            Varbits.BANK_TAB_SIX_COUNT,
            Varbits.BANK_TAB_SEVEN_COUNT,
            Varbits.BANK_TAB_EIGHT_COUNT,
            Varbits.BANK_TAB_NINE_COUNT
    );

    static boolean SessionStarted = false;
    private boolean sessionStarting = false;
    private int itemsHash;

    List<Penta<String, Integer, Integer, Integer, Integer>> CheckItemAvailableInBank(Client client, ItemManager itemManager, List<Penta<String, Integer, Integer, Integer, Integer>> pentaKill) {
        ItemContainer bankInventory = client.getItemContainer(InventoryID.BANK);

        if (bankInventory == null) {
            return pentaKill;
        }

        Item[] items = bankInventory.getItems();
        int currentTab = client.getVar(Varbits.CURRENT_BANK_TAB);

        if (currentTab > 0) {
            int startIndex = 0;

            for (int i = currentTab - 1; i > 0; i--) {
                startIndex += client.getVar(TAB_VARBITS.get(i - 1));
            }

            int itemCount = client.getVar(TAB_VARBITS.get(currentTab - 1));
            items = Arrays.copyOfRange(items, startIndex, startIndex + itemCount);
        }

        if (items.length == 0) {
            return pentaKill;
        }
        if (!isBankDifferent(items)) {
            return pentaKill;
        }

        valueTotalBank = 0;

        for (Item item : items) {

            int id = item.getId();

            for (int i2 = 0; i2 < pentaKill.size(); i2++) {

                if (id == pentaKill.get(i2).getSecond()) {

                    int quantity = item.getQuantity();
                    int gePrice = itemManager.getItemPrice(pentaKill.get(i2).getSecond());
                    int value = gePrice * quantity;


                    if (!SessionStarted) {

                        sessionStarting = true;
                        pentaKill.set(i2, new Penta<>(pentaKill.get(i2).getFirst(), pentaKill.get(i2).getSecond(), value, value, pentaKill.get(i2).getFifth()));

                    } else {

                        pentaKill.set(i2, new Penta<>(pentaKill.get(i2).getFirst(), pentaKill.get(i2).getSecond(), value, pentaKill.get(i2).getFourth(), pentaKill.get(i2).getFifth()));
                    }

                    valueTotalBank = valueTotalBank + pentaKill.get(i2).getThird();

                }

            }

        }

        if (sessionStarting) {

            sessionStarting = false;
            valueTotalMinusValue = valueTotalBank;

            if (!SessionStarted) {
                SessionStarted = true;
            }

        }

        valueTotalBank = valueTotalBank - valueTotalMinusValue;
        return pentaKill;
    }

    private boolean isBankDifferent(Item[] items) {

        Map<Integer, Integer> mapCheck = new HashMap<>();

        for (Item item : items) {
            mapCheck.put(item.getId(), item.getQuantity());
        }

        int curHash = mapCheck.hashCode();

        if (curHash != itemsHash) {
            itemsHash = curHash;
            return true;
        }

        return false;
    }

}
