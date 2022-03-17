package xyz.aquaticraft.aquadiscorddata;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.aquaticraft.aquadiscorddata.AquaCore.Listeners.MessageSent;

import javax.security.auth.login.LoginException;

public final class AquaDiscordData extends JavaPlugin {

    JDA jda;
    @Override
    public void onEnable() {
        Bukkit.getLogger().info("AquaLookup is starting...");
        try {
            JDABuilder.createDefault("OTUzNjUzNjA4MDMxODYyODM3.YjHtIg.UksoV19LowcqBXNg2dERloZ2QDk")
                    .setActivity(Activity.playing("play.aquaticraft.xyz"))
                    .addEventListeners(new MessageSent())
                    .build();
            Bukkit.getLogger().info("AquaLookup started!");
        } catch (LoginException e) {
            Bukkit.getLogger().warning("Incorrect token! Shutting down.");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        jda.shutdownNow();
    }
}
