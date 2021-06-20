/*
 * Mesh
 * Copyright (C) 2019-2021 UpcraftLP
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
package dev.upcraft.mesh.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import org.apache.commons.lang3.Validate;

import java.awt.*;

/**
 * utility class for batch-rendering geometric shapes
 */
@Environment(EnvType.CLIENT)
public final class ShapeRenderer {
    private final float[] color;
    private final MatrixStack matrixStack;
    private VertexConsumer vertexConsumer;
    private Identifier texture = TextureManager.MISSING_IDENTIFIER;
    private VertexConsumerProvider.Immediate immediate;

    public ShapeRenderer(MatrixStack matrixStack, VertexConsumerProvider.Immediate immediate, RenderLayer renderLayer) {
        Validate.isTrue(renderLayer.getDrawMode() == VertexFormat.DrawMode.QUADS, "Only QUADS draw mode supported!");
        Validate.isTrue(renderLayer.getVertexFormat() == VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, "Only POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL vertex format supported!");
        this.matrixStack = matrixStack;
        this.immediate = immediate;
        this.vertexConsumer = immediate.getBuffer(renderLayer);
        this.color = new float[4];
        //render setup
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        //RenderSystem.disableLighting();
        this.matrixStack.push();
    }

    public MatrixStack getMatrixStack() {
        return matrixStack;
    }

    public ShapeRenderer color(float[] color) {
        System.arraycopy(color, 0, this.color, 0, 4);
        return this;
    }

    public ShapeRenderer color(int argb) {
        new Color(argb, true).getComponents(this.color);
        return this;
    }

    public ShapeRenderer color(float red, float green, float blue) {
        return color(red, green, blue, 1.0F);
    }

    public ShapeRenderer color(float red, float green, float blue, float alpha) {
        this.color[0] = red;
        this.color[1] = green;
        this.color[2] = blue;
        this.color[3] = alpha;
        return this;
    }

    public ShapeRenderer pos(double x, double y, double z) {
        return pos(new Vec3f((float) x, (float) y, (float) z));
    }

    public ShapeRenderer pos(Vec3f position) {
        this.matrixStack.translate(position.getX(), position.getY(), position.getZ());
        return this;
    }

    public ShapeRenderer quad(double x, double y, double z, Vec3f orientation, float rotation, float scale) {
        return quad(new Vec3f((float) x, (float) y, (float) z), orientation, rotation, scale);
    }

    public ShapeRenderer quad(Vec3f relativePos, Vec3f orientation, float rotation, float scale) {
        this.matrixStack.push();
        this.matrixStack.translate(relativePos.getX(), relativePos.getY(), relativePos.getZ());
        Matrix4f modelView = this.matrixStack.peek().getModel();
        modelView.multiply(new Quaternion(orientation, rotation, true));
        this.matrixStack.scale(scale, 1.0F, scale);
        Vec3f pos1 = new Vec3f(scale * 1.0F, 0.0005F, scale * -1.0F);
        Vec3f pos2 = new Vec3f(scale * -1.0F, 0.0005F, scale * -1.0F);
        Vec3f pos3 = new Vec3f(scale * -1.0F, 0.0005F, scale * 1.0F);
        Vec3f pos4 = new Vec3f(scale * 1.0F, 0.0005F, scale * 1.0F);
        //TODO use VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL
        if (true)
            throw new UnsupportedOperationException("not fully implemented!");
        this.vertexConsumer.vertex(modelView, pos1.getX(), pos1.getY(), pos1.getZ()).color(this.color[0], this.color[1], this.color[2], this.color[3]).texture(1.0F, 0.0F).overlay(OverlayTexture.DEFAULT_UV).next();
        this.vertexConsumer.vertex(modelView, pos2.getX(), pos2.getY(), pos2.getZ()).color(this.color[0], this.color[1], this.color[2], this.color[3]).texture(0.0F, 0.0F).overlay(OverlayTexture.DEFAULT_UV).next();
        this.vertexConsumer.vertex(modelView, pos3.getX(), pos3.getY(), pos3.getZ()).color(this.color[0], this.color[1], this.color[2], this.color[3]).texture(0.0F, 1.0F).overlay(OverlayTexture.DEFAULT_UV).next();
        this.vertexConsumer.vertex(modelView, pos4.getX(), pos4.getY(), pos4.getZ()).color(this.color[0], this.color[1], this.color[2], this.color[3]).texture(1.0F, 1.0F).overlay(OverlayTexture.DEFAULT_UV).next();
        matrixStack.pop();
        return this;
    }

    public void draw() {
        MinecraftClient.getInstance().getTextureManager().bindTexture(this.texture);
        immediate.draw();
        this.matrixStack.pop();
        RenderSystem.enableCull();
    }
}
