package br.com.martins.social;

import br.com.martins.social.dto.CreateUserRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UserResourcer {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(CreateUserRequest userRequest) {
        return Response.ok(userRequest).build();
    }

    @GET
    public Response listALlUsers() {
        return Response.ok().build();
    }
}

