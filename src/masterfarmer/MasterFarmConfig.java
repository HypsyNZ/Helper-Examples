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

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
import net.runelite.client.plugins.helpers.HelperInput;


@ConfigGroup("MasterFarm")
public interface MasterFarmConfig extends Config {

    @ConfigItem(
            keyName = "keyForSwap",
            name = "Run Key",
            description = "The key to use to start/stop the farmer",
            position = 1
    )
    default HelperInput.KeyToPress runKey() {
        return HelperInput.KeyToPress.END;
    }

    @ConfigItem(
            keyName = "MasterFarmAltarBuiltIn",
            name = "Master Farmer Auto-Farm",
            description = "Auto-farm the Master Farmer",
            hidden = true
    )
    default boolean RunAltarBuiltIn() {
        return false;
    }

    @Range(
            min = 20
    )
    @ConfigItem(
            keyName = "foodAction",
            name = "Hitpoints Percent",
            description = "Determines what HP percent we should eat"
    )
    default int foodAction() {
        return 20;
    }

    @ConfigItem(

            keyName = "wireFrame",
            name = "Player Wire Frame",
            description = "Player Wire Frame"
    )
    default boolean toggleWireFrame() {
        return false;
    }

    @ConfigItem(

            keyName = "litFarmer",
            name = "Highlight Master Farmer",
            description = "Player Wire Frame"
    )
    default boolean litMasterFarmer() {
        return true;
    }

    @ConfigItem(

            keyName = "showItems",
            name = "Items in Panel",
            description = "Show items and their values in the Panel"
    )
    default boolean showItemsPanel() {
        return true;
    }

    @ConfigItem(

            keyName = "xpTracker",
            name = "XP Stats in Panel",
            description = "Show XP per hour and time to level in Panel"
    )
    default boolean showXPPanel() {
        return true;
    }

    @ConfigItem(

            keyName = "showStats",
            name = "Show Plugin Stats",
            description = "Show the status of the plugin"
    )
    default boolean showStats() {
        return true;
    }

}
