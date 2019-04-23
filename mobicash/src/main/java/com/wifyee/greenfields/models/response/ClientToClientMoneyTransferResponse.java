package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

public class ClientToClientMoneyTransferResponse extends JSONObject implements Serializable {

    public String status;
    public String responseCode;
    public String senderMobile;
    public String receiverMobile;
    public String amount;
    public String transactionDate;
    public String msg;
}