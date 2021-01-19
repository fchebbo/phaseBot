package phaseBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    /**
     * The main method that loads brings up the bot
     * @param args none yet bro
     * @throws LoginException Don't
     * @throws InterruptedException be
     * @throws IOException dumb
     */
    public static void main (String [] args) throws LoginException, InterruptedException, IOException {
        Properties p = loadProps();

        String token;
        String mainCmd = "!pb";
        // Adding staging support for the bot (lets me test changes on a separate bot)
        if (args.length ==0 || !args[0].toLowerCase().equals("prod"))
        {
            // To enable staging support, add a "secretTokenStaging" line to the properties file
            token = p.getProperty("secretTokenStaging");
            if (token == null)
            {
                token = p.getProperty("secretToken");
            }
            mainCmd = "!pbs";
        } else {
            token = p.getProperty("secretToken");
        }

        JDA jda = JDABuilder.createDefault(token)
                .addEventListeners(new phaseBotEventListener())
                .addEventListeners(new phaseBotListenerAdapter(mainCmd)).build();

        jda.getPresence().setActivity(Activity.playing("God, type !pb for a list of options"));
    }

    /**
     * Helper method to load properties
     */
    private static Properties loadProps() throws IOException {
        Properties p = new Properties();
        InputStream input = Main.class.getClassLoader().getResourceAsStream("jda.properties") ;
        p.load(input);
        return p;
    }
}
