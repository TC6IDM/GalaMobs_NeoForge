package com.tissi.galamobs;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientChatEvent;

@EventBusSubscriber(modid = GalaMobs.MODID, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class ChatCommandHandler {

    @SubscribeEvent
    public static void onChat(ClientChatEvent event) {
        String msg = event.getMessage();
        Minecraft mc = Minecraft.getInstance();

        if (msg.startsWith("!setrandom")) {
            event.setCanceled(true); // Prevent command from going to the server

            String[] parts = msg.trim().split("\\s+");
            if (parts.length != 3) {
                if (mc.player != null) {
                    mc.player.displayClientMessage(Component.literal("§cUsage: /setrandom <lower> <upper>"), false);
                }
                return;
            }

            try {
                int newLower = Integer.parseInt(parts[1]);
                int newUpper = Integer.parseInt(parts[2]);

                if (newLower >= newUpper || newLower < 0) {
                    if (mc.player != null) {
                        mc.player.displayClientMessage(Component.literal("§cInvalid range. Make sure lower < upper and both ≥ 0."), false);
                    }
                    return;
                }

                MobESPClientTickHandler.lower = newLower;
                MobESPClientTickHandler.upper = newUpper;
                if (mc.player != null) {
                    mc.player.displayClientMessage(Component.literal("§7[GalaMobs] §fWarp delay range set to §a" + newLower + "–" + newUpper + " ticks"), false);
                }

            } catch (NumberFormatException e) {
                if (mc.player != null) {
                    mc.player.displayClientMessage(Component.literal("§cInvalid numbers. Usage: /setrandom <lower> <upper>"), false);
                }
            }
        }
    }
}
