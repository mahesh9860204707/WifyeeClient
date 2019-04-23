package com.wifyee.greenfields.models.requests;

import java.io.Serializable;

/**
 * Created by amit on 9/21/2017.
 */

public class City implements Serializable
{
    private String name;
    private String id;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
