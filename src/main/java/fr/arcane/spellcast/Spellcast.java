package fr.arcane.spellcast;

import fr.arcane.spellcast.commands.GiveScrollCommand;
import fr.arcane.spellcast.events.SpellCastEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Spellcast extends JavaPlugin {

    public static Spellcast INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

        Bukkit.getLogger().info("SpellCast started");

        getServer().getPluginManager().registerEvents(new SpellCastEvent(), this);

        getCommand("givescroll").setExecutor(new GiveScrollCommand());

    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("SpellCast stopped");
    }

    public static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
