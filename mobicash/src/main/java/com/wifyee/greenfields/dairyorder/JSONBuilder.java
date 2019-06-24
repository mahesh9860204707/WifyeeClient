package com.wifyee.greenfields.dairyorder;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.foodorder.GPSTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class JSONBuilder {

    public static JSONObject getAddOrderJSon(Context ctx, ArrayList<PlaceOrderData> orderData,
                                             String totalAmount,String paymentMode,String pinCode,String refId,
                                             String location, String lat, String lng, String complete_add,String discount_amt,
                                             String dateFrom,String dateTo,String perDay,String claimType,String voucherId,
                                             String voucherNo){
        JSONObject addOrderJson = new JSONObject();
       // GPSTracker gps = new GPSTracker(ctx);
        try {
            int size = orderData.size();
            addOrderJson.put(DairyNetworkConstant.ORDER_ID, MobicashUtils.generateTransactionId());
            addOrderJson.put(DairyNetworkConstant.ORDER_USER_TYPE,"client");
            addOrderJson.put(DairyNetworkConstant.ORDER_USER_ID, LocalPreferenceUtility.getUserCode(ctx));
            addOrderJson.put(DairyNetworkConstant.ORDER_DATE_TIME,MobicashUtils.getCurrentDateAndTime());
            addOrderJson.put(DairyNetworkConstant.ORDER_MERCHANT_ID,orderData.get(0).getMerchantId());
            addOrderJson.put(DairyNetworkConstant.ORDER_PAYMENT_MODE,paymentMode);
            addOrderJson.put(DairyNetworkConstant.ORDER_PRICE,totalAmount);
            if(paymentMode.equals(DairyNetworkConstant.PAYMENT_MODE_WALLET)){
                addOrderJson.put(DairyNetworkConstant.ORDER_PIN,MobicashUtils.md5(pinCode));
                addOrderJson.put(DairyNetworkConstant.ORDER_MOB_NUMBER,LocalPreferenceUtility.getUserMobileNumber(ctx));
                addOrderJson.put(DairyNetworkConstant.ORDER_DESCRIPTION,"test");
            }
            if(paymentMode.equals(DairyNetworkConstant.PAYMENT_MODE_INSTAMOJO)){
                addOrderJson.put(DairyNetworkConstant.ORDER_REF_ID,refId);
            }
//            addOrderJson.put(DairyNetworkConstant.ORDER_LAT,getLatitude(gps));
//            addOrderJson.put(DairyNetworkConstant.ORDER_LONG,getLogitude(gps));
            addOrderJson.put(DairyNetworkConstant.ORDER_LAT,lat);
            addOrderJson.put(DairyNetworkConstant.ORDER_LONG,lng);
            addOrderJson.put(DairyNetworkConstant.ORDER_LOCATION,location);
            addOrderJson.put(DairyNetworkConstant.ORDER_COMPLETE_ADD,complete_add);
            addOrderJson.put(DairyNetworkConstant.ORDER_DISCOUNT_AMT,discount_amt);
            addOrderJson.put(DairyNetworkConstant.SUBSCRIPTION_FROM_DATE,dateFrom);
            addOrderJson.put(DairyNetworkConstant.SUBSCRIPTION_TO_DATE,dateTo);
            addOrderJson.put(DairyNetworkConstant.SUBSCRIPTION_PER_DAY,perDay);
            addOrderJson.put(DairyNetworkConstant.CLAIM_TYPE,claimType);
            addOrderJson.put(DairyNetworkConstant.VOUCHER_ID,voucherId);
            addOrderJson.put(DairyNetworkConstant.VOUCHER_NO,voucherNo);
            JSONArray jsonArr = new JSONArray();
            for(int i=0;i<size;i++){
                JSONObject pnObj = new JSONObject();
                pnObj.put("item_id",orderData.get(i).getItemId() );
                pnObj.put("quantity", orderData.get(i).getQuantity());
                pnObj.put(DairyNetworkConstant.ORDER_AMOUNT, orderData.get(i).getOrderPrice());
                jsonArr.put(pnObj);
            }
            addOrderJson.put(DairyNetworkConstant.ORDER_ITEMS, jsonArr);

        }catch(JSONException e){
            Log.d("addorderex",e.toString());
        }

        return addOrderJson;
    }

    private static String getLogitude(GPSTracker gps) {
        String longitude="";
        if (gps.canGetLocation()) {
            double longitudee = gps.getLongitude();
            return String.valueOf(longitudee);
        }
        return longitude;
    }

    private static String getLatitude(GPSTracker gps) {
        String latt="";
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            return String.valueOf(latitude);
        }
        return latt;
    }


}
