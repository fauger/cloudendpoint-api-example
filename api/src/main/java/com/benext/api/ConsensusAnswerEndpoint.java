package com.benext.api;

import com.benext.model.Consensus;
import com.benext.model.ConsensusAnswer;
import com.benext.model.ConsensusAnswerResponse;
import com.benext.model.Question;
import com.benext.model.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.NotFoundException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.benext.services.OfyService.ofy;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "consensusAnswerApi",
        version = "v1",
        resource = "consensus_answer",
        namespace = @ApiNamespace(
                ownerDomain = "model.benext.com",
                ownerName = "model.benext.com",
                packagePath = ""
        )
)
public class ConsensusAnswerEndpoint {

    private static final Logger logger = Logger.getLogger(ConsensusAnswerEndpoint.class.getName());

    /**
     * This inserts a new <code>AnswerRq</code> object.
     *
     * @param consensusAnswer The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insert")
    public ConsensusAnswer insertAnswer(ConsensusAnswer consensusAnswer) throws BadRequestException {

        logger.info("Calling insertAnswer method");
        User user = ofy().load().type(User.class).id(consensusAnswer.getUserId()).now();
        if (user == null) {
            throw new BadRequestException("user " + consensusAnswer.getUserId() + "does not exist");
        }
        Consensus consensus = ofy().load().type(Consensus.class).id(consensusAnswer.getConsensusId()).now();
        if (consensus == null) {
            throw new BadRequestException("consensus " + consensusAnswer.getConsensusId() + "does not exist");
        }
        ofy().save().entity(consensusAnswer).now();
        return null;
    }

    /**
     * Updates an existing {@code Consensus}.
     *
     *
     * @param consensusAnswer the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Consensus}
     */
    @ApiMethod(
            name = "update",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public ConsensusAnswer update(ConsensusAnswer consensusAnswer) throws NotFoundException, BadRequestException {
        Consensus consensus = ofy().load().type(Consensus.class).id(consensusAnswer.getConsensusId()).safe();
        consensus.setLastUpdate(new Date());
        ConsensusAnswer consensusAnswerDB = ofy().load().type(ConsensusAnswer.class).filter("consensusId", consensusAnswer.getConsensusId()).filter("userId", consensusAnswer.getUserId()).first().now();
        if (consensusAnswerDB==null) {
            insertAnswer(consensusAnswer);
        }
        else{
            consensusAnswer.setId(consensusAnswerDB.getId());
            ofy().save().entity(consensusAnswer).now();
        }
        ofy().save().entity(consensus).now();
        logger.info("Updated Consensus: " + consensusAnswer);
        return ofy().load().entity(consensusAnswer).now();
    }

    /**
     * Updates an existing {@code Consensus}.
     *
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Consensus}
     */
    @ApiMethod(
            name = "get",
            path = "consensus_answer",
            httpMethod = ApiMethod.HttpMethod.GET)
    public ConsensusAnswerResponse get(@Named("consensusId") String consensusId,@Named("userId") String userId) throws NotFoundException, BadRequestException {
        User user = ofy().load().type(User.class).id(userId).now();
        if (user == null) {
            throw new BadRequestException("user " + userId + "does not exist");
        }
        Consensus consensus = ofy().load().type(Consensus.class).id(consensusId).now();
        if (consensus == null) {
            throw new BadRequestException("consensus " + consensusId + "does not exist");
        }
        List<ConsensusAnswer> consensusAnswers = ofy().load().type(ConsensusAnswer.class).filter("consensusId",consensusId).filter("userId",userId).list();
        if (consensusAnswers.isEmpty()) {
            throw new NotFoundException("No consensusAnswer for consensusId " + consensusId + " and userId " + userId);
        }

        ConsensusAnswerResponse consensusAnswerResponse = new ConsensusAnswerResponse();

        consensusAnswerResponse.setConsensusId(consensusId);
        consensusAnswerResponse.setUserId(userId);


        for (Question question : consensus.getQuestions()) {
            ConsensusAnswerResponse.AnswerRp answerRpResponse = new ConsensusAnswerResponse.AnswerRp();
            answerRpResponse.setId(question.getId());
            answerRpResponse.setName(question.getName());
            for (ConsensusAnswer.AnswerRq answerRq : consensusAnswers.get(0).getAnswers()) {
                if (question.getId().equals(answerRq.getId())) {
                    answerRpResponse.setNote(answerRq.getNote());
                }

            }
            consensusAnswerResponse.getAnswers().add(answerRpResponse);
        }
        return consensusAnswerResponse;
    }



        /**
         * This inserts a new <code>AnswerRq</code> object.
         *
         * @param consensusId The object to be added.
         * @return The object to be added.
         */
    @ApiMethod(name = "getTotal",
                 path = "total")
    public ConsensusAnswerResponse getAnswer(@Named("consensusId") String consensusId) throws BadRequestException {

        logger.info("Calling insertAnswer method");
        Consensus consensus = ofy().load().type(Consensus.class).id(consensusId).now();
        if (consensus == null) {
            throw new BadRequestException("consensus " + consensusId  + " does not exist");
        }

        List<ConsensusAnswer> consensusAnswers = ofy().load().type(ConsensusAnswer.class).filter("consensusId",consensusId).list();

        ConsensusAnswerResponse mergeAnswers = new ConsensusAnswerResponse();
        mergeAnswers.setUserId("-1");
        mergeAnswers.setConsensusId(consensusId);
        Map<String,Integer> questionNote = new HashMap<>();

        for (ConsensusAnswer canswers : consensusAnswers){
            for (ConsensusAnswer.AnswerRq answerRq : canswers.getAnswers()) {
                Integer note = questionNote.get(answerRq.getId());
                if (note == null) {
                    questionNote.put(answerRq.getId(), answerRq.getNote());
                }
                else {
                    note  += answerRq.getNote();
                    questionNote.put(answerRq.getId(),note);
                }
            }
        }

        for (Question question : consensus.getQuestions()){
            ConsensusAnswerResponse.AnswerRp answerRpResponse = new ConsensusAnswerResponse.AnswerRp();
            answerRpResponse.setId(question.getId());
            answerRpResponse.setName(question.getName());
            for (Map.Entry<String,Integer> entry : questionNote.entrySet()){
                if (question.getId().equals(entry.getKey())){
                    answerRpResponse.setNote(entry.getValue());
                }
            }
            mergeAnswers.getAnswers().add(answerRpResponse);
        }


        return mergeAnswers;
    }
}