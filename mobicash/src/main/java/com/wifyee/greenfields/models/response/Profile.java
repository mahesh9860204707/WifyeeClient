


package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

public class Profile extends JSONObject implements Serializable {

    public String clientFirstName;
    public String clientLastName;
    public String clientUserName;
    public String clientAddress;
    public String clientCity;
    public String clientZipcode;
    public String clientCountry;
    public String clientLanguage;
    public String clientEmail;
    public String clientDob;
    public String clientAdhaar;
    public String clientPan;
    public String profilePicture;
}