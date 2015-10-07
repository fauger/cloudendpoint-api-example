package com.benext.api;

import com.benext.model.Consensus;
import com.benext.model.Question;
import com.benext.model.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.benext.services.OfyService.factory;
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
        version = "v1",
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
    public Consensus get(@Named("id") String id) throws NotFoundException {
        logger.info("Getting Consensus with ID: " + id);
        Consensus consensus = ofy().load().type(Consensus.class).id(id).now();
        if (consensus == null) {
            throw new NotFoundException("Could not find Consensus with ID: " + id);
        }
        return consensus;
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
    public void associateConsensusAndUser(@Named("userId") String userId,@Named("consensusId") String consensusId) {
        logger.info("Getting Consensus with ID: " + userId);
        User user = ofy().load().type(User.class).id(userId).safe();
        Consensus consensus = ofy().load().type(Consensus.class).id(consensusId).safe();
        user.addConsensus(consensusId);
        ofy().save().entity(user).now();
    }

    /**
     * Returns the {@link Consensus} with the corresponding ID.
     *
     * @param userId the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Consensus} with the provided ID.
     */
    @ApiMethod(
            name = "filter_consensus_by_user",
            path = "consensus",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Consensus> getConsensus(@Named("userId") String userId) {
        logger.info("Getting Consensus with ID: " + userId);
        User user = ofy().load().type(User.class).id(userId).safe();
        Map<String,Consensus> map= ofy().load().type(Consensus.class).ids(user.getConsensusID());
        return CollectionResponse.<Consensus>builder().setItems(map.values()).build();
    }

    /**
     * Inserts a new {@code Consensus}.
     */
    @ApiMethod(
            name = "insert",
            path = "consensus",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Consensus insert(@Named("userId") String userId, Consensus consensus) throws BadRequestException,ConflictException {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that consensus.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        if (consensus.getId() != null) {
            throw new BadRequestException("consensusId cannot be null");
        }
            User user = ofy().load().type(User.class).id(userId).safe();
            user.addConsensus(consensus.getId());
            ofy().save().entity(user).now();
            ofy().save().entity(consensus).now();
             logger.info("Created Consensus with ID: " + consensus.getId());
             return consensus;


    }

    /**
     * Updates an existing {@code Consensus}.
     *
     * @param id        the ID of the entity to be updated
     * @param consensus the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Consensus}
     */
    @ApiMethod(
            name = "update",
            path = "consensus/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Consensus update(@Named("id") String id, Consensus consensus) throws NotFoundException {
        Consensus originalConsensus = ofy().load().type(Consensus.class).id(id).safe();
        if (consensus.getId() != null) {
            originalConsensus.setId(consensus.getId());
        }
        if (consensus.getName() != null) {
            originalConsensus.setName(consensus.getName());
        }
        if (consensus.getLastUpdate() != null) {
            originalConsensus.setLastUpdate(consensus.getLastUpdate());
        }
        if (consensus.getQuestions() != null && !consensus.getQuestions().isEmpty()) {
            for (Question question : consensus.getQuestions()) {
                boolean update = false;
                for (Question originalQuestion : originalConsensus.getQuestions()){
                    if (question.equals(originalQuestion)){
                        originalQuestion.setName(question.getName());
                        update=true;
                    }
                }
                if (!update) {
                    originalConsensus.getQuestions().add(question);
                }
            }
        }
        ofy().save().entity(originalConsensus).now();
        logger.info("Updated Consensus: " + consensus);
        return ofy().load().entity(originalConsensus).now();
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
    public void remove(@Named("id") String id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(Consensus.class).id(id).now();
        logger.info("Deleted Consensus with ID: " + id);
    }

//    /**
//     * List all entities.
//     *
//     * @param cursor used for pagination to determine which page to return
//     * @param limit  the maximum number of entries to return
//     * @return a response that encapsulates the result list and the next page token/cursor
//     */
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

    private void checkExists(String id) throws NotFoundException {
        try {
            ofy().load().type(Consensus.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Consensus with ID: " + id);
        }
    }
}