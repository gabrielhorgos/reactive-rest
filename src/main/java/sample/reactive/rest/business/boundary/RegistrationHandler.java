package sample.reactive.rest.business.boundary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.reactive.rest.business.control.ExecutionInfo;
import sample.reactive.rest.business.control.RegistrationValidation;
import sample.reactive.rest.business.control.UserNotification;
import sample.reactive.rest.business.control.UserProfileProcessor;
import sample.reactive.rest.business.entity.User;
import sample.reactive.rest.business.entity.UserRegistration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;

@Singleton
@ExecutionInfo
public class RegistrationHandler {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationHandler.class);

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
    @Inject
    private CommonExecService commonExecService;

    public CompletableFuture<String> handleRegistration(RegistrationForm registrationForm) {
        return CompletableFuture.supplyAsync(() -> registrationValidation.validate(registrationForm),
                commonExecService.getExecService())
                .thenApply(r -> userRegistrationEntry.save(r))
                .thenApply(r -> processUserRegistration(r))
                .handle(this::handleRegistrationFailure);
    }

    private String processUserRegistration(UserRegistration userRegistration) {
        CompletableFuture.supplyAsync(() -> userEntry.createUser(userRegistration), commonExecService.getExecService())
                .thenAccept(this::completeRegistration)
                .exceptionally(t -> {
                    userNotification.notifyUnSuccesfullRegistration(userRegistration.getRegistrationForm().getEmail());
                    return null;
                });

        return "Your registration request has been saved and will be further processed!";
    }

    private String handleRegistrationFailure(String r, Throwable ex) {
        return r != null ? r : "Your registration could not be saved. Reason :\n " +
                ex.getMessage();
    }

    private void completeRegistration(User user) {
        CompletableFuture.runAsync(() -> userNotification.notifySuccesfullRegistration(user),
                commonExecService.getExecService());
        CompletableFuture.runAsync(() -> userRegistrationEntry.removeUserRegistration(user),
                commonExecService.getExecService());
        CompletableFuture.runAsync(() -> userProfileProcessor.initializeUserProfile(user),
                commonExecService.getExecService());
    }


}
