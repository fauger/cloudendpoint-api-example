package com.benext.v2.api;

import com.benext.v2.dto.ConsensusRequest;
import com.benext.v2.dto.ConsensusResponse;
import com.benext.v2.model.Consensus;
import com.benext.v2.model.Question;
import com.benext.v2.model.User;
import com.benext.v2.services.Deref;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;
import com.googlecode.objectify.Key;

import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.benext.services.OfyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "consensusApi",
        version = "v2",
        resource = "consensus",
        namespace = @ApiNamespace(
                ownerDomain = "model.benext.com",
                ownerName = "model.benext.com",
                packagePath = ""
        )
)
public class ConsensusEndpoint {

    private static final Logger logger = Logger.getLogger(ConsensusEndpoint.class.getName());

    /**
     * Inserts a new {@code Consensus}.
     */
    @ApiMethod(
            name = "insert",
            path = "consensus",
            httpMethod = ApiMethod.HttpMethod.POST)
    public ConsensusResponse insert(@Named("userId") long userId, ConsensusRequest consensusRequest) throws BadRequestException {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that consensus.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        User user = ofy().load().type(User.class).id(userId).safe();
        Consensus consensus = new Consensus(consensusRequest, user);

        ofy().save().entity(consensus).now();
        user.addConsensus(consensus);
        ofy().save().entity(user).now();
        logger.info("Created Consensus with ID: " + consensus.getId());
        return new ConsensusResponse(consensus);


    }

    /**
     * Returns the {@link Consensus} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Consensus} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "consensus/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public ConsensusResponse get(@Named("id") long id) throws NotFoundException {
        logger.info("Getting Consensus with ID: " + id);
        Consensus consensus = ofy().load().type(Consensus.class).id(id).safe();
        return new ConsensusResponse(consensus);
    }

    /**
     * Returns the {@link Consensus} with the corresponding ID.
    *
     * @param userId the ID of the entity to be retrieved
    * @return the entity with the corresponding ID
    * @throws NotFoundException if there is no {@code Consensus} with the provided ID.
            */
    @ApiMethod(
            name = "getByUser",
            path = "consensus",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<ConsensusResponse> getConsensus(@Named("userId") long userId) {
        logger.info("Getting Consensus with ID: " + userId);
        User user = ofy().load().type(User.class).id(userId).safe();
        return CollectionResponse.<ConsensusResponse>builder().setItems(user.getConsensusResponseList()).build();
    }

  /**
     * Returns the {@link Consensus} with the corresponding ID.
     *
     * @param userId the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Consensus} with the provided ID.
     */
    @ApiMethod(
            name = "associate_consensus_with_user",
            path = "consensus_user",
            httpMethod = ApiMethod.HttpMethod.POST)
    public void associateConsensusAndUser(@Named("userId") long userId,@Named("consensusId") long consensusId) {
        logger.info("associate_consensus_with_user: " + userId);
        User user = ofy().load().type(User.class).id(userId).safe();
        Consensus consensus = ofy().load().key(Key.create(Key.create(User.class, user.getId()), Consensus.class,consensusId)).now();
        user.addConsensus(consensus);
        consensus.setUser(Key.create(user));
        ofy().save().entity(consensus).now();
        ofy().save().entity(user).now();
    }


    /**
     * Updates an existing {@code Consensus}.
     *
     * @param consensusId        the ID of the entity to be updated
     * @param consensusRequest the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Consensus}
     */
    @ApiMethod(
            name = "update",
            path = "consensus/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public ConsensusResponse update(@Named("id") long consensusId, ConsensusRequest consensusRequest) throws NotFoundException {
        Consensus consensus = ofy().load().type(Consensus.class).id(consensusId).safe();
        ofy().save().entity(consensus.update(consensusRequest)).now();
        logger.info("Updated Consensus: " + consensus);
        return new ConsensusResponse(ofy().load().type(Consensus.class).id(consensus.getId()).now());
    }

    /**
     * Deletes the specified {@code Consensus}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Consensus}
     */
    @ApiMethod(
            name = "remove",
            path = "consensus/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") long id) throws NotFoundException {
        ofy().load().type(Consensus.class).id(id).safe();
        ofy().delete().type(Consensus.class).id(id);
        logger.info("Deleted Consensus with ID: " + id);
    }

//    *//**
//     * List all entities.
//     *
//     * @param cursor used for pagination to determine which page to return
//     * @param limit  the maximum number of entries to return
//     * @return a response that encapsulates the result list and the next page token/cursor
//     *//*
//    @ApiMethod(
//            name = "list",
//            path = "consensus",
//            httpMethod = ApiMethod.HttpMethod.GET)
//    public CollectionResponse<Consensus> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
//        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
//        Query<Consensus> query = ofy().load().type(Consensus.class).limit(limit);
//        if (cursor != null) {
//            query = query.startAt(Cursor.fromWebSafeString(cursor));
//        }
//        QueryResultIterator<Consensus> queryIterator = query.iterator();
//        List<Consensus> consensusList = new ArrayList<Consensus>(limit);
//        while (queryIterator.hasNext()) {
//            consensusList.add(queryIterator.next());
//        }
//        return CollectionResponse.<Consensus>builder().setItems(consensusList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
//    }

}