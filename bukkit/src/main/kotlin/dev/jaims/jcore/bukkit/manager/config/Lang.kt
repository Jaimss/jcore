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

package dev.jaims.jcore.bukkit.manager.config

import dev.jaims.jcore.bukkit.JCore
import dev.jaims.mcutils.bukkit.colorize
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

enum class Lang(override val path: String, override val default: Any) : ConfigFileEnum {
    // GENERAL
    GRAY("colors.gray", "&8"),
    GREEN("colors.green", "&a"),
    RED("colors.red", "&c"),
    NEUTRAL("colors.neutral", "&e"),
    ACCENT("colors.accent", "&3"),
    NAME("colors.name", "&6"),

    PREFIX_GOOD("prefixes.good", "{gray}({green}!{gray}){green}"),
    PREFIX_BAD("prefixes.bad", "{gray}({red}!{gray}){red}"),
    PREFIX_NEUTRAL("prefixes.neutral", "{gray}({neutral}!{gray}){neutral}"),
    PREFIX_INFO("prefixes.info", "{gray}&l»{accent}"),

    // chat format
    CHAT_FORMAT("chat_format", "{name_color}%jcore_displayname% &8&l»&f "),

    // login & logout messages
    JOIN_MESSAGE("join_message", "{prefix_good} {name_color}{player} {green}has logged in!"),
    QUIT_MESSAGE("quit_message", "{prefix_bad} {name_color}{player} {red}has logged out!"),

    // PERMISSION
    NO_PERMISSION("no_permission", "{prefix_bad} You do not have permission!"),

    // MUST BE PLAYER
    NO_CONSOLE_COMMAND(
        "no_console_command",
        "{prefix_bad} This command is not intended for console use. Please run as a player."
    ),

    // Invalid number
    INVALID_NUMBER("invalid_number", "{prefix_bad} Invalid number! Using default if one exists."),

    // TARGET NOT FOUND
    TARGET_NOT_FOUND("target_not_found", "{prefix_bad} No player found!"),
    TARGET_NOT_FOUND_WITH_NAME("named_target_not_found", "{prefix_bad} No player found matching {name_color}{target}."),

    GAMEMODE_CHANGED("gamemode.changed", "{prefix_good} Your gamemode has been changed to {accent}{new}."),
    GAMEMODE_CHANGED_TARGET(
        "gamemode.changed_target",
        "{prefix_good} You changed {name_color}{target}'s {green}gamemode from {accent}{old} {green}to {accent}{new}."
    ),

    // Clear Inventory Command
    INV_CLEARED("clear.inventory_cleared", "{prefix_good} Your inventory has been cleared."),
    INV_CLEARED_TARGET(
        "clear.inventory_cleared_target",
        "{prefix_good} You have cleared {name_color}{target}'s {green}inventory."
    ),

    // FEED Command
    FED("feed.fed", "{prefix_good} You have been fed."),
    FED_TARGET("feed.fed_target", "{prefix_good} You have fed {name_color}{target}."),

    // FLY
    FLIGHT_ENABLED("fly.enabled", "{prefix_good} Your flight has been {accent}enabled!"),
    FLIGHT_DISABLED("fly.disabled", "{prefix_good} Your flight has been {accent}disabled."),
    FLIGHT_ENABLED_TARGET(
        "fly.target.enabled",
        "{prefix_good} You have {accent}enabled {name_color}{target}'s {green}flight!"
    ),
    FLIGHT_DISABLED_TARGET(
        "fly.target.disabled",
        "{prefix_good} You have {accent}disabled {name_color}{target}'s {green}flight."
    ),

    // GIVE Command
    GIVE_MATERIAL_NOT_FOUND("give.material_not_found", "{prefix_bad} No material found matching {accent}{material}."),
    GIVE_SUCCESS("give.success", "{prefix_good} You have been given {accent}x{amount} {material}."),
    GIVE_SUCCESS_TARGET(
        "give.success_target",
        "{prefix_good} You have given {name_color}{target} {accent}x{amount} {material}."
    ),

    // HEAL Command
    HEALED("heal.healed", "{prefix_good} You have been healed!"),
    HEALED_TARGET("heal.healed_target", "{prefix_good} You have healed {name_color}{target}."),

    // HELP
    HELP_HEADER("help.header", "{accent}&lJCore - Help Menu {gray}(Filter: {filter})"),
    HELP_NOT_FOUND(
        "help.no_command_found",
        "{prefix_bad} No command found matching {accent}{name}. {red}Try {accent}/help {red} for a list of commands!"
    ),
    HELP_COMMAND_USAGE("help.command_usage", "{prefix_neutral} {usage}"),
    HELP_COMMAND_DESCIPTION("help.command_description", "{prefix_info} {description}");


    /**
     * Get a message from the enum.
     * Will parse placeholders for a [player] if provided.
     */
    fun get(player: Player? = null, colored: Boolean = true): String {
        val m =
            (JavaPlugin.getPlugin(JCore::class.java).managers.fileManager.lang.getString(path) ?: default.toString())
                .replace("{prefix_good}", PREFIX_GOOD.default.toString())
                .replace("{prefix_bad}", PREFIX_BAD.default.toString())
                .replace("{prefix_neutral}", PREFIX_NEUTRAL.default.toString())
                .replace("{prefix_info}", PREFIX_INFO.default.toString())
                .replace("{accent}", ACCENT.default.toString())
                .replace("{gray}", GRAY.default.toString())
                .replace("{red}", RED.default.toString())
                .replace("{green}", GREEN.default.toString())
                .replace("{neutral}", NEUTRAL.default.toString())
                .replace("{name_color}", NAME.default.toString())
        return if (colored) m.colorize(player) else m
    }

}
