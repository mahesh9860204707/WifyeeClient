package com.wifyee.greenfields.constants;

/**
 * Created by sumanta on 12/14/16.
 */

public class NetworkConstant {
    public static final int LOCATION_INTERVAL = 10000;
    public static final int FASTEST_LOCATION_INTERVAL = 5000;
    public static final String STATUS_USER_OTP_FAIL ="user_otp_fail" ;
    public static final String STATUS_USER_OTP_SUCCESS = "user_otp_success";
    public static final String STATUS_USER_CONFIRMATION_OTP_FAIL ="user_Confirmation_otp_fail" ;
    public static final String STATUS_USER_CONFIRMATION_OTP_SUCCESS = "user_Confirmation_otp_success";
    public static final String VERIFY_MOBILE_NUMBER_SUCCESS ="verify_phone_number_success";
    public static final String VERIFY_MOBILE_NUMBER_FAILURE = "verify_phone_number_failure";
    /**
     * action state for local broadcast -> SignIn
     */
    public static final String STATUS_USER_LOGIN_SUCCESS = "user_login_success";
    public static final String STATUS_USER_LOGIN_FAIL = "user_login_fail";

    /**
     * action state for local broadcast -> SignIn
     */
    public static final String STATUS_SERVER_USAGE_DATA_SUCCESS = "server_data_success";
    public static final String STATUS_SERVER_USAGE_DATA_FAIL = "server_data_fail";

    /**
     * action state for local broadcast -> SignIn
     */
    public static final String STATUS_SEND_MONEY_SUCCESS = "send_money_success";
    public static final String STATUS_SEND_MONEY_FAIL = "send_money_fail";

    /**
     * action state for local broadcast -> SignIn
     */
    public static final String STATUS_SEND_MONEY_CLIENT_WALLET_SUCCESS = "send_client_to_wallet_money_success";
    public static final String STATUS_SEND_CLIENT_WALLET_MONEY_FAIL = "send_client_to_wallet_money_fail";

    /**
     * action state for local broadcast -> Split Money
     */
    public static final String STATUS_USER_SPLIT_SUCCESS = "user_split_success";
    public static final String STATUS_USER_SPLIT_FAIL = "user_split_fail";

    /**
     * action state for local broadcast -> WI FI Payment Success
     */
    public static final String STATUS_USER_WI_FI_PAYMENT_SUCCESS = "user_payment_success";
    public static final String STATUS_USER_WI_FI_PAYMENT_FAIL = "user_payment_fail";

    /**
     * action state for local broadcast -> WI FI Payment Success
     */
    public static final String STATUS_USER_INSTA_MOJO_PAYMENT_SUCCESS = "user_insta_mojo_payment_success";
    public static final String STATUS_USER_INSTA_MOJO_PAYMENT_FAIL = "user_insta_mojo_payment_fail";

    /**
     * action state for local broadcast -> Add Money Wallet Payment Success
     */
    public static final String STATUS_ADD_MONEY_WALLET_SUCCESS = "add_money_success";
    public static final String STATUS_ADD_MONEY_WALLET_FAIL = "add_money_fail";
    /**
     * action state for local broadcast -> DeductMoney Wallet Payment Success
     */
    public static final String STATUS_DEDUCTMONEY_WALLET_SUCCESS = "deduct_money_success";
    public static final String STATUS_DEDUCT_MONEY_WALLET_FAIL = "deduct_money_fail";

    /**
     * action state for local broadcast - Client Reset Passcode
     */
    public static final String STATUS_MERCHANT_PAYMENT_SUCCESS = "merchant_payment_success";
    public static final String STATUS_MERCHANT_PAYMENT_FAIL = "merchant_payment_fail";

    /**
     * action state for local broadcast - SignUp
     */
    public static final String STATUS_USER_SIGNUP_SUCCESS = "user_signup_success";
    public static final String STATUS_USER_SIGNUP_FAIL = "user_signup_fail";

