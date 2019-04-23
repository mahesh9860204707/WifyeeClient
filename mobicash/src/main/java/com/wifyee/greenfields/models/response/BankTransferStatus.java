package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class BankTransferStatus extends JSONObject implements Serializable {

    public String mobicashClientId;
    public List<BankTransferResponse> mobicashClientBankTransferResponseList;
}