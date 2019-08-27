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

import java.util.ArrayList;
import java.util.List;

class xoItems {
    private static void Items() {
        ItemsList.clear();
        ItemsList.add(new Penta<>("Ranarr ", 5295, 0, 0, 0));
        ItemsList.add(new Penta<>("Snapdragon ", 5300, 0, 0, 0));
        ItemsList.add(new Penta<>("Torstol ", 5304, 0, 0, 0));
        ItemsList.add(new Penta<>("Lantadyme ", 5302, 0, 0, 0));
        ItemsList.add(new Penta<>("Avantoe ", 5298, 0, 0, 0));
        ItemsList.add(new Penta<>("Limpwurt ", 5100, 0, 0, 0));
        ItemsList.add(new Penta<>("Kwuarm ", 5299, 0, 0, 0));
        ItemsList.add(new Penta<>("Cadantine ", 5301, 0, 0, 0));
        ItemsList.add(new Penta<>("DwarfWeed ", 5303, 0, 0, 0));
        ItemsList.add(new Penta<>("ToadFlax ", 5296, 0, 0, 0));
        ItemsList.add(new Penta<>("Snapegrass ", 22879, 0, 0, 0));
        ItemsList.add(new Penta<>("Cactus ", 5280, 0, 0, 0));
        ItemsList.add(new Penta<>("Strawberry ", 5323, 0, 0, 0));
        ItemsList.add(new Penta<>("Watermelon ", 5321, 0, 0, 0));
    }

    static void initItems() {
        Items();
        valueTotalInventory = 0;
        valueTotalBank = 0;
        valueTotalMinusValue = 0;
        xoBank.SessionStarted = false;
    }

    static List<Penta<String, Integer, Integer, Integer, Integer>> ItemsList = new ArrayList<>();
    static int valueTotalInventory = 0;
    static int valueTotalBank = 0;
    static int valueTotalMinusValue = 0;
}
