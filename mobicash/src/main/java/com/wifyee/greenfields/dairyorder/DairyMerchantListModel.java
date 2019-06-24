package com.wifyee.greenfields.dairyorder;

import android.os.Parcel;
import android.os.Parcelable;

public class DairyMerchantListModel implements Parcelable {


    private Integer status;
    private String message;
    private String id;
    private String name;
    private String company;
    private String type;
    private String image;
    private String idtId;
    private String idtName;
    private String idtRole;
    private String idtEmail;
    private String idtAddress;
    private String idtCity;
    private String idtZipCode;
    private String idtFax;
    private String idtNationality;
    private String idtContactPhone;
    private String idtAlternatePhone;
    private String currentStatus;

    public DairyMerchantListModel() {

    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
       dest.writeString(getId());
        dest.writeString(getName());
        dest.writeString(getCompany());
        dest.writeString(getType());
        dest.writeString(getImage());
        dest.writeString(getIdtId());
        dest.writeString(getIdtName());
        dest.writeString(getIdtRole());
        dest.writeString(getIdtEmail());
        dest.writeString(getIdtAddress());
        dest.writeString(getIdtCity());
        dest.writeString(getIdtZipCode());
        dest.writeString(getIdtFax());
        dest.writeString(getIdtNationality());
        dest.writeString(getIdtContactPhone());
        dest.writeString(getIdtAlternatePhone());
        dest.writeString(getCurrentStatus());
    }

    /**
     * Retrieving Student data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    public DairyMerchantListModel(Parcel in){
        this.id = in.readString();
        this.name = in.readString();
        this.company = in.readString();
        this.type = in.readString();
        this.image = in.readString();
        this.idtId = in.readString();
        this.idtName = in.readString();
        this.idtRole = in.readString();
        this.idtEmail = in.readString();
        this.idtAddress = in.readString();
        this.idtCity = in.readString();
        this.idtZipCode = in.readString();
        this.idtFax = in.readString();
        this.idtNationality = in.readString();
        this.idtContactPhone = in.readString();
        this.idtAlternatePhone = in.readString();
        this.currentStatus = in.readString();
    }

    public static final Parcelable.Creator<DairyMerchantListModel> CREATOR = new Parcelable.Creator<DairyMerchantListModel>() {

        @Override
        public DairyMerchantListModel createFromParcel(Parcel source) {
            return new DairyMerchantListModel(source);
        }

        @Override
        public DairyMerchantListModel[] newArray(int size) {
            return new DairyMerchantListModel[size];
        }
    };

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIdtId() {
        return idtId;
    }

    public void setIdtId(String idtId) {
        this.idtId = idtId;
    }

    public String getIdtName() {
        return idtName;
    }

    public void setIdtName(String idtName) {
        this.idtName = idtName;
    }

    public String getIdtRole() {
        return idtRole;
    }

    public void setIdtRole(String idtRole) {
        this.idtRole = idtRole;
    }

    public String getIdtEmail() {
        return idtEmail;
    }

    public void setIdtEmail(String idtEmail) {
        this.idtEmail = idtEmail;
    }

    public String getIdtAddress() {
        return idtAddress;
    }

    public void setIdtAddress(String idtAddress) {
        this.idtAddress = idtAddress;
    }

    public String getIdtCity() {
        return idtCity;
    }

    public void setIdtCity(String idtCity) {
        this.idtCity = idtCity;
    }

    public String getIdtZipCode() {
        return idtZipCode;
    }

    public void setIdtZipCode(String idtZipCode) {
        this.idtZipCode = idtZipCode;
    }

    public String getIdtFax() {
        return idtFax;
    }

    public void setIdtFax(String idtFax) {
        this.idtFax = idtFax;
    }

    public String getIdtNationality() {
        return idtNationality;
    }

    public void setIdtNationality(String idtNationality) {
        this.idtNationality = idtNationality;
    }

    public String getIdtContactPhone() {
        return idtContactPhone;
    }

    public void setIdtContactPhone(String idtContactPhone) {
        this.idtContactPhone = idtContactPhone;
    }

    public String getIdtAlternatePhone() {
        return idtAlternatePhone;
    }

    public void setIdtAlternatePhone(String idtAlternatePhone) {
        this.idtAlternatePhone = idtAlternatePhone;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }
}
