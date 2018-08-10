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

@ExecutionInfo
@Path("/")
public class AsyncApiResource {

    @Inject
    private RegistrationHandler registrationHandler;

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void registerUser(RegistrationForm registrationForm, @Suspended AsyncResponse asyncResponse) {
        registrationHandler
                .handleRegistration(registrationForm)
                .thenAccept(r ->  asyncResponse.resume(Response.ok(r).build()));    }

}
