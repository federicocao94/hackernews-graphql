package com.howtographql.hackernews.repositories;

import com.howtographql.hackernews.model.Link;
import com.howtographql.hackernews.model.LinkFilter;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.*;

public class LinkRepository {

    private final MongoCollection<Document> links;

    public LinkRepository(MongoCollection<Document> links) {
        this.links = links;
    }

    public Link findById(String id) {
        Document doc = links.find(eq("_id", new ObjectId(id))).first();
        return link(doc);
    }


    public List<Link> getAllLinks(LinkFilter filter, int skip, int first) {

        Optional<Bson> mongoFilter = Optional.ofNullable(filter).map(this::buildFilter);
        //if mongoFIlter != null -> apply filter, else -> find all
        FindIterable<Document> documents = mongoFilter.map(links::find).orElseGet(links::find);

        List<Link> allLinks = new ArrayList<>();
        for (Document doc : documents.skip(skip).limit(first)) {
            allLinks.add(link(doc));
        }

        return allLinks;

    }


    private Bson buildFilter(LinkFilter filter) {
        String descriptionPattern = filter.getDescriptionContains();
        String urlPattern = filter.getUrlContains();

        Bson descrCondition = null;
        Bson urlCondition = null;

        if(descriptionPattern != null && !descriptionPattern.isEmpty()) {
            descrCondition = regex("description",
                    ".*" + descriptionPattern + ".*", "i");
        }

        if(urlPattern != null && !urlPattern.isEmpty()) {
            urlCondition = regex("url",
                    ".*" + urlPattern + ".*", "i");
        }

        if(descrCondition != null && urlCondition != null) {
            return and(descrCondition, urlCondition);
        }

        return (descrCondition != null) ? descrCondition : urlCondition;
    }


    public Link saveLink(Link link) {
        Document doc = new Document();
        doc.append("url", link.getUrl());
        doc.append("description", link.getDescription());
        doc.append("userId", link.getUserId());
        links.insertOne(doc);
        return link(doc);
    }


    private Link link(Document doc) {
        return new Link(
                doc.get("_id").toString(),
                doc.getString("url"),
                doc.getString("description"),
                doc.getString("userId"));
    }

}
