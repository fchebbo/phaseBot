package phaseBot.messageHandlers;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import phaseBot.utils.PhaseBotUtils;
import java.util.Map;

public class CatHandler implements GuildMessageHandler {
    /**
     * Defines the description of the handler to be used in the help function
     *
     * @return the description
     */
    @Override
    public String getDesc() {
        return ": Sends a random cat fact to the channel";
    }

    /**
     * Defines the trigger to invoke this handler, e.g. "joke"
     *
     * @return
     */
    @Override
    public String getTrigger() {
        return "cat";
    }

    @Override
    public void handleMessage(GuildMessageReceivedEvent event, String s) {
        Map<?,?> responseMap = PhaseBotUtils.getJsonRestResponse(event, "https://catfact.ninja/fact");
        event.getChannel().sendMessage((String)responseMap.get("fact")).queue();
    }
}
