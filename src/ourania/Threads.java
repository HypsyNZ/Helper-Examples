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

import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.helpers.*;

import java.awt.*;
import java.awt.event.KeyEvent;

import static net.runelite.client.plugins.helpers.HelperDelay.*;
import static net.runelite.client.plugins.helpers.HelperFind.FindWidgetItemAndClick;
import static net.runelite.client.plugins.helpers.HelperInput.*;
import static net.runelite.client.plugins.helpers.HelperInventory.CurrentInventoryLen;
import static net.runelite.client.plugins.helpers.HelperRegion.*;
import static net.runelite.client.plugins.helpers.HelperWidget.*;
import static net.runelite.client.plugins.ourania.OuraniaItemsBank.OuraniaBankItemsList;
import static net.runelite.client.plugins.ourania.OuraniaOverlay.*;
import static net.runelite.client.plugins.ourania.OuraniaOverlayMenu.currentStatus;


class InventorySearchDeposit implements Runnable {

    private final Client client;
    private final OuraniaConfig ouraniaConfig;

    InventorySearchDeposit(Client client, OuraniaConfig ouraniaConfig) {

        this.client = client;
        this.ouraniaConfig = ouraniaConfig;

    }

    public void run() {
        if (CurrentInventoryLen > 7) {
            currentStatus = "Deposit, BankStep";
            if (HelperWidget.shouldStop) {
                currentStatus = "Waiting..";
                return;
            }
            if (ouraniaConfig.UseBankFillers()) {
                FindWidgetItemAndClick(HelperWidget.bankDepositInventory,client, false);
                Delay(rand175to300);
            } else {
                Widget inventoryWidget = HelperWidget.inventory;
                if (inventoryWidget == null) {
                    return;
                }

                for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                    Rectangle slotBounds = item.getCanvasBounds();
                    int id = item.getId();

                    if (id == 556 || id == 558 || id == 566 || id == 564 || id == 562 || id == 9075 || id == 561 || id == 563 || id == 560 || id == 565
                            || id == 555 || id == 554 || id == 559 || id == 557 || id == 7936) {

                        int[] xy = HelperTransform.xyFromEdge(slotBounds, client);
                        Click(xy[0], xy[1]);
                    }
                }
            }
            currentStatus = "Waiting..";
        }
    }
}

class SearchEss implements Runnable {

    private final Client client;
    private final Graphics2D graphics;
    private final OuraniaConfig ouraniaConfig;
    private final ConfigManager configManager;

    SearchEss(Client client, Graphics2D graphics, ConfigManager configManager, OuraniaConfig config) {

        this.client = client;
        this.graphics = graphics;
        this.configManager = configManager;
        this.ouraniaConfig = config;

    }

    public void run() {

        try {

            // Withdraw Ess Bank
            int[] pureEssLocation = HelperBank.bXYbyID(7936, OuraniaBankItemsList);
            Click(pureEssLocation[0], pureEssLocation[1]);

            // Exit Bank
            PressKeyRandom(KeyEvent.VK_ESCAPE);

            // Wait For Bank Close
            WaitForBankClose();

            // Fill small
            Widget inventoryWidget = HelperWidget.inventory;
            if (inventoryWidget != null) {
                currentStatus = "Filling, Pouches";
                if (HelperWidget.shouldStop) {
                    currentStatus = "Waiting..";
                    return;
                }
                // Fill Small
                for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                    int id = item.getId();
                    if (id == 5509) {
                         FindWidgetItemAndClick(item, client, true);
                    }
                }

                // Fill Med
                for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                    int id = item.getId();
                    if (id == 5510 || id == 5511) {
                         FindWidgetItemAndClick(item, client, true);
                    }
                }

                // Fill Large
                for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                    int id = item.getId();
                    if (id == 5512 || id == 5513) {
                         FindWidgetItemAndClick(item, client, true);
                    }
                }

