package com.wifyee.greenfields.constants;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.wifyee.greenfields.constants.NetworkConstant.MOBICASH_BASE_URL_TESTING;

/**
 * Created by Sumanta on 2/16/2017.
 */

public class WifiConstant
{
    // Constants used for different security types
    public static final String PSK = "PSK";
    public static final String WEP = "WEP";
    public static final String EAP = "EAP";
    public static final String OPEN = "Open";

    public static  ArrayList<String> connectionsReStoreList = new ArrayList<>();

    public static final String  SSID_TYPE_GVK = "GVK Domestic Lounge";
    public static final String  SSID_TYPE_AIRPORT = "FreeWiFi_Ozone@Airport";
    public static final String  SSID_TYPE_AIRPORT_T1 = "T1_FreeWiFi@Airport";
    public static final String  SSID_TYPE_GVK_LOUNGE = "GVK Lounge";
    public static final String  SSID_TYPE_LOYALTY_LOUNGE =  "loyalty lounge";
    public static final String  SSID_FREE_WIFI_OZONE = "FreeWiFi_Ozone";
    public static final String  SSID_OZONE_T1 = "OZONE";
    public static final String  SSID_OZONE_FORTIES = "0_Zone_Free_Wi-Fi_Wifeye";
    public static final String  SSID_OPEN_NETWORKS = "dd-wrt";

    //base url
    public static String BASE_URL = "http://app.ozone-wifi.in/api/checkNetwork.php";


  //  public static String WIFYEE_REG_BASE_URL = "http://45.249.108.75/wifyee-api/rest/Users.php?request=user_register_request";


    //Test Api
    //http://localhost/hotspot-user/rest/Users.php?request=user_register_request

    public static String WIFYEE_REG_BASE_URL = MOBICASH_BASE_URL_TESTING+"/wifyee-api/rest/Users.php?request=user_register_request";
    public static String WIFYEE_CONNECTION_BASE_URL = "http://45.249.108.75/wifyee-api/rest/Users.php?request=connect";



    //registration url
    public static final String REGISTRATION_URL = "http://ozoneapp.veniso.com/api/reg.php";

    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    public static String OZONE_UA = "";
    //network parameter
    public static final String PARAM_USER_NAME = "username=";
    public static final String PARAM_QUESTION_1 = "&q1ans=";
    public static final String PARAM_QUESTION_2 = "&q2ans=";
    public static final String PARAM_MOBILE_NUMBER = "&mobileno=";
    public static final String PARAM_CC = "&cc=+";
    public static final String PARAM_PASSWORD = "&password=";
    public static final String PARAM_USER_MAC = "&usermac=";
    public static final String PARAM_GET_OTP = "&getotp=1";
    public static final String PARAM_OTP = "&otp=";
    public static final String PARAM_ACTION_SIGN_IN = "&action=signin";
    public static final String PARAM_ACTION_SIGN_UP = "&action=signup";

    //xml node
    public static final String NODE_PASSWORD = "Password";
    public static final String NODE_MESSAGE = "Message";
    public static final String NODE_ERROR = "Error";
    public static final String NODE_MAC = "Mac";
    public static final String NODE_CC = "cc";
    public static final String NODE_USER_NAME = "username";
    public static final String AUTH_MODE = "mode";
    public static final String AUTH_MODE_TYPE = "otp";
    public static final String AUTH_MODE_TYPE_REG ="reg";
    public static final String NODE_PLAN_DATA = "PlanData";
    public static final String NODE_ID = "Id";
    public static final String NODE_DESC = "Desc";
    public static final String NODE_COST = "Cost";
    public static final String NODE_FUP =  "Fup";
    public static final String NODE_DOWNLOAD_SPEED = "Dspeed";
    public static final String NODE_UPLOAD_SPEED = "Uspeed";
    public static final String NODE_TIME_LIMIT = "Timelimit";
    public static final String NODE_DAILY_COUNT = "DailyCount";
    public static final String NODE_REPLY ="Reply";
    public static final String NODE_USER_MAC = "usermac";
    public static final String NODE_NAN ="NAN";
    public static final String NODE_PLAN_ID = "planid";
    public static final String NODE_COSTT = "cost";
    public static final String NODE_TIME_LIMITT ="timelimit";
    public static final String NODE_FUPP = "fup";
    public static final String NODE_DSPEED = "dspeed";
    public static final String NODE_USPEED = "uspeed";
    public static final String NODE_DAILY_COUNTT = "dailycount";
    public static final String NODE_PAY_OPTION = "PayOption";
    public static final String NODE_TAG_TYPE = "Type";
    public static final String NODE_URL = "URL";
    public static final String PAYMENT_TYPE_MOBILE = "mobile";
    public static final String PAYMENT_TYPE_MOBILE_M = "Mobile";

    //http method
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    //message
    public static final String SIGNIN_STATUS = "signin Successful";
    public static final String ALREADY_REGISTERED = "already registered";
    public static final String status = "success";
    public static final String STATUS_FAIL ="fail";
    public static final String ERROR_GETTING_LOGIN_URL_MSG = "Error getting Login url";
    public static final String ERROR_GETTING_LOGIN_SMS_MSG = "Error getting sms url";

