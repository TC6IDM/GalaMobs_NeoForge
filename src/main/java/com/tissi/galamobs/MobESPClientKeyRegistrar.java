package com.tissi.galamobs;

import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = GalaMobs.MODID, bus = EventBusSubscriber.Bus.MOD)
public class MobESPClientKeyRegistrar {
    public static final KeyMapping toggleShellwiseESPKey = new KeyMapping(
            "key.galamobs.toggle_shellwise_esp", // translation key
            GLFW.GLFW_KEY_F6,          // key
            "key.categories.misc"      // category
    );

    public static final KeyMapping toggleShellwiseWarpKey = new KeyMapping(
            "key.galamobs.toggle_shellwise_warp", // translation key
            GLFW.GLFW_KEY_F7,          // key
            "key.categories.misc"      // category
    );

    public static final KeyMapping toggleHideonleafESPKey = new KeyMapping(
            "key.galamobs.toggle_hideonleaf_esp", // translation key
            GLFW.GLFW_KEY_F5,          // key
            "key.categories.misc"      // category
    );

    public static final KeyMapping toggleHideonleafWarpKey = new KeyMapping(
            "key.galamobs.toggle_hideonleaf_warp", // translation key
            GLFW.GLFW_KEY_F9,          // key
            "key.categories.misc"      // category
    );

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(toggleShellwiseESPKey);
        event.register(toggleShellwiseWarpKey);
        event.register(toggleHideonleafESPKey);
        event.register(toggleHideonleafWarpKey);
    }
}
