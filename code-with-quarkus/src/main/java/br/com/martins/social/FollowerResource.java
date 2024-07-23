package br.com.martins.social;

import br.com.martins.social.dto.FollowerRequest;
import br.com.martins.social.dto.FollowerResponse;
import br.com.martins.social.dto.FollowersPerUserResponse;
import br.com.martins.social.model.Follower;
import br.com.martins.social.repository.FollowerRepository;
import br.com.martins.social.repository.UserRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/users/{userId}/followers")
public class FollowerResource {

    private final FollowerRepository repository;
    private final UserRepository userRepository;

    @Inject
    public FollowerResource(FollowerRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response followUser(
            @PathParam("userId") Long userId, FollowerRequest request){

        if(userId.equals(request.getFollowerId())){
            return Response.status(Response.Status.CONFLICT)
                    .entity("You can't follow yourself")
                    .build();
        }

        var user = userRepository.findById(userId);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var follower = userRepository.findById(request.getFollowerId());

        boolean follows = repository.follows(follower, user);

        if(!follows){
            var entity = new Follower();
            entity.setUser(user);
            entity.setFollower(follower);

            repository.persist(entity);
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response listFollowers(@PathParam("userId") Long userId){

        var user = userRepository.findById(userId);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var list = repository.findByUser(userId);
        FollowersPerUserResponse responseObjetc = new FollowersPerUserResponse();
        responseObjetc.setFollowersCount(list.size());

        var followerList = list.stream()
                .map(FollowerResponse::new)
                .collect(Collectors.toList());

        responseObjetc.setContent(followerList);
        return Response.ok(responseObjetc).build();

    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response unfollowUser(
            @PathParam("userId") Long userId, @QueryParam("followerId") Long followerId){
        var user = userRepository.findById(userId);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        repository.deleteByFollowerAndUser(followerId, userId);

        return Response.status(Response.Status.NO_CONTENT).build();

    }

}