    public static final String STATUS_MAC_ADDRESS_UPDATE_SUCCESS = "mac_address_update_success";
    public static final String STATUS_MAC_ADDRESS_UPDATE_FAIL = "mac_address_update_fail";


    public static final String TYPE_FOODVEG = "veg";
    public static final String TYPE_FOODNONVEG = "non-veg";
    /**
     * action state for WiFyee HotspotRegistarion
     */
    public static final String STATUS_USER_WIFYEE_HOTSPOT_SIGNUP_SUCCESS = "user_signup_hotspot_success";
    public static final String STATUS_USER_WIFYEE_HOTSPOT_SIGNUP_FAIL = "user_signup_hotspot_fail";

    /**
     * action state for local broadcast - SignUp
     */
    public static final String STATUS_USER_WIFYEE_CONNECTION_SUCCESS = "user_wifyee_conn_success";
    public static final String STATUS_USER_WIFYEE_CONNECTION_FAIL = "user_wifyee_conn_fail";

    /**
     * action state for local broadcast - Client To Client Money Transfer
     */
    public static final String STATUS_USER_CLIENT_TO_CLIENT_MONEY_TRANSFER_SUCCESS = "user_client_to_client_money_transfer_success";
    public static final String STATUS_USER_CLIENT_TO_CLIENT_MONEY_TRANSFER_FAIL = "user_client_to_client_money_transfer_fail";

    /**
     * action state for local broadcast - Client Status
     */
    public static final String STATUS_USER_CLIENT_STATUS_SUCCESS = "user_client_status_success";
    public static final String STATUS_USER_CLIENT_STATUS_FAIL = "user_client_status_fail";
    /**
     * action state for local broadcast - Client Balance
     */
    public static final String STATUS_USER_CLIENT_BALANCE_SUCCESS = "user_client_balance_success";
    public static final String STATUS_USER_CLIENT_BALANCE_FAIL = "user_client_balance_fail";

    /**
     * action state for local broadcast - Airtime
     */
    public static final String STATUS_USER_CLIENT_AIRTIME_SUCCESS = "user_client_airtime_success";
    public static final String STATUS_USER_CLIENT_AIRTIME_FAIL = "user_client_airtime_fail";

    /**
     * action state for local broadcast - Operator List
     */
    public static final String STATUS_OPERATOR_LIST_SUCCESS = "operator_list_success";
    public static final String STATUS_OPERATOR_LIST_FAIL = "operator_list_fail";

    /**
     * action state for local broadcast - Client Log
     */
    public static final String STATUS_USER_CLIENT_LOG_SUCCESS = "user_client_log_success";
    public static final String STATUS_USER_CLIENT_LOG_FAIL = "user_client_log_fail";

    /**
     * action state for local broadcast -  Client Profile Update
     */
    public static final String STATUS_CLIENT_PROFILE_UPDATE_SUCCESS = "client_profile_update_success";
    public static final String STATUS_CLIENT_PROFILE_UPDATE__FAIL = "client_profile_update_fail";

    /**
     * action state for local broadcast -  Get Client Profile
     */
    public static final String STATUS_GET_CLIENT_PROFILE_SUCCESS = "get_client_profile_success";
    public static final String STATUS_GET_CLIENT_PROFILE_FAIL = "get_client_profile_fail";

    /**
     * action state for local broadcast - Get Client Profile Info
     */
    public static final String STATUS_GET_CLIENT_PROFILE_INFO_SUCCESS = "get_client_profile_info_success";
    public static final String STATUS_GET_CLIENT_PROFILE_INFO_FAIL = "get_client_profile_info_fail";

    /**
     * action state for local broadcast - Client Bank Transfer
     */
    public static final String STATUS_CLIENT_BANK_TRANSFER_SUCCESS = "client_bank_transfer_success";
    public static final String STATUS_CLIENT_BANK_TRANSFER_FAIl = "client_bank_transfer_fail";

