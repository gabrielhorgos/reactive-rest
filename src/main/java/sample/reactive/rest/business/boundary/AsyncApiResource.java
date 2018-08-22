package sample.reactive.rest.business.boundary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Register user",
            description = "Save and attempt user registration", responses = {
            @ApiResponse(description = "Success message",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)))})
    public void registerUser( @RequestBody(description = "User registration form", required = true,
            content = @Content(
                    schema = @Schema(implementation = RegistrationReponse.class))) RegistrationForm registrationForm, @Suspended AsyncResponse asyncResponse) {
        CompletableFuture.supplyAsync(() -> registrationHandler.handleRegistration(registrationForm),
                commonExecService.getExecService())
                .thenCompose(cf -> cf.thenAccept(r -> asyncResponse.resume(Response.ok(new RegistrationReponse("message", r)).build())));
    }

}
