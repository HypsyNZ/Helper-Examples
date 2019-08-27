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


import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.helpers.*;
import net.runelite.client.plugins.xptracker.XpTrackerService;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.*;
import java.awt.event.KeyEvent;

import static net.runelite.client.plugins.helpers.HelperBank.bQuanByID;
import static net.runelite.client.plugins.helpers.HelperFind.FindGameObjectLocation;
import static net.runelite.client.plugins.helpers.HelperFind.FindGameObjectOdd;
import static net.runelite.client.plugins.helpers.HelperInventory.InventoryItemXYbyID;
import static net.runelite.client.plugins.helpers.HelperRegion.ladderTile;
import static net.runelite.client.plugins.helpers.HelperRegion.regionString;
import static net.runelite.client.plugins.helpers.HelperThread.*;
import static net.runelite.client.plugins.helpers.HelperWidget.bankIsOpen;
import static net.runelite.client.plugins.ourania.OuraniaItemsBank.OuraniaBankItemsList;
import static net.runelite.client.plugins.ourania.OuraniaItemsInv.OuraniaInvItemsList;


class OuraniaOverlay extends Overlay {

    private final OuraniaConfig config;
    private final Client client;
    private final XpTrackerService xpTrackerService;
    private final ConfigManager configManager;

    @Inject
    public OuraniaOverlay(Client client, OuraniaConfig config, ConfigManager configManager, XpTrackerService xpTrackerService, HelperBank helperBank, HelperInventory helperInventory) {

        setPosition(OverlayPosition.DYNAMIC);
        this.client = client;
        this.configManager = configManager;
        this.config = config;
        this.xpTrackerService = xpTrackerService;
        this.helperBank = helperBank;
        this.helperInv = helperInventory;

    }

    static boolean banker = false;
    static boolean DepositBank = false;

    static boolean NextToBanker = false;
    static boolean NeedStam = false;
    static boolean NeedEnergy = false;
    static boolean NeedFood = false;
    static boolean NeedEss = false;
    static boolean NeedDarkMage = false;
    static boolean NeedLadder = false;
    static boolean NeedLadderPray = false;

    static boolean altarClose = false;
    static boolean AltarPath = false;
    static boolean AltarStep = false;
    static boolean altar = false;

    private static int lastpottick = 0;
    private static int magicActionsLast = 0;
    private static int lastActionFinder = 0;
    private static int[] Tasks = new int[]{0, 0, 0, 0, 0, 0};
    private static final int OBJECT_INTERACTION_FAR = 27;
    private static final int OBJECT_INTERACTION_CLOSE = 1;
    private static boolean ToggleNPCoff = false;
    static int Progress = 0;

    static boolean StepTwo = false;
    static boolean StepThree = false;
    static boolean thirdTileClicked = false;
    static boolean secondTileClick = false;

    static boolean GiantSack = false;
    static boolean Degraded = false;
    static boolean Cosmic = false;

    static boolean Teleport = false;
    static boolean teleportedOkay = false;

    private HelperBank helperBank;
    private HelperInventory helperInv;

    static int StamFour = 0;
    static int StamThree = 0;
    static int StamTwo = 0;
    static int StamPotQuan = 0;

    static int EnergyFour = 0;
    static int EnergyThree = 0;
    static int EnergyTwo = 0;
    static int EnergyPotQuan = 0;

    static int actions = 0;
    static int energy = 0;
    static int prayer = 0;
    static int Hpoints = 0;


    public static void Teleport(Client client, ConfigManager configManager, OuraniaConfig config) {
        if (Hpoints * 100 / client.getRealSkillLevel(Skill.HITPOINTS) < config.teleAction()) {

            // Don't teleport if not running obviously
            if (config.RunAltarBuiltIn() && config.RunAltarAutoTele()) {

                // Don't teleport if we aren't on the path or by the altar
                if (!banker && !regionString.equalsIgnoreCase("Outside ZMI") && !Teleport) {

                    // Stop Everything
                    HelperWidget.shouldStop = true;
                    configManager.setConfiguration("ourania", "OuraniaAltarBuiltIn", false);

                    // Teleport Runnable
                    HelperRunnable.Teleport(configManager, true,client);
                    Teleport = true;

                }

            }
        }
    }


