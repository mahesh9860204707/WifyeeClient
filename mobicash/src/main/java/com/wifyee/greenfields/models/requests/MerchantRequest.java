package com.wifyee.greenfields.models.requests;

import java.io.Serializable;

/**
 * Created by amit on 7/21/2017.
 */
public class MerchantRequest implements Serializable
{
    public String merchantId;
    public String amount;
    public String mobile;
    public String hash;
}
