package com.howtographql.hackernews.repositories;

import com.howtographql.hackernews.model.Scalars;
import com.howtographql.hackernews.model.Vote;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class VoteRepository {

    private final MongoCollection<Document> votes;

    public VoteRepository(MongoCollection<Document> votes) {
        this.votes = votes;
    }


    public List<Vote> findByLinkId(String linkId) {
        List<Vote> voteList = new ArrayList<>();
        for(Document doc : votes.find(eq("linkId", linkId))) {
            voteList.add(vote(doc));
        }
        return voteList;
    }


    public List<Vote> findByUserId(String userId) {
        List<Vote> voteList = new ArrayList<>();
        for(Document doc : votes.find(eq("userId", userId))) {
            voteList.add(vote(doc));
        }
        return voteList;
    }


    public Vote saveVote(Vote vote) {
        Document doc = new Document();
        doc.append("linkId", vote.getLinkId());
        doc.append("userId", vote.getUserId());
        doc.append("createdAt", Scalars.dateTime.getCoercing().serialize(vote.getCreatedAt()));

        votes.insertOne(doc);
        Vote savedVote = vote(doc);
        return savedVote;
//        return vote(doc);
    }


    private Vote vote(Document doc) {
        return new Vote(
                doc.get("_id").toString(),
                ZonedDateTime.parse(doc.getString("createdAt")),
                doc.getString("linkId"),
                doc.getString("userId")
        );
    }

}
