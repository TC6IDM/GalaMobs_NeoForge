package com.tissi.galamobs;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.joml.Matrix4f;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = GalaMobs.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = GalaMobs.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class RenderHandler {
    @SubscribeEvent
    public static void onRender(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;


        PoseStack poseStack = event.getPoseStack();
        Camera camera = event.getCamera();
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();

        Vec3 camPos = camera.getPosition();
        Vec3 start = camPos; // from player's eye (camera)
        Vec3 end = new Vec3(0, -60, 0);

        VertexConsumer builder = buffer.getBuffer(RenderType.LINES);

        Matrix4f matrix = poseStack.last().pose();

        builder.addVertex(matrix, (float)(start.x - camPos.x), (float)(start.y - camPos.y), (float)(start.z - camPos.z))
                .setColor(0f, 1f, 0f, 1f)
                .setNormal(0,1,0);

        builder.addVertex(matrix, (float)(end.x - camPos.x), (float)(end.y - camPos.y), (float)(end.z - camPos.z))
                .setColor(0f, 1f, 0f, 1f)
                .setNormal(0,1,0);
    }

}
