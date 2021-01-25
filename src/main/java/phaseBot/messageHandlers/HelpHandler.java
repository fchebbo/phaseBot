package phaseBot.messageHandlers;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import phaseBot.utils.PhaseBotUtils;

public class HelpHandler implements GuildMessageHandler {
    String helpStr;

    @Override
    public void handleMessage(GuildMessageReceivedEvent event, String s) {
        PhaseBotUtils.sendDmToAuthor(event, helpStr);
    }

    /**
     * Defines the description of the handler to be used in the help function
     *
     * @return the description
     */
    @Override
    public String getDesc() {
        return ": Literally whispers this menu";
    }

    /**
     * Defines the trigger to invoke this handler, e.g. "joke"
     *
     * @return
     */
    @Override
    public String getTrigger() {
        return "help";
    }

    public void setHelpStr(String helpStr) {
        this.helpStr = helpStr;
    }
}
