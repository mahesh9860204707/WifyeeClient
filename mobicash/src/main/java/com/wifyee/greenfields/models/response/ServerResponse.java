package com.wifyee.greenfields.models.response;

import org.json.JSONObject;

import java.io.Serializable;

public class ServerResponse extends JSONObject implements Serializable
{
    public String sessionID;
    public String uniqueID;
    public String nasIPAddress;
    public String nasPortID;
    public String nasPortType;
    public String startTime;
    public String stopTime;
    public String sessionTime;
    public String uploadData;
    public String downloadData;
    public String calledStationId;
    public String framedIPAddress;
}
