package com.benext.model;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by SHIVAN on 13/09/2015.
 */

@Entity
public @Data class User {

    @Id private String id;

    private String name;

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    List<String> consensusID = new ArrayList<>();

    @Transient
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public Key<User> getKey() {
        return Key.create(User.class, id);
    }

    public void addConsensus(String id){
        if (id!=null)
            consensusID.add(id);
    }


}