    /**
     * action state for local broadcast - Client Send Passcode
     */
    public static final String STATUS_CLIENT_SEND_PASSCODE_SUCCESS = "client_send_passcode_success";
    public static final String STATUS_CLIENT_SEND_PASSCODE_FAIL = "client_send_passcode_fail";

    /**
     * action state for local broadcast - Client Reset Passcode
     */
    public static final String STATUS_CLIENT_RESET_PASSCODE_SUCCESS = "client_reset_passcode_success";
    public static final String STATUS_CLIENT_RESET_PASSCODE_FAIL = "client_reset_passcode_fail";

    /**
     * action state for local broadcast - Upload Client Profile Picture
     */
    public static final String STATUS_UPLOAD_CLIENT_PROFILE_PICTURE_SUCCESS = "upload_client_profile_picture_success";
    public static final String STATUS_UPLOAD_CLIENT_PROFILE_PICTURE_FAIL = "upload_client_profile_picture_fail";

    /**
     * action state for local broadcast - Upload Medicine
     */
    public static final String STATUS_UPLOAD_MEDICINE_ORDER_SUCCESS = "upload_medicine_order_success";
    public static final String STATUS_UPLOAD_MEDICINE_ORDER_FAIL = "upload_medicine_order_fail";

    /**
     * action state for local broadcast - Upload Client Profile Picture
     */
    public static final String STATUS_UPLOAD_ADD_KYC_SUCCESS = "upload_client_kyc_success";
    public static final String STATUS_UPLOAD_CLIENT_ADD_KYC_FAIL = "upload_client_kyc_fail";
    /**
     * action state for local broadcast - Client PayU Payment Gateway.
     */
    public static final String STATUS_PAYU_PAYMENT_GATEWAY_SUCCESS = "client_payu_payment_gateway_success";
    public static final String STATUS_PAYU_PAYMENT_GATEWAY_FAIL = "client_payu_payment_gateway_fail";


    public static final String ACTION_SHOW_ID_SUCCESS_PAID = "payment_success";
    public static final String ACTION_BACK_TO_WALLET = "payment_back_wallet";

    public static final String BANK_LIST_SUCCESS ="bank_list_success";
    public static final String BANK_LIST_FAILURE = "bank_list_failure";

    public static final String CITY_LIST_SUCCESS ="city_list_success";
    public static final String CITY_LIST_FAILURE = "city_list_failure";

    public static final String STATE_LIST_SUCCESS ="state_list_success";
    public static final String STATE_LIST_FAILURE = "state_list_failure";

    public static final String ADD_BENEFICIARY_SUCCESS ="add_beneficiary_success";
    public static final String ADD_BENEFICIARY_FAILURE = "add_beneficiary_failure";

    public static final String PLAN_LIST_SUCCESS ="plan_list_success";
    public static final String PLAN_LIST_FAILURE = "plan_list_failure";

