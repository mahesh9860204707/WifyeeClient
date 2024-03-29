package com.wifyee.greenfields.dairyorder;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.foodorder.GPSTracker;
import com.wifyee.greenfields.foodorder.NetworkConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class JSONBuilder {

    public static JSONObject getAddOrderJSon(Context ctx, ArrayList<PlaceOrderData> orderData,
                                             String totalAmount,String paymentMode,String pinCode,String refId,
                                             String location, String lat, String lng, String complete_add,String discount_amt,
                                             String dateFrom,String dateTo,String perDay,String claimType,String voucherId,
                                             String voucherNo,String tuvId,String wifyeeComm, String distComm, String deliveryAmt,
                                             String gstAmt,String subTotal){
        JSONObject addOrderJson = new JSONObject();
        String todayDate = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(new Date());

        // GPSTracker gps = new GPSTracker(ctx);
        try {
            int size = orderData.size();
            //addOrderJson.put(DairyNetworkConstant.ORDER_ID, MobicashUtils.generateTransactionId());
            addOrderJson.put(DairyNetworkConstant.ORDER_ID, "WO_"+todayDate);
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

            } else if(paymentMode.equals(DairyNetworkConstant.PAYMENT_MODE_ONLINE)){
                addOrderJson.put(DairyNetworkConstant.ORDER_REF_ID,refId);

            } else if (paymentMode.equals(DairyNetworkConstant.PAYMENT_MODE_VOUCHER)){
                addOrderJson.put(DairyNetworkConstant.TUV_ID,tuvId);
            }else if (paymentMode.equals(DairyNetworkConstant.PAYMENT_MODE_CREDIT)){
                addOrderJson.put(DairyNetworkConstant.MER_CREDIT_ID,tuvId);
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
            addOrderJson.put(DairyNetworkConstant.WIFYEE_COMMISION,wifyeeComm);
            addOrderJson.put(DairyNetworkConstant.DIST_COMMISSION,distComm);
            addOrderJson.put(DairyNetworkConstant.DELIVERY_AMT,deliveryAmt);
            addOrderJson.put(DairyNetworkConstant.GST_AMT,gstAmt);
            addOrderJson.put(DairyNetworkConstant.SUB_TOTAL,subTotal);
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

    public static JSONObject getPostAddOrderJson(Context context,String order_id,String gstAmount,String deliveryAmt,String discountAmt,
                                                 String orderAmount,String claimType,String voucherId,String voucherNo,String orderOn,
                                                 String paymentMode,String refId){
        JSONObject json = new JSONObject();
        try {
            json.put(DairyNetworkConstant.ORDER_ID, order_id);
            json.put(DairyNetworkConstant.ORDER_USER_ID, LocalPreferenceUtility.getUserCode(context));
            json.put(DairyNetworkConstant.ORDER_GST_AMT,gstAmount);
            json.put(DairyNetworkConstant.ORDER_DELIVERY_AMT,deliveryAmt);
            json.put(DairyNetworkConstant.ORDER_DISCOUNT_AMT,discountAmt);
            json.put(DairyNetworkConstant.ORD_ORDER_AMT,orderAmount);
            json.put(DairyNetworkConstant.CLAIM_TYPE,claimType);
            json.put(DairyNetworkConstant.VOUCHER_ID,voucherId);
            json.put(DairyNetworkConstant.VOUCHER_NO,voucherNo);
            json.put(DairyNetworkConstant.ORDER_ON,orderOn);
            json.put(DairyNetworkConstant.ORDER_PAYMENT_MODE,paymentMode);
            if(paymentMode.equals(DairyNetworkConstant.PAYMENT_MODE_WALLET)){
                json.put(DairyNetworkConstant.ORDER_PIN,MobicashUtils.md5(LocalPreferenceUtility.getUserPassCode(context)));
                json.put(DairyNetworkConstant.ORDER_MOBILE_NUMBER,LocalPreferenceUtility.getUserMobileNumber(context));
                json.put(DairyNetworkConstant.ORDER_DESCRIPTION,"Medicine");
            }
            if(paymentMode.equals(DairyNetworkConstant.PAYMENT_MODE_ONLINE)){
                json.put(DairyNetworkConstant.ORDER_REF_ID,refId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
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