                // Fill Giant
                for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                    int id = item.getId();
                    if (id == 5514 || id == 5515) {
                         FindWidgetItemAndClick(item, client, true);
                    }
                }

            }
            currentStatus = "Waiting..";

            // Open Bank
            WaitForBankOpen(client);

            // Withdraw Ess Bank
            Click(pureEssLocation[0], pureEssLocation[1]);

            // Exit Bank
            PressKeyRandom(KeyEvent.VK_ESCAPE);

            // Wait For Bank Close
            WaitForBankClose();

            // Fill Giant Only
            if (HelperWidget.shouldStop) {
                currentStatus = "Waiting..";
                return;
            }
            if (inventoryWidget != null) {
                currentStatus = "Filling, Pouches";
                for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                    int id = item.getId();
                    if (id == 5514 || id == 5515) {
                         FindWidgetItemAndClick(item, client, true);
                    }
                }
                currentStatus = "Waiting..";
            }

            // Open Bank
            WaitForBankOpen(client);

            if (HelperWidget.shouldStop) {
                currentStatus = "Waiting..";
                return;
            }

            // Withdraw Ess Bank
            Click(pureEssLocation[0], pureEssLocation[1]);

            // Exit Bank
            PressKeyRandom(KeyEvent.VK_ESCAPE);

            // Wait For Bank Close
            WaitForBankClose();

            // Turn on run
            if (HelperWidget.runningEnabled <= 0) {
                FindWidgetItemAndClick(runningToggle,client,false);
            }

            if (HelperWidget.shouldStop) {
                currentStatus = "Waiting..";
                return;
            }
            // Click on "Tile One"

            int[] tileOne = HelperFind.ClickTileLargeArea(3014, 5598, graphics, 1, client);
            if (tileOne[0] != 0) {
                Click(tileOne[0], tileOne[1]);
            } else {
                Teleport(client, configManager, ouraniaConfig);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

class SearchStam implements Runnable {

    private final Client client;

    SearchStam(Client client) {

        this.client = client;

    }

    public void run() {

        try {

            if (HelperWidget.shouldStop) {
                currentStatus = "Waiting..";
                return;
            }

            int[] Stam4Location = HelperBank.bXYbyID(12625, OuraniaBankItemsList);
            int[] Stam3Location = HelperBank.bXYbyID(12627, OuraniaBankItemsList);
            int[] Stam2Location = HelperBank.bXYbyID(12629, OuraniaBankItemsList);
            currentStatus = "Withdraw, Stamina Pot";
            if (OuraniaOverlay.StamFour > 0) {
                if (Stam4Location[0] == 0) {
                    return;
                }
                ClickRightLeft(Stam4Location[0], Stam4Location[1], 1);
            } else if (OuraniaOverlay.StamThree > 0) {
                if (Stam3Location[0] == 0) {
                    return;
                }
                ClickRightLeft(Stam3Location[0], Stam3Location[1], 1);
            } else if (OuraniaOverlay.StamTwo > 0) {
                if (Stam2Location[0] == 0) {
                    return;
                }
                ClickRightLeft(Stam2Location[0], Stam2Location[1], 1);
            }

            // Exit Bank
            PressKeyRandom(KeyEvent.VK_ESCAPE);
            Delay(rand125to200);

            // Wait For Bank Close
            WaitForBankClose();

            Widget inventoryWidget = HelperWidget.inventory;
            if (HelperWidget.shouldStop) {
                currentStatus = "Waiting..";
                return;
            }
            if (inventoryWidget != null) {
                currentStatus = "Drinking, Stamina Pot";
                for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                    int id = item.getId();
                    if (id == 12625) {
                         FindWidgetItemAndClick(item, client,false);
                        Delay(rand1500to1700);
                    } else if (id == 12627) {
                         FindWidgetItemAndClick(item, client,false);
                        Delay(rand1500to1700);
                    } else if (id == 12629) {
                         FindWidgetItemAndClick(item, client,false);
                        Delay(rand1500to1700);
                    }
                }
                currentStatus = "Waiting..";
            }

            // Open Bank
            WaitForBankOpen(client);
            Delay(rand125to200);

            // Deposit Stam Pots
            if (HelperWidget.shouldStop) {
                currentStatus = "Waiting..";
                return;
            }
            if (inventoryWidget != null) {
                currentStatus = "Deposit, Stamina Pot";
                for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                    int id = item.getId();
                    if (id == 12625 || id == 12627 || id == 12629 || id == 12631) {
                         FindWidgetItemAndClick(item, client,false);
                        Delay(rand1500to1700);
                    }
                }
                currentStatus = "Waiting..";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}


class SearchEnergy implements Runnable {

    private final Client client;

    SearchEnergy(Client client) {

        this.client = client;

    }

    public void run() {

        try {
            Delay(rand125to200);

            // Withdraw 1
            FindWidgetItemAndClick(withdrawOptionOne,client,true);
            Delay(rand125to200);

            int[] Stam4Location = HelperBank.bXYbyID(12625, OuraniaBankItemsList);
            int[] Stam3Location = HelperBank.bXYbyID(12627, OuraniaBankItemsList);
            int[] Stam2Location = HelperBank.bXYbyID(12629, OuraniaBankItemsList);

            int[] Energy4Location = HelperBank.bXYbyID(3016, OuraniaBankItemsList);
            int[] Energy3Location = HelperBank.bXYbyID(3018, OuraniaBankItemsList);
            int[] Energy2Location = HelperBank.bXYbyID(3020, OuraniaBankItemsList);


            Delay(rand125to200);

            // Withdraw Stam
            if (HelperWidget.shouldStop) {
                currentStatus = "Waiting..";
                return;
            }
            if (OuraniaOverlay.StamFour > 0) {
                Click(Stam4Location[0], Stam4Location[1]);
            } else if (OuraniaOverlay.StamThree > 0) {
                Click(Stam3Location[0], Stam3Location[1]);
            } else if (OuraniaOverlay.StamTwo > 0) {
                Click(Stam2Location[0], Stam2Location[1]);
            }

            Delay(rand125to200);

            // Withdraw Energy
            if (HelperWidget.shouldStop) {
                currentStatus = "Waiting..";
                return;
            }
            if (OuraniaOverlay.EnergyFour > 0) {
                Click(Energy4Location[0], Energy4Location[1]);
            } else if (OuraniaOverlay.EnergyThree > 0) {
                Click(Energy3Location[0], Energy3Location[1]);
            } else if (OuraniaOverlay.EnergyTwo > 0) {
                Click(Energy2Location[0], Energy2Location[1]);
            }

            Delay(rand125to200);

            // Withdraw Energy
            if (HelperWidget.shouldStop) {
                currentStatus = "Waiting..";
                return;
            }
            if (OuraniaOverlay.EnergyFour > 0) {
                Click(Energy4Location[0], Energy4Location[1]);
            } else if (OuraniaOverlay.EnergyThree > 0) {
                Click(Energy3Location[0], Energy3Location[1]);
            } else if (OuraniaOverlay.EnergyTwo > 0) {
                Click(Energy2Location[0], Energy2Location[1]);
            }

            if (HelperWidget.shouldStop) {
                currentStatus = "Waiting..";
                return;
            }

            // Set Withdraw To All
            Delay(rand125to200);
            FindWidgetItemAndClick(withdrawOptionALL,client,true);
            Delay(rand125to200);

            // Exit Bank
            PressKeyRandom(KeyEvent.VK_ESCAPE);
            Delay(rand125to200);

            // Wait For Bank Close
            WaitForBankClose();


            // Drink Stam
            if (HelperWidget.shouldStop) {
                currentStatus = "Waiting..";
                return;
            }
            Widget inventoryWidget = HelperWidget.inventory;
            if (inventoryWidget != null) {
                currentStatus = "Drinking, Stamina Pot";
                for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                    int id = item.getId();
                    if (id == 12625) {
                         FindWidgetItemAndClick(item, client,true);
                        Delay(rand1500to1700);
                    } else if (id == 12627) {
                         FindWidgetItemAndClick(item, client,true);
                        Delay(rand1500to1700);
                    } else if (id == 12629) {
                         FindWidgetItemAndClick(item, client,true);
                        Delay(rand1500to1700);
                    }


                }

            }

            // Drink Energy til 100
            if (HelperWidget.shouldStop) {
                currentStatus = "Waiting..";
                return;
            }
            while (client.getEnergy() < 99) {
                currentStatus = "Drinking, Energy Pot";
                if (inventoryWidget != null) {

                    for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                        int id = item.getId();
                        if (id == 3016 && client.getEnergy() < 99) {
                             FindWidgetItemAndClick(item, client,true);
                            Delay(rand1500to1700);
                        } else if (id == 3018 && client.getEnergy() < 99) {
                             FindWidgetItemAndClick(item, client,true);
                            Delay(rand1500to1700);
                        } else if (id == 3020 && client.getEnergy() < 99) {
                             FindWidgetItemAndClick(item, client,true);
                            Delay(rand1500to1700);
                        }


                    }
                }

            }

            // Open Bank
            WaitForBankOpen(client);

            boolean ones = false, twos = false, threes = false, fours = false;
            boolean onee = false, twoe = false, threee = false, foure = false;
            // Deposit Stam Pots
            if (HelperWidget.shouldStop) {
                currentStatus = "Waiting..";
                return;
            }
            if (inventoryWidget != null) {

                for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                    int id = item.getId();


                    if (id == 12631 && !ones) {
                        ones = true;
                         FindWidgetItemAndClick(item, client,true);
                        Delay(rand125to200);
                    }

                    if (id == 12629 && !twos) {
                        twos = true;
                         FindWidgetItemAndClick(item, client,true);
                        Delay(rand125to200);
                    }

                    if (id == 12627 && !threes) {
                        threes = true;
                         FindWidgetItemAndClick(item, client,true);
                        Delay(rand125to200);
                    }

                    if (id == 12625 && !fours) {
                        fours = true;
                         FindWidgetItemAndClick(item, client,true);
                        Delay(rand125to200);
                    }


                }
            }

            if (HelperWidget.shouldStop) {
                currentStatus = "Waiting..";
                return;
            }

            // Deposit Energy
            if (inventoryWidget != null) {
                for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                    int id = item.getId();
                    if (id == 3022 && !onee) {
                        onee = true;
                         FindWidgetItemAndClick(item, client,true);
                        Delay(rand125to200);
                    }

                    if (id == 3020 && !twoe) {
                        twoe = true;
                         FindWidgetItemAndClick(item, client,true);
                        Delay(rand125to200);
                    }

                    if (id == 3018 && !threee) {
                        threee = true;
                         FindWidgetItemAndClick(item, client,true);
                        Delay(rand125to200);
                    }

                    if (id == 3016 && !foure) {
                        foure = true;
                         FindWidgetItemAndClick(item, client,true);
                        Delay(rand125to200);
                    }
                }
            }

            Delay(rand400);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}


