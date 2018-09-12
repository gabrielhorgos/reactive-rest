package sample.reactive.rest.business.boundary;

import sample.reactive.rest.business.control.ExecutionInfo;
import sample.reactive.rest.business.boundary.exception.DuplicateUsernameException;
import sample.reactive.rest.business.entity.UserRegistration;

import javax.inject.Inject;
import javax.inject.Singleton;

@ExecutionInfo
@Singleton
public class UserRegistrationEntry {

    @Inject
    private ApplicationStorage applicationStorage;

    public UserRegistration save(RegistrationForm user) throws DuplicateUsernameException {
        return applicationStorage.saveUserRegistration(new UserRegistration(user));
    }


    public void removeUserRegistration(String username) {
        applicationStorage.removeApplicationsForUsername(username);
    }

}
