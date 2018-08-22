package sample.reactive.application;


import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import sample.reactive.rest.business.boundary.AsyncApiResource;

import javax.ws.rs.core.Application;
import java.util.Set;

@OpenAPIDefinition(info =
@Info(
        title = "Reactive Rest sample application",
        version = "0.0",
        description = "Reactive-REST",
        license = @License(name = "Reactive-REST 1.0", url = "http://foo.bar"),
        contact = @Contact(url = "http://gigantic-server.com", name = "Fred", email = "Fred@gigagantic-server.com")
)
)
@javax.ws.rs.ApplicationPath("/rest")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> resources = new java.util.HashSet<>();

        addRestResourceClasses(resources);

        return resources;

    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(AsyncApiResource.class);

        //register JAX-RS openapi resources
        resources.add(OpenApiResource.class);
        resources.add(AcceptHeaderOpenApiResource.class);
    }

}
