package com.wifyee.greenfields.models.requests;

import java.io.Serializable;

/**
 * Created by amit on 7/14/2017.
 */

public class AddMoneyWallet implements Serializable
{
    public String hash;
    public String clientMobile;
    public String clientreceiver;
    public String pincode;
    public String amount;
    public String txnId;
    public String status;
    public String description;



}
