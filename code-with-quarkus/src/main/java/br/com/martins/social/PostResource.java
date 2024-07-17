package br.com.martins.social;

import br.com.martins.social.dto.CreatePostRequest;
import br.com.martins.social.dto.PostResponse;
import br.com.martins.social.model.Post;
import br.com.martins.social.model.User;
import br.com.martins.social.repository.PostRepository;
import br.com.martins.social.repository.UserRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/users/{userId}/posts")
public class PostResource {

    private final UserRepository userRepository;
    private final PostRepository repository;

    @Inject
    public PostResource(
            UserRepository userRepository,
            PostRepository repository) {
        this.userRepository = userRepository;
        this.repository = repository;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response savePost(
            @PathParam("userId") Long userId, CreatePostRequest request){

        User user = userRepository.findById(userId);
        if(user == null){
            return  Response.status(Response.Status.NOT_FOUND).build();
        }

        Post post = new Post();
        post.setText(request.getText());
        post.setUser(user);

        repository.persist(post);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response listPosts( @PathParam("userId") Long userId){
        User user = userRepository.findById(userId);
        if(user == null){
            return  Response.status(Response.Status.NOT_FOUND).build();
        }

        var query = repository.find(
                "user", Sort.by("dateTime", Sort.Direction.Descending) ,user);
        var list = query.list();

        var postResponseList = list.stream()
                .map(PostResponse::fromEntity)
                .collect(Collectors.toList());

        return Response.ok(postResponseList).build();
    }
}
