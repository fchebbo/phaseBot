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
        JDA jda = JDABuilder.createDefault(p.getProperty("secretToken"))
                .addEventListeners(new phaseBotEventListener())
                .addEventListeners(new phaseBotListenerAdapter()).build();
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
