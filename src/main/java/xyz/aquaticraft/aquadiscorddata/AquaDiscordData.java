package xyz.aquaticraft.aquadiscorddata;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.aquaticraft.aquadiscorddata.Listeners.MessageSent;

import javax.security.auth.login.LoginException;
import java.util.Collections;
import java.util.EventListener;

public final class AquaDiscordData extends JavaPlugin {

    JDA jda;
    @Override
    public void onEnable() {
        Bukkit.getLogger().info("AquaLookup is starting...");
        try {
            JDABuilder.createDefault("no")
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
