package phaseBot.messageHandlers;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import phaseBot.utils.PhaseBotUtils;

import java.util.ArrayList;
import java.util.Map;

public class DogHandler implements GuildMessageHandler{
    /**
     * Defines the description of the handler to be used in the help function
     *
     * @return the description
     */
    @Override
    public String getDesc() {
        return ": Sends a random dog fact to the channel";
    }

    /**
     * Defines the trigger to invoke this handler, e.g. "joke"
     *
     * @return
     */
    @Override
    public String getTrigger() {
        return "dog";
    }

    @Override
    public void handleMessage(GuildMessageReceivedEvent event, String s) {
        Map<?, ?> responseMap = PhaseBotUtils.getJsonRestResponse(event, "https://dog-api.kinduff.com/api/facts");
        event.getChannel().sendMessage(((ArrayList<String>) responseMap.get("facts")).get(0)).queue();
    }

}
