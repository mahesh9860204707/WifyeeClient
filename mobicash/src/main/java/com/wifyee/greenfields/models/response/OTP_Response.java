package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

public class OTP_Response extends JSONObject implements Serializable {

    public String status;
    public String responseCode;
    public String code;
    public String mobile;
    public String timefrom;
    public String generateOTPSendStatus;
    public String userId;
}
