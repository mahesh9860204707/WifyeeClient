package com.wifyee.greenfields.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sumanta on 12/27/16.
 */

public class LocalPreferenceUtility {
    public static final String KEY_CLIENT_MERCHANT_ID_CODE="key_merchant_id";
    public static final String KEY_MOBICASH_LOCAL_PREFERENCE = "key_mobicash_local_database";
    public static final String KEY_CLIENT_MOBILE_NUMBER = "key_client_mobile_number";
    public static final String KEY_CLIENT_CODE = "key_client_code";
    public static final String KEY_CLIENT_PASS_CODE = "key_client_pass_code";
    public static final String WIFI_CONNECT_CONNECTION_TIME = "wifi_connect_connection_time";
    public static final String KEY_FIRST_NAME = "key_first_name";
    public static final String KEY_LAST_NAME = "key_last_name";
    public static final String KEY_EMIL = "key_email";
    public static final String KEY_CLIENT_MERCHANT_NAME_CODE="key_merchant_name";

    public static final String KEY_CUSTOMER_DOB = "key_dob";
    public static final String KEY_CUSTOMER_TITLE = "key_title";
    public static final String KEY_CUSTOMER_IDENTITY_ID = "key_identity_id";
    public static final String KEY_CUSTOMER_ADDRESS = "key_address";
    public static final String KEY_DEVICE_MAC_ADDRESS = "mac_address";
    public static final String KEY_WALLET_BALANCE = "key_wallet_balance";
    public static final String KEY_PIN_CODE = "key_pin_code";
    public static final String KEY_CURRENT_PIN_CODE = "key_current_pin_code";
    public static final String KEY_LATTITUDE = "key_latitude";
    public static final String KEY_LONGITUDE = "key_longitude";
    public static final String KEY_LATITUDE_FOOD = "key_latitude_food";
    public static final String KEY_LONGITUDE_FOOD = "key_longitude_food";
    public static final String KEY_LATITUDE_OTHER = "key_latitude_other";
    public static final String KEY_LONGITUDE_OTHER = "key_longitude_other";


