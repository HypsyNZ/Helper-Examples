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
package net.runelite.client.plugins.jad;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

@PluginDescriptor(
        name = "∞ Jad Plugin",
        description = "Show Jad Prayer and Switches Tabs",
        tags = {"highlight", "jad", "npcs", "switch", "respawn", "tags"}
)
@Slf4j
public class JadPlugin extends Plugin {

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private JadOverlay jadOverlay;

    @Inject
    private JadInputListener jadInputListener;

    @Inject
    private JadOverlayNPC jadOverlayNPC;

    @Inject
    private KeyManager keyManager;

    @Inject
    private ThreadMain threadMain;


    @Provides
    JadConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(JadConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        overlayManager.add(jadOverlay);
        overlayManager.add(jadOverlayNPC);
        keyManager.registerKeyListener(jadInputListener);
        threadMain.nullThreads();
    }

    @Override
    protected void shutDown() throws Exception {
        overlayManager.remove(jadOverlay);
        overlayManager.remove(jadOverlayNPC);
        keyManager.unregisterKeyListener(jadInputListener);
        threadMain.nullThreads();
    }

}
