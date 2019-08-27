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
package net.runelite.client.plugins.ourania;

import net.runelite.api.widgets.Widget;
import net.runelite.client.plugins.helpers.Octagon;

import java.util.ArrayList;
import java.util.List;

class OuraniaItemsBank {
    private static void Items() {
        OuraniaBankItemsList.clear();
        OuraniaBankItemsList.add(new Octagon<>("Pure Essence ", 7936, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("Stamina Pot (4) ", 12625, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("Stamina Pot (3)", 12627, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("Stamina Pot (2) ", 12629, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("Energy Pot (4)", 3016, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("Energy Pot (3)", 3018, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("Energy Pot (2)", 3020, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("Monkfish", 7946, 0, 0, null, 0, 0, 0));

        OuraniaBankItemsList.add(new Octagon<>("D Pouch", 5515, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("G Pouch", 5514, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("L Pouch", 5512, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("M Pouch", 5510, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("S Pouch", 5509, 0, 0, null, 0, 0, 0));

        OuraniaBankItemsList.add(new Octagon<>("Fire Rune", 554, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("Water Rune", 555, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("Air Rune", 556, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("Earth Rune", 557, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("Mind Rune", 558, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("Body Rune", 559, 0, 0, null, 0, 0, 0));

        OuraniaBankItemsList.add(new Octagon<>("Death Rune", 560, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("Nature Rune", 561, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("Chaos Rune", 562, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("Law Rune", 563, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("Cosmic Rune", 564, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("Blood Rune", 565, 0, 0, null, 0, 0, 0));
        OuraniaBankItemsList.add(new Octagon<>("Soul Rune", 566, 0, 0, null, 0, 0, 0));
    }

    static void initItems() {
        Items();
    }

    static List<Octagon<String, Integer, Integer, Integer, Widget, Integer, Integer, Integer>> OuraniaBankItemsList = new ArrayList<>();

}
