package xyz.aquaticraft.aquadiscorddata.Listeners;

import me.activated.core.api.player.GlobalPlayer;
import me.activated.core.api.player.PlayerData;
import me.activated.core.api.rank.RankData;
import me.activated.core.plugin.AquaCoreAPI;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.aquaticraft.aquadiscorddata.AquaDiscordData;
import xyz.aquaticraft.aquadiscorddata.Embed;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.EventListener;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MessageSent extends ListenerAdapter {
    AquaDiscordData main = AquaDiscordData.getPlugin(AquaDiscordData.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Member m =  event.getMember();
        String content = event.getMessage().getContentRaw();
        String[] args = content.split(" ");
        AquaCoreAPI api = AquaCoreAPI.INSTANCE;
        GlobalPlayer player = api.getGlobalPlayer(args[0]);
        if (content.startsWith("!aqualookup ")) {
            System.out.println("Got thru content#startsWith");
            if (!(args.length > 1)) {
                System.out.println("Couldn't get past argument check.");
            }
            OfflinePlayer p = Bukkit.getOfflinePlayer(args[1]);
            String lastServer = "**Unknown**";
            String lastPlayed = "**Unknown**";
            String rank = "**Unknown**";
            if (p.hasPlayedBefore()) {
                long i = (System.currentTimeMillis() - p.getLastPlayed());
                SimpleDateFormat daysformatter = new SimpleDateFormat("dd", Locale.UK);
                SimpleDateFormat hoursformatter = new SimpleDateFormat("HH", Locale.UK);
                SimpleDateFormat minutesformatter = new SimpleDateFormat("mm", Locale.UK);
                SimpleDateFormat secondsformatter = new SimpleDateFormat("ss", Locale.UK);
                Date date = new Date(i);
                String days = daysformatter.format(date);
                String hours = hoursformatter.format(date);
                String minutes = minutesformatter.format(date);
                String seconds = secondsformatter.format(date);
                lastPlayed = "**" + days + " Day(s), " + hours + " Hour(s), " + minutes + " Minute(s), " + seconds + " Second(s)**";
                RankData rankData = api.getPlayerRank(p.getUniqueId());
                rank = rankData.getDisplayName();
            }

            PlayerData playerData = AquaCoreAPI.INSTANCE.getPlayerData(p.getUniqueId());
            String vanished = "**Offline**";
            String staffMode = "**Offline**";
            if (p.isOnline()) {
                if (playerData.isVanished()) {
                    vanished = "True";
                } else {
                    vanished = "False";
                }
                if (playerData.isInStaffMode()) {
                    staffMode = "True";
                } else {
                    staffMode = "False";
                }
            }

            new Embed(
                    p.getName() + "'s info",
                    Color.RED,
                    "Rank: " + rank + "\n\n" + "UUID: **" + p.getUniqueId() + "**\n\nVanished: " + vanished  + "\n\nStaff Mode: " + staffMode + "\n\nLast server: " + lastServer + "\n\nTime Since Last Login: " + lastPlayed,
                    "Aqua Lookup",
                    "https://yt3.ggpht.com/46uaz7FAfVDB4iEyHSwAWbG_NKT8sfQYyNwIa43otCBY8BKqi0mOjRvSY0Pn0m3d1hx_ZMxtzQ=s176-c-k-c0x00ffffff-no-rj",
                    event.getTextChannel()
            );
            main.getLogger().info("Sent info for " + p.getName());

        } else {
            System.out.println("Couldn't get past content#StartsWith");
        }

    }

}
