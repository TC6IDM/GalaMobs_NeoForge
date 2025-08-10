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

import java.awt.*;


public class LineRenderer {

    // Main entry: Draw a line from the player's crosshair to the given world position
    public static void drawLineToPosition(PoseStack poseStack, MultiBufferSource buffer, double targetX, double targetY, double targetZ) {
        Minecraft mc = Minecraft.getInstance();

        Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();   // Camera position for rendering offset
        if (mc.player == null) return;
        Vec3 eyePos = mc.player.getEyePosition();                         // Player eye position
        Vec3 lookDir = mc.player.getLookAngle();                          // Player look vector

        // Start slightly in front of crosshair (feels better than exact eye)
        Vec3 start = eyePos.add(lookDir.scale(0.1));
        Vec3 end = new Vec3(targetX, targetY, targetZ);
        RGBAColour colour = new RGBAColour(0.0f, 1.0f,0.0f, 1.0f);
        drawLine(poseStack, buffer, start, end, cameraPos,colour);
    }

    // Lower-level drawing method with full control
    public static void drawLine(PoseStack poseStack, MultiBufferSource buffer, Vec3 from, Vec3 to, Vec3 cameraPos, RGBAColour colour) {
        VertexConsumer builder = buffer.getBuffer(RenderType.LINES);
        Matrix4f matrix = poseStack.last().pose();

        builder.addVertex(matrix,
                (float) (from.x - cameraPos.x),
                (float) (from.y - cameraPos.y),
                (float) (from.z - cameraPos.z))
                .setColor(colour.r, colour.g, colour.b, colour.a).setNormal(0, 1, 0);

        builder.addVertex(matrix,
                (float) (to.x - cameraPos.x),
                (float) (to.y - cameraPos.y),
                (float) (to.z - cameraPos.z))
                .setColor(colour.r, colour.g, colour.b, colour.a).setNormal(0, 1, 0);
    }

    public static void drawBox(PoseStack poseStack, MultiBufferSource buffer, Vec3 middle,  Vec3 cameraPos, Vec3 boxVec, RGBAColour colour){
        double xlen = boxVec.x;
        double ylen = boxVec.y;
        double zlen = boxVec.z;

        /*

              +y|
                |
                |
                |
                |
               / \
              /   \
             /     \
          +x/       \+z

         */

        Vec3 backBottomLeft = new Vec3(middle.x -xlen/2,middle.y,middle.z -zlen/2);
        Vec3 frontTopRight = new Vec3(backBottomLeft.x + xlen,backBottomLeft.y + ylen,backBottomLeft.z + zlen);

        Vec3 backBottomRight = new Vec3(backBottomLeft.x,backBottomLeft.y,backBottomLeft.z+zlen);
        Vec3 backTopLeft = new Vec3(backBottomLeft.x,backBottomLeft.y+ylen,backBottomLeft.z);
        Vec3 frontBottomLeft = new Vec3(backBottomLeft.x+xlen,backBottomLeft.y,backBottomLeft.z);

        Vec3 frontTopLeft = new Vec3(frontTopRight.x,frontTopRight.y,frontTopRight.z-zlen);
        Vec3 frontBottomRight = new Vec3(frontTopRight.x,frontTopRight.y-ylen,frontTopRight.z);
        Vec3 backTopRight = new Vec3(frontTopRight.x-xlen,frontTopRight.y,frontTopRight.z);

        drawLine(poseStack,buffer,backBottomLeft,backBottomRight,cameraPos,colour);
        drawLine(poseStack,buffer,backBottomLeft,backTopLeft,cameraPos,colour);
        drawLine(poseStack,buffer,backBottomLeft,frontBottomLeft,cameraPos,colour);

        drawLine(poseStack,buffer,frontTopRight,frontTopLeft,cameraPos,colour);
        drawLine(poseStack,buffer,frontTopRight,frontBottomRight,cameraPos,colour);
        drawLine(poseStack,buffer,frontTopRight,backTopRight,cameraPos,colour);

        drawLine(poseStack,buffer,frontBottomLeft,frontTopLeft,cameraPos,colour);
        drawLine(poseStack,buffer,frontTopLeft,backTopLeft,cameraPos,colour);
        drawLine(poseStack,buffer,backTopLeft,backTopRight,cameraPos,colour);
        drawLine(poseStack,buffer,backTopRight,backBottomRight,cameraPos,colour);
        drawLine(poseStack,buffer,backBottomRight,frontBottomRight,cameraPos,colour);
        drawLine(poseStack,buffer,frontBottomRight,frontBottomLeft,cameraPos,colour);

    }
}
