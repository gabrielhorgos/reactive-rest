package sample.reactive.rest.control.service;

import sample.reactive.aop.ExecutionInfo;
import sample.reactive.rest.control.dto.UserData;
import sample.reactive.rest.control.model.RegisterApplication;
import sample.reactive.rest.control.model.User;
import sample.reactive.rest.control.persistence.ApplicationStorage;
import sample.reactive.rest.exception.DuplicateUsernameException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Random;

@ExecutionInfo
@Singleton
public class UserService {

    @Inject
    private ApplicationStorage applicationStorage;

    private Random random = new Random();

    public User createUser(RegisterApplication lApplication) throws DuplicateUsernameException {
        UserData uData = lApplication.getUserData();
        User user = new User(uData.getUsername(), uData.getPassword(), uData.getFirstName(), uData.getLastName());

        return applicationStorage.storeUser(user);
    }

    public void initializeUserProfile(User user) {
        System.out.println("Starting to build profile for user : " + user.toString());
        try {
            Thread.sleep(random.nextInt(2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Successfully created profile for user : " + user.toString());
    }
}
