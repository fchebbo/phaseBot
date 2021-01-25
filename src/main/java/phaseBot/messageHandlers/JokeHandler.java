package phaseBot.messageHandlers;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import phaseBot.utils.PhaseBotUtils;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class JokeHandler implements GuildMessageHandler{
    /**
     * Defines the description of the handler to be used in the help function
     *
     * @return the description
     */
    @Override
    public String getDesc() {
        return ": Sends a random (probably bad) joke to the channel";
    }

    /**
     * Defines the trigger to invoke this handler, e.g. "joke"
     *
     * @return
     */
    @Override
    public String getTrigger() {
        return "joke";
    }

    @Override
    public void handleMessage(GuildMessageReceivedEvent event, String s) {
        int choice = ThreadLocalRandom.current().nextInt(1, 3);
        Map<?, ?> responseMap;
        switch (choice) {
            //This random joke API has too few jokes, and repeat jokes are fucking annoying.
            case 1:
                responseMap = PhaseBotUtils.getJsonRestResponse(event, "https://official-joke-api.appspot.com/random_joke");
                // using || around the punchline to mark as spoiler
                String Joke = (String) responseMap.get("setup") + "\n" + "||" + (String) responseMap.get("punchline") + "||";
                event.getChannel().sendMessage(Joke).queue();
                return;
            case 2:
                responseMap = PhaseBotUtils.getJsonRestResponse(event, "https://icanhazdadjoke.com/");
                event.getChannel().sendMessage((String) responseMap.get("joke")).queue();
                return;
        }
    }
}
