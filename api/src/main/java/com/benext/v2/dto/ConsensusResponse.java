package com.benext.v2.dto;

import com.benext.v2.model.Consensus;
import com.benext.v2.model.Question;

import java.util.List;

import lombok.Data;


public @Data class ConsensusResponse {

    public long id;

    public String name;

    public List<Question> questions;

    public ConsensusResponse(Consensus consensus) {
        this.id = consensus.id;
        this.name = consensus.name;
        this.questions=consensus.getQuestions();
    }


}
