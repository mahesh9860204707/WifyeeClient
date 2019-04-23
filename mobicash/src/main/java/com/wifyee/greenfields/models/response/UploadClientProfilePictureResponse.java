package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

public class UploadClientProfilePictureResponse extends JSONObject implements Serializable {

    public String status;
    public String responseCode;
    public String clientId;
    public String clientProfileUploadState;
    public String msg;
}