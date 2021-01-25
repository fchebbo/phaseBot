package phaseBot.messageHandlers;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import phaseBot.utils.PhaseBotUtils;

import java.util.Map;

public class QuoteHandler implements GuildMessageHandler{
    /**
     * Defines the description of the handler to be used in the 'help' function
     *
     * @return the description
     */
    @Override
    public String getDesc() {
        return ": Returns a (hopefully) inspirational quote.";
    }

    /**
     * Defines the trigger to invoke this handler, e.g. "joke"
     *
     * @return
     */
    @Override
    public String getTrigger() {
        return "quote";
    }

    /**
     * Defines what happens when this handler is invoked
     *
     * @param event The event that invoked this handler
     * @param s     Anything that the user sent that wasn't the command itself (not always used)
     */
    @Override
    public void handleMessage(GuildMessageReceivedEvent event, String s) {
        Map<?,?> responseMap = PhaseBotUtils.getJsonRestResponse(event, "https://api.quotable.io/random");
        String quote = "`" + (String)responseMap.get("content") +"`\n" +
                "*---"+(String)responseMap.get("author")+"*";
        event.getChannel().sendMessage(quote).queue();
    }
}
