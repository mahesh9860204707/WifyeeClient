package com.wifyee.greenfields.models.requests;

import java.io.Serializable;

/**
 * Created by amit on 3/19/2018.
 */
public class BeneficiaryBean implements Serializable
{
    private String user_id;
    private String beneficiary_id;
    private String status;
    private String userType;
    private String customerTitle;
    private String agentCode;
    private String customerMobile;
    private String benFirstName;
    private String customerLname;
    private String benBankName;
    private String benLastName;
    private String benAccountNumber;
    private String benIfscCode;
    private String benMobileNumber;
    private String benBranchName;
    private String custAddess;
    private String state;
    private String pincode;
    private String customerEmail;
    private String customerFname;
    private String customerAltMobile;
    private String source;
    private String type;
    private String addarVerificationCode;
    private String KYCType;
    private String KYC;
    private String city;
    private String customerDOB;
    private boolean benOtpVerify;

    public boolean isBenOtpVerify() {
        return benOtpVerify;
    }

    public void setBenOtpVerify(boolean benOtpVerify) {
        this.benOtpVerify = benOtpVerify;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getBeneficiary_id() {
        return beneficiary_id;
    }

    public void setBeneficiary_id(String beneficiary_id) {
        this.beneficiary_id = beneficiary_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCustomerTitle() {
        return customerTitle;
    }

    public void setCustomerTitle(String customerTitle) {
        this.customerTitle = customerTitle;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getBenFirstName() {
        return benFirstName;
    }

    public void setBenFirstName(String benFirstName) {
        this.benFirstName = benFirstName;
    }

    public String getCustomerLname() {
        return customerLname;
    }

    public void setCustomerLname(String customerLname) {
        this.customerLname = customerLname;
    }

    public String getBenBankName() {
        return benBankName;
    }

    public void setBenBankName(String benBankName) {
        this.benBankName = benBankName;
    }

    public String getBenLastName() {
        return benLastName;
    }

    public void setBenLastName(String benLastName) {
        this.benLastName = benLastName;
    }

    public String getBenAccountNumber() {
        return benAccountNumber;
    }

    public void setBenAccountNumber(String benAccountNumber) {
        this.benAccountNumber = benAccountNumber;
    }

    public String getBenIfscCode() {
        return benIfscCode;
    }

    public void setBenIfscCode(String benIfscCode) {
        this.benIfscCode = benIfscCode;
    }

    public String getBenMobileNumber() {
        return benMobileNumber;
    }

    public void setBenMobileNumber(String benMobileNumber) {
        this.benMobileNumber = benMobileNumber;
    }

    public String getBenBranchName() {
        return benBranchName;
    }

    public void setBenBranchName(String benBranchName) {
        this.benBranchName = benBranchName;
    }

    public String getCustAddess() {
        return custAddess;
    }

    public void setCustAddess(String custAddess) {
        this.custAddess = custAddess;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerFname() {
        return customerFname;
    }

    public void setCustomerFname(String customerFname) {
        this.customerFname = customerFname;
    }

    public String getCustomerAltMobile() {
        return customerAltMobile;
    }

    public void setCustomerAltMobile(String customerAltMobile) {
        this.customerAltMobile = customerAltMobile;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddarVerificationCode() {
        return addarVerificationCode;
    }

    public void setAddarVerificationCode(String addarVerificationCode) {
        this.addarVerificationCode = addarVerificationCode;
    }

    public String getKYCType() {
        return KYCType;
    }

    public void setKYCType(String KYCType) {
        this.KYCType = KYCType;
    }

    public String getKYC() {
        return KYC;
    }

    public void setKYC(String KYC) {
        this.KYC = KYC;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCustomerDOB() {
        return customerDOB;
    }

    public void setCustomerDOB(String customerDOB) {
        this.customerDOB = customerDOB;
    }
}
