package com.wifyee.greenfields.models.requests;

import java.io.Serializable;

/**
 * Created by amit on 4/5/2018.
 */

public class KYCDocumentsBean implements Serializable {

    private String documentFile;
    private String documentType;
    private String documentName;
    private String proofNumber;
    private String CUSTOMER_MOBILE;
    private String CUSTOMER_NAME;
    private String AID;
    private String OP;
    private String ST;

    public String getCUSTOMER_MOBILE() {
        return CUSTOMER_MOBILE;
    }

    public void setCUSTOMER_MOBILE(String CUSTOMER_MOBILE) {
        this.CUSTOMER_MOBILE = CUSTOMER_MOBILE;
    }

    public String getCUSTOMER_NAME() {
        return CUSTOMER_NAME;
    }

    public void setCUSTOMER_NAME(String CUSTOMER_NAME) {
        this.CUSTOMER_NAME = CUSTOMER_NAME;
    }

    public String getAID() {
        return AID;
    }

    public void setAID(String AID) {
        this.AID = AID;
    }

    public String getOP() {
        return OP;
    }

    public void setOP(String OP) {
        this.OP = OP;
    }

    public String getST() {
        return ST;
    }

    public void setST(String ST) {
        this.ST = ST;
    }


    public String getDocumentFile() {
        return documentFile;
    }

    public void setDocumentFile(String documentFile) {
        this.documentFile = documentFile;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getProofNumber() {
        return proofNumber;
    }

    public void setProofNumber(String proofNumber) {
        this.proofNumber = proofNumber;
    }





}