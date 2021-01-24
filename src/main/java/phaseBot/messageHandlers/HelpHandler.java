package phaseBot.messageHandlers;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class HelpHandler implements GuildMessageHandler {
    // todo: MAYBE enable this to make it so people can use it for thier own bots, idfk
    private static String OPT_STRING = "Phase Bot comes with the following commands:" +
            "```" +
            "!pb help: literally whispers this menu \n"  +
            "!pb cat: sends a random cat fact to the channel \n" +
            "!pb dog: sends a random dog fact to the channel \n" +
            "!pb fact: send a fun random fact to the channel \n" +
            "!pb joke: sends a random (probably bad) joke to the channel \n" +
            "!pb meme: sends a random meme to the channel \n" +
            "!pb norris: sends a random 'Chuck Norris' joke to the channel (YES HE'S BACK!!) \n" +
            "```";
    @Override
    public void handleMessage(GuildMessageReceivedEvent event, String s) {
        // this weird thing is what enables us to whisper a user...//TODO (PERHAPS) make this a helper method
        event.getAuthor().openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage(OPT_STRING).queue();
        });
    }
}
