package phaseBot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import phaseBot.messageHandlers.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
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
        GuildMessageHandler catHandler = new CatHandler();
        GuildMessageHandler dogHandler = new DogHandler();
        GuildMessageHandler factHandler = new FactHandler();
        GuildMessageHandler helpHandler = new HelpHandler();
        GuildMessageHandler jokeHandler = new JokeHandler();
        GuildMessageHandler memeHandler = new MemeHandler();
        GuildMessageHandler norrisHandler = new NorrisHandler();
        handlerMap.put("cat",catHandler);
        handlerMap.put("catfact",catHandler);
        handlerMap.put("dog",dogHandler);
        handlerMap.put("dogFact",dogHandler);
        handlerMap.put("fact",factHandler);
        handlerMap.put("help",helpHandler);
        handlerMap.put("joke",jokeHandler);
        handlerMap.put("meme",memeHandler);
        handlerMap.put("norris",norrisHandler);
    }

    public static String OPT_STRING = "Phase Bot comes with the following commands:" +
                                      "```" +
                                      "!pb help: literally whispers this menu \n"  +
                                      "!pb cat: sends a random cat fact to the channel \n" +
                                      "!pb dog: sends a random dog fact to the channel \n" +
                                      "!pb fact: send a fun random fact to the channel \n" +
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

    private void sendHelp(GuildMessageReceivedEvent event)
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
        event.getChannel().sendMessage((String)responseMap.get("fact")).queue();
    }

    private void sendDogFact(GuildMessageReceivedEvent event) {
        Map<?, ?> responseMap = getJsonRestResponse(event, "https://dog-api.kinduff.com/api/facts");
        event.getChannel().sendMessage(((ArrayList<String>) responseMap.get("facts")).get(0)).queue();
    }

    private void sendFunFact(GuildMessageReceivedEvent event) {
        Map<?,?> responseMap = getJsonRestResponse(event, "https://uselessfacts.jsph.pl/random.json?language=en");
        event.getChannel().sendMessage((String)responseMap.get("text")).queue();
    }

    private void sendJoke(GuildMessageReceivedEvent event){
        int choice = ThreadLocalRandom.current().nextInt(1, 6);
        Map<?, ?> responseMap;
        switch (choice) {
            //This random joke API has too few jokes, and repeat jokes are fucking annoying.
            case 0:
                responseMap = getJsonRestResponse(event, "https://official-joke-api.appspot.com/random_joke");
                // using || around the punchline to mark as spoiler
                String Joke = (String) responseMap.get("setup") + "\n" + "||" + (String) responseMap.get("punchline") + "||";
                event.getChannel().sendMessage(Joke).queue();
                return;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                responseMap = getJsonRestResponse(event, "https://icanhazdadjoke.com/");
                // using || around the punchline to mark as spoiler
                event.getChannel().sendMessage((String)responseMap.get("joke")).queue();
                return;
        }
//        Map<?,?> responseMap = getJsonRestResponse(event, "https://official-joke-api.appspot.com/random_joke");
//        // using || around the punchline to mark as spoiler
//        String Joke =(String)responseMap.get("setup") + "\n" + "||" + (String)responseMap.get("punchline") + "||" ;
//        event.getChannel().sendMessage(Joke).queue();
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
            event.getChannel().sendMessage((String) responseMap.get("url")).queue();
        }
        else
        {
            event.getChannel().sendMessage("Your call could not be completed as dialed, please try again").complete();
        }
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

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("User-Agent","phaseBot (firahs.chebbo@utoronto.ca)");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> k = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
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
