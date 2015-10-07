package com.benext.model;

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


    @Id public String id;

    public String name;


    public Date lastUpdate =  new Date();

    @Load public List<Question> questions;

    @Transient
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public Key<Consensus> getKey() {
        return Key.create(Consensus.class, id);
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public void setLastUpdate(Date date) {
        lastUpdate = date;
    }

}
