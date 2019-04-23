package com.wifyee.greenfields.foodorder;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by amit on 6/9/2017.
 */
public class MerchantPaymentResponse extends JSONObject implements Serializable
{
    public String amount;
    public String responseCode;
    public int logId;
    public String merchantPaymentStatus;
    public String merchantId;
    public String msg;
    public String transactionDate;
    public String status;


}
