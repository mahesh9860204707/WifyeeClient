


package com.wifyee.greenfields.models.requests;

import org.json.JSONObject;

import java.io.Serializable;

public class ClientProfileUpdateRequest extends JSONObject implements Serializable {

    public String clientId;
    public String pincode;
    public String hash;
    public String firstname;
    public String lastname;
    public String username;
    public String address;
    public String zipcode;
    public String city;
    public String country;
    public String language;
    public String email;
    public String dob;
    public String adhaar;
    public String pan;
}