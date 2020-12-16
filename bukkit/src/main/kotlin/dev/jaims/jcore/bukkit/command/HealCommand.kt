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
import dev.jaims.mcutils.bukkit.feed
import dev.jaims.mcutils.bukkit.heal
import dev.jaims.mcutils.bukkit.send
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HealCommand(private val plugin: JCore) : JCoreCommand {
    override val usage = "/heal [target]"
    override val description: String = "Heal yourself or a target."
    override val commandName: String = "heal"

    val playerManager = plugin.managers.playerManager

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        when (args.size) {
            // heal self
            0 -> {
                // check if they have permission
                if (!Perm.HEAL.has(sender)) return false
                // only players can run command
                if (sender !is Player) {
                    sender.noConsoleCommand()
                    return false
                }
                sender.heal()
                sender.feed()
                sender.send(Lang.HEALED.get(sender))
            }
            // heal others
            1 -> {
                if (!Perm.HEAL_OTHERS.has(sender)) return false
                val target = playerManager.getTargetPlayer(args[0]) ?: run {
                    sender.playerNotFound(args[0])
                    return false
                }
                target.heal()
                target.feed()
                target.send(Lang.HEALED.get(target))
                sender.send(Lang.HEALED_TARGET.get(target).replace("{target}", playerManager.getName(target)))
            }
            // usage
            else -> {
                sender.usage(usage, description)
            }
        }

        return true
    }
}