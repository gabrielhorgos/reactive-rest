package sample.reactive.rest.control.service;

import sample.reactive.aop.ExecutionInfo;
import sample.reactive.rest.control.dto.UserData;
import sample.reactive.rest.control.model.UserRegistration;
import sample.reactive.rest.control.persistence.ApplicationStorage;
import sample.reactive.rest.exception.DuplicateUsernameException;

import javax.inject.Inject;
import javax.inject.Singleton;

@ExecutionInfo
@Singleton
public class UserRegistrationService {

    @Inject
    private ApplicationStorage storage;

    public UserRegistration storeApplication(UserData user) throws DuplicateUsernameException {
        return storage.saveApplication(new UserRegistration(user));
    }

    public boolean isValid(UserRegistration lApplication) {
        String username = lApplication.getUserData().getUsername();
        String password = lApplication.getUserData().getPassword();

        if (username == null || username.isEmpty() || username.length() < 3) {
            return false;
        }

        if (password == null || password.isEmpty() || password.length() < 6) {
            return false;
        }

        return true;
    }


    public void completeUserRegistration(String username) {
        storage.removeApplicationsForUsername(username);
    }
}
