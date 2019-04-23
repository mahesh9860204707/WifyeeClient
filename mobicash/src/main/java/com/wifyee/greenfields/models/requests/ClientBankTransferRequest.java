package com.wifyee.greenfields.models.requests;

import org.json.JSONObject;

import java.io.Serializable;

public class ClientBankTransferRequest extends JSONObject implements Serializable {

    public String userId;
    public String userMobile;
    public String beneficiaryID;
    public String kycStatus;
    public String agentCode;
    public String userTerminal;
    public String clientmobile;
    public String clientId;
    public String pincode;
    public String transferType;
    public String accountNo;
    public String IFSC;
    public String bankName;
    public String branchName;
    public String amount;
    public String firstName;
    public String lastName;
    public String beneficiaryMobile;
    public String hash;
    public String userType;
    public String comments;
    public String beneficiaryMMID;
    public String emailId;
}