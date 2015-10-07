package com.benext.model;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by SHIVAN on 20/09/2015.
 */

public @Data class ConsensusAnswerResponse {

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public Long id;

    public String consensusId;

    public String userId;

    public List<AnswerRp> answers = new ArrayList<>();

    public static @Data class AnswerRp {

        public AnswerRp() {

        }

        public AnswerRp(String questionId, String name, Integer note) {
            this.id = questionId;
            this.name = name;
            this.note = note;
        }

        public String id;

        public String name;

        public Integer note = -1;

    }
}
