package phaseBot.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import phaseBot.messageHandlers.GuildMessageHandler;

import java.util.*;

public class PhaseBotUtils {
    private static RestTemplate restTemplate = new RestTemplate();

    private static HttpHeaders headers = new HttpHeaders();
    private static HttpEntity<String> entity;
    static {
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        // though this header is only used for jokebot fuck it - lets keep it here
        headers.add("User-Agent","phaseBot (firahs.chebbo@utoronto.ca)");
        entity = new HttpEntity<>(headers);
    }
    // prevents instantiation, TF you wanna instaniate a util class for?
    private PhaseBotUtils(){};

    /**
     * Returns a map containing the JSON response of a request
     * @param event - Just so the bot can yell at phase if shit goes wrong
     * @param url
     * @return
     */
    public static Map<?, ?> getJsonRestResponse(GuildMessageReceivedEvent event, String url)
    {
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
    public static boolean isUrlOk(String url)
    {
        //TODO: maybe make a static restTemplate so we dont need to keep making new objects..idfk
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> k = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        return k.getStatusCode()==HttpStatus.OK;
    }

    public static void sendDmToAuthor(GuildMessageReceivedEvent event, String msg)
    {
        // this weird thing is what enables us to whisper a user...//TODO (PERHAPS) make this a helper method
        event.getAuthor().openPrivateChannel().queue((channel) ->
        {
            channel.sendMessage(msg).queue();
        });
    }

    /**
     * Generates a helpString based on the Descriptions of all the handlers in the map
     */
    public static String generateHelpStr(String botTrigger, HashMap<String, GuildMessageHandler> handlerMap) {
        String helpStr = "```\n";
        ArrayList<GuildMessageHandler> newList = new ArrayList<>(handlerMap.values());
        //bro we sorting like a champ
        Collections.sort(newList, Comparator.comparing(GuildMessageHandler::getTrigger));
        for (Iterator<GuildMessageHandler> it = newList.iterator(); it.hasNext(); ) {
            GuildMessageHandler handler = it.next();
            helpStr += botTrigger+ " " + handler.getTrigger() + handler.getDesc() +"\n";
        }
        helpStr += "```";
        return helpStr;
    }
}