    public static final String OFFERS_LIST_SUCCESS ="offers_list_success";
    public static final String OFFERS_LIST_FAILURE = "offers_list_failure";
    /**
     * param extra data
     */
    public static final String EXTRA_DATA = "extra_data";
    public static final String EXTRA_DATA_WALLET = "extra_data_wallet";
    public static final String EXTRA_DATA1 = "extra1_data";
    public static final String EXTRA_DATA_CATEGORY_ID = "extra1_data_category_id";
    public static final String EXTRA_DATA_SUB_CLASSIFICATION_ID = "extra1_sub_classification_id";
    public static final String EXTRA_DATA_LONGITUDE = "extra1_data_longitude";
    public static final String EXTRA_DATA_LATITUDE = "extra1_data_latitude";
    public static final String MERCHANT_ID = "merchant_id";
    public static final String MOBICASH_IP_ADDRESS = "45.249.108.75";
     //public static final String MOBICASH_IP_ADDRESS = "103.241.147.70";
    public static final String MOBICASH_BASE_URL_TESTING = "http://" + MOBICASH_IP_ADDRESS;
    public static final String LOGOUT_API = MOBICASH_BASE_URL_TESTING+"/api/clientlogoutapi.php?request=clientLogout";
    public static final String CHECK_VERSION = MOBICASH_BASE_URL_TESTING +"/api/checkVersion.php?name=client";
    public static final String PLANS_URL = MOBICASH_BASE_URL_TESTING+"/wifyee-api/rest/Users.php?request=get_hotspot_user_plans";
    public static final String OFFERS_URL = MOBICASH_BASE_URL_TESTING+"/api/merchantoffersapi.php?request=merchantOffers";
    //http://wifyee.wifi-soft.com/wifilan/index
    /*public static final String WIFYEE_HOTSPOT_REGISTRATION ="https://w1.wifi-soft.com/wifilan/api/create_freeuser.php?";
    public static final String WIFYEE_HOTSPOT_FETCH_USER ="https://w1.wifi-soft.com/wifilan/api/wifyee/fetch_userusage.php?";
    public static final String WIFYEE_HOTSPOT_UPDATE_USER_POLICY ="http:/w1.wifi-soft.com/wifilan/api/wifyee/update_userpolicy.php?";*/
    public static final String WIFYEE_HOTSPOT_REGISTRATION ="http://wifyee.wifi-soft.com/wifilan/api/create_freeuser.php";
    public static final String WIFYEE_HOTSPOT_FETCH_USER ="http://wifyee.wifi-soft.com/wifilan/api/wifyee/fetch_userusage.php";
    public static final String USER_DATA_HOTSPOT_FETCH_USER ="http://45.249.108.75/wifyee-api/rest/WifyeeApiToWifiSoft.php?request=update_user_data_usages";
    public static final String WIFYEE_HOTSPOT_UPDATE_USER_POLICY ="http://wifyee.wifi-soft.com/wifilan/api/wifyee/update_userpolicy.php";
    public static final String LOGIN_STATUS ="http://10.0.0.1:3990/json/status";
    public static final String LOGON_STATUS_ON_WI_FI ="http://10.0.0.1:3990/logon?";
    public static final String LOGOUT_API_WIFYEE ="http://10.0.0.1:3990/logoff";
    public static final String GET_ISP_LOGO ="http://45.249.108.75/api/merchantprofileapi.php?request=getISPImage";
    public static final String LOGON_APP_USER_STATUS_ON_WI_FI ="http://wifyee.wifi-soft.com/wifilan/api/app2.6/user";
    public static final String PLAN_UPDATE_ON_WIFYEE_SERVER ="http://45.249.108.75/wifyee-api/rest/Users.php?request=update_plan_data";
    public static final String PAY_TO_MERCHANT_MONEY ="http://45.249.108.75/api/merchantCreditapi.php?request=merchantCredit";
    public static final String ADD_KYC_TO_SERVER ="https://uat2yesmoney.easypay.co.in/web-mpm/rest/uploadCustomerKycDocumentData";
    public static final String INSTA_MOJO_PAYMENT_GATEWAY_REQUEST ="http://wifyeepay.com/payments/rest/WifyeePayments.php?request=payment_request";

