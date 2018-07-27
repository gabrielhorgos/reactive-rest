package sample.reactive.rest.control.service;

import sample.reactive.aop.ExecutionInfo;
import sample.reactive.rest.control.dto.UserData;
import sample.reactive.rest.control.model.RegisterApplication;
import sample.reactive.rest.control.model.User;
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
    private RegisterApplicationService registerApplicationService;
    @Inject
    private UserService userService;
    @Inject
    private NotificationService notificationService;

    private final ExecutorService regExecutors = Executors.newFixedThreadPool(10);

    public void processUserRegistration(UserData userData, AsyncResponse asyncResponse) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return registerApplicationService.storeApplication(userData);
            } catch (DuplicateUsernameException e) {
                e.printStackTrace();
                asyncResponse.resume(Response.ok("Your registration could not be saved. Reason :\n " + e.getMessage()).build());

                return null;
            }
        }, regExecutors).thenAccept(application -> {
            if (application != null)
                asyncResponse.resume(Response.ok("Your registration has been saved and will be further processed!").build());
                processRegistration(application);
        }).exceptionally(ex -> {
            asyncResponse.resume(Response.ok("Your registration could not be saved. Reason :\n " + ex.getMessage()).build());
            return null;
        });
    }

    private void processRegistration(RegisterApplication application) {
        CompletableFuture.supplyAsync(() -> registerApplicationService.isValid(application), regExecutors)
                .thenApply(isValid -> {
                    if (!isValid)
                        return null;

                    try {
                        return userService.createUser(application);

                    } catch (DuplicateUsernameException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).thenAccept(user -> {
            finishRegistration(user);
        });
    }

    private void finishRegistration(User user) {
        if (user == null)
            return;

        CompletableFuture.runAsync(() -> notificationService.notifySuccesfullRegistration(user), regExecutors);
        CompletableFuture.runAsync(() -> registerApplicationService.completeUserRegistration(user.getUsername()), regExecutors);
        CompletableFuture.runAsync(() -> userService.initializeUserProfile(user), regExecutors);
    }
}
