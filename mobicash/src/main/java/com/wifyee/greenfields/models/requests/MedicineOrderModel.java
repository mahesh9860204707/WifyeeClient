package com.wifyee.greenfields.models.requests;

import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;

/**
 * Created by amit on 5/29/2018.
 */

public class MedicineOrderModel extends JSONObject implements Serializable
{
    private String userId;
    private String userType;
    private String lotLoc;
    private String longLoc;
    public File priscriptionDocument;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLotLoc() {
        return lotLoc;
    }

    public void setLotLoc(String lotLoc) {
        this.lotLoc = lotLoc;
    }

    public String getLongLoc() {
        return longLoc;
    }

    public void setLongLoc(String longLoc) {
        this.longLoc = longLoc;
    }
}
