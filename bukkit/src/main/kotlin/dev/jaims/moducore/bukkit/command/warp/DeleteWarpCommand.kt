/*
 * This file is a part of ModuCore, licensed under the MIT License.
 *
 * Copyright (c) 2020 James Harrell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.jaims.moducore.bukkit.command.warp

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.jaims.mcutils.bukkit.util.send
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.config.Warps
import dev.jaims.moducore.bukkit.util.Perm
import dev.jaims.moducore.bukkit.util.usage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class DeleteWarpCommand(override val plugin: ModuCore) : BaseCommand {
    override val usage: String = "/delwarp <name>"
    override val description: String = "Delete a warp."
    override val commandName: String = "deletewarp"

    override val commodoreSyntax: LiteralArgumentBuilder<*>?
        get() = LiteralArgumentBuilder.literal<String>(commandName)
            .then(
                RequiredArgumentBuilder.argument("name", StringArgumentType.word())
            )

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (!Perm.DEL_WARP.has(sender)) return

        if (args.size != 1) {
            sender.usage(usage, description)
            return
        }

        val warp = args[0]
        val currentWarps = fileManager.warps.getProperty(Warps.WARPS).toMutableMap()
        val removed = currentWarps.remove(warp)
        fileManager.warps.setProperty(Warps.WARPS, currentWarps)
        fileManager.warps.save()

        if (removed == null) {
            sender.send(fileManager.getString(Lang.WARP_NOT_FOUND).replace("{name}", warp))
        } else {
            sender.send(fileManager.getString(Lang.WARP_DELETED).replace("{name}", warp))
        }

    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String> {
        return mutableListOf<String>().apply {
            when (args.size) {
                1 -> addAll(fileManager.warps.getProperty(Warps.WARPS).keys.filter { it.startsWith(args[0], ignoreCase = true) })
            }
        }
    }

}