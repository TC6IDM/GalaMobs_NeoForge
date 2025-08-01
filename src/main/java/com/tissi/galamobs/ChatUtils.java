package com.tissi.galamobs;

import net.minecraft.client.Minecraft;

public class ChatUtils {
    public static void sendChatCommand(String command) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.connection.sendCommand(command);
        }
    }
}
