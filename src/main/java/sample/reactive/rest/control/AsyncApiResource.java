package sample.reactive.rest.control;

import sample.reactive.aop.ExecutionInfo;
import sample.reactive.rest.control.dto.UserData;
import sample.reactive.rest.control.service.RegistrationService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

@ExecutionInfo
@Path("/")
public class AsyncApiResource {

    @Inject
    private RegistrationService registrationService;

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void registerUser(UserData user, @Suspended AsyncResponse asyncResponse) {
        registrationService.processUserRegistration(user, asyncResponse);
    }

    @GET
    @Path("hello")
    public String sayHello() {
        return "Hello, I'm alive!";
    }

    @GET
    @Path("json")
    @Produces(MediaType.APPLICATION_JSON)
    public UserData jsonUser() {

        return new UserData("1", "2", "3", "4");
    }
}
