package com.howtographql.hackernews.resolvers.mutations;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import com.howtographql.hackernews.AuthContext;
import com.howtographql.hackernews.model.*;
import com.howtographql.hackernews.repositories.LinkRepository;
import com.howtographql.hackernews.repositories.UserRepository;
import com.howtographql.hackernews.repositories.VoteRepository;
import graphql.GraphQLException;
import graphql.schema.DataFetchingEnvironment;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLRootContext;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Mutation implements GraphQLRootResolver {

    private final LinkRepository linkRepository;

    private final UserRepository userRepository;

    private final VoteRepository voteRepository;

    public Mutation(LinkRepository linkRepository,
                    UserRepository userRepository,
                    VoteRepository voteRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }


    @GraphQLMutation
    public Link createLink(String url, String description, @GraphQLRootContext AuthContext context) {
        Link newLink = new Link(null, url, description, context.getUser().getId());

        return linkRepository.saveLink(newLink);
    }


    @GraphQLMutation
    public User createUser(String name, @GraphQLArgument(name = "authProvider") AuthData auth) {
        User newUser = new User(name, auth.getEmail(), auth.getPassword());
        return userRepository.saveUser(newUser);
    }


    @GraphQLMutation
    public SigninPayload signinUser(AuthData auth) {
        User user = userRepository.findByEmail(auth.getEmail());
        if(user.getPassword().equals(auth.getPassword())) {
            return new SigninPayload(user.getId(), user);
        }
        throw new GraphQLException("Invalid credentials");
    }


    @GraphQLMutation
    public Vote createVote(String linkId, String userId) {
        ZonedDateTime now = Instant.now().atZone(ZoneOffset.UTC);
        Vote vote = new Vote(now, linkId, userId);
        return voteRepository.saveVote(vote);
    }

}
