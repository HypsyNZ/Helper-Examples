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

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;


@ConfigGroup("ourania")
public interface OuraniaConfig extends Config {
    int ACTION_LIMIT_MAX = 100;

    @ConfigItem(
            keyName = "laps",
            name = "",
            description = "",
            hidden = true
    )
    default int laps() {
        return 0;
    }


    @ConfigItem(
            keyName = "laps",
            name = "",
            description = ""
    )
    void laps(int laps);


    @ConfigItem(
            position = 1,
            keyName = "hideAll2D",
            name = "Hide all 2D Entities",
            description = "Configures whether or not all 2D Entities are hidden"
    )
    default boolean hideAll2DEntities() {
        return true;
    }


    @ConfigItem(
            position = 2,
            keyName = "hideAttackers",
            name = "Hide Attackers",
            description = "Hides all Attackers and Projectiles"
    )
    default boolean hideAttackers() {
        return false;
    }


    @ConfigItem(
            position = 3,
            keyName = "hideFriendsAndLocal",
            name = "Hide Friends and Local",
            description = "Hide Friends, Clan and Local Player"
    )
    default boolean hideFriendsAndLocal() {
        return false;
    }


    @ConfigItem(
            position = 4,
            keyName = "PrayAltar",
            name = "Pray Altar Enabled",
            description = "Enable Pray at Altar"
    )
    default boolean PrayEnabled() {
        return false;
    }


    @ConfigItem(
            position = 5,
            keyName = "OuraniaAltarBuiltIn",
            name = "Ourania Altar Built In",
            description = "ZMI Altar Runner"
    )
    default boolean RunAltarBuiltIn() {
        return false;
    }


    @ConfigItem(
            position = 6,
            keyName = "OuraniaAltarTeleport",
            name = "Auto Teleport",
            description = "ZMI Altar Runner"
    )
    default boolean RunAltarAutoTele() {
        return false;
    }


    @ConfigItem(
            position = 7,
            keyName = "OuraniaAltarFood",
            name = "Auto Eat Monkfish",
            description = "Automatically Eat Monkfish (Bank Tab 2, first slot)"
    )
    default boolean AutoFood() {
        return true;
    }


    @ConfigItem(
            position = 8,
            keyName = "OuraniaAltarStam",
            name = "Auto Stamina",
            description = "Automatically Stamina Pot (Bank tab 2, second row, slots 1-2)"
    )
    default boolean AutoStam() {
        return true;
    }


    @ConfigItem(
            position = 9,
            keyName = "OuraniaAltarEnergy",
            name = "Auto Energy",
            description = "Automatically Energy Pot (Bank tab 2, third row, slots 1-2)"
    )
    default boolean AutoEnergy() {
        return true;
    }


    @Range(
            min = 15,
            max = ACTION_LIMIT_MAX
    )
    @ConfigItem(
            keyName = "teleAction",
            name = "Teleport Percent",
            description = "Determines what HP percent we should teleport",
            position = 10
    )
    default int teleAction() {
        return 0;
    }


    @Range(
            min = 20,
            max = ACTION_LIMIT_MAX
    )
    @ConfigItem(
            keyName = "foodAction",
            name = "Hitpoints Percent",
            description = "Determines what HP percent we should eat",
            position = 11
    )
    default int foodAction() {
        return 0;
    }


    @Range(
            min = 20,
            max = ACTION_LIMIT_MAX
    )
    @ConfigItem(
            keyName = "energyAction",
            name = "Minimum Energy",
            description = "Determines what energy percent we should Auto pot our energy to near 100",
            position = 12
    )
    default int energyAction() {
        return 0;
    }


    @Range(
            min = 220,
            max = 1000
    )
    @ConfigItem(
            keyName = "staminaAction",
            name = "Stamina Buff Ticks",
            description = "220 will Stam buff every time you lose it, 270+ will end up using Energy Pots",
            position = 13
    )
    default int staminaAction() {
        return 0;
    }


    @ConfigItem(
            position = 14,
            keyName = "UseBankFillers",
            name = "Use Bank Fillers",
            description = "Use Bank Fillers and deposit all instead of clicking on individual runes to deposit"
    )
    default boolean UseBankFillers() {
        return true;
    }


    @ConfigItem(
            position = 15,
            keyName = "OnlyShowValueInBank",
            name = "Show Value Bank Only",
            description = "Only show item total values with the bank open"
    )
    default boolean ShowValueBankOnly() {
        return false;
    }


    @ConfigItem(
            position = 16,
            keyName = "ShowStamBuffWarning",
            name = "Stam Buff Warning",
            description = "Show a warning when you don't have a stamina pot buff"
    )
    default boolean ShowStamBuffWarning() {
        return true;
    }


    @ConfigItem(
            position = 17,
            keyName = "ShowFoodWarning",
            name = "Food Warning",
            description = "Show a warning when you are low on health"
    )
    default boolean ShowFoodWarning() {
        return true;
    }


    @ConfigItem(
            position = 18,
            keyName = "ShowDegradeWarning",
            name = "Degrade Warning",
            description = "Show a warning when your giant pouch has degraded"
    )
    default boolean ShowDegradeWarning() {
        return true;
    }


    @ConfigItem(
            position = 19,
            keyName = "ShowPotWarning",
            name = "Potions Low Warning",
            description = "Show a warning when you need to buy stam/energy potions"
    )
    default boolean ShowPotWarning() {
        return true;
    }


}
