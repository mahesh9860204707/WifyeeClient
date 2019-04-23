package com.wifyee.greenfields.models.requests;

import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;

public class UploadClientPictureRequest extends JSONObject implements Serializable {

    public String clientId;
    public String pincode;
    public File picture;
    public String hash;
}