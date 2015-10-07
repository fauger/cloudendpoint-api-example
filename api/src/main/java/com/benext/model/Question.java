package com.benext.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by SHIVAN on 13/09/2015.
 */

@EqualsAndHashCode(of = {"id"})
public @Data class Question {

    public String id;

    public String name;




}
