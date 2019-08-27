package net.runelite.client.plugins.ourania;

import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.helpers.HelperDelay;
import net.runelite.client.plugins.helpers.HelperRegion;
import net.runelite.client.plugins.helpers.HelperWidget;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.Executors;

import static net.runelite.client.plugins.helpers.HelperDelay.*;
import static net.runelite.client.plugins.helpers.HelperFind.FindWidgetItemAndClick;

public class HelperRunnable {

    /**
     * Teleport to Safety (Ourania Teleport)
     */
    public static void Teleport(ConfigManager configManager, Boolean restart, Client client) {

        Executors.newSingleThreadExecutor().execute(() -> {

            Robot r;
            try {
                r = new Robot();

                // Magic Tab
                if (HelperRegion.currentInventoryTab != 6) {

                    r.delay(HelperDelay.rand125to200);
                    r.keyPress(KeyEvent.VK_F6);
                    r.delay(HelperDelay.rand25to50);
                    r.keyRelease(KeyEvent.VK_F6);
                    r.delay(HelperDelay.rand25to50);
                    r.delay(rand900to1100);
                }

                // Teleport
                FindWidgetItemAndClick(HelperWidget.spellOuraniaTeleport,client,false);
                r.delay(rand700to900);

                // Restart Everything

                if (restart) {
                    HelperWidget.shouldStop = false;
                    configManager.setConfiguration("ourania", "OuraniaAltarBuiltIn", true);
                }
            } catch (AWTException e) {
                e.printStackTrace();
            }
        });
    }
}