class SearchFood implements Runnable {

    private final Client client;

    SearchFood(Client client) {

        this.client = client;
    }


    public void run() {

        int[] MonkfishLocation = HelperBank.bXYbyID(7946, OuraniaBankItemsList);

        // Withdraw Monkfish
        Click(MonkfishLocation[0], MonkfishLocation[1]);

        Delay(rand125to200);
        // Exit Bank
        PressKeyRandom(KeyEvent.VK_ESCAPE);
        Delay(rand125to200);

        // Wait For Bank Close
        HelperRegion.WaitForBankClose();

        Delay(rand700to900);
        boolean depoOne = false;

        // Click on Monkfish 7946
        Widget inventoryWidget = HelperWidget.inventory;
        if (inventoryWidget != null) {
            currentStatus = "Eating, Monkfish";
            if (HelperWidget.shouldStop) {
                currentStatus = "Waiting..";
                return;
            }
            for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                int id = item.getId();
                if (id == 7946 && client.getBoostedSkillLevel(Skill.HITPOINTS) < client.getRealSkillLevel(Skill.HITPOINTS)) {
                     FindWidgetItemAndClick(item, client,true);
                    Delay(rand1500to1700);
                }
            }
            currentStatus = "Waiting..";
        }


        // Open Bank
        HelperRegion.WaitForBankOpen(client);

