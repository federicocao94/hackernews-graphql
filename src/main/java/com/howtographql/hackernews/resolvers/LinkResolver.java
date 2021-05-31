package com.howtographql.hackernews.resolvers;

import com.howtographql.hackernews.model.Link;
import com.howtographql.hackernews.model.User;
import com.howtographql.hackernews.repositories.UserRepository;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLQuery;

public class LinkResolver {

    private final UserRepository userRepository;

    public LinkResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GraphQLQuery
    public User postedBy(@GraphQLContext Link link) {
        if(link.getUserId() == null) {
            return null;
        }

        return userRepository.findById(link.getUserId());
    }

}
