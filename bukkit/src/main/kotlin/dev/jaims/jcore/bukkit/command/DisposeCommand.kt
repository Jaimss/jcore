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
import dev.jaims.jcore.bukkit.manager.config.Config
import dev.jaims.jcore.bukkit.util.noConsoleCommand
import dev.jaims.mcutils.bukkit.log
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import javax.print.attribute.standard.Severity

class DisposeCommand(private val plugin: JCore) : JCoreCommand
{

    override val usage: String = "/dispose"
    override val description: String = "Get rid of your extra items!"
    override val commandName: String = "dispose"

    val fileManager = plugin.api.fileManager

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean
    {

        if (!Perm.DISPOSE.has(sender)) return false

        if (sender !is Player)
        {
            sender.noConsoleCommand()
            return false
        }

        val rows = fileManager.config.getProperty(Config.DISPOSE_SIZE)
        if (rows < 1 || rows > 6)
        {
            plugin.log("${Config.DISPOSE_SIZE.path} must be an integer between 1 and 6!", Severity.ERROR)
        }

        val inventory = Bukkit.createInventory(
            null,
            rows * 9,
            fileManager.getString(Config.DISPOSE_TITLE, manager = fileManager.config)
        )
        sender.openInventory(inventory)

        return true
    }
}