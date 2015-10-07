package com.benext.v2.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by SHIVAN on 13/09/2015.
 */

@EqualsAndHashCode(of = {"id"})
public @Data class Question {

    public String id;

    public String questionName;

    public int note;




}