    public static String getMerchantName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_CLIENT_MERCHANT_NAME_CODE, "");
    }


    public static void putMerchantName(Context context, String merchant_name) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_CLIENT_MERCHANT_NAME_CODE, merchant_name);
        editor.commit();
    }

    public static String getPinCode(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_PIN_CODE, "");
    }


    public static void putPinCode(Context context, String pinCode) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_PIN_CODE, pinCode);
        editor.commit();
    }

    public static String getCurrentPincode(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_CURRENT_PIN_CODE, "");
    }


    public static void putCurrentPincode(Context context, String current_pincode) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_CURRENT_PIN_CODE, current_pincode);
        editor.commit();
    }

    public static String getLatitude(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_LATTITUDE, "");
    }


    public static void putLatitude(Context context, String latitude) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_LATTITUDE, latitude);
        editor.commit();
    }

    public static String getLatitudeFood(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_LATITUDE_FOOD, "");
    }


    public static void putLatitudeFood(Context context, String latitude) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_LATITUDE_FOOD, latitude);
        editor.commit();
    }

    public static String getLatitudeOther(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_LATITUDE_OTHER, "");
    }


    public static void putLatitudeOther(Context context, String latitude) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_LATITUDE_OTHER, latitude);
        editor.commit();
        editor.apply();
    }

    public static String getLongitude(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_LONGITUDE, "");
    }


    public static void putLongitude(Context context, String longitude) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_LONGITUDE, longitude);
        editor.commit();
    }


    public static String getLongitudeFood(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_LONGITUDE_FOOD, "");
    }


    public static void putLongitudeFood(Context context, String longitude) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_LONGITUDE_FOOD, longitude);
        editor.commit();
    }


    public static String getLongitudeOther(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_LONGITUDE_OTHER, "");
    }


    public static void putLongitudeOther(Context context, String longitude) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_LONGITUDE_OTHER, longitude);
        editor.commit();
    }


    public static String getDeviceMacID(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_DEVICE_MAC_ADDRESS, "");
    }


    public static void putDeviceMacID(Context context, String deviceId) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_DEVICE_MAC_ADDRESS, deviceId);
        editor.commit();
    }
   //Get Client Dob
    public static String getClientDob(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_CUSTOMER_DOB, "");
    }

   //Put Client Dob
    public static void putClientDob(Context context, String client_dob) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_CUSTOMER_DOB, client_dob);
        editor.commit();
    }

    //Get Client Address
    public static String getClientAddress(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_CUSTOMER_ADDRESS, "");
    }

    //Put Client Address
    public static void putClientAddress(Context context, String client_address) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_CUSTOMER_ADDRESS, client_address);
        editor.commit();
    }

    //Get Client Title
    public static String getClientTitle(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_CUSTOMER_TITLE, "");
    }

    //Put Client Title
    public static void putClientTitle(Context context, String client_title) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_CUSTOMER_TITLE, client_title);
        editor.commit();
    }

    //Get Client Title
    public static String getClientIdentityID(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_CUSTOMER_IDENTITY_ID, "");
    }

    //Put Client Title
    public static void putClientClientIdentityID(Context context, String client_identity_id) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_CUSTOMER_IDENTITY_ID, client_identity_id);
        editor.commit();
    }

    /**
     * put client mobile number on local preference after successful login.
     *
     * @param context
     * @param mobileNumber
     */
    public static void putUserMobileNumber(Context context, String mobileNumber) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_CLIENT_MOBILE_NUMBER, mobileNumber);
        editor.commit();
    }

    /**
     * put client email on local preference after successful registration.
     *
     * @param context
     * @param email
     */
    public static void putUserEmail(Context context, String email) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_EMIL, email);
        editor.commit();
    }

    /**
     * return client email.
     *
     * @param context
     * @return
     */
    public static String getUserEmail(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_EMIL, "");
    }

    /**
     * put client first name on local preference after successful registration.
     *
     * @param context
     * @param firstName
     */
    public static void putUserFirstName(Context context, String firstName) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_FIRST_NAME, firstName);
        editor.commit();
    }

    /**
     * return client first name.
     *
     * @param context
     * @return
     */
    public static String getUserFirstsName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_FIRST_NAME, "");
    }

    /**
     * put client last name on local preference after successful registration.
     *
     * @param context
     * @param firstName
     */
    public static void putUserLastName(Context context, String firstName) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_LAST_NAME, firstName);
        editor.commit();
    }

    /**
     * return client last name.
     *
     * @param context
     * @return
     */
    public static String getUserLastName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_LAST_NAME, "");
    }


    /**
     * return client mobile number.
     *
     * @param context
     * @return
     */
    public static String getUserMobileNumber(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_CLIENT_MOBILE_NUMBER, "");
    }

    /**
     * put client code on preference which is return from server after successful login
     * for future use.
     *
     * @param context
     * @param code
     */
    public static void putUserCode(Context context, String code) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_CLIENT_CODE, code);
        editor.commit();
    }

    /**
     * return client code
     *
     * @param context
     * @return
     */
    public static String getUserCode(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_CLIENT_CODE, "");
    }

    /**
     * put client pass code on preference which is return from server after successful login
     * for future use.
     *
     * @param context
     * @param passCode
     */
    public static void putUserPassCode(Context context, String passCode) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_CLIENT_PASS_CODE, passCode);
        editor.commit();
    }

    /**
     * return client pass code
     *
     * @param context
     * @return
     */
    public static String getUserPassCode(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_CLIENT_PASS_CODE, "");
    }

    public static void putWifiConnectionTime(Context context, Long time) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(WIFI_CONNECT_CONNECTION_TIME, time);
        editor.commit();
    }

    /**
     * put wallet balance
     */

    public static void saveWalletBalance(Context context,String balance){
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_WALLET_BALANCE, balance);
        editor.commit();
    }

    /**
     * get wallet balance
     */

    public static String getWalletBalance(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_WALLET_BALANCE, "");
    }

    /**
     * gate connection time interval
     */

    public static Long getWifiConnectionTime(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getLong(WIFI_CONNECT_CONNECTION_TIME, 0);
    }


    public static void putMerchantId(Context context, String merchant_id) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_CLIENT_MERCHANT_ID_CODE, merchant_id);
        editor.commit();
    }
    public static String getMerchantId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, android.content.Context.MODE_PRIVATE);
        return preferences.getString(KEY_CLIENT_MERCHANT_ID_CODE, "");
    }

    public static void clearSession(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(KEY_MOBICASH_LOCAL_PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
