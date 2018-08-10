package sample.reactive.rest.business.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.reactive.rest.business.boundary.RegistrationHandler;
import sample.reactive.rest.business.entity.User;

import javax.inject.Singleton;
import java.util.Random;

@ExecutionInfo
@Singleton
public class UserNotification {

    private static final Logger logger = LoggerFactory.getLogger(UserNotification.class);
    private Random random = new Random();

    public void notifySuccesfullRegistration(User user) {
        if (user == null) {
            logger.warn("Can't notify null user");
            return;
        }

        //TODO external call tu notifying service
        System.out.println("Sending email notification to user " + user.toString());


        try {
            Thread.sleep(random.nextInt(3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Email notification has been sent to " + user.toString());
    }

    public void notifyUnSuccesfullRegistration(String emailAddress) {
        if (emailAddress == null) {
            logger.warn("Invalid null email address");
            return;
        }

        System.out.println("Sending unsuccesfull registration notification email to user " + emailAddress);

        try {
            Thread.sleep(random.nextInt(3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Email notification has been sent to " + emailAddress);
    }
}
