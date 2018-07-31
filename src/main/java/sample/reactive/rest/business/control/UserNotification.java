package sample.reactive.rest.business.control;

import sample.reactive.rest.business.entity.User;

import javax.inject.Singleton;
import java.util.Random;

@ExecutionInfo
@Singleton
public class UserNotification {

    private Random random = new Random();

    public User notifySuccesfullRegistration(User user) {
        //TODO external call tu notifying service
        System.out.println("Sending email notification to user " + user.toString());


        try {
            Thread.sleep(random.nextInt(3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Email notification has been sent to " + user.toString());
        return user;
    }
}
