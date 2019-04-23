package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by amit on 3/26/2018.
 */

public class BeneficiaryResponse extends JSONObject implements Serializable
{
    public String status;
    public String responseCode;
    public String ben_id;
    public String msg;

}
