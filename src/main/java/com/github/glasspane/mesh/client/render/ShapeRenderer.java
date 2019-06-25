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

import com.github.glasspane.mesh.util.MathUtil;
import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.awt.*;

/**
 * utility class for batch-rendering geometric shapes
 */
@Environment(EnvType.CLIENT)
public final class ShapeRenderer {
    private final float[] color;
    private Tessellator tessellator;
    private BufferBuilder vertexBuffer;
    private Identifier texture = TextureManager.MISSING_IDENTIFIER;
    private Matrix4f model = new Matrix4f();
    private Vector3f pos = new Vector3f();

    public ShapeRenderer() {
        this.color = new float[4];
        //render setup
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        this.tessellator = Tessellator.getInstance();
        this.vertexBuffer = this.tessellator.getBufferBuilder();
        this.vertexBuffer.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV_COLOR);
        this.model.setIdentity();
    }

    public ShapeRenderer texture(Identifier texture) {
        this.texture = texture;
        return this;
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
        return pos(new Vector3f((float) x, (float) y, (float) z));
    }

    public ShapeRenderer pos(Vector3f position) {
        this.pos = position;
        model.setTranslation(this.pos);
        return this;
    }

    public ShapeRenderer quad(double x, double y, double z, Vector3f orientation, float rotation, float scale) {
        return quad(new Vector3f((float) x, (float) y, (float) z), orientation, rotation, scale);
    }

    public ShapeRenderer quad(Vector3f relativePos, Vector3f orientation, float rotation, float scale) {
        //TODO implement scale!
        this.model.rotY((float) (rotation * MathUtil.TAU));
        relativePos.add(this.pos);
        this.vertexBuffer.setOffset(relativePos.x, relativePos.y, relativePos.z);
        Vector3f pos1 = new Vector3f(scale * 1.0F, 0.0005F, scale * -1.0F);
        Vector3f pos2 = new Vector3f(scale * -1.0F, 0.0005F, scale * -1.0F);
        Vector3f pos3 = new Vector3f(scale * -1.0F, 0.0005F, scale * 1.0F);
        Vector3f pos4 = new Vector3f(scale * 1.0F, 0.0005F, scale * 1.0F);
        this.model.transform(pos1);
        this.model.transform(pos2);
        this.model.transform(pos3);
        this.model.transform(pos4);
        this.vertexBuffer.vertex(pos1.x, pos1.y, pos1.z).texture(1.0D, 0.0D).color(this.color[0], this.color[1], this.color[2], this.color[3]).next();
        this.vertexBuffer.vertex(pos2.x, pos2.y, pos2.z).texture(0.0D, 0.0D).color(this.color[0], this.color[1], this.color[2], this.color[3]).next();
        this.vertexBuffer.vertex(pos3.x, pos3.y, pos3.z).texture(0.0D, 1.0D).color(this.color[0], this.color[1], this.color[2], this.color[3]).next();
        this.vertexBuffer.vertex(pos4.x, pos4.y, pos4.z).texture(1.0D, 1.0D).color(this.color[0], this.color[1], this.color[2], this.color[3]).next();
        this.vertexBuffer.setOffset(0.0D, 0.D, 0.0D);
        return this;
    }

    public void draw() {
        MinecraftClient.getInstance().getTextureManager().bindTexture(this.texture);
        this.tessellator.draw();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }
}
