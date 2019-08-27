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
package net.runelite.client.plugins.construction;


import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.helpers.HelperDraw;
import net.runelite.client.plugins.helpers.HelperFind;
import net.runelite.client.plugins.helpers.HelperThread;
import net.runelite.client.plugins.helpers.HelperWidget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.PanelComponent;

import javax.inject.Inject;
import java.awt.*;

import static java.awt.Color.CYAN;

class cMain extends Overlay {

    private final cConfig config;
    private final Client client;
    private final ConfigManager configManager;
    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    public cMain(Client client, cConfig config, ConfigManager configManager) {
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(OverlayPriority.HIGHEST);
        this.client = client;
        this.config = config;
        this.configManager = configManager;
    }


    @Override
    public Dimension render(Graphics2D graphics) {

        if (HelperWidget.shouldStop) {
            configManager.setConfiguration("c", "cAltarBuiltIn", false);
        }

        // Update All Helpers
        HelperFind.FindGameObjectOdd(client, graphics, 15403, 3);
        HelperFind.FindGameObjectOdd(client, graphics, 13566, 3);
        HelperFind.FindNPCLocationByID(229, client, graphics);

        // Find Glass
        if (config.RunAltarBuiltIn() && !HelperThread.isBusy()) {
            cActions fg = new cActions(client, graphics);
            HelperThread.tThreadOne = new Thread(fg);
            HelperThread.tThreadOne.start();
            HelperThread.setBusy();
        }

        // Render Player Wire Frame
        if (config.toggleWireFrame()) {
            Player local = client.getLocalPlayer();
            HelperDraw.renderPlayerWireframe(graphics, local, CYAN);
        }

        return panelComponent.render(graphics);
    }
}

