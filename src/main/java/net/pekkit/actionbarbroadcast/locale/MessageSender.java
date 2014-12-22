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
package net.pekkit.actionbarbroadcast.locale;

import org.bukkit.Bukkit;
import static org.bukkit.ChatColor.translateAlternateColorCodes;
import org.bukkit.command.CommandSender;
import org.bukkit.util.ChatPaginator;

/**
 *
 * @author Squawkers13
 */
public class MessageSender {
    
    private static final String prefix = "&f[&5ActionBarBroadcast&f] ";

    /**
     * Plugin method to easily send messages to players (and console too). This
     * is the core of all the sender methods included here.
     *
     * @param sender The CommandSender to send the message to.
     * @param msg The message to be sent.
     * @param pagination Whether to paginate the message or not.
     *
     * @since 1.5
     */
    public static void sendMsg(CommandSender sender, String msg, boolean pagination) {
        String message = translateAlternateColorCodes('&', msg);
        if (message.length() > ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH && pagination) {
            String[] multiline = ChatPaginator.wordWrap(message, ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH);
            sender.sendMessage(multiline);
        } else {
            sender.sendMessage(message);
        }
    }

    /**
     * Plugin method to easily send messages to players (and console too). This
     * version assumes you want to paginate the message.
     *
     * @param sender The CommandSender to send the message to.
     * @param msg The message to be sent.
     *
     * @since 1.5
     */
    public static void sendMsg(CommandSender sender, String msg) {
        sendMsg(sender, msg, true);
    }

    /**
     * Plugin method to easily send messages prefixed with the plugin name.
     * Prefixes the message with the plugin name and calls sendMsg.
     *
     * @param sender The CommandSender to send the message to.
     * @param msg The message to be sent.
     * @param pagination Whether to paginate the message or not.
     *
     * @see sendMsg
     *
     * @since 1.5
     */
    public static void sendPluginMsg(CommandSender sender, String msg, boolean pagination) {
        sendMsg(sender, prefix + msg, pagination);
    }

    /**
     * Plugin method to easily log messages to the console. Actually just calls
     * sendPluginMsg and specifies the sender as the server's
     * ConsoleCommandSender.
     *
     * @param msg The message to be sent to the console.
     *
     * @see sendPluginMsg
     *
     * @since 1.5
     */
    public static void log(String msg) {
        Bukkit.getServer().getConsoleSender().sendMessage(translateAlternateColorCodes('&', prefix + msg));
    }

    /**
     * Plugin method to easily log caught exceptions to the console. 
     * 
     *
     * @param msg Message to log
     * @param ex The Exception to log a stacktrace for.
     *
     * @see log
     *
     * @since 1.5
     */
    public static void logStackTrace(String msg, Exception ex) {        
        log("&4An error occured: &f" + msg);
        log(ex.getClass().getName());
        for (StackTraceElement ee : ex.getStackTrace()) {
            log("   at " + ee.getClassName() + ":" + ee.getMethodName() + " (line " + ee.getLineNumber() + ")");
        }
    }

}
