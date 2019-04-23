package com.wifyee.greenfields.models.response;

import java.io.Serializable;

/**
 * Created by amit on 9/1/2017.
 */

public class SendMoneyResponse implements Serializable
{
    public String amount;
    public String receiverMobile;
    public String responseCode;
    public String receiverType;
    public String msg;
    public String status;

    public String senderMobile;


}