    //Food Order End Point
    public static final String PARAM_FOODODER_LIST_REQUEST_MODEL = "/api/menuitemsapi.php?request=getmenuitemsbycategory";
    public static final String PARAM_FOODORDER_BYPARENTSLUG_LIST_REQUEST_MODEL = "/api/menuitemsapi.php?request=getsubcategorybyparentslug";
    public static final String PARAM_GSTONFOOD_LIST_REQUEST_MODEL = "/api/gstapi.php?request=getgstvalue";
   // public static final String PARAM_FOODORDER_BYMERCHANT_LIST_REQUEST_MODEL = "/api/menuitemsapi.php?request=getmenuitemsbylatlong";
    public static final String PARAM_FOODORDER_BYMERCHANT_LIST_REQUEST_MODEL = "/api/menuitemsapi.php?request=getfoodmerchants";
    public static final String PARAM_FOODORDERLIST_BYMERCHANT_LIST_REQUEST_MODEL = "/api/food_order.php?request=addorder";
    public static final String PARAM_FOODORDER_STATUS_LIST_REQUEST_MODEL = "/api/food_order.php?request=getfoodorders";
    public static final String PARAM_MYORDER = "/api/food_order.php?request=getorders";
    public static final String PARAM_ALLFOODODER_LIST_REQUEST_MODEL = "/api/menuitemsapi.php?request=getmenuitemsbymerchantid";
    public static final String PARAM_FOODORDER_STATUS_DETAILS_LIST_REQUEST_MODEL="/api/food_order.php?request=getfoodorderdetails";
    public static final String WIFYEE_HOTSPOT_VERFIFY_MOBILE_USER ="http://wifyee.wifi-soft.com/wifilan/api/app2.6/user";
    public static final String PARAM_MY_CASHBACK = "/api/clientcashbackapi.php?request=CashbackList&client_id=";
    public static final String PARAM_GET_VOUCHERS = "/api/voucherapi.php?request=getVochers";
    public static final String PARAM_MERCHANT_CREDIT_CLIENT = "/api/merchantCreditsClientApi.php?request=getCreditedMerchants";
    public static final String PARAM_CLIENT_CREDIT_BY_MERCHANT_ID= "/api/merchantCreditsClientApi.php?request=getClientsCreditByMerchantId";
    public static final String PARAM_VOUCHER_LIST_BY_PINCODE= "/api/voucherapi.php?request=getVoucherListByPincode";
    public static final String PARAM_GET_CLIENT_VOUCHER_LIST = "/api/voucherapi.php?request=getClientsVoucher";
    public static final String PARAM_GET_FOOD_ITEM_BY_VOUCHER_ID = "/api/voucherapi.php?request=getVoucherItems";
    public static final String PARAM_GET_OTHER_MERCHANT_LIST = "/api/merchantapi.php?request=getMerTypeListByPincode";

    // API end point
    public static final String USER_REQUEST_MERCHANT_CREDIT="http://wifyeepay.com/api/RequestMoneyApi.php?request=requestMoney";
    public static final String USER_LOGIN_END_POINT = "/api/clientloginapi.php?request=clientLogin";
    public static final String USER_SPLIT_END_POINT = "/api/split_money_api.php?request=sendInvoiceToSplitPayments";
    public static final String USER_SIGNUP_END_POINT = "/api/clientenrollmentapi.php?request=clientEnrolment";
    public static final String USER_CLIENT_TO_CLIENT_MONEY_TRANSFER_END_POINT = "/api/clienttoclientmoneytransferapi.php?request=clientWalletToWallet";
    public static final String USER_CLIENT_STATUS_END_POINT = "/api/clientaccountstatusapi.php?request=clientAccountStatusCheck";
    public static final String USER_CLIENT_BALANCE_END_POINT = "/api/clientcheckbalanceapi.php?request=clientBalanceCheck";
    public static final String USER_CLIENT_AIRTIME_END_POINT = "/api/clientairtimeapi.php?request=clientAirtime";
    public static final String PARAM_OPERATOR_LIST_REQUEST_MODEL = "/api/getoperatorslist.php?request=getOperatorsList";
    public static final String USER_CLIENT_LOG_END_POINT = "/api/clientlogapi.php?request=clientLog";
    public static final String USER_CLIENT_PROFILE_UPDATE_END_POINT = "/api/clientprofileapi.php?request=clientProfile";
    public static final String USER_GET_CLIENT_PROFILE_END_POINT = "/api/clientprofileapi.php?request=clientProfile";
    public static final String USER_GET_CLIENT_PROFILE_INFO_END_POINT = "/api/clientprofileinfoapi.php?request=clientProfileInfo";
    public static final String USER_CLIENT_BANK_TRANSFER_END_POINT = "/api/clientbanktransferapi.php?request=clientBankTransfer";
    public static final String USER_CLIENT_SEND_PASSCODE_END_POINT = "/api/clientsendpasscodeapi.php?request=clientSendPasscode";
    public static final String USER_CLIENT_ADD_BENEFICIARY_END_POINT ="/api/beneficiaryapi.php?request=createBeneficiary";
    public static final String USER_CLIENT_RESET_PASSCODE_END_POINT = "/api/clientresetpasscodeapi.php?request=clientResetPasscode";
    public static final String USER_CLIENT_UPDATE_CLIENT_PROFILE_PICTURE_END_POINT = "/api/clientprofilepictureapi.php?request=clientProfilePicture";
    public static final String USER_MEDICINE_UPLOAD_END_POINT ="http://wifyeepay.com/api/usermedicineapi.php?request=medicineOrder";
    public static final String USER_MAC_ADDRESS_END_POINT ="http://wifyeepay.com/api/updateMACAddress.php?request=updateMAC";

