package phaseBot.messageHandlers;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import phaseBot.utils.PhaseBotUtils;

import java.util.Map;

/**
 * Not that anyone can actually handle chuck norris
 */
public class NorrisHandler implements GuildMessageHandler{
    @Override
    public void handleMessage(GuildMessageReceivedEvent event, String s) {
        Map<?,?> responseMap = PhaseBotUtils.getJsonRestResponse(event, "http://api.icndb.com/jokes/random");

        event.getChannel().sendMessage((String)((Map) responseMap.get("value")).get("joke")).queue();
    }
}
