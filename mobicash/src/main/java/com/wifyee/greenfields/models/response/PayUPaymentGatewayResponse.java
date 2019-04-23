package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

public class PayUPaymentGatewayResponse extends JSONObject implements Serializable {

    public String responseStatus;
    public String responseCode;
    public String ResponseMsg;
    public String RefId;
    public String cliIdtId;
    public String merIdtId;
    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return ResponseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        ResponseMsg = responseMsg;
    }

    public String getRefId() {
        return RefId;
    }

    public void setRefId(String refId) {
        RefId = refId;
    }

    public String getCliIdtId() {
        return cliIdtId;
    }

    public void setCliIdtId(String cliIdtId) {
        this.cliIdtId = cliIdtId;
    }

    public String getMerIdtId() {
        return merIdtId;
    }

    public void setMerIdtId(String merIdtId) {
        this.merIdtId = merIdtId;
    }


}
