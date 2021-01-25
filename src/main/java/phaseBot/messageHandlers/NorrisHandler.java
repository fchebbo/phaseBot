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

    /**
     * Defines the description of the handler to be used in the help function
     *
     * @return the description
     */
    @Override
    public String getDesc() {
        return ": Sends a random 'Chuck Norris' joke to the channel (YES HE'S BACK!!)";
    }

    /**
     * Defines the trigger to invoke this handler, e.g. "joke"
     *
     * @return
     */
    @Override
    public String getTrigger() {
        return "norris";
    }
}
