package phaseBot.messageHandlers;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import phaseBot.utils.PhaseBotUtils;
import java.util.Map;

public class CatHandler implements GuildMessageHandler {
    @Override
    public void handleMessage(GuildMessageReceivedEvent event, String s) {
        Map<?,?> responseMap = PhaseBotUtils.getJsonRestResponse(event, "https://catfact.ninja/fact");
        event.getChannel().sendMessage((String)responseMap.get("fact")).queue();
    }
}