    public static final String USER_CLIENT_PAYU_PAYMENT_GATEWAY_END_POINT = "/payments/rest/PaymentRequest.php?request=payRequest";
    public static final String USER_CLIENT_BANK_LIST_END_POINT ="/payments/rest/PaymentRequest.php?request=getBankList";
    public static final String USER_CLIENT_CITY_LIST_END_POINT ="/api/busbookingapi.php?request=busBooking&requestType=1";
    public static final String BENEFICIARY_CLIENT_CITY_LIST_END_POINT ="/api/beneficiaryapi.php?request=getStateList";
    public static final String USER_CLIENT_BANK_TRANSFER ="/api/banktransferapi.php?request=bankTransfer";
    public static final String USER_CLIENT_NEFT_IMPS_BANK_TRANSFER="http://45.249.108.74/api/banktransferapi.php?request=bankTransfer";
    public static final String USER_CLIENT_PAYU_PAYMENT_GATEWAY_PAYMENT_RESPONSE_URL = MOBICASH_BASE_URL_TESTING+"/app_response.php";
    public static final String USER_CLIENT_PAYU_PAYMENT_UNWALLET_GATEWAY_PAYMENT_RESPONSE_URL = MOBICASH_BASE_URL_TESTING+"/payments/app_response.php";
    public static final String WI_FI_PAYMENT_END_POINT = "/wifyee-api/rest/Users.php?request=paidbyapp";
    public static final String USER_CLIENT_DEDUCT_MONEY_WALLET ="/api/clientwalletdebitapi.php?request=clientWalletDebit";
    public static final String USER_CLIENT_ADD_MONEY_WALLET_END_POINT ="/api/clientloadbalancebypaymentgatewayapi.php?request=clientLoadBalanceByPaymentGateway";
    public static final String USER_SPLIT_LIST_POINT =MOBICASH_BASE_URL_TESTING+"/api/split_money_api.php?request=getAllSplitRequestByUser&userId=";
    public static final String MERCHANT_PROFILE_POINT =MOBICASH_BASE_URL_TESTING+"/api/merchant_qrcode_api.php?request=getMerchantByQRCode";
    public static final String MERCHANT_PROFILE_MOBILE_POINT =MOBICASH_BASE_URL_TESTING+"/api/merchant_qrcode_api.php?request=getMerchantByMobile";
    public static final String BUS_BOOKING_END_POINT ="http://www.wifyeepay.com/api/busbookingwebviewapi.php?request=busBooking";
    //Delete Beneficiary Item
    public static final String USER_TRANSACTIONS_LIST_END_POINT = MOBICASH_BASE_URL_TESTING+"/api/banktransferapi.php?request=getListTransactions";
    public static final String USER_BENEFICIARY_LIST_END_POINT = MOBICASH_BASE_URL_TESTING+"/api/beneficiaryapi.php?request=getBeneficiaryList";
    public static final String BENEFICIARY_DELETE_LIST_POINT =MOBICASH_BASE_URL_TESTING+"/api/beneficiaryapi.php?request=deleteBeneficiary";
    public static final String USER_PARTICIPENT_SPLIT_LIST_POINT =MOBICASH_BASE_URL_TESTING+"/api/split_money_api.php?request=getAllSplitRequestParticipentsBySplitMoneyId&splitMoneyId=";
    public static final String USER_CLIENT_PAYMENT_END_POINT ="/api/merchantpaymentapi.php?request=merchantPayment";
    public static final String USER_SEND_BENEFICIARY_OTP_VERIFICATION_END_POINT = MOBICASH_BASE_URL_TESTING+"/api/beneficiaryapi.php?request=generateOTP";
    public static final String USER_CLIENT_STATE_CITY_END_POINT = MOBICASH_BASE_URL_TESTING+"/api/beneficiaryapi.php?request=getCityListByStateId";
    public static final String USER_VIEW_DATA_USAGE = MOBICASH_BASE_URL_TESTING+"/wifyee-api/rest/Users.php?request=getCurrentPlanDataUsage";
    public static final String USER_PROMO_CODDE ="http://wifyeepay.com/api/promocodeapi.php?request=getPromocodeList";
    public static final String USER_PROMO_CODDE_APPLIED ="http://wifyeepay.com/api/promocodeapi.php?request=validatePromocode";

