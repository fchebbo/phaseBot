package phaseBot;

import java.util.concurrent.ThreadLocalRandom;

public class ProofOfConcept {

    public static void main (String [] args)
    {
        for(int i =1; i<10; ++i)
        {
            System.out.println(ThreadLocalRandom.current().nextInt(1, 3));
        }
    }
}
