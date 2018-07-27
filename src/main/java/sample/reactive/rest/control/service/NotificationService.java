package sample.reactive.rest.control.service;

import sample.reactive.aop.ExecutionInfo;
import sample.reactive.rest.control.model.User;

import javax.inject.Singleton;
import java.util.Random;

@ExecutionInfo
@Singleton
public class NotificationService {

    private Random random = new Random();

    public User notifySuccesfullRegistration(User user) {
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
