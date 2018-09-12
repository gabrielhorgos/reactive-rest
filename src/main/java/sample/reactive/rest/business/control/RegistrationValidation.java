package sample.reactive.rest.business.control;

import sample.reactive.rest.business.entity.UserRegistration;

@ExecutionInfo
public class RegistrationValidation {

    public boolean validate(UserRegistration lApplication) {
        String username = lApplication.getRegistrationForm().getUsername();
        String password = lApplication.getRegistrationForm().getPassword();

        if (username == null || username.isEmpty() || username.length() < 3) {
            return false;
        }

        if (password == null || password.isEmpty() || password.length() < 6) {
            return false;
        }

        return true;
    }
}
