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

package dev.jaims.jcore.bukkit.manager

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.jcore.bukkit.command.*
import dev.jaims.jcore.bukkit.command.gamemode.GamemodeAdventure
import dev.jaims.jcore.bukkit.command.gamemode.GamemodeCreative
import dev.jaims.jcore.bukkit.command.gamemode.GamemodeSpectator
import dev.jaims.jcore.bukkit.command.gamemode.GamemodeSurvival
import dev.jaims.jcore.bukkit.event.listener.PlayerChatListener
import dev.jaims.jcore.bukkit.event.listener.PlayerJoinListener
import dev.jaims.jcore.bukkit.event.listener.PlayerQuitListener
import dev.jaims.jcore.bukkit.manager.config.FileManager
import dev.jaims.jcore.bukkit.manager.config.Modules
import dev.jaims.mcutils.bukkit.register

/**
 * Managers class to avoid clutter in the main class
 */
class Managers(plugin: JCore) {
    val fileManager = FileManager(plugin)
    val playerManager = PlayerManager(plugin)
}

/**
 * Method to register the events of the plugin
 */
internal fun registerEvents(plugin: JCore) {
    plugin.register(
        PlayerChatListener(plugin),
        PlayerJoinListener(plugin),
        PlayerQuitListener(plugin)
    )
}

/**
 * Method to register the commands.
 */
internal fun registerCommands(plugin: JCore) {
    // add a list of elements
    fun <T> MutableList<T>.addMultiple(vararg element: T): MutableList<T> {
        element.forEach {
            add(it)
        }
        return this
    }

    if (Modules.COMMAND_GAMEMODE.getBool()) allCommands.addMultiple(
        GamemodeAdventure(plugin),
        GamemodeCreative(plugin),
        GamemodeSpectator(plugin),
        GamemodeSurvival(plugin)
    )
    if (Modules.COMMAND_CLEARINVENTORY.getBool()) allCommands.add(ClearInventoryCommand(plugin))
    if (Modules.COMMAND_FEED.getBool()) allCommands.add(FeedCommand(plugin))
    if (Modules.COMMAND_FLY.getBool()) allCommands.add(FlyCommand(plugin))
    if (Modules.COMMAND_GIVE.getBool()) allCommands.add(GiveCommand(plugin))
    if (Modules.COMMAND_HEAL.getBool()) allCommands.add(HealCommand(plugin))
    if (Modules.COMMAND_HEAL.getBool()) allCommands.add(HelpCommand(plugin))

    allCommands.forEach {
        it.register(plugin)
    }
}