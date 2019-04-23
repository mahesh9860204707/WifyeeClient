package com.wifyee.greenfields.models.requests;

import java.io.Serializable;

/**
 * Created by amit on 7/17/2017.
 */

public class PlanPaymentRequest implements Serializable
{
    public String macAddress;
    public String invoice_id;
    public String plan_id;
    public String paid_amount;
    public String paid_tax;
    public String paid_on;
    public String productInfo;
    public String mobile;
}
