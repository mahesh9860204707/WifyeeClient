package com.wifyee.greenfields.dairyorder;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONParser {

    public static ArrayList<DairyMerchantListModel> getDairyMerchantList(JSONObject response){
        ArrayList<DairyMerchantListModel> item = new ArrayList<>() ;

        try {
            if (response.has(DairyNetworkConstant.DATA)) {

                JSONArray itemArray = response.getJSONArray(DairyNetworkConstant.DATA);
                int length = itemArray.length();
                for(int i=0;i<length;i++){
                    DairyMerchantListModel list = new DairyMerchantListModel();
                    JSONObject dataObject = itemArray.getJSONObject(i);
                    if(dataObject.has(DairyNetworkConstant.MER_ID)){
                        list.setId(dataObject.getString(DairyNetworkConstant.MER_ID));
                    }
                    if(dataObject.has(DairyNetworkConstant.MER_NAME)){
                        list.setName(dataObject.getString(DairyNetworkConstant.MER_NAME));
                    }
                    if(dataObject.has(DairyNetworkConstant.MER_COMPANY)){
                        list.setCompany(dataObject.getString(DairyNetworkConstant.MER_COMPANY));
                    }
                    if(dataObject.has(DairyNetworkConstant.MER_TYPE)){
                        list.setType(dataObject.getString(DairyNetworkConstant.MER_TYPE));
                    }
                    if(dataObject.has(DairyNetworkConstant.MER_IMAGE)){
                        list.setImage(dataObject.getString(DairyNetworkConstant.MER_IMAGE));
                    }
                    if(dataObject.has(DairyNetworkConstant.MER_IDT_ID)){
                        list.setIdtId(dataObject.getString(DairyNetworkConstant.MER_IDT_ID));
                    }
                    if(dataObject.has(DairyNetworkConstant.MER_IDT_NAME)){
                        list.setIdtName(dataObject.getString(DairyNetworkConstant.MER_IDT_NAME));
                    }
                    if(dataObject.has(DairyNetworkConstant.MER_IDT_ROLE)){
                        list.setIdtRole(dataObject.getString(DairyNetworkConstant.MER_IDT_ROLE));
                    }
                    if(dataObject.has(DairyNetworkConstant.MER_IDT_EMAIL)){
                        list.setIdtEmail(dataObject.getString(DairyNetworkConstant.MER_IDT_EMAIL));
                    }
                    if(dataObject.has(DairyNetworkConstant.MER_IDT_ADDRESS)){
                        list.setIdtAddress(dataObject.getString(DairyNetworkConstant.MER_IDT_ADDRESS));
                    }
                    if(dataObject.has(DairyNetworkConstant.MER_IDT_CITY)){
                        list.setIdtCity(dataObject.getString(DairyNetworkConstant.MER_IDT_CITY));
                    }
                    if(dataObject.has(DairyNetworkConstant.MER_IDT_ZIP_CODE)){
                        list.setIdtZipCode(dataObject.getString(DairyNetworkConstant.MER_IDT_ZIP_CODE));
                    }
                    if(dataObject.has(DairyNetworkConstant.MER_IDT_FAX)){
                        list.setIdtFax(dataObject.getString(DairyNetworkConstant.MER_IDT_FAX));
                    }
                    if(dataObject.has(DairyNetworkConstant.MER_IDT_NATIONALITY)){
                        list.setIdtNationality(dataObject.getString(DairyNetworkConstant.MER_IDT_NATIONALITY));
                    }
                    if(dataObject.has(DairyNetworkConstant.MER_IDT_PHONE)){
                        list.setIdtContactPhone(dataObject.getString(DairyNetworkConstant.MER_IDT_PHONE));
                    }
                    if(dataObject.has(DairyNetworkConstant.MER_IDT_ALT_PHONE)){
                        list.setIdtAlternatePhone(dataObject.getString(DairyNetworkConstant.MER_IDT_ALT_PHONE));
                    }
                    if(dataObject.has(DairyNetworkConstant.MER_CURRENT_STATUS)){
                        list.setCurrentStatus(dataObject.getString(DairyNetworkConstant.MER_CURRENT_STATUS));
                    }

                    item.add(list);
                }

            }
        }catch (JSONException e){}

        return item;
    }


    /**
     * dairy image item list
     * @param response
     * @return
     */
    public static ArrayList<DairyProductListItem> getDairyListItem(JSONObject response){
        ArrayList<DairyProductListItem> item = new ArrayList<>() ;

        try {
            if (response.has(DairyNetworkConstant.DATA)) {
                JSONArray itemArray = response.getJSONArray(DairyNetworkConstant.DATA);
                int length = itemArray.length();
                for(int i=0;i<length;i++){
                    DairyProductListItem list = new DairyProductListItem();
                    JSONObject dataObject = itemArray.getJSONObject(i);
                    if(dataObject.has(DairyNetworkConstant.MERCHANT_ID)){
                        list.setMerchantId(dataObject.getString(DairyNetworkConstant.MERCHANT_ID));
                    }
                    if(dataObject.has(DairyNetworkConstant.ITEM_ID)){
                        list.setItemId(dataObject.getString(DairyNetworkConstant.ITEM_ID));
                    }
                    if(dataObject.has(DairyNetworkConstant.ITEM_NAME)){
                        list.setItemName(dataObject.getString(DairyNetworkConstant.ITEM_NAME));
                    }
                    if(dataObject.has(DairyNetworkConstant.ITEM_PRICE)){
                        list.setItemPrice(dataObject.getString(DairyNetworkConstant.ITEM_PRICE));
                    }
                    if(dataObject.has(DairyNetworkConstant.ITEM_TYPE)){
                        list.setItemType(dataObject.getString(DairyNetworkConstant.ITEM_TYPE));
                    }
                    if(dataObject.has(DairyNetworkConstant.ITEM_QUALITY)){
                        list.setItemQuality(dataObject.getString(DairyNetworkConstant.ITEM_QUALITY));
                    }
                    if(dataObject.has(DairyNetworkConstant.ITEM_DISCOUNT_TYPE)){
                        list.setItemDiscountType(dataObject.getString(DairyNetworkConstant.ITEM_DISCOUNT_TYPE));
                    }
                    if(dataObject.has(DairyNetworkConstant.ITEM_DISCOUNT_AMT)){
                        list.setItemDiscount(dataObject.getString(DairyNetworkConstant.ITEM_DISCOUNT_AMT));
                    }
                    if(dataObject.has(DairyNetworkConstant.ITEM_MOB_COMMISSION)){
                        list.setItemMobCommission(dataObject.getString(DairyNetworkConstant.ITEM_MOB_COMMISSION));
                    }
                    if(dataObject.has(DairyNetworkConstant.ITEM_TYPE_COMMISSION)){
                        list.setItemCommissionType(dataObject.getString(DairyNetworkConstant.ITEM_TYPE_COMMISSION));
                    }
                    if(dataObject.has(DairyNetworkConstant.ITEM_QUENTITY)){
                        list.setItemQuantity(dataObject.getString(DairyNetworkConstant.ITEM_QUENTITY));
                    }
                    if(dataObject.has(DairyNetworkConstant.ITEM_STATUS)){
                        list.setItemStatus(dataObject.getString(DairyNetworkConstant.ITEM_STATUS));
                    }
                    if(dataObject.has(DairyNetworkConstant.ITEM_CREATE_DATE)){
                        list.setItemCreatedDate(dataObject.getString(DairyNetworkConstant.ITEM_CREATE_DATE));
                    }
                    if(dataObject.has(DairyNetworkConstant.ITEM_UPDATED_DATE)){
                        list.setItemUpdatedDate(dataObject.getString(DairyNetworkConstant.ITEM_UPDATED_DATE));
                    }
                    if(dataObject.has(DairyNetworkConstant.ITEM_UNIT)){
                        list.setItemUnit(dataObject.getString(DairyNetworkConstant.ITEM_UNIT));
                    }
                    if(dataObject.has(DairyNetworkConstant.ITEM_UNIT_QTY)){
                        list.setItemUnitQty(dataObject.getString(DairyNetworkConstant.ITEM_UNIT_QTY));
                    }
                    if(dataObject.has(DairyNetworkConstant.MER_CURRENT_STATUS)){
                        list.setCurrentStatus(dataObject.getString(DairyNetworkConstant.MER_CURRENT_STATUS));
                    }
                    if(dataObject.has(DairyNetworkConstant.ITEM_DIST_COMMISSION)){
                        list.setDistCommission(dataObject.getString(DairyNetworkConstant.ITEM_DIST_COMMISSION));
                    }

                    if(dataObject.has(DairyNetworkConstant.ITEM_IMAGE_OBJ)){
                        JSONArray imageArray = dataObject.getJSONArray(DairyNetworkConstant.ITEM_IMAGE_OBJ);
                        int size = imageArray.length();
                        for(int j=0;j<size;j++){
                            JSONObject imageObject = imageArray.getJSONObject(j);
                            if(imageObject.has(DairyNetworkConstant.ITEM_IMAGE_ID)){
                                list.setItemImageId(imageObject.getString(DairyNetworkConstant.ITEM_IMAGE_ID));
                            }
                            if(imageObject.has(DairyNetworkConstant.ITEM_IMAGE_PATH)){
                                list.setItemImagePath(imageObject.getString(DairyNetworkConstant.ITEM_IMAGE_PATH));
                            }
                        }
                    }
                    item.add(list);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return item;
    }

    /**
     * main category product listing
     */
    public static ArrayList<MainCategoryModel> getMainCategoryListItem(JSONArray response) {
        ArrayList<MainCategoryModel> item = new ArrayList<>();
        try {
            int size = response.length();
            for (int index = 0; index < size; index++) {
                JSONObject json_data = response.getJSONObject(index);
                MainCategoryModel model = new MainCategoryModel();
                if (json_data.has(DairyNetworkConstant.ID)) {
                    model.setCatId(json_data.getString(DairyNetworkConstant.ID));
                }
                if (json_data.has(DairyNetworkConstant.MERCHANTTYPE)) {
                    model.setCatName(json_data.getString(DairyNetworkConstant.MERCHANTTYPE));
                }
                item.add(model);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }
}
