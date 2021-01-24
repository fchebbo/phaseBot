package phaseBot.messageHandlers;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import phaseBot.utils.PhaseBotUtils;

import java.util.ArrayList;
import java.util.Map;

public class DogHandler implements GuildMessageHandler{
    @Override
    public void handleMessage(GuildMessageReceivedEvent event, String s) {
        Map<?, ?> responseMap = PhaseBotUtils.getJsonRestResponse(event, "https://dog-api.kinduff.com/api/facts");
        event.getChannel().sendMessage(((ArrayList<String>) responseMap.get("facts")).get(0)).queue();
    }
}