        // Deposit
        if (inventoryWidget != null) {
            currentStatus = "Deposit, Monkfish";
            if (HelperWidget.shouldStop) {
                currentStatus = "Waiting..";
                return;
            }
            for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                int id = item.getId();
                if (depoOne) {
                    return;
                }
                if (id == 7946) {
                     FindWidgetItemAndClick(item, client,true);
                    Delay(rand1500to1700);
                    depoOne = true;
                }
            }
            currentStatus = "Waiting..";
        }

        Delay(rand400);


    }
}


class DepositBank implements Runnable {

    private final Client client;

    DepositBank(Client client) {

        this.client = client;

    }

    public void run() {

        // Open Inventory If It is Closed
        if (HelperRegion.currentInventoryTab != 3) {
            HelperInput.PressKeyRandom(KeyEvent.VK_ESCAPE);
        }

        // Find Banker in this Region and Wait for Bank to Open
        Delay(rand35to70);
        HelperRegion.WaitForBankOpen(client);
        Delay(rand35to70);

        // Change to Skilling Tab (2)
        if (HelperRegion.currentBankTab != 2) {
            FindWidgetItemAndClick(bankTabTwo,client,false);
        }

        Delay(rand35to70);
    }

}


class SecondStepAltar implements Runnable {

    private final Client client;
    private final Graphics2D graphics;
    private final ConfigManager configManager;
    private final OuraniaConfig ouraniaConfig;

