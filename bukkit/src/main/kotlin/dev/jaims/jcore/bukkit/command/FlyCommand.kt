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
import dev.jaims.jcore.bukkit.manager.*
import dev.jaims.jcore.bukkit.manager.config.Lang
import dev.jaims.mcutils.bukkit.send
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FlyCommand(private val plugin: JCore) : JCoreCommand {

    private val playerManager = plugin.managers.playerManager

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        // invalid args length
        if (args.size > 1) {
            sender.usage(usage, description)
            return false
        }

        when (args.size) {
            // for a single player
            0 -> {
                if (!Perm.FLY.has(sender)) return false
                // only fly for Players
                if (sender !is Player) {
                    sender.noConsoleCommand()
                    return false
                }
                sender.toggleFlight(playerManager)
            }
            // for a target player
            1 -> {
                if (!Perm.FLY_OTHERS.has(sender)) return false
                val target = playerManager.getTargetPlayer(args[0]) ?: run {
                    sender.playerNotFound(args[0])
                    return false
                }
                target.toggleFlight(playerManager, sender)
            }
        }

        return true
    }

    override val commandName = "fly"
    override val usage = "/fly [target]"
    override val description = "Enable fly for yourself or another player."

}

/**
 * Enable flight for a [this] and optionally [sendMessage] to the player letting them know they
 * now have flight enabled.
 * If [executor] is null, the player changed their own flight. If [executor] is not null, someone else changed
 * their flight.
 *
 * @return True if they are now flying, false if they were already flying.
 */
internal fun Player.enableFlight(
    playerManager: PlayerManager,
    executor: CommandSender? = null,
    sendMessage: Boolean = true
) {
    // set them to flying
    isFlying = true
    if (sendMessage) {
        send(Lang.FLIGHT_ENABLED.get())
        executor?.send(Lang.FLIGHT_ENABLED_TARGET.get().replace("{target}", playerManager.getName(this)))
    }
}

/**
 * Disable flight for a [this] and optionally [sendMessage] to the player letting them know they are no longer
 * flying.
 * If [executor] is null, the player changed their own flight. If [executor] is not null, someone else changed
 * their flight.
 *
 * @return True if they are no longer flying, false if they were already flying.
 */
internal fun Player.disableFlight(
    playerManager: PlayerManager,
    executor: CommandSender? = null,
    sendMessage: Boolean = true
) {
    isFlying = false
    if (sendMessage) {
        send(Lang.FLIGHT_DISABLED.get())
        executor?.send(Lang.FLIGHT_DISABLED_TARGET.get().replace("{target}", playerManager.getName(this)))
    }
}

/**
 * Toggle flight using [disableFlight] and [enableFlight]
 */
internal fun Player.toggleFlight(
    playerManager: PlayerManager,
    executor: CommandSender? = null,
    sendMessage: Boolean = true
) {
    if (isFlying) disableFlight(playerManager, executor, sendMessage)
    else enableFlight(playerManager, executor, sendMessage)
}
