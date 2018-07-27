package sample.reactive.rest.control.service;

import sample.reactive.aop.ExecutionInfo;
import sample.reactive.rest.control.dto.UserData;
import sample.reactive.rest.control.model.RegisterApplication;
import sample.reactive.rest.control.persistence.ApplicationStorage;
import sample.reactive.rest.exception.DuplicateUsernameException;

import javax.inject.Inject;
import javax.inject.Singleton;

@ExecutionInfo
@Singleton
public class RegisterApplicationService {

    @Inject
    private ApplicationStorage storage;

    public RegisterApplication storeApplication(UserData user) throws DuplicateUsernameException {
        return storage.saveApplication(new RegisterApplication(user));
    }

    public boolean isValid(RegisterApplication lApplication) {
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
