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
import net.runelite.api.Skill;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.helpers.HelperInput;
import net.runelite.client.plugins.helpers.HelperRegion;
import net.runelite.client.plugins.helpers.HelperWidget;
import net.runelite.client.util.Text;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static net.runelite.client.plugins.helpers.HelperDelay.*;
import static net.runelite.client.plugins.helpers.HelperFind.FindWidgetItemAndClick;
import static net.runelite.client.plugins.helpers.HelperInput.*;
import static net.runelite.client.plugins.helpers.HelperRegion.*;

//class ThreadMain {
//
//	Thread tNeedFood;
//	Thread tFarmer;
//	Thread tStunned;
//	Thread tDiscardSeeds;
//	Thread tShop;
//	volatile boolean holdDownShift = false;
//	volatile boolean holdDownShiftOnce = false;
//	private boolean busy = false;
//
//	private void ThreadStateHelper(Thread currentThread) {
//
//		if (currentThread != null && currentThread.isAlive()) {
//			busy = true;
//		}
//
//		if (currentThread != null && currentThread.getState() != Thread.State.TERMINATED) {
//			busy = true;
//		}
//	}
//
//	void getThreadStates() {
//		busy = false;
//		HoldDownShift();
//		ThreadStateHelper(tNeedFood);
//		ThreadStateHelper(tFarmer);
//		ThreadStateHelper(tStunned);
//		ThreadStateHelper(tDiscardSeeds);
//		ThreadStateHelper(tShop);
//	}
//
//	boolean isBusy() {
//		return busy;
//	}
//
//	void setBusy() {
//		busy = true;
//	}
//
//	void nullThreads() {
//		tNeedFood = null;
//		tFarmer = null;
//		tStunned = null;
//		tDiscardSeeds = null;
//		tShop = null;
//	}
//
//	private void HoldDownShift() {
//		if (holdDownShift && !holdDownShiftOnce) {
//			holdDownShiftOnce = true;
//			Thread hd;
//			HoldDownShift hds = new HoldDownShift(this);
//			hd = new Thread(hds);
//			hd.start();
//		}
//	}
//}
//
//class HoldDownShift implements Runnable {
//
//	Robot r;
//	private ThreadMain threadMain;
//
//	{
//		try {
//			r = new Robot();
//		} catch (AWTException e) {
//			e.printStackTrace();
//		}
//	}
//
//	HoldDownShift(ThreadMain threadMain) {
//		this.threadMain = threadMain;
//	}
//
//	public void run() {
//		r.keyPress(KeyEvent.VK_SHIFT);
//		while (threadMain.holdDownShift) {
//			r.delay(10);
//		}
//		r.keyRelease(KeyEvent.VK_SHIFT);
//
//	}
//
//}

class SearchFood implements Runnable {

    private MasterFarmOverlay overlay;
    private Client client;
    private Graphics2D graphics;
    private xoFindTiles masterFarmFindTiles;

    SearchFood(Client client, MasterFarmOverlay overlay, Graphics2D graphics, xoFindTiles masterFarmFindTiles) {

        this.client = client;
        this.overlay = overlay;
        this.graphics = graphics;
        this.masterFarmFindTiles = masterFarmFindTiles;

    }


    public void run() {

        if (HelperWidget.shouldStop) {
            return;
        }

        // Doorway
        int[] tile = masterFarmFindTiles.ClickTileLargeArea(3094, 3243, graphics, 1, client);
        if (tile != null) {
            Click(tile[0], tile[1]);
            Delay(rand1500to1700);
            Delay(rand700to900);
        }

        if (HelperWidget.shouldStop) {
            return;
        }

        // Open Bank
        WaitForBankOpen(client, 10);
        Delay(rand700to900);


        // Change bank tab
        if (HelperRegion.currentBankTab != 2) {
            Delay(rand700to900);
            ClickTab(xoWidget.tab2x, xoWidget.tab2y);
            Delay(rand700to900);
        }


        // Deposit Seeds
        FindWidgetItemAndClick(HelperWidget.bankDepositInventory,client,false);
        Delay(rand700to900);

        if (HelperWidget.shouldStop) {
            return;
        }


        // Withdraw 4
        if (HelperRegion.currentWithdrawSetting != 4) {
            // Set Withdraw To All
            Delay(rand125to200);
            FindWidgetItemAndClick(HelperWidget.withdrawOptionALL,client,false);
            Delay(rand125to200);
        }

        // Withdraw Monkfish
        Click(xoWidget.MonkfishX, xoWidget.MonkfishY);
        Delay(rand700to900);

        if (HelperWidget.shouldStop) {
            return;
        }

        // Exit Bank
        PressKeyRandom(KeyEvent.VK_ESCAPE);
        Delay(rand700to900);

        if (HelperWidget.shouldStop) {
            return;
        }

        // Wait For Bank Close
        WaitForBankClose();
        Delay(rand700to900);


        // Click on Monkfish 7946
        Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
        if (inventoryWidget != null) {
            for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                int id = item.getId();

                if (id == 7946 && client.getBoostedSkillLevel(Skill.HITPOINTS) < client.getRealSkillLevel(Skill.HITPOINTS)) {
                    FindWidgetItemAndClick(item, client,true);
                    Delay(rand75to100);
                    Delay(rand1500to1700);
                }

            }
        }


