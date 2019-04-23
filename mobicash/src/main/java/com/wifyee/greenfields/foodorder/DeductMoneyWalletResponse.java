package com.wifyee.greenfields.foodorder;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by user on 12/21/2017.
 */

public class DeductMoneyWalletResponse extends JSONObject implements Serializable {

    public String status;
    public String responseCode;
    public String clientNewBalance;
    public String clientId;
    public String clientMobile;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getClientNewBalance() {
        return clientNewBalance;
    }

    public void setClientNewBalance(String clientNewBalance) {
        this.clientNewBalance = clientNewBalance;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientMobile() {
        return clientMobile;
    }

    public void setClientMobile(String clientMobile) {
        this.clientMobile = clientMobile;
    }

    public String getClientMobicashLogId() {
        return clientMobicashLogId;
    }

    public void setClientMobicashLogId(String clientMobicashLogId) {
        this.clientMobicashLogId = clientMobicashLogId;
    }

    public String getClientTransactionDate() {
        return clientTransactionDate;
    }

    public void setClientTransactionDate(String clientTransactionDate) {
        this.clientTransactionDate = clientTransactionDate;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String clientMobicashLogId;
    public String clientTransactionDate;
    public String msg;
}