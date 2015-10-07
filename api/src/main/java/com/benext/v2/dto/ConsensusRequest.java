package com.benext.v2.dto;

import com.benext.v2.model.Question;



import java.util.Date;
import java.util.List;

import lombok.Data;


public @Data class ConsensusRequest {

    public String name;

    public List<Question> questions;


}
