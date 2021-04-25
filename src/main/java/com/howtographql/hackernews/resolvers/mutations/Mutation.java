package com.howtographql.hackernews.resolvers.mutations;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import com.howtographql.hackernews.model.AuthData;
import com.howtographql.hackernews.model.Link;
import com.howtographql.hackernews.model.User;
import com.howtographql.hackernews.repositories.LinkRepository;
import com.howtographql.hackernews.repositories.UserRepository;

public class Mutation implements GraphQLRootResolver {

    private final LinkRepository linkRepository;

    private final UserRepository userRepository;

    public Mutation(LinkRepository linkRepository, UserRepository userRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
    }

    public Link createLink(String url, String description) {
        Link newLink = new Link(null, url, description);
        return linkRepository.saveLink(newLink);
    }

    public User createUser(String name, AuthData auth) {
        User newUser = new User(name, auth.getEmail(), auth.getPassword());
        return userRepository.saveUser(newUser);
    }

}
