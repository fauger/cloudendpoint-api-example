package com.benext.v2.model;

import com.benext.v2.dto.ConsensusResponse;
import com.benext.v2.dto.UserRequest;
import com.benext.v2.services.Deref;
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

    @Id private Long id;

    private String deviceId;

    private String name;

    @Load
    List<Ref<Consensus>> consensusList = new ArrayList<>();

    public void addConsensus(Consensus consensus) {
        consensusList.add(Ref.create(consensus));
    }

    public User() {

    }

    public User (UserRequest ur) {
        this.deviceId = ur.getDeviceId();
        this.name = ur.getName();
    }

    public User update (UserRequest ur) {
        this.deviceId = ur.getDeviceId();
        this.name = ur.getName();

        return this;
    }

    public List<ConsensusResponse> getConsensusResponseList() {
        List<ConsensusResponse> consensusResponses = new ArrayList<>();
        for (Consensus consensus : Deref.deref(consensusList)) {
            consensusResponses.add(new ConsensusResponse(consensus));
        }
        return consensusResponses;

    }





}
