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

package dev.jaims.moducore.bukkit.util

import dev.jaims.mcutils.bukkit.log
import dev.jaims.mcutils.bukkit.register
import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.command.*
import dev.jaims.moducore.bukkit.command.gamemode.GamemodeAdventure
import dev.jaims.moducore.bukkit.command.gamemode.GamemodeCreative
import dev.jaims.moducore.bukkit.command.gamemode.GamemodeSpectator
import dev.jaims.moducore.bukkit.command.gamemode.GamemodeSurvival
import dev.jaims.moducore.bukkit.command.nickname.NicknameCommand
import dev.jaims.moducore.bukkit.command.nickname.NicknameRemoveCommand
import dev.jaims.moducore.bukkit.command.repair.Repair
import dev.jaims.moducore.bukkit.command.repair.RepairAll
import dev.jaims.moducore.bukkit.command.speed.FlySpeedCommand
import dev.jaims.moducore.bukkit.command.speed.SpeedCommand
import dev.jaims.moducore.bukkit.command.speed.WalkSpeedCommand
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.event.listener.*
import javax.print.attribute.standard.Severity

/**
 * A list of all commands on the server for easy registration & help pages.
 */
val allCommands: MutableList<BaseCommand> = mutableListOf()

/**
 * Check the latest version and alert the servers console if it isn't the latest.
 */
internal fun ModuCore.notifyVersion()
{
    val latestVersion = getLatestVersion(86911)
    if (latestVersion != null && latestVersion != description.version)
    {
        log(
            "There is a new version of ModuCore Available ($latestVersion)! Please download it from https://www.spigotmc.org/resources/86911/",
            Severity.WARNING
        )
    }
}

/**
 * Method to register the events of [ModuCore]
 */
internal fun ModuCore.registerEvents()
{
    this.register(
        SignChangeListener(this),
        PlayerChatListener(this),
        PlayerInteractListener(this),
        PlayerJoinListener(this),
        PlayerQuitListener(this)
    )
}

/**
 * Method to register the commands of [ModuCore]
 */
internal fun ModuCore.registerCommands()
{
    val modules = this.api.fileManager.modules

    // add a list of elements
    fun <T> MutableList<T>.addMultiple(vararg element: T): MutableList<T>
    {
        element.forEach {
            add(it)
        }
        return this
    }

    if (modules.getProperty(Modules.COMMAND_GAMEMODE)) allCommands.addMultiple(
        GamemodeAdventure(this),
        GamemodeCreative(this),
        GamemodeSpectator(this),
        GamemodeSurvival(this)
    )
    if (modules.getProperty(Modules.COMMAND_NICKNAME)) allCommands.addMultiple(
        NicknameCommand(this),
        NicknameRemoveCommand(this)
    )
    if (modules.getProperty(Modules.COMMAND_REPAIR)) allCommands.addMultiple(
        Repair(this),
        RepairAll(this)
    )
    if (modules.getProperty(Modules.COMMAND_SPEED)) allCommands.addMultiple(
        FlySpeedCommand(this),
        SpeedCommand(this),
        WalkSpeedCommand(this)
    )
    if (modules.getProperty(Modules.COMMAND_CLEARINVENTORY)) allCommands.add(ClearInventoryCommand(this))
    if (modules.getProperty(Modules.COMMAND_DISPOSE)) allCommands.add(DisposeCommand(this))
    if (modules.getProperty(Modules.COMMAND_FEED)) allCommands.add(FeedCommand(this))
    if (modules.getProperty(Modules.COMMAND_FLY)) allCommands.add(FlyCommand(this))
    if (modules.getProperty(Modules.COMMAND_GIVE)) allCommands.add(GiveCommand(this))
    if (modules.getProperty(Modules.COMMAND_HEAL)) allCommands.add(HealCommand(this))
    if (modules.getProperty(Modules.COMMAND_HELP)) allCommands.add(HelpCommand(this))
    allCommands.add(ReloadCommand(this))

    allCommands.forEach {
        it.register(this)
    }
}