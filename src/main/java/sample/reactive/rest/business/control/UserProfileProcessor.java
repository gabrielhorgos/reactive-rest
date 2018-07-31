package sample.reactive.rest.business.control;

import sample.reactive.rest.business.entity.User;

import javax.inject.Singleton;
import java.util.Random;

@ExecutionInfo
@Singleton
public class UserProfileProcessor {

    private Random random = new Random();

    public void initializeUserProfile(User user) {
        //TODO call REST endpoint to do stuff (BCI - component interface)
        System.out.println("Starting to build profile for user : " + user.toString());
        try {
            Thread.sleep(random.nextInt(2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Successfully created profile for user : " + user.toString());
    }
}
