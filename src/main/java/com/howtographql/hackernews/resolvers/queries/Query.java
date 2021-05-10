package com.howtographql.hackernews.resolvers.queries;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import com.howtographql.hackernews.model.Link;
import com.howtographql.hackernews.model.LinkFilter;
import com.howtographql.hackernews.repositories.LinkRepository;

import java.util.List;

public class Query implements GraphQLRootResolver {

    private final LinkRepository linkRepository;

    public Query(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public List<Link> allLinks(LinkFilter filter, int skip, int first) {
        return  linkRepository.getAllLinks(filter, skip, first);
    }

}
