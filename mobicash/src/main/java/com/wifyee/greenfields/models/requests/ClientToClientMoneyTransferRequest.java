package com.wifyee.greenfields.models.requests;

import org.json.JSONObject;

import java.io.Serializable;

public class ClientToClientMoneyTransferRequest extends JSONObject implements Serializable {

    // sender mobile number
    public String senderMobileNumber;
    // receiver mobile number who receive money
    public String receiverMobileNumber;
    // amount to be send
    public String amount;
    // hash key
    public String hash;
}