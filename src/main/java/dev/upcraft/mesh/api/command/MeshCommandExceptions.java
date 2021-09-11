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
package dev.upcraft.mesh.api.command;

import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.text.TranslatableText;

/**
 * @since 0.13.0
 */
public interface MeshCommandExceptions {

    DynamicCommandExceptionType INVALID_FILE_PATH = new DynamicCommandExceptionType(path -> new TranslatableText("command.mesh.error.path.invalid", path));
    SimpleCommandExceptionType IO_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText("command.mesh.error.io_error"));

    Dynamic2CommandExceptionType INVALID_ENUM_ARGUMENT_EXCEPTION = new Dynamic2CommandExceptionType((value, clazz) -> new TranslatableText("command.mesh.argument.enum.invalid", clazz, value));
    DynamicCommandExceptionType UNKNOWN_BLOCK_TAG = new DynamicCommandExceptionType(id -> new TranslatableText("command.mesh.argument.block_tag.unknown", id));
}
