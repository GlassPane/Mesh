/*
 * Mesh
 * Copyright (C) 2019 GlassPane
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; If not, see <https://www.gnu.org/licenses>.
 */
package com.github.glasspane.mesh.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.GlAllocationUtils;
import net.minecraft.util.Identifier;
import net.minecraft.util.SystemUtil;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.Random;

public class EndTextureRenderer {

    private static final Identifier SKY_TEX = new Identifier("textures/environment/end_sky.png");
    private static final Identifier PORTAL_TEX = new Identifier("textures/entity/end_portal.png");
    private static final Random RANDOM = new Random(31100L);
    private static final FloatBuffer modelBuffer = GlAllocationUtils.allocateFloatBuffer(16);
    private static final FloatBuffer projectionBuffer = GlAllocationUtils.allocateFloatBuffer(16);
    private static final FloatBuffer STRBuffer = GlAllocationUtils.allocateFloatBuffer(16);
    /**
     * render the end portal texture
     *
     * @param color the color in {@code 0xAARRGGBB} format
     */
    public static void renderEndPortalTexture(double x, double y, double width, double height, double zLevel, int color) {
        float[] col = new Color(color, true).getRGBComponents(null);
        renderEndPortalTexture(x, y, width, height, zLevel, col[0], col[1], col[2], col[3]);
    }

    /**
     * render the end portal texture
     *
     * @param red,green,blue,alpha the color components in a range from 0.0 to 1.0 (both inclusive)
     */
    public static void renderEndPortalTexture(double x, double y, double width, double height, double zLevel, float red, float green, float blue, float alpha) {
        RANDOM.setSeed(31100L);
        MinecraftClient client = MinecraftClient.getInstance();
        GameRenderer gameRenderer = client.gameRenderer;
        boolean flag = false;
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.pushTextureAttributes();
        GlStateManager.pushMatrix();
        {
            GlStateManager.getMatrix(GL11.GL_MODELVIEW_MATRIX, modelBuffer);
            GlStateManager.getMatrix(GL11.GL_PROJECTION_MATRIX, projectionBuffer);
            for(int i = 0; i <= 15; i++) {
                float float_3 = 2.0F / (float) (18 - i);
                GlStateManager.pushMatrix();
                {
                    if(i == 0) {
                        client.getTextureManager().bindTexture(SKY_TEX);
                        float_3 = 0.15F;
                        GlStateManager.enableBlend();
                        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                    }
                    else {
                        client.getTextureManager().bindTexture(PORTAL_TEX);
                        flag = true;
                        gameRenderer.method_3201(true);
                    }
                    if(i == 1) {
                        GlStateManager.enableBlend();
                        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
                    }
                    GlStateManager.texGenMode(GlStateManager.TexCoord.S, GL11.GL_EYE_LINEAR);
                    GlStateManager.texGenMode(GlStateManager.TexCoord.T, GL11.GL_EYE_LINEAR);
                    GlStateManager.texGenMode(GlStateManager.TexCoord.R, GL11.GL_EYE_LINEAR);
                    GlStateManager.texGenParam(GlStateManager.TexCoord.S, GL11.GL_EYE_PLANE, prepareBuffer(1.0F, 0.0F, 0.0F));
                    GlStateManager.texGenParam(GlStateManager.TexCoord.T, GL11.GL_EYE_PLANE, prepareBuffer(0.0F, 1.0F, 0.0F));
                    GlStateManager.texGenParam(GlStateManager.TexCoord.R, GL11.GL_EYE_PLANE, prepareBuffer(0.0F, 0.0F, 1.0F));
                    GlStateManager.enableTexGen(GlStateManager.TexCoord.S);
                    GlStateManager.enableTexGen(GlStateManager.TexCoord.T);
                    GlStateManager.enableTexGen(GlStateManager.TexCoord.R);
                }
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(GL11.GL_TEXTURE);
                GlStateManager.pushMatrix();
                {
                    GlStateManager.loadIdentity();
                    GlStateManager.translatef(0.5F, 0.5F, 0.0F);
                    GlStateManager.scalef(0.5F, 0.5F, 1.0F);
                    float float_4 = i + 1;
                    GlStateManager.translatef(17.0F / float_4, (2.0F + float_4 / 1.5F) * ((float) (SystemUtil.getMeasuringTimeMs() % 800000L) / 800000.0F), 0.0F);
                    GlStateManager.rotatef((float_4 * float_4 * 4321.0F + float_4 * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.scalef(4.5F - float_4 / 4.0F, 4.5F - float_4 / 4.0F, 1.0F);
                    GlStateManager.multMatrix(projectionBuffer);
                    GlStateManager.multMatrix(modelBuffer);
                    GlStateManager.color3f(red, green, blue);
                    Tessellator tessellator_1 = Tessellator.getInstance();
                    BufferBuilder bufferBuilder_1 = tessellator_1.getBufferBuilder();
                    bufferBuilder_1.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);
                    float bufferRed = (RANDOM.nextFloat() * 0.5F + 0.1F) * float_3;
                    float bufferGreen = (RANDOM.nextFloat() * 0.5F + 0.4F) * float_3;
                    float bufferBlue = (RANDOM.nextFloat() * 0.5F + 0.5F) * float_3;
                    bufferBuilder_1.setOffset(x, y, zLevel);
                    bufferBuilder_1.vertex(0.0D, height, 0.0D).color(bufferRed, bufferGreen, bufferBlue, alpha).next();
                    bufferBuilder_1.vertex(width, height, 0.0D).color(bufferRed, bufferGreen, bufferBlue, alpha).next();
                    bufferBuilder_1.vertex(width, 0.0D, 0.0D).color(bufferRed, bufferGreen, bufferBlue, alpha).next();
                    bufferBuilder_1.vertex(0.0D, 0.0D, 0.0D).color(bufferRed, bufferGreen, bufferBlue, alpha).next();
                    bufferBuilder_1.setOffset(0.0D, 0.0D, 0.0D);
                    tessellator_1.draw();
                }
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(GL11.GL_MODELVIEW);
                client.getTextureManager().bindTexture(SKY_TEX);
            }
        }
        GlStateManager.popAttributes();
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
        GlStateManager.disableTexGen(GlStateManager.TexCoord.S);
        GlStateManager.disableTexGen(GlStateManager.TexCoord.T);
        GlStateManager.disableTexGen(GlStateManager.TexCoord.R);
        GlStateManager.disableLighting();
        if(flag) {
            gameRenderer.method_3201(false);
        }
        GlStateManager.color3f(1.0F, 1.0F, 1.0F);
    }

    private static FloatBuffer prepareBuffer(float red, float green, float blue) {
        STRBuffer.clear();
        STRBuffer.put(red).put(green).put(blue).put(0.0F);
        STRBuffer.flip();
        return STRBuffer;
    }
}
