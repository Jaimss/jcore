/*
 * This file is a part of JCore, licensed under the MIT License.
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

package dev.jaims.jcore.bukkit.command

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.manager.Perm
import dev.jaims.jcore.bukkit.manager.config.Lang
import dev.jaims.jcore.bukkit.manager.noConsoleCommand
import dev.jaims.jcore.bukkit.manager.playerNotFound
import dev.jaims.jcore.bukkit.manager.usage
import dev.jaims.mcutils.bukkit.send
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ClearInventoryCommand(private val plugin: JCore) : JCoreCommand {
    override val usage = "/clear [target]"
    override val description = "Clear your inventory or a targets."
    override val commandName = "clear"

    val playerManager = plugin.managers.playerManager

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when (args.size) {
            0 -> {
                if (!Perm.CLEAR.has(sender)) return false
                if (sender !is Player) {
                    sender.noConsoleCommand()
                    return false
                }
                sender.inventory.clear()
                sender.send(Lang.INV_CLEARED.get(sender))
            }
            1 -> {
                if (!Perm.CLEAR_OTHERS.has(sender)) return false
                val target = playerManager.getTargetPlayer(args[0]) ?: run {
                    sender.playerNotFound(args[0])
                    return false
                }
                target.inventory.clear()
                target.send(Lang.INV_CLEARED.get(target))
                sender.send(Lang.INV_CLEARED_TARGET.get(target).replace("{target}", playerManager.getName(target)))
            }
            else -> sender.usage(usage, description)
        }
        return true
    }
}