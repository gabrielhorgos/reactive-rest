package sample.reactive.rest.business.boundary;

import sample.reactive.aop.ExecutionInfo;
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
        return applicationStorage.saveApplication(new UserRegistration(user));
    }


    public void removeUserRegistration(String username) {
        applicationStorage.removeApplicationsForUsername(username);
    }

}
