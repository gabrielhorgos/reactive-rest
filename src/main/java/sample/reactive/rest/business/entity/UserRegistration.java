package sample.reactive.rest.business.entity;

import sample.reactive.rest.business.boundary.RegistrationForm;

import java.time.Instant;

public class UserRegistration {

    private long id;
    private RegistrationForm registrationForm;
    private Instant timestamp = Instant.now();

    public UserRegistration(RegistrationForm user) {
        this.registrationForm = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RegistrationForm getRegistrationForm() {
        return registrationForm;
    }

    public void setRegistrationForm(RegistrationForm registrationForm) {
        this.registrationForm = registrationForm;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
