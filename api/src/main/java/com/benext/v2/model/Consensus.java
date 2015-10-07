package com.benext.v2.model;

import com.benext.v2.dto.ConsensusRequest;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Entity
public @Data class Consensus {


    @Id public Long id;

    @Parent public Key<User> user;

    public String name;


    public Date lastUpdate =  new Date();

    public List<Question> questions;

    public Consensus() {

    }



    public Consensus(ConsensusRequest cr, User user) {
        this.name = cr.name;
        this.questions = cr.getQuestions();
        this.user = Key.create(user);
    }

    public Consensus update (ConsensusRequest cr) {
        this.name = cr.getName();
        this.questions = cr.getQuestions();

        return this;

    }


}
