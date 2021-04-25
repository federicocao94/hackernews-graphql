package com.howtographql.hackernews.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.howtographql.hackernews.model.SigninPayload;
import com.howtographql.hackernews.model.User;

public class SigninResolver implements GraphQLResolver<SigninPayload> {

    public User user(SigninPayload payload) {
        return payload.getUser();
    }

}