    SecondStepAltar(Client client, Graphics2D graphics, OuraniaConfig ouraniaConfig, ConfigManager configManager) {

        this.client = client;
        this.graphics = graphics;
        this.configManager = configManager;
        this.ouraniaConfig = ouraniaConfig;

    }

    public void run() {

        while (true) {
            currentStatus = "Looking, SecondStep";
            if (OuraniaOverlay.altarClose || HelperWidget.shouldStop || OuraniaOverlay.banker || regionString.equalsIgnoreCase("Outside ZMI")) {
                currentStatus = "Waiting..";
                break;
            }
            Delay(rand175to300);
        }
        currentStatus = "Waiting..";


        // Hold down key
        HelperInput.HoldKeyDown();

        Delay(rand900to1100);
        Delay(rand700to900);
        Delay(rand1to300);

        // Load inventory widget and discard runes we don't want and empty pouches
        Widget inventoryWidget = HelperWidget.inventory;
        if (inventoryWidget != null) {
            currentStatus = "Discard, SecondStep";
            for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                int id = item.getId();

                if (id == 556 || id == 558 || id == 555 || id == 554 || id == 559 || id == 557) {
                     FindWidgetItemAndClick(item, client,true);
                }

            }
            currentStatus = "Empty, SecondStep";
            for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                int id = item.getId();
                if (id == 5509 || id == 5510 || id == 5512) {
                     FindWidgetItemAndClick(item, client,true);
                }
            }

            currentStatus = "Clicking, Altar";
            Delay(rand900to1100);

            // Click on altar
            int[] findTile = HelperFind.FindGameObjectOdd(client, graphics, 29631, 1);
            if (findTile[0] != 0) {
                Click(findTile[0], findTile[1]);
            }
            currentStatus = "Waiting..";
        } else {
            Teleport(client, configManager, ouraniaConfig);
        }

    }
}


class ThirdStepAltar implements Runnable {

    private final Client client;
    private final Graphics2D graphics;
    private final OuraniaConfig ouraniaConfig;
    private final ConfigManager configManager;

    ThirdStepAltar(Client client, Graphics2D graphics, OuraniaConfig ouraniaConfig, ConfigManager configManager) {

        this.client = client;
        this.graphics = graphics;
        this.ouraniaConfig = ouraniaConfig;
        this.configManager = configManager;
    }

