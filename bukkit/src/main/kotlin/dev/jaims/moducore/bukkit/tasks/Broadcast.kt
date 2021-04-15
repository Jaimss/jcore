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

package dev.jaims.moducore.bukkit.tasks

import dev.jaims.moducore.bukkit.ModuCore
import dev.jaims.moducore.bukkit.config.Config
import dev.jaims.moducore.bukkit.config.Modules
import dev.jaims.moducore.bukkit.util.bukkitMessage
import dev.jaims.moducore.bukkit.util.langParsed

fun startBroadcast(plugin: ModuCore) {
    if (!plugin.api.fileManager.modules[Modules.AUTO_BROADCAST]) return
    plugin.server.scheduler.runTaskTimerAsynchronously(plugin, Runnable {

        val messageListRaw = plugin.api.fileManager.config[Config.BROADCAST_MESSAGES]

        if (messageListRaw.isEmpty()) {
            plugin.logger.warning("Your broadcast message list is empty!")
            return@Runnable
        }

        val messageRaw = messageListRaw.random()

        val messages = messageRaw.split("\\n").map { bukkitMessage.parse(it.langParsed) }
        for (message in messages) {
            for (player in plugin.server.onlinePlayers) {
                message.sendMessage(player)
            }
        }

    }, 10 * 20, plugin.api.fileManager.config[Config.BROADCAST_INTERVAL] * 20L)
}