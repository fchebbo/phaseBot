package phaseBot.messageHandlers;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface GuildMessageHandler {
    /**
     * Defines the description of the handler to be used in the 'help' function
     * @return the description
     */
    public String getDesc();

    /**
     * Defines the trigger to invoke this handler, e.g. "joke"
     * @return
     */
    public String getTrigger();

    /**
     * Defines what happens when this handler is invoked
     * @param event The event that invoked this handler
     * @param s Anything that the user sent that wasn't the command itself (not always used)
     */
    void handleMessage(GuildMessageReceivedEvent event, String s);
}
