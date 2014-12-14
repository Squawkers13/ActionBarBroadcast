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
package net.pekkit.actionbarbroadcast;

import java.util.ArrayList;
import net.pekkit.actionbarbroadcast.util.ActionUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Squawkers13
 */
public class BroadcastManager {

    private ActionBarBroadcast plugin;
    private TaskBroadcast task;

    public BroadcastManager(ActionBarBroadcast par1) {
        plugin = par1;
    }

    public void startBroadcasting() {
        int delay = plugin.getConfig().getInt("settings.broadcast.duration", 60) * 20;

        task = new TaskBroadcast(delay);
        task.runTaskTimer(plugin, 0L, delay);
    }
    
    public void stop() {
        task.cancel();
    }

    private void setBar(String message, int d) {
        String msg = ChatColor.translateAlternateColorCodes('&', message);

        for (Player p : Bukkit.getOnlinePlayers()) {
            String m = msg.replaceAll("%player%", p.getName());
            ActionUtils.sendActionBar(p, m);
        }
    }

    private class TaskBroadcast extends BukkitRunnable {

        private int delay;

        private ArrayList<String> messages;

        public TaskBroadcast(int d) {
            delay = d / 20;

            messages = new ArrayList<String>(plugin.getConfig().getStringList("settings.broadcast.messages"));
        }

        @Override
        public void run() {
            String message = messages.get(0); //Fetch the first message

            setBar(message, delay);

            messages.remove(0); //Remove the message from the list

            messages.add(message); //Append it back at the end
        }

    }
}
