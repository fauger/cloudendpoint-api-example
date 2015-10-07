package com.benext.v2.dto;

import com.benext.v2.model.Consensus;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by SHIVAN on 13/09/2015.
 */


public @Data class UserRequest {

    private String deviceId;

    private String name;


}
