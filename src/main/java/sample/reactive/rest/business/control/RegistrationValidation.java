package sample.reactive.rest.business.control;

import sample.reactive.rest.business.boundary.RegistrationForm;
import sample.reactive.rest.business.entity.UserRegistration;

@ExecutionInfo
public class RegistrationValidation {

    public RegistrationForm validate(RegistrationForm registrationForm) {
        if (registrationForm == null) {
            throw new RuntimeException("Null registration form");
        }

        String username = registrationForm.getUsername();
        String password = registrationForm.getPassword();

        if (username == null || username.isEmpty() || username.length() < 3) {
            throw new RuntimeException("Invalid username format.");
        }

        if (password == null || password.isEmpty() || password.length() < 6) {
            throw new RuntimeException("Invalid password format");
        }

        return registrationForm;
    }
}
