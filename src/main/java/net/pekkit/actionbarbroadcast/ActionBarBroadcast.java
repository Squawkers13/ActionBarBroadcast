package net.pekkit.actionbarbroadcast;

import java.io.File;
import java.io.IOException;
import net.pekkit.actionbarbroadcast.commands.BaseCommandExecutor;
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
