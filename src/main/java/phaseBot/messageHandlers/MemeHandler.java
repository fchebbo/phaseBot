package phaseBot.messageHandlers;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import phaseBot.utils.PhaseBotUtils;

import java.util.Map;

public class MemeHandler implements GuildMessageHandler{
    @Override
    public void handleMessage(GuildMessageReceivedEvent event, String s) {
        // I might have to Axe this if too many images are 404...
        int tries = 0;
        boolean found = false;
        String url;
        Map<?, ?> responseMap = null;
        while (tries<5 && !found) {
            responseMap = PhaseBotUtils.getJsonRestResponse(event, "https://meme-api.herokuapp.com/gimme");
            url = (String)responseMap.get("url");
            found = PhaseBotUtils.isUrlOk(url);
        }
        if (found) {
            event.getChannel().sendMessage((String) responseMap.get("url")).queue();
        }
        else
        {
            event.getChannel().sendMessage("Your call could not be completed as dialed, please try again").complete();
        }
    }
}
