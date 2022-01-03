package me.nesox.brush;


import me.nesox.brush.commands.BrushesTabComplete;
import me.nesox.brush.commands.Brushes;
import me.nesox.brush.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class Brush extends JavaPlugin {
    private static Brush instance;

    @Override
    public void onEnable() {
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
