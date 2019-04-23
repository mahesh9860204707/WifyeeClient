


package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

public class GetClientProfileInfo extends JSONObject implements Serializable {

    public String mobicashClientId;
    public GetProfileInfo mobicashClientProfile;
}