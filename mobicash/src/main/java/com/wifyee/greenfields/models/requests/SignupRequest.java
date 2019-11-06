package com.wifyee.greenfields.models.requests;

import org.json.JSONObject;

import java.io.Serializable;

public class SignupRequest extends JSONObject implements Serializable {

    public String clientmobile;
    public String firstname;
    public String lastname;
    public String pincode;
    public String customerTitle;
    public String custAddess;
    public String customerDOB;
    public String email;
    public String hash;
    public String zipcode;
    public String nationalityId;
    public String countryId;
}