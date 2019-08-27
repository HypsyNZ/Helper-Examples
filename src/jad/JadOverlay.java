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

import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.VarClientInt;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

import static net.runelite.client.plugins.jad.JadOverlayNPC.jad;
import static net.runelite.client.plugins.jad.JadOverlayNPC.jadPrayer;


public class JadOverlay extends Overlay {
    private final Client client;
    private final PanelComponent panelComponent = new PanelComponent();
    static int invtab = 0;

    @Inject
    JadOverlay(Client client) {

        this.client = client;
    }

    @Override
    public Dimension render(Graphics2D graphics) {

        int prayer = client.getBoostedSkillLevel(Skill.PRAYER);
        invtab = client.getVar(VarClientInt.INVENTORY_TAB);


        panelComponent.getChildren().clear();

        int p = prayer * 100 / client.getRealSkillLevel(Skill.PRAYER);

        if (p < 30) {
            StringBuilder scbp = new StringBuilder();
            scbp.append("DRINK PRAYER NOW!: ").append((p)).append("%");
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(scbp.toString())
                    .color(Color.RED)
                    .build());
        } else if (p < 60) {
            StringBuilder scbp = new StringBuilder();
            scbp.append("DRINK PRAYER: ").append((p)).append("%");
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(scbp.toString())
                    .color(Color.YELLOW)
                    .build());

        } else {
            StringBuilder sbp = new StringBuilder();
            sbp.append("PRAYER: ").append((p)).append("%");
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text(sbp.toString())
                    .color(Color.BLUE)
                    .build());
        }

        if (jad) {
            if (jadPrayer) {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("PRAY RANGE")
                        .color(Color.GREEN)
                        .build());
            } else {
                panelComponent.getChildren().add(TitleComponent.builder()
                        .text("PRAY MAGE")
                        .color(Color.BLUE)
                        .build());
            }
        } else {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Jad Not Spawned")
                    .color(Color.WHITE)
                    .build());
        }
        return panelComponent.render(graphics);
    }

}
