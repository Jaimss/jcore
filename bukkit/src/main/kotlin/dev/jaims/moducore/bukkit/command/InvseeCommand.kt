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

package dev.jaims.moducore.bukkit.command

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.gui.FILLER
import dev.jaims.moducore.bukkit.util.Permissions
import dev.jaims.moducore.bukkit.util.noConsoleCommand
import dev.jaims.moducore.bukkit.util.playerNotFound
import dev.jaims.moducore.bukkit.util.usage
import me.mattstudios.config.properties.Property
import me.mattstudios.mfgui.gui.guis.Gui
import me.mattstudios.mfgui.gui.guis.GuiItem
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class InvseeCommand(override val plugin: ModuCore) : BaseCommand {
    override suspend fun execute(sender: CommandSender, args: List<String>, props: CommandProperties) {
        if (!Permissions.INVSEE.has(sender)) return
        if (sender !is Player) {
            sender.noConsoleCommand()
            return
        }

        val targetName = args.firstOrNull() ?: run {
            sender.usage(usage, description)
            return
        }
        val target = playerManager.getTargetPlayer(targetName) ?: run {
            sender.playerNotFound(targetName)
            return
        }
        Gui("Inventory").apply {
            addItem(*target.inventory.storageContents.mapNotNull { if (it == null) it else GuiItem(it) }.toTypedArray());
            rows = 6
            filler.fillBetweenPoints(5, 1, 5, 9, FILLER)
            if (target.inventory.helmet != null) setItem(6, 1, GuiItem(target.inventory.helmet!!))
            if (target.inventory.chestplate != null) setItem(6, 2, GuiItem(target.inventory.chestplate!!))
            if (target.inventory.leggings != null) setItem(6, 3, GuiItem(target.inventory.leggings!!))
            if (target.inventory.boots != null) setItem(6, 4, GuiItem(target.inventory.boots!!))
            setDefaultClickAction { it.isCancelled = true }
        }.open(sender)
        return
    }

    override val module: Property<Boolean> = Modules.COMMAND_INVSEE
    override val usage: String = "/invsee <target>"
    override val description: String = "View a players inventory."
    override val commandName: String = "invsee"
}