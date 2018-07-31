package sample.reactive.rest.business.boundary;

import sample.reactive.rest.business.control.ExecutionInfo;
import sample.reactive.rest.business.boundary.exception.DuplicateUsernameException;
import sample.reactive.rest.business.entity.User;
import sample.reactive.rest.business.entity.UserRegistration;

import javax.inject.Inject;
import javax.inject.Singleton;

@ExecutionInfo
@Singleton
public class UserEntry {

    @Inject
    private ApplicationStorage applicationStorage;

    public User createUser(UserRegistration lApplication) throws DuplicateUsernameException {
        RegistrationForm uData = lApplication.getRegistrationForm();
        User user = new User(uData.getUsername(), uData.getPassword(), uData.getFirstName(), uData.getLastName());

        return applicationStorage.storeUser(user);
    }

}
