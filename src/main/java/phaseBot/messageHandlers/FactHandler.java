package phaseBot.messageHandlers;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import phaseBot.utils.PhaseBotUtils;

import java.util.Map;

public class FactHandler implements GuildMessageHandler{
    /**
     * Defines the description of the handler to be used in the help function
     *
     * @return the description
     */
    @Override
    public String getDesc() {
        return ": Sends a fun random fact to the channel";
    }

    /**
     * Defines the trigger to invoke this handler, e.g. "joke"
     *
     * @return
     */
    @Override
    public String getTrigger() {
        return "fact";
    }

    @Override
    public void handleMessage(GuildMessageReceivedEvent event, String s) {
        Map<?,?> responseMap = PhaseBotUtils.getJsonRestResponse(event, "https://uselessfacts.jsph.pl/random.json?language=en");
        event.getChannel().sendMessage((String)responseMap.get("text")).queue();
    }
}
