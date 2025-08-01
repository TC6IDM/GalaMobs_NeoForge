package com.tissi.galamobs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tissi.galamobs.mixin.ParticleEngineAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;


import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod(value = GalaMobs.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = GalaMobs.MODID, value = Dist.CLIENT)
public class MobESPClientTickHandler {
    // Reference toggleESPKey from MobESPClientKeyRegistrar
    private static boolean shellwiseEspEnabled = false;
    private static boolean hideonleafEspEnabled = false;
    private static boolean shellwiseWarpLoopEnabled = false;
    private static long lastWarpTime = 0;
    private static boolean inGalatea = false;
    public static int upper = 7000;
    public static int lower = 5000;
    private static int r = 6000;
    public static final ConcurrentHashMap<UUID, Vec3> trackedShellwise = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<UUID, Vec3> trackedHideonleaf = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
//        if (event.phase != ClientTickEvent..END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;  // Make sure the world is loaded, or else skip


        //Toggle warp loop
        if (MobESPClientKeyRegistrar.toggleShellwiseWarpKey.consumeClick()) {
            shellwiseWarpLoopEnabled = !shellwiseWarpLoopEnabled;
            System.out.println("Shellwise Warp Loop toggled: " + (shellwiseEspEnabled ? "ON" : "OFF"));
            if (mc.player != null) {
                mc.player.displayClientMessage(Component.literal("§7[GalaMobs] §5Shellwise §fWarp Loop toggle: " + (shellwiseWarpLoopEnabled ? "§aEnabled" : "§cDisabled")), false);
                if (shellwiseWarpLoopEnabled) {
                    ChatUtils.sendChatCommand("locraw"); // Immediately check location
                    inGalatea = LocationTracker.inGalatea;

                }
                lastWarpTime = upper; // reset
                r = (int) (Math.random() * (upper - lower)) + lower;
                mc.player.displayClientMessage(Component.literal("[GalaMobs] Warping in "+r+" Milliseconds"), false);
            }

        }

//        if (!shellwiseWarpLoopEnabled) return;

        // Toggle ESP
        if (MobESPClientKeyRegistrar.toggleShellwiseESPKey.consumeClick()) {
            shellwiseEspEnabled = !shellwiseEspEnabled;
            System.out.println("Shellwise ESP toggled: " + (shellwiseEspEnabled ? "ON" : "OFF"));
            if (mc.player != null) {
                mc.player.displayClientMessage(Component.literal("§7[GalaMobs] §5Shellwise §fESP toggle: " + (shellwiseEspEnabled ? "§aEnabled" : "§cDisabled")), false);
            }
        }

        if (MobESPClientKeyRegistrar.toggleHideonleafESPKey.consumeClick()) {
            hideonleafEspEnabled = !hideonleafEspEnabled;
            System.out.println("Hideonleaf ESP toggled: " + (hideonleafEspEnabled ? "ON" : "OFF"));
            if (mc.player != null) {
                mc.player.displayClientMessage(Component.literal("§7[GalaMobs] §fHideonleaf §fESP toggle: " + (hideonleafEspEnabled ? "§aEnabled" : "§cDisabled")), false);
            }
        }

        MobESPClientTickHandler.trackedHideonleaf.clear(); // Clear old positions
        MobESPClientTickHandler.trackedShellwise.clear(); // Clear old positions

        for (Entity e : mc.level.entitiesForRendering()) {
            if (e instanceof Shulker le) {
//                System.out.println("Turtle" + le);
                MobESPClientTickHandler.trackedHideonleaf.put(le.getUUID(), le.position());
//                le.setGlowingTag(true);
            }
        }

        for (Entity e : mc.level.entitiesForRendering()) {
            if (e instanceof Turtle le) {
//                System.out.println("Turtle" + le);
                MobESPClientTickHandler.trackedShellwise.put(le.getUUID(), le.position());
//                le.setGlowingTag(true);
            }
        }
        boolean turtlesFound = MobESPClientTickHandler.trackedShellwise.size() >= 1;

//        long currentTime = System.currentTimeMillis();

//        System.out.println(mc.player.tickCount - lastWarpTime);
        long now = System.currentTimeMillis();

        if (shellwiseWarpLoopEnabled && !turtlesFound && mc.player != null && (now - lastWarpTime) > r ){
            if (inGalatea) {
                ChatUtils.sendChatCommand("warp home");
            } else {
                ChatUtils.sendChatCommand("warp murkwater");
            }
            inGalatea = !inGalatea;
            lastWarpTime = System.currentTimeMillis();
            r = (int) (Math.random() * (upper - lower)) + lower;
            mc.player.displayClientMessage(Component.literal("[GalaMobs] Warping in " + r + " Milliseconds"), false);
        }

        if (shellwiseWarpLoopEnabled && turtlesFound){
            shellwiseWarpLoopEnabled = false;
            System.out.println("Shellwise Warp Loop toggled: " + "OFF");

            if (mc.player != null) {
                mc.player.displayClientMessage(Component.literal("§7[GalaMobs] §5Shellwise §fWarp Loop toggle: " + (shellwiseWarpLoopEnabled ? "§aEnabled" : "§cDisabled")), false);
                if (!shellwiseEspEnabled) mc.player.displayClientMessage(Component.literal("§7[GalaMobs] §5Shellwise §fESP toggle: " + "§aEnabled"), false);
                shellwiseEspEnabled = true;
            }

        }
    }


    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (!shellwiseEspEnabled && !hideonleafEspEnabled) return;

        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();

        if (shellwiseEspEnabled){
            for (Vec3 target : MobESPClientTickHandler.trackedShellwise.values()) {
                Vec3 lookStart = mc.player.getEyePosition().add(mc.player.getLookAngle().scale(0.1));
                float r = 0.8f, g = 0.0f, b = 1.0f, a = 1.0f;
                LineRenderer.drawLine(poseStack, buffer, lookStart, target, cameraPos,r,g,b,a);
            }
        }

        if (hideonleafEspEnabled) {
            for (Vec3 target : MobESPClientTickHandler.trackedHideonleaf.values()) {
                Vec3 lookStart = mc.player.getEyePosition().add(mc.player.getLookAngle().scale(0.1));
                float r = 0.66f, g = 1.0f, b = 0.0f, a = 1.0f;
                LineRenderer.drawLine(poseStack, buffer, lookStart, target, cameraPos, r, g, b, a);
            }
        }

        buffer.endBatch(); // Do this once per frame
    }

}
