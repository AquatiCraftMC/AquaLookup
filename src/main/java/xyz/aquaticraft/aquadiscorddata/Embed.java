package xyz.aquaticraft.aquadiscorddata;

import me.activated.core.api.player.GlobalPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Embed {
    public Embed(String title, Color color, String text, String author, String authorimage, TextChannel channel, @Nullable List<GlobalPlayer> playerList, @Nullable String image) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(color);
        embed.setTitle(title);
        embed.setAuthor(author, null, authorimage);
        embed.setDescription(text);
        if (playerList != null) {
            if (!playerList.isEmpty()) {
                List<String> playerNames = new ArrayList<>();
                for (GlobalPlayer o : playerList) {
                    playerNames.add(o.getName());
                }
                embed.addField("**Online Players**", String.valueOf(playerNames), false);
            }
            else {
                embed.addField("**Online Players**", "None", false);
            }
        }
        if (image != null) {
            embed.setThumbnail(image);
        }

        channel.sendMessageEmbeds(embed.build()).queue();
    }
}
