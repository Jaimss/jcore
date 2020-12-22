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

package dev.jaims.jcore.api.manager

import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

/**
 * Manages all [Player] related methods.
 */
interface PlayerManager {

    /**
     * Change a players gamemode to a new gamemode.
     *
     * @param player The [Player] whose gamemode we are changing.
     * @param newGameMode the new [GameMode] that the player will be.
     * @param executor the person who ran the command or null if the player did it to themselves
     * @param sendMessage if true sends messages to players involved, if false it doesn't
     *
     * @sample dev.jaims.jcore.example.listener.JoinListener
     */
    fun changeGamemode(player: Player, newGameMode: GameMode, executor: CommandSender? = null, sendMessage: Boolean = true)

    /**
     * Disable a players flight.
     *
     * @param player the player whose flight to change
     * @param executor the person who ran the command or null if the player changed their own flight
     * @param sendMessage true if message should be sent, false if otherwise.
     */
    fun disableFlight(player: Player, executor: CommandSender? = null, sendMessage: Boolean = true)

    /**
     * Enable flight for a player.
     *
     * @param player the player whose flight to change
     * @param executor the person who ran the command or null if the player changed their own flight
     * @param sendMessage true if message should be sent, false if otherwise.
     */
    fun enableFlight(player: Player, executor: CommandSender? = null, sendMessage: Boolean = true)

    /**
     * Get a players name from their [uuid] - Sample shows a join listener that uses the [getName]
     * method.
     *
     * @param uuid the UUID of the [Player] whose name you want to get.
     *
     * @sample dev.jaims.jcore.example.listener.JoinListener
     */
    fun getName(uuid: UUID): String

    /**
     * Get a list of Player Names that can be used in tab completions.
     *
     * @param input the arg that they are currently typing
     *
     * @return A MutableList of Player names as Strings
     *
     * @sample dev.jaims.jcore.bukkit.command.FeedCommand.onTabComplete
     */
    fun getPlayerCompletions(input: String): MutableList<String>


    /**
     * Get a target player from their name.
     *
     * @param input the players name
     *
     * @return the [Player] or null, if none is found.
     *
     * @sample dev.jaims.jcore.bukkit.command.gamemode.GamemodeCreative.onCommand
     */
    fun getTargetPlayer(input: String): Player?

    /**
     * Toggle flight for a player.
     * @see enableFlight
     * @see disableFlight
     *
     * @param player the player whose flight to change
     * @param executor the person who ran the command or null if the player changed their own flight
     * @param sendMessage true if message should be sent, false if otherwise.
     */
    fun toggleFlight(player: Player, executor: CommandSender? = null, sendMessage: Boolean = true) {
        if (player.allowFlight) disableFlight(player, executor, sendMessage)
        else enableFlight(player, executor, sendMessage)
    }

    /**
     * Method to repair a players item in hand.
     *
     * @param player the player whose item you want to repair
     * @param executor is nullable. if it is null, the player ran the command on themselves, otherwise someone else ran it on the player.
     * @param sendMessage if it should send the message to the player saying their item was repaired.
     *
     * @sample dev.jaims.jcore.bukkit.command.repair.Repair.onCommand
     */
    fun repair(player: Player, executor: CommandSender? = null, sendMessage: Boolean = true)

    /**
     * Method to repair all things in a players inventory.
     *
     * @param player the player whose item you want to repair
     * @param executor is nullable. if it is null, the player ran the command on themselves, otherwise someone else ran it on the player.
     * @param sendMessage if it should send the message to the player saying their item was repaired.
     *
     * @sample dev.jaims.jcore.bukkit.command.repair.RepairAll.onCommand
     */
    fun repairAll(player: Player, executor: CommandSender? = null, sendMessage: Boolean = true)

}