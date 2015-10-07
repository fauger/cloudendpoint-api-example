package com.benext.model;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by SHIVAN on 20/09/2015.
 */

@Entity
public @Data class ConsensusAnswer {

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public @Id Long id;

    public @Index String consensusId;

    public @Index String userId;

    public List<AnswerRq> answers = new ArrayList<>();

    public static @Data class AnswerRq {

        public AnswerRq() {

        }

        public AnswerRq(String questionId, Integer note) {
            this.id = questionId;
            this.note = note;
        }

        public String id;

        public Integer note = -1;

    }
}
