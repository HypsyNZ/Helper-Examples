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
package net.runelite.client.plugins.woodcutting;

import net.runelite.api.Client;
import net.runelite.client.Notifier;
import net.runelite.client.plugins.helpers.HelperInventory;
import net.runelite.client.plugins.helpers.HelperRegion;

import java.util.HashMap;
import java.util.Map;

import static net.runelite.client.plugins.helpers.HelperThread.*;

class aTaskMan {

    public static Map<String, Boolean> map = new HashMap<>();
    private static long ready = 0;

    /**
     * Populates Hash Map with the Tasks
     * <p>
     * Left (String)    : Task Name
     * Right (Boolean)  : Conditions to perform Task
     */
    private static void tasksToMap(WoodcuttingConfig co, Client cl, WoodcuttingPlugin plugin) {

        map = new HashMap<>();
        map.put("empty", co.RunAltarBuiltIn() && !isBusy() && HelperInventory.CurrentInventoryLen == 28 && co.dropLogs());
        map.put("find", co.RunAltarBuiltIn() && !isBusy() && plugin.getAxe() != null && plugin.getAxe().getAnimId() != cl.getLocalPlayer().getAnimation() && !HelperRegion.isMoving && HelperInventory.CurrentInventoryLen != 28);

    }

    /**
     * Main Methods for Starting Threads related to Tasks named in tasksToMap
     */
    static void runTasks(Client client, WoodcuttingConfig config, Notifier notifier, WoodcuttingPlugin plugin) {

        if (taskIsReady()) {

            tasksToMap(config, client, plugin);

            if (map.get("empty")) {
                notifier.notify("Dropping Logs!");
                aTasks click = new aTasks(client, config, "empty", plugin, plugin.getTreeObjects());
                tThreadTwo = new Thread(click);
                tThreadTwo.start();
                taskSetBusy();

            }

            if (map.get("find")) {
                notifier.notify("Finding Tree!");
                aTasks click = new aTasks(client, config, "find", plugin, plugin.getTreeObjects());
                tThreadThree = new Thread(click);
                tThreadThree.start();
                taskSetBusy();

            }

        }
    }

    /**
     * Task set busy
     */
    private static void taskSetBusy() {
        ready = System.currentTimeMillis();
        setBusy();
    }

    /**
     * Task is ready
     */
    private static boolean taskIsReady() {
        boolean r = ready + 200 <= System.currentTimeMillis();
        if (r) {
            ready = System.currentTimeMillis();
        }
        return r;
    }
}
