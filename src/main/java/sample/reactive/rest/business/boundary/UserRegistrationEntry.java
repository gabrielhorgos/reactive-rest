package sample.reactive.rest.business.boundary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.reactive.rest.business.control.ExecutionInfo;
import sample.reactive.rest.business.entity.User;
import sample.reactive.rest.business.entity.UserRegistration;

import javax.inject.Inject;
import javax.inject.Singleton;

@ExecutionInfo
@Singleton
public class UserRegistrationEntry {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationEntry.class);


    @Inject
    private ApplicationStorage applicationStorage;

    public UserRegistration save(RegistrationForm user) {
        return applicationStorage.saveUserRegistration(new UserRegistration(user));
    }


    public void removeUserRegistration(User user) {
        if (user == null) {
            logger.warn("Can't notify null user");
            return;
        }

        applicationStorage.removeApplicationsForUsername(user.getUsername());
    }

}
