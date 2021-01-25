package phaseBot;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import phaseBot.messageHandlers.*;
import phaseBot.utils.PhaseBotUtils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class phaseBotListenerAdapter extends ListenerAdapter {

    /**
     * What the bot looks for in a message to test if it needs to do anything
     */
    String botTrigger = "!pb";

    HashMap<String, BiConsumer<GuildMessageReceivedEvent,String>>funcMap = new HashMap<>();

    HashMap <String, GuildMessageHandler> handlerMap= new HashMap<>();

    public phaseBotListenerAdapter(String botTrigger) {
        this.botTrigger = botTrigger;
        addHandlerToMap(new EightBallHandler());
        addHandlerToMap(new CatHandler());
        addHandlerToMap(new DogHandler());
        addHandlerToMap(new FactHandler());
        addHandlerToMap(new JokeHandler());
        addHandlerToMap(new MemeHandler());
        addHandlerToMap(new NorrisHandler());
        addHandlerToMap(new RollHandler());
        addHandlerToMap(new QuoteHandler());
        HelpHandler helpHandler = new HelpHandler();
        addHandlerToMap(helpHandler);

        helpHandler.setHelpStr(PhaseBotUtils.generateHelpStr(botTrigger, handlerMap));

    }

    private void addHandlerToMap(GuildMessageHandler handler){
        handlerMap.put(handler.getTrigger(),handler);
    }

    /**
     * Rofl, the abstract class does not fucking have java documentation.  Though, I guess to be fair, this is a
     * simple method
     * @param event The message receive event to which we can do something with
     */
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        String message = event.getMessage().getContentRaw().trim();

        String [] msgTokens = {};

        // Only tokenize if these conditions are met..otherwise just gtfo
        if (!event.getAuthor().isBot() && (message.toLowerCase().startsWith(botTrigger)))
        {
            msgTokens = message.split(" ",3);
        }
        else {
            return;
        }

        // using toLoserCase, to save headaches
        if (msgTokens.length>0 && msgTokens[0].equals(botTrigger)) {

            // using toLowercase because i don't want confusion of "catfact" vs "catFact"
            String cmd = (msgTokens.length>1) ? msgTokens[1].toLowerCase() : "help";
            String args = (msgTokens.length>2) ? msgTokens[2] : "";

            if (msgTokens.length ==3 ) {
                System.out.println(msgTokens[2]);
            }

            GuildMessageHandler handler = handlerMap.get(cmd);
            if (handler != null)
            {
                handler.handleMessage(event, args);
            }
        }
    }
}
