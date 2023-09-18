package org.frostyservices;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.frostyservices.Configurations.Configurations;
import org.frostyservices.SQL.SQL;
import java.util.logging.Logger;

public class Main extends ListenerAdapter {
    private static Main instance;
    public SQL sql;
    public JDA jda;
    public static Configurations configurations;
    public static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        Configurations.init();

        instance = new Main();

        String token = Configurations.getConfigValue("Token");

        JDA jda = JDABuilder.createLight(token)
                .addEventListeners(new Main())
                .enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .setActivity(Activity.playing("Development"))
                .build();

        CommandListUpdateAction commands = jda.updateCommands();

        // Create Command
        commands.addCommands(
                Commands.slash("create", "Creates a project!")
                        .addOptions(new OptionData(STRING, "name", "The name of the project.")
                                .setRequired(true))
                        .addOptions(new OptionData(CHANNEL, "channel", "The channel where the project will be logged.")
                                .setChannelTypes(ChannelType.TEXT)
                                .setRequired(true))
                        .setGuildOnly(true)
        );

        // Leaderboard Command
        commands.addCommands(
                Commands.slash("leaderboard", "Shows you the highest earned members!")
        );

        // Clockout Command
        commands.addCommands(
                Commands.slash("clockout", "Clocks out a specific member!")
                        .addOptions(new OptionData(USER, "user", "The user to clockout.")
                                .setRequired(true))
                        .setGuildOnly(true)
        );

        // Stats Command
        commands.addCommands(
                Commands.slash("stats", "Shows a user's total time elapsed & earnings!")
                        .addOptions(new OptionData(USER, "user", "Who's stats should I display?")
                                .setRequired(true))
                        .setGuildOnly(true)
        );

        commands.queue();

        // SQL Setup
        instance.sql = new SQL(instance);
        instance.sql.Connect();
    }


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getGuild() == null)
            return;
        switch (event.getName()) {
            case "create":
                leaderboard(event);
                break;
            case "leaderboard":
                leaderboard(event);
                break;
            default:
                event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }
    }

    public void leaderboard(SlashCommandInteractionEvent event) {
        String leaderboard = instance.sql.getLeaderboard();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Earnings Leaderboard");
        embedBuilder.setDescription(leaderboard);
        embedBuilder.setColor(0x43A6C6);
        MessageEmbed embed = embedBuilder.build();
        event.replyEmbeds(embed).setEphemeral(true).queue();
    }
}