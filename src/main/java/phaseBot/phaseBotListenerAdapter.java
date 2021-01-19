package phaseBot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Map;

public class phaseBotListenerAdapter extends ListenerAdapter {

    /**
     * What the bot looks for in a message to test if it needs to do anything
     */
    String botTrigger = "!pb";

    public phaseBotListenerAdapter(String botTrigger) {
        this.botTrigger = botTrigger;
    }

    public static String OPT_STRING = "Phase Bot comes with the following commands:" +
                                      "```" +
                                      "!pb help: literally whispers this menu \n"  +
                                      "!pb cat: sends a random cat fact to the channel \n" +
                                      "!pb dog: sends a random dog fact to the channel \n" +
                                      "!pb joke: sends a random (probably bad) joke to the channel \n" +
                                      "!pb meme: sends a random meme to the channel \n" +
                                      "```";

    /**
     * Rofl, the abstract class does not fucking have java documentation.  Though, I guess to be fair, this is a
     * simple method
     * @param event The message receive event to which we can do something with
     */
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        String message = event.getMessage().getContentRaw().trim();

        // using toLoserCase, to save headaches
        if (!event.getAuthor().isBot() && (message.toLowerCase().startsWith(botTrigger))) {
            String [] msgTokens = message.split(" ");
            // using toLowercase because i don't want confusion of "catfact" vs "catFact"
            String cmd = (msgTokens.length>1) ? msgTokens[1].toLowerCase() : "help";
            String target = (msgTokens.length>2) ? msgTokens[2] : "";

            switch (cmd) {
                case("help"):
                    sendHelp(event);
                    break;
                case("cat"):
                case("catfact"):
                    sendCatFact(event);
                    break;
                case("dog"):
                case("dogfact"):
                    sendDogFact(event);
                    break;
                case("joke"):
                    sendJoke(event);
                    break;
                case("meme"):
                    sendMeme(event);
            }
        }
    }

    private static void sendHelp(GuildMessageReceivedEvent event)
    {
        // this weird thing is what enables us to whisper a user...//TODO (PERHAPS) make this a helper method
        event.getAuthor().openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage(OPT_STRING).queue();
        });
    }

    /**
     * Sends a cat fact to the discord channel that invoked this command.
     */
    private void sendCatFact(GuildMessageReceivedEvent event){
        Map<?,?> responseMap = getJsonRestResponse(event, "https://catfact.ninja/fact");
        event.getChannel().sendMessage((String)responseMap.get("fact")).complete();
    }

    private void sendDogFact(GuildMessageReceivedEvent event) {
        Map<?, ?> responseMap = getJsonRestResponse(event, "https://dog-api.kinduff.com/api/facts");
        event.getChannel().sendMessage(((ArrayList<String>) responseMap.get("facts")).get(0)).complete();
    }

    private void sendMeme(GuildMessageReceivedEvent event) {
        // I might have to Axe this if too many images are 404...
        int tries = 0;
        boolean found = false;
        String url;
        Map<?, ?> responseMap = null;
        while (tries<5 && !found) {
            responseMap = getJsonRestResponse(event, "https://meme-api.herokuapp.com/gimme");
            url = (String)responseMap.get("url");
            found = isUrlOk(url);
        }
        if (found) {
            event.getChannel().sendMessage((String) responseMap.get("url")).complete();
        }
        else
        {
            event.getChannel().sendMessage("Your call could not be completed as dialed, please try again").complete();
        }
    }

    private void sendJoke(GuildMessageReceivedEvent event){
        Map<?,?> responseMap = getJsonRestResponse(event, "https://official-joke-api.appspot.com/random_joke");
        String Joke =(String)responseMap.get("setup") + "\n" + (String)responseMap.get("punchline") ;
        event.getChannel().sendMessage(Joke).complete();
    }

    /**
     * Helper function to get a map representing the json response of a REST request
     * @param event the event (used to bitch at me if this thing fails)
     * @param url the URL to make the rest request from
     * @return
     */
    private  Map<?, ?> getJsonRestResponse(GuildMessageReceivedEvent event, String url)
    {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> k = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

        ObjectMapper mapper = new ObjectMapper();
        Map<?, ?> fullMap = null;
        try {
            fullMap = mapper.readValue((String) k.getBody(), Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            event.getChannel().sendMessage("Something broke reading the cat fact, yell at phase");
        }
        return fullMap;
    }

    /**
     * Tests to see if the meme is successfully returned
     * @return True if the url returns 200...fucking random meme generator needs to get on its game
     */
    private boolean isUrlOk(String url)
    {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> k = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        return k.getStatusCode()==HttpStatus.OK;
    }
}
