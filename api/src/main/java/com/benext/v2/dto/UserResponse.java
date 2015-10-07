package com.benext.v2.dto;

import com.benext.v2.model.User;

import lombok.Data;

/**
 * Created by SHIVAN on 13/09/2015.
 */


public @Data class UserResponse {

    private Long id;

    private String deviceId;

    private String name;

    public UserResponse(User user) {
        this.id = user.getId();
        this.deviceId = user.getDeviceId();
        this.name = user.getName();
    }


}
