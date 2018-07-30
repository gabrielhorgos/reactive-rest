package sample.reactive.rest.service;

import sample.reactive.aop.ExecutionInfo;
import sample.reactive.rest.dto.UserData;
import sample.reactive.rest.model.User;
import sample.reactive.rest.model.UserRegistration;
import sample.reactive.rest.exception.DuplicateUsernameException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Singleton
@ExecutionInfo
public class RegistrationService {

    @Inject
    private UserRegistrationService userRegistrationService;
    @Inject
    private UserService userService;
    @Inject
    private NotificationService notificationService;

    private final ExecutorService regExecutors = Executors.newFixedThreadPool(10);

    public void processUserRegistration(UserData userData, AsyncResponse asyncResponse) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return userRegistrationService.storeApplication(userData);
            } catch (DuplicateUsernameException e) {
                e.printStackTrace();
                asyncResponse.resume(Response.ok("Your registration could not be saved. Reason :\n " +
                        e.getMessage()).build());

                return null;
            }
        }, regExecutors).thenAccept(application -> {
           try {
               processRegistration(application);
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

    private void processRegistration(UserRegistration application) throws Exception {
        if (application == null) {
            throw new Exception("Can not process null application");
        }

        CompletableFuture.supplyAsync(() -> userRegistrationService.isValid(application), regExecutors)
                .thenApply(isValid -> {
                    if (!isValid)
                        return null;

                    try {
                        return userService.createUser(application);

                    } catch (DuplicateUsernameException e) {
                        e.printStackTrace();
                        return null;
                    }
        }).thenApply(user -> {
            try {
                finishRegistration(user);
            } catch (Exception e) {
                //TODO log
                e.printStackTrace();
            }
            return null;
        });


    }

    private void finishRegistration(User user) throws Exception {
        if (user == null) {
            //TODO throw exception
            throw new Exception("User is null");
        }

        CompletableFuture.runAsync(() -> notificationService.notifySuccesfullRegistration(user), regExecutors);
        CompletableFuture.runAsync(() -> userRegistrationService.completeUserRegistration(user.getUsername()),
                regExecutors);
        CompletableFuture.runAsync(() -> userService.initializeUserProfile(user), regExecutors);
    }
}
