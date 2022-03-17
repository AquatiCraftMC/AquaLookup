package xyz.aquaticraft.aquadiscorddata.AquaCore.Listeners;

import me.activated.core.antileak.LicenseCheck;
import me.activated.core.api.ServerData;
import me.activated.core.api.player.GlobalPlayer;
import me.activated.core.api.player.PlayerData;
import me.activated.core.api.player.PunishData;
import me.activated.core.api.punishment.Punishment;
import me.activated.core.api.punishment.PunishmentType;
import me.activated.core.api.rank.RankData;
import me.activated.core.plugin.AquaCore;
import me.activated.core.plugin.AquaCoreAPI;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PrivateChannel;
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
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MessageSent extends ListenerAdapter {
    AquaDiscordData main = AquaDiscordData.getPlugin(AquaDiscordData.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Member m =  event.getMember();
        String content = event.getMessage().getContentRaw();
        String[] args = content.split(" ");
        AquaCoreAPI api = AquaCoreAPI.INSTANCE;
        GlobalPlayer player = api.getGlobalPlayer(args[2]);
        if (content.startsWith("!aqualookup player")) {
            System.out.println("Got thru content#startsWith");
            if (!(args.length > 2)) {
                System.out.println("Couldn't get past argument check.");
            }
            if (!m.hasPermission(Permission.MANAGE_THREADS)) {
                return;
            }
            OfflinePlayer p = Bukkit.getOfflinePlayer(args[2]);
            String lastServer = "**Unknown**";
            String lastPlayed = "**Unknown**";
            String rank = "**Unknown**";
            if (p.hasPlayedBefore()) {
                long i = (System.currentTimeMillis() - p.getLastPlayed());
                SimpleDateFormat daysformatter = new SimpleDateFormat("dd", Locale.UK);
                SimpleDateFormat hoursformatter = new SimpleDateFormat("HH", Locale.UK);
                SimpleDateFormat minsformatter = new SimpleDateFormat("mm", Locale.UK);
                SimpleDateFormat secformatter = new SimpleDateFormat("ss", Locale.UK);

                Date date = new Date(i);
                String days = daysformatter.format(date);
                String hours = hoursformatter.format(date);
                String mins = minsformatter.format(date);
                String secs = minsformatter.format(date);
                lastPlayed = "**" + days + " day(s), " + hours + " hour(s), " + mins + " min(s), " + secs + " sec(s)**";

                // Rank stuff
                PlayerData playerData = AquaCore.INSTANCE.getPlayerManagement().getOfflineLoadedData(args[2]);
                AquaCore core = AquaCore.INSTANCE;
                RankData rankData = playerData.getHighestRank();
                rank = rankData.getName();
            }

            PlayerData playerData = AquaCore.INSTANCE.getPlayerManagement().getOfflineLoadedData(args[2]);

            String vanished = "**Offline**";
            String staffMode = "**Offline**";
            String banned = "**False**";
            String blacklisted = "**False**";
            if (p.isOnline()) {
                if (playerData.isVanished()) {
                    vanished = "**True**";
                } else {
                    vanished = "**False**";
                }
                if (playerData.isInStaffMode()) {
                    staffMode = "**True**";
                } else {
                    staffMode = "False";
                }
                if (playerData.getPunishData().isBanned()) {
                    banned = "**True**";
                    event.getMessage().reply("To see punish history, type !aqualookup punishment [name]").queue();
                } else {
                    banned = "False";
                }
                if (playerData.getPunishData().isBlacklisted()) {
                    blacklisted = "**True**";
                } else {
                    blacklisted = "False";
                }
            }

            new Embed(
                    p.getName() + "'s info",
                    Color.GREEN,
                    "Rank: " + rank + "\n\n" + "UUID: **" + p.getUniqueId() + "**\n\nVanished: " + vanished  + "\n\nStaff Mode: " + staffMode + "\n\nLast server: " + lastServer + "\n\nTime Since Last Login: " + lastPlayed,
                    "Aqua Lookup",
                    "https://yt3.ggpht.com/46uaz7FAfVDB4iEyHSwAWbG_NKT8sfQYyNwIa43otCBY8BKqi0mOjRvSY0Pn0m3d1hx_ZMxtzQ=s176-c-k-c0x00ffffff-no-rj",
                    event.getTextChannel(),
                    null,
                    "https://mc-heads.net/avatar/" + p.getUniqueId()
            );
            main.getLogger().info("Sent info for " + p.getName());
        } else if (content.startsWith("!aqualookup server")) {
            if (!(args.length > 2)) {
                System.out.println("Couldn't get past argument check.");
            }
            if (api.getServerData(args[2]) != null) {
                ServerData serverData = api.getServerData(args[2]);
                String whitelisted = "**False**";
                String maintenance = "**False**";
                if (serverData.isWhitelisted()) {
                    whitelisted = "**True**";
                }
                if (serverData.isMaintenance()) {
                    maintenance = "**True**";
                }


                new Embed(
                        "Server " + serverData.getServerName() + "'s info",
                        Color.GREEN,
                        "In Maintenance: " + maintenance + "\nWhitelisted: " + whitelisted,
                        "Aqua Lookup",
                        "https://yt3.ggpht.com/46uaz7FAfVDB4iEyHSwAWbG_NKT8sfQYyNwIa43otCBY8BKqi0mOjRvSY0Pn0m3d1hx_ZMxtzQ=s176-c-k-c0x00ffffff-no-rj",
                        event.getTextChannel(),
                        serverData.getPlayers(),
                        null
                );
            }
            else {
                new Embed("Error", Color.RED, "Couldn't find that server.", "Aqua Lookup", "https://yt3.ggpht.com/46uaz7FAfVDB4iEyHSwAWbG_NKT8sfQYyNwIa43otCBY8BKqi0mOjRvSY0Pn0m3d1hx_ZMxtzQ=s176-c-k-c0x00ffffff-no-rj", event.getTextChannel(), null, null);
            }
        }
        else if (content.startsWith("!aqualookup punishment")) {
            OfflinePlayer p = Bukkit.getOfflinePlayer(args[2]);            if (!(args.length > 2)) {
                System.out.println("Couldn't get past argument check.");
            }


            PlayerData playerData = AquaCore.INSTANCE.getPlayerManagement().getOfflineLoadedData(args[2]);
            PunishData punishData = playerData.getPunishData();
            event.getMessage().reply("TEst").queue();
            if (punishData.isBanned()) {
                event.getMessage().reply("player is banned").queue();
                Punishment punishment = punishData.getActiveBan();;
                event.getMessage().reply("gotten punishment").queue();

                new Embed(
                        "Player " + args[2] + "'s ban info",
                        Color.RED,
                        "Reason: " + punishment.getReason() + "\nAdded by: " + punishment.getAddedBy() + "\nExpires: " + punishment.getNiceExpire() + "Duration: " + punishment.getNiceDuration(),
                        "Aqua Lookup",
                        "https://yt3.ggpht.com/46uaz7FAfVDB4iEyHSwAWbG_NKT8sfQYyNwIa43otCBY8BKqi0mOjRvSY0Pn0m3d1hx_ZMxtzQ=s176-c-k-c0x00ffffff-no-rj",
                        event.getTextChannel(),
                        null,
                        "https://mc-heads.net/avatar/" + p.getUniqueId()
                );
            }
            if (punishData.isMuted()) {
                Punishment punishment = punishData.getActiveMute();
                new Embed(
                        "Player " + player.getName() + "'s mute info",
                        Color.RED,
                        "Reason: " + punishment.getReason() + "\nAdded by: " + punishment.getAddedBy() + "\nExpires: " + punishment.getNiceExpire() + "Duration: " + punishment.getNiceDuration(),
                        "Aqua Lookup",
                        "https://yt3.ggpht.com/46uaz7FAfVDB4iEyHSwAWbG_NKT8sfQYyNwIa43otCBY8BKqi0mOjRvSY0Pn0m3d1hx_ZMxtzQ=s176-c-k-c0x00ffffff-no-rj",
                        event.getTextChannel(),
                        null,
                        "https://mc-heads.net/avatar/" + player.getUniqueId()
                );
            }
            if (punishData.isBlacklisted()) {
                Punishment punishment = punishData.getActiveBlacklist();;
                new Embed(
                        "Player " + player.getName() + "'s **BLACKLIST** info",
                        Color.RED,
                        "Reason: " + punishment.getReason() + "\nAdded by: " + punishment.getAddedBy() + "\nExpires: " + punishment.getNiceExpire() + "Duration: " + punishment.getNiceDuration(),
                        "Aqua Lookup",
                        "https://yt3.ggpht.com/46uaz7FAfVDB4iEyHSwAWbG_NKT8sfQYyNwIa43otCBY8BKqi0mOjRvSY0Pn0m3d1hx_ZMxtzQ=s176-c-k-c0x00ffffff-no-rj",
                        event.getTextChannel(),
                        null,
                        "https://mc-heads.net/avatar/" + player.getUniqueId()
                );
            }
            if (punishData.isWarned()) {
                for (Punishment punishment : punishData.getPunishments(PunishmentType.WARN) ) {
                    new Embed(
                            "Player " + player.getName() + "'s warn info",
                            Color.RED,
                            "Reason: " + punishment.getReason() + "\nAdded by: " + punishment.getAddedBy() + "\nExpires: " + punishment.getNiceExpire() + "Duration: " + punishment.getNiceDuration(),
                            "Aqua Lookup",
                            "https://yt3.ggpht.com/46uaz7FAfVDB4iEyHSwAWbG_NKT8sfQYyNwIa43otCBY8BKqi0mOjRvSY0Pn0m3d1hx_ZMxtzQ=s176-c-k-c0x00ffffff-no-rj",
                            event.getTextChannel(),
                            null,
                            "https://mc-heads.net/avatar/" + player.getUniqueId()
                    );

                }
            }
        }
        else if (content.startsWith("!aqualookup") && !content.startsWith("!aqualookup player") && !content.startsWith("!aqualookup server") && !content.startsWith("!aqualookup punishment")) {
            new Embed("Error", Color.RED, "Incorrect Syntax! !aqualookup [player|server] [name]", "Aqua Lookup", "https://yt3.ggpht.com/46uaz7FAfVDB4iEyHSwAWbG_NKT8sfQYyNwIa43otCBY8BKqi0mOjRvSY0Pn0m3d1hx_ZMxtzQ=s176-c-k-c0x00ffffff-no-rj", event.getTextChannel(), null, null);
        }


    }


    private String ConvertSecondToHHMMString(int secondtTime)
    {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(tz);
        String time = df.format(new Date(secondtTime*1000L));

        return time;

    }

}
