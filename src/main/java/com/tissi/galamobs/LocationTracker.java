package com.tissi.galamobs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EventBusSubscriber(modid = GalaMobs.MODID, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class LocationTracker {

    public static boolean inGalatea = false;

    private static final Pattern MAP_PATTERN = Pattern.compile("\"map\"\\s*:\\s*\"([^\"]+)\"");

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent event) {
        String msg = event.getMessage().getString();

        // Look for map data from /locraw
        Matcher matcher = MAP_PATTERN.matcher(msg);
        if (matcher.find()) {
            String mapName = matcher.group(1);
            inGalatea = mapName.equalsIgnoreCase("galatea");
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;

            player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("[GalaMobs] Map: " + mapName),
                    false
            );
        }
    }
}
