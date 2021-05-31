package com.howtographql.hackernews.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.howtographql.hackernews.model.Link;
import com.howtographql.hackernews.model.User;
import com.howtographql.hackernews.model.Vote;
import com.howtographql.hackernews.repositories.LinkRepository;
import com.howtographql.hackernews.repositories.UserRepository;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLQuery;

public class VoteResolver {

    private final LinkRepository linkRepository;

    private final UserRepository userRepository;

    public VoteResolver(LinkRepository linkRepository, UserRepository userRepository) {
        this.linkRepository = linkRepository;
        this.userRepository = userRepository;
    }

    @GraphQLQuery(name = "link")
    public Link link(@GraphQLContext Vote vote) {
        return linkRepository.findById(vote.getLinkId());
    }

    @GraphQLQuery(name = "user")
    public User user(@GraphQLContext Vote vote) {
        return userRepository.findById(vote.getUserId());
    }

}
