package phaseBot.messageHandlers;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface GuildMessageHandler {
    void handleMessage(GuildMessageReceivedEvent event, String s);
}
