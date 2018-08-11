package sample.reactive.rest.business.boundary;

import sample.reactive.rest.business.control.ExecutionInfo;
import sample.reactive.rest.business.entity.User;
import sample.reactive.rest.business.entity.UserRegistration;

import javax.inject.Inject;
import javax.inject.Singleton;

@ExecutionInfo
@Singleton
public class UserEntry {

    @Inject
    private ApplicationStorage applicationStorage;

    public User createUser(UserRegistration lApplication) {
        RegistrationForm uData = lApplication.getRegistrationForm();
        User user = new User(uData.getUsername(), uData.getPassword(), uData.getFirstName(), uData.getLastName(),
                uData.getEmail());

        return applicationStorage.storeUser(user);
    }

}