    @Override
    public Dimension render(Graphics2D graphics) {

        // Update Bank and Inventory Lists if we can
        if (HelperWidget.bank != null) {
            OuraniaBankItemsList = helperBank.CheckItemAvailableInBank(OuraniaBankItemsList);
        }
        if (HelperWidget.inventory != null) {
            OuraniaInvItemsList = helperInv.CheckItemAvailableInInventory(OuraniaInvItemsList);
        }


        // Set HoldKey Listener
        HelperInput.HoldKeyListener(KeyEvent.VK_SHIFT);

        // Draw tile visuals
        HelperFind.FindTileLocation(3022, 5581, graphics, client);
        HelperFind.FindTileLocation(3042, 5580, graphics, client);
        HelperFind.FindTileLocation(3010, 5601, graphics, client);
        FindGameObjectLocation(client, graphics, 2452, 3231, 29635, 1);
        FindGameObjectOdd(client, graphics, 29631, 1);

        // API Stuff
        actions = xpTrackerService.getActions(Skill.RUNECRAFT);
        energy = client.getEnergy();
        Hpoints = client.getBoostedSkillLevel(Skill.HITPOINTS);
        prayer = client.getBoostedSkillLevel(Skill.PRAYER);
        int actionsMagic = xpTrackerService.getActions(Skill.MAGIC);
        int tick = client.getTickCount();


        // If we are stopped thats all for now
        if (HelperWidget.shouldStop) {
            configManager.setConfiguration("ourania", "OuraniaAltarBuiltIn", false);
            return null;
        }


        // Scene
        Scene scene = client.getScene();
        Tile[][][] tiles = scene.getTiles();
        int z = client.getPlane();

        // Check Item Quan
        StamFour = bQuanByID(12625, OuraniaBankItemsList);
        StamThree = bQuanByID(12627, OuraniaBankItemsList);
        StamTwo = bQuanByID(12629, OuraniaBankItemsList);
        EnergyFour = bQuanByID(3016, OuraniaBankItemsList);
        EnergyThree = bQuanByID(3018, OuraniaBankItemsList);
        EnergyTwo = bQuanByID(3020, OuraniaBankItemsList);


        // Check Inv for items of interest
        GiantSack = InventoryItemXYbyID(5514, OuraniaInvItemsList)[0] > 0;
        Degraded = InventoryItemXYbyID(5515, OuraniaInvItemsList)[0] > 0;
        Cosmic = InventoryItemXYbyID(564, OuraniaInvItemsList)[0] > 0;


        // Check for Degraded Sack
        boolean degradedSack = !GiantSack || Degraded;

        // Pot Doses
        int s4d = StamFour * 4;
        int s3d = StamThree * 3;
        int s2d = StamTwo * 2;
        StamPotQuan = s4d + s3d + s2d;
        int e4d = EnergyFour * 4;
        int e3d = EnergyThree * 3;
        int e2d = EnergyTwo * 2;
        EnergyPotQuan = e4d + e3d + e2d;


        // Scene Tiles
        altar = false;
        altarClose = false;
        secondTileClick = false;
        for (int x = 0; x < Constants.SCENE_SIZE; ++x) {
            for (int y = 0; y < Constants.SCENE_SIZE; ++y) {
                Tile tile = tiles[z][x][y];

                if (tile == null) {
                    continue;
                }
                GameObject[] gameObjects = tile.getGameObjects();
                if (gameObjects != null) {
                    for (GameObject gameObject : gameObjects) {
                        if (gameObject != null) {
                            Player player = client.getLocalPlayer();

                            if (gameObject.getId() == 29631) {
                                if (player.getWorldLocation().distanceTo(tile.getWorldLocation()) <= OBJECT_INTERACTION_CLOSE) {
                                    altarClose = true;
                                }
                            }

                            if (gameObject.getId() == 29631) {
                                if (player.getWorldLocation().distanceTo(tile.getWorldLocation()) <= OBJECT_INTERACTION_FAR) {

                                    altar = true;
                                }
                            }
                        }
                    }
                }

                WorldPoint twp = tile.getWorldLocation();
                if (twp.getX() == 3022 && twp.getY() == 5580) {

                    Player player = client.getLocalPlayer();
                    if (player.getWorldLocation().distanceTo(tile.getWorldLocation()) <= 9) {
                        secondTileClick = true;
                    }

                }
            }
        }


        // Banker
        banker = false;
        for (NPC npc : client.getNpcs()) {
            final String npcName = npc.getName();

            if (npcName == null) {
                continue;
            }

            if (npcName.toLowerCase().equals("eniola")) {

                if (npc.getComposition().isVisible()) {
                    banker = true;
                }
            }

        }

        // TELEPORT
        Teleport(client, configManager, config);

        // Magic Actions
        if (altarClose && magicActionsLast != actionsMagic) {
            magicActionsLast = actionsMagic;
            teleportedOkay = true;
        }

        // RC Actions
        if (lastActionFinder != actions) {
            lastActionFinder = actions;
            Progress++;
        }

        if (regionString.equalsIgnoreCase("Outside ZMI") || banker) {
            Progress = 0;
        }

        if (Progress == 0) {

            StepTwo = false;
            StepThree = false;
        }

        if (Progress == 1) {

            if (!StepTwo && config.RunAltarBuiltIn() && !isBusy()) {
                SecondStepAltar ss = new SecondStepAltar(client, graphics, config, configManager);
                tThreadTwo = new Thread(ss);
                tThreadTwo.start();
                setBusy();
                StepTwo = true;
            }
        }

        if (Progress == 2) {

            if (!StepThree && config.RunAltarBuiltIn() && !isBusy()) {
                ThirdStepAltar ss = new ThirdStepAltar(client, graphics, config, configManager);
                tThreadThree = new Thread(ss);
                tThreadThree.start();
                setBusy();
                StepThree = true;
            }
        }


        if (regionString.equalsIgnoreCase("Inside ZMI")) {
            if (banker) {
                NeedLadder = false;
                NeedLadderPray = false;
                NeedDarkMage = false;
                thirdTileClicked = false;
                AltarPath = false;
                AltarStep = false;

                if (!ToggleNPCoff) {
                    client.setNPCsHidden(false);
                    ToggleNPCoff = true;
                }

                // BANKER
                if (!isBusy() && config.RunAltarBuiltIn()) {

                    if (!NextToBanker && !isBusy()) {
                        NextToBanker = true;
                        DepositBank ss = new DepositBank(client);
                        tThreadFour = new Thread(ss);
                        tThreadFour.start();
                        setBusy();
                    }

                    if (bankIsOpen) {


                        if (!DepositBank) {
                            Tasks[0] = 1;
                        }

                        if (config.AutoFood() && !NeedFood && Hpoints * 100 / client.getRealSkillLevel(Skill.HITPOINTS) < config.foodAction()) {
                            Tasks[1] = 1;
                        }

                        if (config.AutoEnergy() && !NeedEnergy && energy < config.energyAction()) {
                            Tasks[2] = 1;
                        } else if (config.AutoStam() && !NeedStam && energy < 60
                                && client.getVar(Varbits.RUN_SLOWED_DEPLETION_ACTIVE) == 0
                                && tick - config.staminaAction() > lastpottick) {
                            Tasks[3] = 1;
                        }

                        if (!NeedEss) {
                            Tasks[4] = 1;
                        }


                        if (Tasks[0] == 1 && !isBusy()) {
                            DepositBank = true;
                            InventorySearchDeposit ss = new InventorySearchDeposit(client, config);
                            tThreadFive = new Thread(ss);
                            tThreadFive.start();
                            setBusy();
                            Tasks[0] = 0;
                        }


                        if (Tasks[1] == 1 && Tasks[0] == 0 && !isBusy()) {
                            SearchFood ss = new SearchFood(client);
                            tThreadFour = new Thread(ss);
                            tThreadFour.start();
                            NeedFood = true;
                            setBusy();
                            Tasks[1] = 0;

                        }

                        if (Tasks[2] == 1 && Tasks[0] == 0 && Tasks[1] == 0 && !isBusy()) {
                            if (EnergyFour > 0 || EnergyThree > 0 || EnergyTwo > 0) {
                                SearchEnergy ss = new SearchEnergy(client);
                                tThreadOne = new Thread(ss);
                                tThreadOne.start();
                                NeedEnergy = true;
                                setBusy();
                                Tasks[2] = 0;
                            }
                        }
                        if (Tasks[3] == 1 && Tasks[0] == 0 && Tasks[1] == 0 && !isBusy()) {
                            if (StamFour > 0 || StamThree > 0 || StamTwo > 0) {
                                SearchStam ss = new SearchStam(client);
                                tThreadTwo = new Thread(ss);
                                tThreadTwo.start();
                                lastpottick = client.getTickCount();
                                NeedStam = true;
                                setBusy();
                                Tasks[3] = 0;
                            }

                        }

                        if (Tasks[4] == 1 && Tasks[0] == 0 && Tasks[1] == 0 && Tasks[2] == 0 && Tasks[3] == 0 && !isBusy()) {
                            NeedEss = true;
                            SearchEss ss = new SearchEss(client, graphics, configManager, config);
                            tThreadFour = new Thread(ss);
                            tThreadFour.start();
                            setBusy();
                            Tasks[4] = 0;
                        }

                    }
                }
            }

            // SECOND TILE
            if (!altar && !banker && !secondTileClick) {

                if (!AltarPath && config.RunAltarBuiltIn() && !isBusy()) {
                    RobotClickTileStep ss = new RobotClickTileStep(3022, 5581, client, graphics);
                    tThreadThree = new Thread(ss);
                    tThreadThree.start();
                    AltarPath = true;
                    setBusy();
                }
            }
            if (secondTileClick && !banker && !altar) {

                if (config.RunAltarBuiltIn() && !thirdTileClicked && !isBusy()) {
                    RobotClickTileStep ss = new RobotClickTileStep(3042, 5580, client, graphics);
                    tThreadFour = new Thread(ss);
                    tThreadFour.start();
                    thirdTileClicked = true;
                    setBusy();
                }

            }
            if (altar && !altarClose && thirdTileClicked) {

                if (!AltarStep && config.RunAltarBuiltIn() && !isBusy()) {
                    RobotClickAltar ss = new RobotClickAltar(graphics, client);
                    tThreadOne = new Thread(ss);
                    tThreadOne.start();
                    AltarStep = true;
                    setBusy();
                }

            }
        } else if (regionString.equalsIgnoreCase("Outside ZMI")) {

            AltarPath = false;
            AltarStep = false;
            NextToBanker = false;
            DepositBank = false;
            NeedFood = false;
            NeedEnergy = false;
            NeedStam = false;
            NeedEss = false;
            ToggleNPCoff = false;

            Teleport = false;
            teleportedOkay = false;


            client.setNPCsHidden(true);
            if (config.RunAltarBuiltIn()) {
                if (!NeedDarkMage && degradedSack && !isBusy()) {
                    DarkMage ss = new DarkMage(client);
                    tThreadFive = new Thread(ss);
                    tThreadFive.start();
                    NeedDarkMage = true;
                    setBusy();
                } else if (!NeedLadder && !isBusy() && !degradedSack) {
                    client.setNPCsHidden(true);
                    if (ladderTile[0] != 0) {
                        FindLadder ss = new FindLadder(ladderTile[0], ladderTile[1], client);
                        tThreadSix = new Thread(ss);
                        tThreadSix.start();
                        NeedLadder = true;
                        setBusy();
                    }

                }
            }

        }
        return null;
    }
}