    public static final String SEND_MONEY_TO_CLIENT_API = MOBICASH_BASE_URL_TESTING+"/api/clientsendmoneyapi.php?request=clientSendMoney";
    public static final String SEND_MONEY_CLIENT_TO_CLIENT_API =MOBICASH_BASE_URL_TESTING+"/api/clienttoclientmoneytransferapi.php?request=clientWalletToWallet";
    public static final String SEND_OTP_AUTHENTICATION=MOBICASH_BASE_URL_TESTING+"/api/otpsmsapi.php?request=generateOTP";
    public static final String SEND_OTP_AUTHENTICATION_COMPLETE=MOBICASH_BASE_URL_TESTING+"//api/otpsmsapi.php?request=authenticateOTP";
    public static final String SEND_BENEFICIARY_OTP_AUTHENTICATION_COMPLETE=MOBICASH_BASE_URL_TESTING+"/api/beneficiaryapi.php?request=verifyOTP";
    public static final String SELLING_ITEMS_MERCHANT="/api/usersellingitemapi.php?request=listSellingItemsForClient";
    public static final String MERCHANT_LIST = MOBICASH_BASE_URL_TESTING+"/api/merchantapi.php?request=getmerchantlist";
    public static final String CITY_LIST_API=MOBICASH_BASE_URL_TESTING+"/api/busbookingapi.php?request=busBooking&requestType=1";
    public static final String REQUEST_MONEY_TO_CLIENT_API= MOBICASH_BASE_URL_TESTING+"/api/clientrequestmoneyapi.php?request=clientRequestMoney";
    public static final String MENU_ITEM_BY_CATEGORY =MOBICASH_BASE_URL_TESTING+"/api/menuitemsapi.php?request=getsubcategorybyparentslug";
    public static final String ADD_FOOD_ORDER =MOBICASH_BASE_URL_TESTING+"/api/food_order.php?request=addorder";
    public static final String MENU_ITEM_BY =MOBICASH_BASE_URL_TESTING+"/api/menuitemsapi.php?request=getmenuitemsbycategory";
    public static final String BUS_BOOKING =MOBICASH_BASE_URL_TESTING+"/api/busbookingapi.php?request=busBooking";
    public static final String UPDATE_PASSWORD_WIYEE_SERVER ="http://wifyee.wifi-soft.com/wifilan/api/wifyee/update_password.php";
    public static final String REFUND_CANCELLATION ="http://wifyee.com/refund-cancellation.html";
    public static final String PRIVACY_POLICY ="http://wifyee.com/privacy.html";
    public static final String TOKEN_UPDATE ="http://wifyeepay.com/api/commonApi.php?request=updateToken";
    public static final String MEDICINE_UPDATE ="/api/usermedicineapi.php?request=updateMedicineOrder";
    public static final String CREDIT_WALLET_PAYMENT ="/api/merchantCreditsClientApi.php?request=creditPayment";
    public static final String PURCHASE_VOUCHER_WALLET_PAYMENT ="/api/voucherapi.php?request=purchaseVoucher";

