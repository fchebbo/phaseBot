package phaseBot.messageHandlers;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import phaseBot.utils.PhaseBotUtils;

import java.util.concurrent.ThreadLocalRandom;

public class EightBallHandler implements GuildMessageHandler{

    /**
     * Tells you if the response is good, bad or neutral
     */
    private static enum Type {
        CONTRARY,
        NEUTRAL,
        POSITIVE
    }

    /**
     * The Response Array
     */
    private static BallResponse[] ballResponses = {
            new BallResponse(Type.CONTRARY, "Don’t count on it"),
            new BallResponse(Type.CONTRARY, "Hell No!!!"),
            new BallResponse(Type.CONTRARY, "As likely as finding water in the Sahara"),
            new BallResponse(Type.CONTRARY, "Negative 420.69% chance :( "),
            new BallResponse(Type.CONTRARY, "Outlook not so good"),
            new BallResponse(Type.CONTRARY, "Snowballs have a better chance in hell"),
            new BallResponse(Type.CONTRARY, "Very doubtful"),
            new BallResponse(Type.NEUTRAL, "Please hang up and try again"),
            new BallResponse(Type.NEUTRAL, "Reply hazy, try again"),
            new BallResponse(Type.POSITIVE, "420% positive!!!"),
            new BallResponse(Type.POSITIVE, "BRO FOR SURE!"),
            new BallResponse(Type.POSITIVE, "Chances are as high as I am"),
            new BallResponse(Type.POSITIVE, "Hell yes!"),
            new BallResponse(Type.POSITIVE, "Outlook good"),
            new BallResponse(Type.POSITIVE, "Signs point to yes"),
            new BallResponse(Type.POSITIVE, "You may rely on it"),
    };

    private static String[] ballActions = {
            "*Phasebot shakes the eight ball vigorously*",
            "*Phasebot asks Odin for guidance*",
            "*Phasebot does a bunch of shrooms and responds*",
            "*Phasebot does a dark ritual and asks the spirit of George Carlin*",
            "*Phasebot casts farsight*",
            "*Phasebot asks the ghost of Nostradamus*",
            "*Phasebot asks the spirit of Nostra-dumb-ass*",
            "*Phasebot shakes the eight ball vigorously*",

    };



    /**
     * Defines the description of the handler to be used in the help function
     *
     * @return the description
     */
    @Override
    public String getDesc() {
        return " **<question>**: Invoke the power of the magic eight ball to get a 100% guaranteed correct answer!";
    }

    /**
     * Defines the trigger to invoke this handler, e.g. "joke"
     *
     * @return
     */
    @Override
    public String getTrigger() {
        return "8ball";
    }

    @Override
    public void handleMessage(GuildMessageReceivedEvent event, String question) {
        question = question.trim();
        if (question.isEmpty())
        {
            PhaseBotUtils.sendDmToAuthor(event,"Invalid use of 8ball command, must be in the form of !8ball <question>.  Example:\n"
                    + "!pb 8ball will I ever give you up?");
            return;
        }

        BallResponse response = ballResponses[ThreadLocalRandom.current().nextInt(0, ballResponses.length)];
        question = "**" + question.substring(0, Math.min(question.length(), 200)).replace("*","") + "**";
        String answerString =
                event.getAuthor().getAsMention() + " asks: " + question + "\n" + //This is the question line
                        ballActions[ThreadLocalRandom.current().nextInt(0, ballActions.length)] +  // This is the Ball action
                        padAnswer(response.type, response.answer);
        event.getChannel().sendMessage(answerString).queue();
    }

    /**
     * This makes the text change colors!!
     * TODO: MOVE THIS TO UTIL CLASS IF IT EVER HAS ADDITIONAL USE!!
     * @param type
     * @param Answer
     * @return
     */
    private static String padAnswer(Type type, String Answer)
    {
        if (type == Type.CONTRARY)
        {
            return "```diff\n" +
                    "- " + Answer + "\n" +
                    "```";
        }
        else if (type == Type.NEUTRAL)
        {
            return "```fix\n" +
                    Answer + "\n" +
                    "```";
        }
        else
        {
            return "```diff\n" +
                    "+ " + Answer + "\n" +
                    "```";
        }
    }

    private static class BallResponse
    {
        public Type type;
        public String answer;

        public BallResponse(Type type,String answer)
        {
            this.type=type;
            this.answer=answer;
        }
    }
}
