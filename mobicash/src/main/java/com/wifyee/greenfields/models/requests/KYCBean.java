package com.wifyee.greenfields.models.requests;

import java.io.File;
import java.io.Serializable;

/**
 * Created by amit on 3/19/2018.
 */
public class KYCBean implements Serializable
{
    private File kycIdProof;
    private String kycIdProofDocNumber;
    private File kycAddressProof;
    private String kycAddressDocNumber;

    public File getKycIdProof() {
        return kycIdProof;
    }

    public void setKycIdProof(File kycIdProof) {
        this.kycIdProof = kycIdProof;
    }

    public File getKycAddressProof() {
        return kycAddressProof;
    }

    public void setKycAddressProof(File kycAddressProof) {
        this.kycAddressProof = kycAddressProof;
    }




    public String getKycIdProofDocNumber() {
        return kycIdProofDocNumber;
    }

    public void setKycIdProofDocNumber(String kycIdProofDocNumber) {
        this.kycIdProofDocNumber = kycIdProofDocNumber;
    }



    public String getKycAddressDocNumber() {
        return kycAddressDocNumber;
    }

    public void setKycAddressDocNumber(String kycAddressDocNumber) {
        this.kycAddressDocNumber = kycAddressDocNumber;
    }
}
