package sample.reactive.rest.control.model;

import sample.reactive.rest.control.dto.UserData;

import java.time.Instant;

public class RegisterApplication {

    private long id;
    private UserData userData;
    private Instant timestamp = Instant.now();

    public RegisterApplication(UserData user) {
        this.userData = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
