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

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
import net.runelite.client.plugins.helpers.HelperInput;


@ConfigGroup("g")
public interface gConfig extends Config {

    @ConfigItem(
            keyName = "runKey",
            name = "Run Key",
            description = "The key to start the glass maker",
            position = 1
    )
    default HelperInput.KeyToPress keyForSwap() {
        return HelperInput.KeyToPress.END;
    }

    @Range(
            min = 200
    )
    @ConfigItem(
            keyName = "castProfit",
            name = "Wiki PPC",
            description = "The profit per cast to calculate profit per hour with",
            position = 2
    )
    default int profitPerCast() {
        return 20;
    }

    @ConfigItem(
            keyName = "astralCost",
            name = "Astral Rune Cost",
            description = "The actual cost you are paying for astral runes",
            position = 3
    )
    default int astralCost() {
        return 140;
    }

    @ConfigItem(
            keyName = "sandCost",
            name = "Bucket of Sand Cost",
            description = "The actual cost you are paying for buckets of sand",
            position = 4
    )
    default int sandCost() {
        return 40;
    }

    @ConfigItem(
            keyName = "seaweedCost",
            name = "Seaweed Cost",
            description = "The actual cost you are paying for seaweed",
            position = 5
    )
    default int seaweedCost() {
        return 40;
    }

    @ConfigItem(
            keyName = "moltenSellPrice",
            name = "Molten Sell Price",
            description = "The actual price you are getting for Molten Glass",
            position = 6
    )
    default int moltenPrice() {
        return 140;
    }

    @ConfigItem(

            keyName = "manualGuessProfit",
            name = "Use Manual PPC",
            description = "Guess the profit per cast from inputs instead of Wiki PPC",
            position = 7
    )
    default boolean useManualGuessProfit() {
        return false;
    }

    @ConfigItem(

            keyName = "bankFillers",
            name = "Use Bank Fillers",
            description = "Use Bank Filler instead of widrawing Astral rune",
            position = 8
    )
    default boolean useBankFillers() {
        return false;
    }

    @ConfigItem(

            keyName = "showAstralsLeftInPouch",
            name = "Show Astrals In Pouch",
            description = "Show the number of astrals remaining in the runepouch in the side menu",
            position = 9
    )
    default boolean showAstralsLeftInPouch() {
        return false;
    }

    @ConfigItem(

            keyName = "showProfitInPanel",
            name = "Show Manual PPC",
            description = "Shows the manual profit per cast in the overlay panel",
            position = 10
    )
    default boolean showProfitInPanel() {
        return false;
    }

    @ConfigItem(

            keyName = "wireFrame",
            name = "Show Player Frame",
            description = "Shows the Player Wire Frame",
            position = 11
    )
    default boolean toggleWireFrame() {
        return false;
    }

    @ConfigItem(

            keyName = "showStats",
            name = "Show Plugin Stats",
            description = "Show the status of the plugin",
            position = 12
    )
    default boolean showStats() {
        return true;
    }

    @ConfigItem(

            keyName = "showGlassValue",
            name = "Show Glass Value",
            description = "Show the value of molten glass in the bank",
            position = 13
    )
    default boolean showGlassValue() {
        return true;
    }

    @ConfigItem(

            keyName = "showMatsRemaining",
            name = "Show Mats Remaining",
            description = "Shows how long we have until we run out of materials",
            position = 14
    )
    default boolean showMatsRemaining() {
        return true;
    }

    @ConfigItem(

            keyName = "showSessionTime",
            name = "Show Sesh Timer",
            description = "Shows how long its been since the plugin started",
            position = 15
    )
    default boolean showSessionTime() {
        return true;
    }

    @ConfigItem(

            keyName = "showEXP",
            name = "Show EXP Stats",
            description = "Show the EXP per hour",
            position = 16

    )
    default boolean showExp() {
        return true;
    }


    @ConfigItem(
            keyName = "gAltarBuiltIn",
            name = "",
            description = "",
            hidden = true
    )
    default boolean RunAltarBuiltIn() {
        return false;
    }

}
