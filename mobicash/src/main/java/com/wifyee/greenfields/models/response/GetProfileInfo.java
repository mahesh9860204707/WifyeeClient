


package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

public class GetProfileInfo extends JSONObject implements Serializable {

    public String clientWalletBalance;
    public String clientProfileImage;
    public String client_firstname;
    public String client_lastname;
    public String client_email;
    public String clientApprovedCreditLimit;
    public String custAddess;
    public String customerTitle;
    public String customerDOB;
    public String indentityId;

}