package phaseBot.messageHandlers;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.concurrent.ThreadLocalRandom;

public class RollHandler implements GuildMessageHandler{
    /**
     * Defines the description of the handler to be used in the help function
     *
     * @return the description
     */
    @Override
    public String getDesc() {
        return " **<Number (optional)>**: Rolls the dice of fates";
    }

    /**
     * Defines the trigger to invoke this handler, e.g. "joke"
     *
     * @return
     */
    @Override
    public String getTrigger() {
        return "roll";
    }

    @Override
    public void handleMessage(GuildMessageReceivedEvent event, String numberStr) {
        int highEnd;
        try {
            highEnd = Integer.parseInt(numberStr);
        }
        catch (NumberFormatException e) {
            highEnd = 100;
        }

        event.getChannel().sendMessage(event.getAuthor().getAsMention() + " rolls " +
                ThreadLocalRandom.current().nextInt(1, highEnd + 1)+ " (1-" + highEnd +")").queue();
    }
}
