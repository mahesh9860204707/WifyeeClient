package com.wifyee.greenfields.models.requests;

import com.wifyee.greenfields.Utils.LocalPreferenceUtility;

import org.json.JSONObject;

import java.io.Serializable;

public class ServerRequest extends JSONObject implements Serializable {

    public String req_action;
    public String req_customercode;
    public String req_username;
    public String req_timeinterval;
}
