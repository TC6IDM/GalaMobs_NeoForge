package com.tissi.galamobs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;


public class LineRenderer {

    // Main entry: Draw a line from the player's crosshair to the given world position
    public static void drawLineToPosition(PoseStack poseStack, MultiBufferSource buffer, double targetX, double targetY, double targetZ) {
        Minecraft mc = Minecraft.getInstance();

        Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();   // Camera position for rendering offset
        Vec3 eyePos = mc.player.getEyePosition();                         // Player eye position
        Vec3 lookDir = mc.player.getLookAngle();                          // Player look vector

        // Start slightly in front of crosshair (feels better than exact eye)
        Vec3 start = eyePos.add(lookDir.scale(0.1));
        Vec3 end = new Vec3(targetX, targetY, targetZ);
        float r = 0.0f, g = 1.0f, b = 0.0f, a = 1.0f;
        drawLine(poseStack, buffer, start, end, cameraPos,r,g,b,a);
    }

    // Lower-level drawing method with full control
    public static void drawLine(PoseStack poseStack, MultiBufferSource buffer, Vec3 from, Vec3 to, Vec3 cameraPos, float r, float g, float b, float a) {
        VertexConsumer builder = buffer.getBuffer(RenderType.LINES);
        Matrix4f matrix = poseStack.last().pose();

//        float r = 0.0f, g = 1.0f, b = 0.0f, a = 1.0f;

        builder.addVertex(matrix,
                (float) (from.x - cameraPos.x),
                (float) (from.y - cameraPos.y),
                (float) (from.z - cameraPos.z))
                .setColor(r, g, b, a).setNormal(0, 1, 0);

        builder.addVertex(matrix,
                (float) (to.x - cameraPos.x),
                (float) (to.y - cameraPos.y),
                (float) (to.z - cameraPos.z))
                .setColor(r, g, b, a).setNormal(0, 1, 0);
    }
}
