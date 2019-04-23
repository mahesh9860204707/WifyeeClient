package com.wifyee.greenfields.models.requests;

import java.io.Serializable;

/**
 * Created by amit on 7/19/2017.
 */

public class Participent implements Serializable
{

    public String participentMobile;
    public String participentEmail;

    public String getParticipentMobile() {
        return participentMobile;
    }

    public void setParticipentMobile(String participentMobile) {
        this.participentMobile = participentMobile;
    }

    public String getParticipentEmail() {
        return participentEmail;
    }

    public void setParticipentEmail(String participentEmail) {
        this.participentEmail = participentEmail;
    }


}
