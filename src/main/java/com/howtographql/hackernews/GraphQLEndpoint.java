package com.howtographql.hackernews;

import com.coxautodev.graphql.tools.SchemaParser;
import com.howtographql.hackernews.model.Scalars;
import com.howtographql.hackernews.model.User;
import com.howtographql.hackernews.repositories.LinkRepository;
import com.howtographql.hackernews.repositories.UserRepository;
import com.howtographql.hackernews.repositories.VoteRepository;
import com.howtographql.hackernews.resolvers.LinkResolver;
import com.howtographql.hackernews.resolvers.SigninResolver;
import com.howtographql.hackernews.resolvers.VoteResolver;
import com.howtographql.hackernews.resolvers.mutations.Mutation;
import com.howtographql.hackernews.resolvers.queries.Query;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLContext;
import graphql.servlet.SimpleGraphQLServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

    private static final LinkRepository linkRepository;

    private static final UserRepository userRepository;

    private static final VoteRepository voteRepository;

    static {
        //Change to `new MongoClient("<host>:<port>")`
        //if you don't have Mongo running locally on port 27017
        MongoDatabase mongo = new MongoClient().getDatabase("hackernews");
        linkRepository = new LinkRepository(mongo.getCollection("links"));
        userRepository = new UserRepository(mongo.getCollection("users"));
        voteRepository = new VoteRepository(mongo.getCollection("votes"));
    }

    public GraphQLEndpoint() {
        super(buildSchema());
    }

    private static GraphQLSchema buildSchema() {
        return  SchemaParser.newParser()
                .file("schema.graphqls")
                .resolvers(
                        new Query(linkRepository),
                        new Mutation(linkRepository, userRepository, voteRepository),
                        new SigninResolver(),
                        new LinkResolver(userRepository),
                        new VoteResolver(linkRepository, userRepository))
                .scalars(Scalars.dateTime)
                .build()
                .makeExecutableSchema();
    }

    @Override
    protected GraphQLContext createContext(Optional<HttpServletRequest> request,
                                           Optional<HttpServletResponse> response) {
        User user = request
                .map(req -> req.getHeader("Authorization"))
                .filter(id -> !id.isEmpty())
                .map(id -> id.replace("Bearer ", ""))
                .map(userRepository::findById)
                .orElse(null);
        return new AuthContext(user, request, response);
    }


    //handle data fetching errors
    @Override
    protected List<GraphQLError> filterGraphQLErrors(List<GraphQLError> errors) {
        return errors.stream()
                .filter(e -> e instanceof ExceptionWhileDataFetching || isClientError(e))
                .map(e -> (e instanceof ExceptionWhileDataFetching) ?
                        new SanitizedError((ExceptionWhileDataFetching) e) : e)
                .collect(Collectors.toList());
    }

}