    public static final int PAGE_FRAG_CONNECTED = 3;
    public static final int PAGE_FRAG_ERROR = 4;
    public static final int PAGE_FRAG_LOGIN = 5;
    public static final int PAGE_FRAG_PLANS = 6;
    public static final int PAGE_FRAG_PROFILE = 7;

    //status
    public static final String CONNECTIVITY_STATUS_CONNECTED = "CONNECTED";

    public static final String isRegisterd_server;
    public static final String password;
    public static final String pin;
    public static final String msisdn;
    public static final String KEY_REG_URL;
    public static final String userName;
    public static final String uGender ;
    public static final String yearOfBirth;
    public static final String userMac;

    public static final String mobileCc="";

    public static final String recovery1;
    public static final String recovery2;


    static {
        pin = "original_pin";
        msisdn = "MSISDN";
        userName = "user_name";
        uGender = "gender";
        yearOfBirth = "yob";
        password = "password";
        recovery1 = "rec1";
        recovery2 = "rec2";
        KEY_REG_URL = "regurl";
        isRegisterd_server = "reg";
        userMac = "user_mac";
    }

    public static final int ERROR_NO_API = 1001;
    public static final int ERROR_SMS_URL_ERROR = 1002;
    public static final int ERROR_SMS_URL_ERROR2 = 1003;
    public static final int ERROR_SMS_SEND_ERROR = 1004;
    public static final int ERROR_SMS_TIMEOUT = 1005;
    public static final int ERROR_FINAL_AUTH_TIMEOUT = 1006;
    public static final int ERROR_EXCEPTION = 1007;
    public static final int ERROR_LOGOUT = 1008;
    public static final int ERROR_REG_FAIL = 1009;


    public static final int CORNETTO_CODE_LOGIN_SUCCESS=5001;
    public static final int CORNETTO_CODE_LOGIN_FAIL_NOSESSION=5002;
    public static final int CORNETTO_CODE_TIMEOUT=5003;
    public static final int CORNETTO_CODE_LOGIN_FAIL_SYSTEMERR=5004;
    public static final int CORNETTO_CODE_LOGOUT_SUCCESS=5005;
    public static final int CORNETTO_CODE_AUTHORIZE_SUCCESS=5006;
    public static final int CORNETTO_CODE_AUTHORIZE_FAIL_NOSESSION=5007;
    public static final int CORNETTO_CODE_AUTHORIZE_FAIL_SYSTEMERR=5008;
    public static final int CORNETTO_CODE_LOGOFF_SUCCESS=5009;

    /*
       Wifyee constant for registration
     */
    public static final String WIFYEE_REG_USER_NAME = "username";
    public static final String WIFYEE_REG_PASSWORD = "password";
    public static final String WIFYEE_REG_IP = "ipAddress";
    public static final String WIFYEE_REG_MAC = "macAddress";
    public static final String WIFYEE_REG_RES_URL = "responseUrl";
    public static final String WIFYEE_DEFAULT_URL = "http://google.com";
    public static final String WIFYEE_REG_USER_FIRST_NAME="fname";
    public static final String WIFYEE_REG_USER_LAST_NAME="lname";
    public static final String WIFYEE_REG_USER_EMAIL="email";
    public static final String WIFYEE_REG_USER_MOBILE_PHONE="mobilePhone";

    public static final String WIFYEE_REG_RES_LONGITUDE = "longitude";
    public static final String WIFYEE_REG_RES_LATITUDE = "latitude";

    /*
       Wifyee constants for Wifyee Hotspot Registration
     */
    public static final String WIFYEE_HOTSPOT_REG_PASSWORD = "req_password=";
    public static final String WIFYEE_HOTSPOT_REG_RE_PASSWORD = "req_repassword=";
    public static final String WIFYEE_HOTSPOT_REG_ACTION = "req_action=";
    public static final String WIFYEE_HOTSPOT_REG_PLANID = "req_planid=";
    public static final String WIFYEE_HOTSPOT_REG_LOCATIONID = "req_locationid=";
    public static final String WIFYEE_HOTSPOT_REG_CUSTOMER_CODE = "req_customercode=";
    public static final String WIFYEE_HOTSPOT_REG_USER_FIRST_NAME="req_firstname=";
    public static final String WIFYEE_HOTSPOT_REG_USER_LAST_NAME="req_lastname=";
    public static final String WIFYEE_HOTSPOT_REG_USER_EMAIL="req_username=";
    public static final String WIFYEE_HOTSPOT_REG_USER_CALLBACK="callback=";
    public static final String WIFYEE_HOTSPOT_LOGIN_USER="username=";
    public static final String WIFYEE_HOTSPOT_LOGIN_USER_RESPONSE="response=";
    public static final String WIFYEE_HOTSPOT_TIME_INTERVAL="req_timeinterval=";
}
