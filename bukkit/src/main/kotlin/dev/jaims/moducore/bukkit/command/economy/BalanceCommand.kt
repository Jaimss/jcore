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

package dev.jaims.moducore.bukkit.command.economy

import dev.jaims.mcutils.bukkit.util.send
import dev.jaims.mcutils.common.InputType
import dev.jaims.mcutils.common.getInputType
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.BaseCommand
import dev.jaims.moducore.bukkit.command.CommandProperties
import dev.jaims.moducore.bukkit.config.Lang
import dev.jaims.moducore.bukkit.util.*
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class BalanceCommand(override val plugin: ModuCore) : BaseCommand
{
    override val usage: String = "/balance [target]"
    override val description: String = "Check your or another players balance."
    override val commandName: String = "balance"

    override fun execute(sender: CommandSender, args: List<String>, props: CommandProperties)
    {
        when (args.size)
        {
            0 ->
            {
                if (!Perm.BALANCE.has(sender)) return
                if (sender !is Player)
                {
                    sender.noConsoleCommand()
                    return
                }
                val balance = economyManager.getBalance(sender.uniqueId)
                sender.send(fileManager.getString(Lang.BALANCE, sender).replace("{balance}", decimalFormat.format(balance)))
            }
            1 ->
            {
                if (!Perm.BALANCE_TARGET.has(sender)) return
                var target: Player? = null
                val uuid = if (args[0].getInputType() == InputType.NAME)
                {
                    target = playerManager.getTargetPlayer(args[0]) ?: run {
                        sender.playerNotFound(args[0])
                        return
                    }
                    target.uniqueId
                } else
                {
                    UUID.fromString(args[0])
                }
                val balance = economyManager.getBalance(uuid)
                sender.send(fileManager.getString(Lang.BALANCE, target).replace("{balance}", decimalFormat.format(balance)))
            }
            else -> sender.usage(usage, description)
        }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String>
    {
        return mutableListOf<String>().apply {
            when (args.size)
            {
                1 -> addAll(playerManager.getPlayerCompletions(args[0]))
            }
        }

    }
}