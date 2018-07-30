package sample.reactive.rest.business.boundary;

import sample.reactive.aop.ExecutionInfo;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

@ExecutionInfo
@Path("/")
public class AsyncApiResource {

    @Inject
    private RegistrationEntry registrationEntry;

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void registerUser(RegistrationForm registrationForm, @Suspended AsyncResponse asyncResponse) {
        registrationEntry.handleRegistration(registrationForm, asyncResponse);
    }

}
