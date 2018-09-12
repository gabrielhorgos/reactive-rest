package sample.reactive.rest.business.boundary;

import sample.reactive.rest.business.control.ExecutionInfo;
import sample.reactive.rest.business.boundary.exception.DuplicateUsernameException;
import sample.reactive.rest.business.control.RegistrationValidation;
import sample.reactive.rest.business.control.UserNotification;
import sample.reactive.rest.business.control.UserProfileProcessor;
import sample.reactive.rest.business.entity.User;
import sample.reactive.rest.business.entity.UserRegistration;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
@ExecutionInfo
public class RegistrationHandler {

    @Inject
    private UserEntry userEntry;
    @Inject
    private UserRegistrationEntry userRegistrationEntry;
    @Inject
    private UserNotification userNotification;
    @Inject
    private RegistrationValidation registrationValidation;
    @Inject
    private UserProfileProcessor userProfileProcessor;

    private final ExecutorService regExecutors = Executors.newFixedThreadPool(5);

    public void handleRegistration(RegistrationForm registrationForm, AsyncResponse asyncResponse) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return userRegistrationEntry.save(registrationForm);
            } catch (DuplicateUsernameException e) {
                e.printStackTrace();
                asyncResponse.resume(Response.ok("Your registration could not be saved. Reason :\n " +
                        e.getMessage()).build());

                return null;
            }
        }, regExecutors).thenAccept(userRegistration -> {
            try {
                processUserRegistration(userRegistration);
                asyncResponse.resume(Response.ok("Your registration request has been saved and will be further" +
                        " processed!").build());
            } catch (Exception e) {
                asyncResponse.resume(Response.ok("Your registration request can't be processed. Reason :\n " +
                        e.getMessage()).build());
            }

        }).exceptionally(ex -> {
            asyncResponse.resume(Response.ok("Your registration could not be saved. Reason :\n " +
                    ex.getMessage()).build());
            return null;
        });
    }

    private void processUserRegistration(UserRegistration userRegistration) throws Exception {
        if (userRegistration == null) {
            throw new Exception("Can not process null user registration.");
        }

        CompletableFuture.supplyAsync(() -> registrationValidation.validate(userRegistration), regExecutors)
                .thenApply(isValid -> {
                    try {
                        return createUser(userRegistration, isValid);
                    } catch (DuplicateUsernameException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).thenAccept(user -> {
            try {
                completeRegistration(user);
            } catch (Exception e) {
                //TODO log
                e.printStackTrace();
            }
        });


    }

    private User createUser(UserRegistration userRegistration, Boolean isValid) throws DuplicateUsernameException {
        if (!isValid)
            return null;

        return userEntry.createUser(userRegistration);
    }

    private void completeRegistration(User user) throws Exception {
        if (user == null) {
            //TODO throw exception
            throw new Exception("User is null");
        }

        CompletableFuture.runAsync(() -> userNotification.notifySuccesfullRegistration(user), regExecutors);
        CompletableFuture.runAsync(() -> userRegistrationEntry.removeUserRegistration(user.getUsername()),
                regExecutors);
        CompletableFuture.runAsync(() -> userProfileProcessor.initializeUserProfile(user), regExecutors);
    }

}
