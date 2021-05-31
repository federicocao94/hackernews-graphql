package com.howtographql.hackernews.resolvers.queries;

import com.howtographql.hackernews.model.Link;
import com.howtographql.hackernews.model.LinkFilter;
import com.howtographql.hackernews.repositories.LinkRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;

import java.util.List;

public class Query {

    private final LinkRepository linkRepository;

    public Query(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @GraphQLQuery
    public List<Link> allLinks(LinkFilter filter,
                               @GraphQLArgument(name = "skip", defaultValue = "0") int skip,
                               @GraphQLArgument(name = "first", defaultValue = "0")int first) {
        return  linkRepository.getAllLinks(filter, skip, first);
    }

}
