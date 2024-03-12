package br.com.martins;

import br.com.martins.quarkussocial.controller.dto.CreateUserRequest;
import br.com.martins.quarkussocial.model.User;
import br.com.martins.quarkussocial.model.dao.UserDao;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UserResource {

    private final UserDao dao;

    @Inject
    public UserResource(UserDao dao) {
        this.dao = dao;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createUser(CreateUserRequest userRequest) {

        User user = new User();
        user.setName(userRequest.getName());
        user.setAge(userRequest.getAge());

        dao.persist(user);

        return Response.ok(user).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAllUsers() {

        PanacheQuery<User>query = dao.findAll();

        return Response.ok(query.list()).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser ( @PathParam("id") Long id) {

        User user = dao.findById(id);

        if(user != null){
            dao.delete(user);
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser ( @PathParam("id") Long id, CreateUserRequest userData) {

        User user = dao.findById(id);

        if(user != null){
            user.setName(userData.getName());
            user.setAge(userData.getAge());

            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();

    }
}