    //ORDER_ITEM API
    public static final String FOOD_ORDER_ITEM ="/api/food_order.php?request=getfoodorderdetails";
    public static final String OTHER_ORDER_ITEM ="/api/food_order.php?request=getotherorderdetails";
    public static final String MEDICINE_ORDER_ITEM ="/api/food_order.php?request=getmedicineorderdetails";
    public static final String CREDIT_PAY_ONLINE ="/api/merchantCreditsClientApi.php?request=creditPayment";


    // parameters
    public static final String PARAM_CLIENT_ID = "clientId";
    public static final String PARAM_HASH = "hash";
    // Airtime API type constants
    public static final String TYPE_AIRTIME = "AIRTIME";
    public static final String TYPE_PREPAID = "PREPAID";
    public static final String TYPE_POSTPAID = "POSTPAID";
    public static final String TYPE_LAND_LINE = "LANDLINE";
    public static final String TYPE_DTH = "DTH";
    public static final String TYPE_GAS = "GAS";
    public static final String TYPE_ELECTRICITY ="ELECTRICITY";
    public static final String TYPE_ELECTRICITY_GAS = "ELECTRICITY_GAS";
    /**
     * action state for local broadcast - Foododer List
     */
    public static final String STATUS_FOODODER_LIST_SUCCESS = "food_list_success";
    public static final String STATUS_FOODORDER_LIST_FAIL = "food_list_fail";

    public static final String STATUS_FOODODER_BYMERCHANT_LIST_SUCCESS = "food_bymerchant_list_success";
    public static final String STATUS_FOODORDER_BYMERCHANT_LIST_FAIL = "food_bymerchant_list_fail";

    public static final String STATUS_OFFER_NEAR_LIST_SUCCESS =" OFFER_NEAR_list_success";
    public static final String STATUS_OFFER_NEAR_LIST_FAIL = "OFFER_NEAR_list_fail";

    public static final String STATUS_FOODODER_STATUS_LIST_SUCCESS = "food_status_list_success";
    public static final String STATUS_FOODORDER_STATUS_LIST_FAIL = "food_status_list_fail";

    public static final String STATUS_FOODODER_STATUS_DEATILS_LIST_SUCCESS = "food_status_details_list_success";
    public static final String STATUS_FOODORDER_STATUS_DETAILS_LIST_FAIL = "food_status_details_list_fail";
    /**
     * action state for local broadcast - Foododer List
     */
    public static final String STATUS_ALLFOODODER_LIST_SUCCESS = "allfood_list_success";
    public static final String STATUS_ALLFOODORDER_LIST_FAIL = "allfood_list_fail";

    public static final String STATUS_FOODODER_BYPARENTSLUG_LIST_SUCCESS = "food_by_parent_slug_list_success";
    public static final String STATUS_FOODORDER_BYPARENTSLUG_LIST_FAIL = "food_by_parent_slug_list_list_fail";


    public static final String STATUS_GSTONFOOD_LIST_SUCCESS = "gstonfood_list_success";
    public static final String STATUS_GSTONFOOD_LIST_FAIL = "gstonfood_list_fail";

    public static final String STRING_EXTRA_DATA ="extra_data";
    public static final String ASSESS_DATA = "access_data";
}
