package me.nesox.brush;


import me.nesox.brush.utils.UpdateChecker;
import org.bstats.bukkit.Metrics;
import me.nesox.brush.commands.BrushesTabComplete;
import me.nesox.brush.commands.Brushes;
import me.nesox.brush.listeners.*;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Brush extends JavaPlugin {
    private static Brush instance;

    @Override
    public void onEnable() {

        new Metrics(this, 13843);

        new UpdateChecker(this, 98895).getVersion(version -> {
            if (!this.getDescription().getVersion().equals(version)) {
                getLogger().info(ChatColor.RED + "New update available! https://www.spigotmc.org/resources/brush.98895/");
            }
        });

        instance = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();
        getCommand("brushes").setExecutor(new Brushes());
        getCommand("brushes").setTabCompleter(new BrushesTabComplete());
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItem(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawn(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Brush getInstance() {
        return instance;
    }
}
