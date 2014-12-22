/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2014 Squawkers13 <Squawkers13@pekkit.net>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.pekkit.actionbarbroadcast.commands;

import java.io.File;
import java.io.IOException;

import net.pekkit.actionbarbroadcast.ActionBarBroadcast;
import net.pekkit.actionbarbroadcast.BroadcastManager;
import net.pekkit.actionbarbroadcast.locale.MessageSender;
import net.pekkit.actionbarbroadcast.util.ActionUtils;
import net.pekkit.actionbarbroadcast.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Squawkers13
 */
public class BaseCommandExecutor implements CommandExecutor {

    private final ActionBarBroadcast plugin;
    private final BroadcastManager bm;

    public BaseCommandExecutor(ActionBarBroadcast par1, BroadcastManager par2) {
        plugin = par1;
        bm = par2;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ab")) {
            baseCommand(command, sender, args);
        } else if (command.getName().equalsIgnoreCase("broadcast")) {
            broadcastCommand(command, sender, args);
        }
        return true;
    }

    public void baseCommand(Command command, CommandSender sender, String[] args) {
        if (args.length < 1) {
            MessageSender.sendMsg(sender, "&5ActionBarBroadcast &d" + plugin.getDescription().getVersion() + ", &5created by &dSquawkers13");
            MessageSender.sendMsg(sender, "&5Type &d/ab ? &5for help.");
        } else if (args[0].equalsIgnoreCase("?")) {
            helpCommand(sender);
        } else if (args[0].equalsIgnoreCase("broadcast") || args[0].equalsIgnoreCase("b")) {
            broadcastCommand(command, sender, args);
        } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r")) {
            reloadCommand(sender);
        } else { //Invalid arguments
            MessageSender.sendMsg(sender, "&5I'm not sure what you mean: &d" + args[0]);
            MessageSender.sendMsg(sender, "&5Type &d/ab ?&b for help.");
        }
    }

    /**
     *
     * @param sender
     */
    public void helpCommand(CommandSender sender) {
        MessageSender.sendMsg(sender, "&9---------- &5ActionBarBroadcast: &dHelp &9----------");
        if (sender.hasPermission("actionbarbroadcast.broadcast")) {
            MessageSender.sendMsg(sender, "&5/ab &db,broadcast &9[message] &5- Broadcast a message to all online players.");
        }
        if (sender.hasPermission("actionbarbroadcast.reload")) {
            MessageSender.sendMsg(sender, "&5/ab &dr,reload &5- Reload the plugin's configuration.");
        }
    }

    /**
     *
     * @param command
     * @param sender
     * @param args
     */
    public void broadcastCommand(Command command, CommandSender sender, String[] args) {
        if (!sender.hasPermission("actionbarbroadcast.broadcast")) {
            MessageSender.sendMsg(sender, "&cYou don't have permission to do that!");
            return;
        }

        int i = 0;
        
        if (command.getName().equalsIgnoreCase("ab")) {
            if (args.length < 2) {
                MessageSender.sendMsg(sender, "&5Broadcasts a message to all online players.");
                MessageSender.sendMsg(sender, "&d/ab &dbroadcast &9[message]");
                return;
            }
        } else if (command.getName().equalsIgnoreCase("broadcast")) {
            if (args.length < 1) {
                MessageSender.sendMsg(sender, "&5Broadcasts a message to all online players.");
                MessageSender.sendMsg(sender, "&5/broadcast &9[message]");
                return;
            }
            i = 1;
        }

        StringBuilder sb = new StringBuilder();

        for (String msg : args) {
            if (i != 0) {
                sb.append(msg).append(" ");
            }
            i++;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            ActionUtils.sendTitle(player, sb.toString(), null);
        }
        
        MessageSender.sendMsg(sender, "&5Message broadcasted sucessfully.");
    }

    /**
     *
     * @param sender
     */
    public void reloadCommand(CommandSender sender) {
        if (!sender.hasPermission("actionbarbroadcast.reload")) {
            MessageSender.sendMsg(sender, "&cYou don't have permission to do that!");
            return;
        }
        bm.stop();
        plugin.reloadConfig();

        if (plugin.getConfig() == null) {
            plugin.saveResource("config.yml", true);
        }

        if (plugin.getConfig().getDouble("settings.config-version", -1.0D) != Constants.CONFIG_VERSION) {
            String old = plugin.getConfig().getString("settings.config-version", "OLD");
            MessageSender.sendMsg(sender, "&cIncompatible config detected! Renaming it to config-" + old + ".yml");
            MessageSender.sendMsg(sender, "&cA new config has been created, please transfer your settings.");
            MessageSender.sendMsg(sender, "&cWhen you have finished, type &6/ab reload&c to load your settings.");
            try {
                plugin.getConfig().save(new File(plugin.getDataFolder(), "config-" + old + ".yml"));
            } catch (IOException ex) {
                MessageSender.logStackTrace("Error while renaming config!", ex);
            }
            plugin.saveResource("config.yml", true);
        } else {
            MessageSender.sendMsg(sender, "&5Config successfully reloaded.");
        }

        bm.startBroadcasting();
    }
}
