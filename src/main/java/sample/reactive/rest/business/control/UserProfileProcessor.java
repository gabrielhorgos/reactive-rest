package sample.reactive.rest.business.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.reactive.rest.business.entity.User;

import javax.inject.Singleton;
import java.util.Random;

@ExecutionInfo
@Singleton
public class UserProfileProcessor {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileProcessor.class);
    private Random random = new Random();

    public void initializeUserProfile(User user) {
        if (user == null) {
            logger.warn("Can't initialize profile for user null");
            return;
        }

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
