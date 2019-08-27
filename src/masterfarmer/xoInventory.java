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
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.game.ItemManager;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.runelite.client.plugins.helpers.HelperFind.FindWidgetItemAndClick;
import static net.runelite.client.plugins.masterfarmer.xoItems.valueTotalInventory;

class xoInventory {
    private int itemsHash;
    static int currentInvlen = 0;

    static void DiscardItem(List<Integer> itemsToDiscard, Client client) {
        // Load inventory widget and discard runes we don't want and empty pouches
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        if (inventoryWidget != null) {

            for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                int id = item.getId();

                for (Integer seed : itemsToDiscard) {
                    if (id == seed) {
                        FindWidgetItemAndClick(item, client,true);
                    }
                }

            }

        }
    }

    List<Penta<String, Integer, Integer, Integer, Integer>> InventoryContainerSearch(Client client, ItemManager itemManager, List<Penta<String, Integer, Integer, Integer, Integer>> penta) {
        ItemContainer equipment = client.getItemContainer(InventoryID.INVENTORY);

        if (equipment == null) {
            return penta;
        }

        Item[] items = equipment.getItems();
        if (items == null) {
            return penta;
        }

        if (!isInventoryDifferent(items)) {
            return penta;
        }

        currentInvlen = items.length;
        valueTotalInventory = 0;

        for (int i2 = 0; i2 < penta.size(); i2++) {
            penta.set(i2, new Penta<>(penta.get(i2).getFirst(), penta.get(i2).getSecond(), penta.get(i2).getThird(), penta.get(i2).getFourth(), 0));
        }

        for (Item item : items) {
            if (item.getId() <= 0) {
                currentInvlen = currentInvlen - 1;
            } else {

                int id = item.getId();
                for (int i2 = 0; i2 < penta.size(); i2++) {
                    if (id == penta.get(i2).getSecond()) {
                        int quantity = item.getQuantity();
                        int gePrice = itemManager.getItemPrice(penta.get(i2).getSecond());
                        int value = gePrice * quantity;
                        penta.set(i2, new Penta<>(penta.get(i2).getFirst(), penta.get(i2).getSecond(), penta.get(i2).getThird(), penta.get(i2).getFourth(), value));
                        valueTotalInventory = valueTotalInventory + penta.get(i2).getFifth();
                    }

                }
            }
        }

        return penta;
    }

    private boolean isInventoryDifferent(Item[] items) {

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