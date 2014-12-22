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

import java.io.File;
import java.io.IOException;
import net.pekkit.actionbarbroadcast.commands.BaseCommandExecutor;
import net.pekkit.actionbarbroadcast.listeners.PlayerListener;
import net.pekkit.actionbarbroadcast.locale.MessageSender;
import net.pekkit.actionbarbroadcast.util.Constants;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

/** ActionBarBroadcast
 * 
 * @author Squawkers13
 */
public class ActionBarBroadcast extends JavaPlugin {

    private BroadcastManager bbh;
    
    private BaseCommandExecutor bce;

    /**
     * Called when Bukkit enables the plugin.
     */
    @Override
    public void onEnable() {

        saveDefaultConfig();
        // --- Config check ---
        if (getConfig().getDouble("settings.config-version", -1.0D) != Constants.CONFIG_VERSION) {
            
            String old = getConfig().getString("settings.config-version", "OLD");
            
            MessageSender.log("&cIncompatible config detected! Renaming it to config-" + old + ".yml");
            MessageSender.log("&cA new config has been created, please transfer your settings.");
            MessageSender.log("&cWhen you have finished, type &6/econ reload&c to load your settings.");
            try {
                getConfig().save(new File(getDataFolder(), "config-" + old + ".yml"));
            } catch (IOException ex) {
                MessageSender.logStackTrace("Error while renaming config!", ex);
            }
            saveResource("config.yml", true);
        }

        // --- MCStats submission ---
        if (getConfig().getBoolean("settings.general.stats")) {
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
            } catch (IOException e) {
                // Failed to submit the stats :-(
            }
        }
        
        bbh = new BroadcastManager(this);
        
        bce = new BaseCommandExecutor(this, bbh);
        
        getCommand("ab").setExecutor(bce);
        getCommand("broadcast").setExecutor(bce);
        
        // --- Tab list headers ---
        if (getConfig().getBoolean("settings.util.headers", true)) {
            MessageSender.log("Enabling tab list headers!");
            getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        }
        
        bbh.startBroadcasting();
    }

    /**
     *
     */
    @Override
    public void onDisable() {
        //Nothing here yet!
    }
}
