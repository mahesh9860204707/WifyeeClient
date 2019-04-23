
package com.wifyee.greenfields.models;

import java.util.HashMap;
import java.util.Map;

public class Example {

    private String documentFile;
    private String documentType;
    private String documentName;
    private String proofNumber;
    private String cUSTOMERMOBILE;
    private String cUSTOMERNAME;
    private String aID;
    private String oP;
    private String sT;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    public String getCUSTOMERMOBILE() {
        return cUSTOMERMOBILE;
    }

    public void setCUSTOMERMOBILE(String cUSTOMERMOBILE) {
        this.cUSTOMERMOBILE = cUSTOMERMOBILE;
    }

    public String getCUSTOMERNAME() {
        return cUSTOMERNAME;
    }

    public void setCUSTOMERNAME(String cUSTOMERNAME) {
        this.cUSTOMERNAME = cUSTOMERNAME;
    }

    public String getAID() {
        return aID;
    }

    public void setAID(String aID) {
        this.aID = aID;
    }

    public String getOP() {
        return oP;
    }

    public void setOP(String oP) {
        this.oP = oP;
    }

    public String getST() {
        return sT;
    }

    public void setST(String sT) {
        this.sT = sT;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