        // Open Bank
        if (HelperWidget.shouldStop) {
            return;
        }
        WaitForBankOpen(client);

        // Deposit
        if (inventoryWidget != null) {

            for (WidgetItem item : inventoryWidget.getWidgetItems()) {
                int id = item.getId();
                if (id == 7946) {
                    FindWidgetItemAndClick(item, client,true);
                    Delay(rand700to900);
                    break;
                }

            }
        }

        if (HelperWidget.shouldStop) {
            return;
        }

        // Exit Bank
        Delay(rand400);
        PressKeyRandom(KeyEvent.VK_ESCAPE);
        Delay(rand400);

        // Wait For Bank Close
        WaitForBankClose();
        Delay(rand400);

        if (HelperWidget.shouldStop) {
            return;
        }

        int[] area = masterFarmFindTiles.ClickTileLargeArea(3083, 3248, graphics, 2, client);
        Click(area[0], area[1]);

        Delay(rand1500to1700);
        Delay(rand1500to1700);
        Delay(rand400);
        Delay(rand400);
        overlay.DoneBanking = false;
    }

}

class RobotClickExact implements Runnable {

    private int x;
    private int y;

    // Get Parameters
    RobotClickExact(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Run Runnable
    public void run() {
        if (HelperWidget.shouldStop) {
            return;
        }
        Click(x, y);
        Delay(rand700to900);
        Delay(rand125to200);
        Delay(rand125to200);
        Delay(rand125to200);


    }

}


class RobotClickTarget implements Runnable {

    private int x;
    private int y;
    private String target;


    // Get Parameters
    RobotClickTarget(int x, int y, String target) {

        this.x = x;
        this.y = y;
        this.target = target;
    }

    // Run Runnable
    public void run() {
        if (HelperWidget.shouldStop) {
            return;
        }

        final String menuTarget = Text.removeTags(MasterFarmPlugin.eventMenu.getTarget()).toLowerCase();
        if (menuTarget.contains(target)) {
            Click(x, y);
            Delay(rand700to900);
            Delay(rand125to200);
            Delay(rand125to200);
        }

        Delay(30);
    }

}


class ExitShop implements Runnable {

    public void run() {
        if (HelperWidget.shouldStop) {
            return;
        }

        Delay(rand400);
        PressKeyRandom(KeyEvent.VK_ESCAPE);
        Delay(rand400);
    }

}

class RobotWaitRandom implements Runnable {

    public void run() {
        if (HelperWidget.shouldStop) {
            return;
        }
        try {
            Robot r = new Robot();
            r.delay(rand75to100);
            r.delay(rand75to100);
            r.delay(rand700to900);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

}

class DiscardSeeds implements Runnable {

    private Client client;
    //private ThreadMain threadMain;

    DiscardSeeds(Client client) {
        this.client = client;
    }

    // Run Runnable
    public void run() {
        if (HelperWidget.shouldStop) {
            return;
        }

        // Create List
        List<Integer> SeedsToDiscard = new ArrayList<>();
        SeedsToDiscard.add(5102);
        SeedsToDiscard.add(5319);
        SeedsToDiscard.add(5305);
        SeedsToDiscard.add(5310);
        SeedsToDiscard.add(5322);
        SeedsToDiscard.add(5096);
        SeedsToDiscard.add(5104);
        SeedsToDiscard.add(5307);
        SeedsToDiscard.add(5099);
        SeedsToDiscard.add(5308);
        SeedsToDiscard.add(5318);
        SeedsToDiscard.add(5097);
        SeedsToDiscard.add(5320);
        SeedsToDiscard.add(5103);
        SeedsToDiscard.add(5309);
        SeedsToDiscard.add(5101);
        SeedsToDiscard.add(5306);
        SeedsToDiscard.add(5098);
        SeedsToDiscard.add(5311);
        SeedsToDiscard.add(5105);
        SeedsToDiscard.add(5282);
        SeedsToDiscard.add(5106);
        SeedsToDiscard.add(22873);
        SeedsToDiscard.add(1937);
        SeedsToDiscard.add(1993);
        SeedsToDiscard.add(1935);
        SeedsToDiscard.add(7919);
        SeedsToDiscard.add(1987);
        SeedsToDiscard.add(5324);
        SeedsToDiscard.add(5281);


        // Hold Down Shift
        HelperInput.HoldKeyDown();

        Delay(rand900to1100);

        // Discard Seeds
        xoInventory.DiscardItem(SeedsToDiscard, client);

        Delay(rand400);

        // Hold Shift Up
        HelperInput.HoldKeyRelease();

        Delay(rand400);


    }

}