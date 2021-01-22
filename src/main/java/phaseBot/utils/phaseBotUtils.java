package phaseBot.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

public class phaseBotUtils {
    private static RestTemplate restTemplate = new RestTemplate();

    private static HttpHeaders headers = new HttpHeaders();
    static {
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        // though this header is only used for jokebot fuck it - lets keep it here
        headers.add("User-Agent","IDONTWANNATELLYOU (...)");
    }
    // prevents instantiation, TF you wanna instaniate a util class for?
    private phaseBotUtils(){};

    /**
     * Returns a map containing the JSON response of a request
     * @param event - Just so the bot can yell at phase if shit goes wrong
     * @param url
     * @return
     */
    public static Map<?, ?> getJsonRestResponse(GuildMessageReceivedEvent event, String url)
    {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        // though this header is only used for jokebot fuck it - lets keep it here
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
}
