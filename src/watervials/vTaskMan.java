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
package net.runelite.client.plugins.watervials;

import net.runelite.api.Client;

import java.util.HashMap;
import java.util.Map;

import static net.runelite.client.plugins.helpers.HelperThread.*;


class vTaskMan {

    public static Map<String, Boolean> map = new HashMap<>();
    private static long ready = 0;

    /**
     * Populates Hash Map with the Tasks
     * <p>
     * Left (String)    : Task Name
     * Right (Boolean)  : Conditions to perform Task
     */
    private static void tasksToMap(vConfig co, Client cl) {
        map = new HashMap<>();
        map.put("run", co.RunAltarBuiltIn() && !isBusy());
    }

    /**
     * Main Methods for Starting Threads related to Tasks named in tasksToMap
     */
    static void runTasks(Client client, vConfig config) {

        if (taskIsReady()) {

            tasksToMap(config, client);

            /* 1 */
            if (map.get("run")) {

                vTasks click = new vTasks(client, config);
                tThreadFour = new Thread(click);
                tThreadFour.start();
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
