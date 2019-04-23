package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

public class ClientBankTransferResponse extends JSONObject implements Serializable {

    public String status;
    public String responseCode;
    public String clientId;
    public BankTransferStatus bankTransferStatus;
    public String format;
    public String msg;
}