package com.wifyee.greenfields.models.requests;

import java.io.Serializable;

/**
 * Created by amit on 7/14/2017.
 */

public class DeductMoneyWallet implements Serializable
{
    public String hash;
    public String clientMobile;
    public String pincode;
    public String amount;
    public String status;
    public String description;
    public String merchantDebit;
    public String merchantId;



}