    public void run() {

        while (true) {
            currentStatus = "Looking, ThirdStep";
            if (OuraniaOverlay.altarClose || HelperWidget.shouldStop || OuraniaOverlay.banker || regionString.equalsIgnoreCase("Outside ZMI")) {
                currentStatus = "Waiting..";
                break;
            }
            Delay(rand175to300);
        }
        currentStatus = "Waiting..";

        Delay(rand900to1100);
        Delay(rand700to900);
        Delay(rand1to300);

        // Discard Runes
        Widget inventoryWidget = HelperWidget.inventory;
        if (inventoryWidget != null) {
            currentStatus = "Discard, ThirdStep";
            for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                int id = item.getId();

                if (id == 556 || id == 558 || id == 555 || id == 554 || id == 559 || id == 557) {
                     FindWidgetItemAndClick(item, client,true);
                }

            }
            currentStatus = "Empty, ThirdStep";
            for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                int id = item.getId();
                if (id == 5514 || id == 5515 || id == 5512) {
                     FindWidgetItemAndClick(item, client,true);
                }
            }

            // Hold Shift Up
            HelperInput.HoldKeyRelease();

            // Click on altar
            int[] findTile = HelperFind.FindGameObjectOdd(client, graphics, 29631, 1);
            currentStatus = "Clicking, Altar";
            if (findTile[0] != 0) {
                Click(findTile[0], findTile[1]);
                Delay(rand900to1100);
                Delay(rand900to1100);


                // Magic Tab
                if (HelperRegion.currentInventoryTab != 6) {
                    currentStatus = "Tab, ThirdStep";
                    PressKeyRandom(KeyEvent.VK_F6);
                    Delay(rand900to1100);
                }

                // Teleport
                currentStatus = "Teleport, ThirdStep";
                Delay(rand100to500);
                FindWidgetItemAndClick(spellOuraniaTeleport,client,true);
                int laps = ouraniaConfig.laps() + 1;
                ouraniaConfig.laps(laps);


                // Check we aren't standing by altar forever
                Delay(rand400);
                Delay(rand1500to1700);
                currentStatus = "Stuck?, ThirdStep";

                while (true) {
                    currentStatus = "Teleport, ThirdStep";
                    if (HelperWidget.shouldStop || regionString.equalsIgnoreCase("Outside ZMI") || teleportedOkay) {
                        currentStatus = "Waiting..";
                        break;
                    }

                    Delay(rand400);
                    Delay(rand1500to1700);

                    // Teleport
                    Delay(rand400);
                    FindWidgetItemAndClick(spellOuraniaTeleport,client,true);
                    ouraniaConfig.laps(laps);

                    if (regionString.equalsIgnoreCase("Outside ZMI")) {
                        currentStatus = "Outside, Waiting..";
                        break;
                    }
                    if (teleportedOkay) {
                        currentStatus = "Teleported, Waiting..";
                        break;
                    }
                }

            }
        } else {
            Teleport(client, configManager, ouraniaConfig);
        }


    }

}


class RobotClickTileStep implements Runnable {

    private final Client c;
    private final Graphics2D graphics2D;
    private int x;
    private int y;

    // Get Parameters
    RobotClickTileStep(int x, int y, Client c, Graphics2D graphics2D) {
        this.x = x;
        this.y = y;
        this.c = c;
        this.graphics2D = graphics2D;


    }

    // Run Runnable
    public void run() {
        currentStatus = "Finding, Next Tile";
        if (HelperWidget.shouldStop) {
            currentStatus = "Waiting..";
            return;
        }

        Delay(rand400to1700);
        int[] findTile = HelperFind.ClickTileLargeArea(x, y, graphics2D, 1, c);
        if (findTile[0] != 0) {
            Click(findTile[0], findTile[1]);
        }
        currentStatus = "Waiting..";
    }
}


class RobotClickAltar implements Runnable {

    private final Client client;
    private final Graphics2D graphics2D;

