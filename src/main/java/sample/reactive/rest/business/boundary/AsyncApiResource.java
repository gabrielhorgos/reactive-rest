package sample.reactive.rest.business.boundary;

import sample.reactive.rest.business.control.ExecutionInfo;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;

@ExecutionInfo
@Path("/")
public class AsyncApiResource {

    @Inject
    private RegistrationHandler registrationHandler;
    @Inject
    private CommonExecService commonExecService;

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void registerUser(RegistrationForm registrationForm, @Suspended AsyncResponse asyncResponse) {
        CompletableFuture.supplyAsync(() -> registrationHandler.handleRegistration(registrationForm),
                commonExecService.getExecService())
                .thenCompose(cf -> cf.thenAccept(r -> asyncResponse.resume(Response.ok(r).build())));
    }

}
