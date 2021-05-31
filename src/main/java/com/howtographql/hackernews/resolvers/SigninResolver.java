package com.howtographql.hackernews.resolvers;

import com.howtographql.hackernews.model.SigninPayload;
import com.howtographql.hackernews.model.User;
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLQuery;

public class SigninResolver {

    @GraphQLQuery
    public User user(@GraphQLContext SigninPayload payload) {
        return payload.getUser();
    }

}
