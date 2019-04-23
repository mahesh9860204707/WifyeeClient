package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class NearOfferResponse extends JSONObject implements Serializable {
    public String status;
    public String msg;
    public List<NearOfferList> mNearOfferListList;
}
