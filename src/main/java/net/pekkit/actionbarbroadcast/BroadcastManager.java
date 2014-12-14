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
