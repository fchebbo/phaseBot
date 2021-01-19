package phaseBot;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class phaseBotEventListener implements EventListener {
    /**
     * Handles any {@link GenericEvent GenericEvent}.
     *
     * <p>To get specific events with Methods like {@code onMessageReceived(MessageReceivedEvent event)}
     * take a look at: {@link ListenerAdapter ListenerAdapter}
     *
     * @param event The Event to handle.
     */
    @Override
    public void onEvent(@NotNull GenericEvent event) {
        System.out.println("DEALING WITH EVENT: " + event);
        if (event instanceof GuildJoinEvent)
        {
            ((GuildJoinEvent) event).getGuild().getDefaultChannel().sendMessage("YOU EXPECTED A BOT, BUT IT WAS ME DIO! Type \"!pb\" for a list of options").complete();
        }
    }
}
