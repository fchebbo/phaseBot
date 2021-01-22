package phaseBot.messageHandlers;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface messageHandler {
    void handleMessage(GuildMessageReceivedEvent ev, String s);
}