    RobotClickAltar(Graphics2D graphics2D, Client client) {
        this.graphics2D = graphics2D;
        this.client = client;
    }

    public void run() {
        currentStatus = "Clicking, Altar";
        if (HelperWidget.shouldStop) {
            currentStatus = "Waiting..";
            return;
        }

        Delay(rand400to1700);
        int[] findTile = HelperFind.FindGameObjectOdd(client, graphics2D, 29631, 1);
        if (findTile[0] != 0) {
            Click(findTile[0], findTile[1]);
            currentStatus = "Waiting..";
        }
    }
}


class FindLadder implements Runnable {

    private final Client client;
    private final int x;
    private final int y;

    FindLadder(int x, int y, Client client) {
        this.x = x;
        this.y = y;
        this.client = client;
    }

    private boolean ClickedToAvoidMage = false;

    public void run() {

        client.setNPCsHidden(true);
        Delay(rand900to1100);
        Click(x, y);

        while (true) {
            if (HelperWidget.shouldStop || regionString.equalsIgnoreCase("Inside ZMI")) {
                currentStatus = "Waiting..";
                break;
            }

            currentStatus = "Finding, Ladder";
            Delay(rand900to1100);

            if (HelperWidget.dialogOpen) {
                currentStatus = "ERROR, Dark Mage";
                if (!ClickedToAvoidMage) {
                    Delay(rand100to500);
                    Delay(rand100to500);
                    Delay(rand100to500);

                    if (ladderTile[0] != 0) {
                        Delay(rand100to500);
                        java.awt.Toolkit.getDefaultToolkit().beep();
                        Click(ladderTile[0], ladderTile[1]);
                    }
                    ClickedToAvoidMage = true;
                    break;
                }

            }

        }
        currentStatus = "Waiting..";

    }
}


class DarkMage implements Runnable {

    private final Client client;
    DarkMage(Client client) {
        this.client = client;
    }

    public void run() {

        if (!Cosmic) {
            currentStatus = "ERROR, No Cosmics";
            HelperWidget.shouldStop = true;
        }

        Delay(rand900to1100);

        // Magic Tab
        if (HelperRegion.currentInventoryTab != 6) {
            currentStatus = "Tab, Dark Mage";
            PressKeyRandom(KeyEvent.VK_F6);
            Delay(rand900to1100);
        }


        // Click NPC Contact Spell
        currentStatus = "Spell, Dark Mage";
        Delay(rand900to1100);
        FindWidgetItemAndClick(spellNpcContact,client,false);
        Delay(rand900to1100);
        Delay(rand100to500);

        // Click Dark Mage 618, 304
        currentStatus = "Click, Dark Mage";
        FindWidgetItemAndClick(npcContactDarkMage,client,false);
        Delay(rand900to1100);
        Delay(rand900to1100);
        Delay(rand900to1100);


        // Inv Tab
        currentStatus = "Tab, Dark Mage";
        PressKeyRandom(KeyEvent.VK_ESCAPE);
        Delay(rand175to300);

        while (true) {
            currentStatus = "Waiting, Dark Mage";
            Delay(rand900to1100);
            if (HelperWidget.dialogOpen) {
                Delay(rand700to900);
                break;
            }
        }

        currentStatus = "Space, Dark Mage";
        Delay(rand900to1100);
        PressKeyRandom(KeyEvent.VK_SPACE);
        Delay(rand900to1100);
        PressKeyRandom(KeyEvent.VK_SPACE);
        Delay(rand900to1100);
        PressKeyRandom(KeyEvent.VK_SPACE);
    }

}

//
//class HoldDownKey implements Runnable {
//
//	private final int i;
//
//	HoldDownKey(int key) {
//		this.i = key;
//	}
//
//	public void run() {
//		try {
//			Robot r = new Robot();
//
//			// Press Key
//			r.keyPress(i);
//
//			// Wait Until holdDownKey is false
//			while (holdDownKey) {
//				r.delay(10);
//			}
//
//			// Release Key
//			r.keyRelease(i);
//
//		} catch (AWTException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//}

