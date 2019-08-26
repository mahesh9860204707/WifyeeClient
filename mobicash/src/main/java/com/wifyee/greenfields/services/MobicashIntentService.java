package com.wifyee.greenfields.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.gson.Gson;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.constants.WifiConstant;
import com.wifyee.greenfields.dairyorder.DairyNetworkConstant;
import com.wifyee.greenfields.foodorder.AddressRequest;
import com.wifyee.greenfields.foodorder.AddressResponse;
import com.wifyee.greenfields.foodorder.CartFoodOderRequest;
import com.wifyee.greenfields.foodorder.CartFoodOrderResponse;
import com.wifyee.greenfields.foodorder.DeductMoneyWalletResponse;
import com.wifyee.greenfields.foodorder.FoodOrderRequest;
import com.wifyee.greenfields.foodorder.FoodOrderResponse;
import com.wifyee.greenfields.foodorder.FoodOrderStatusRequest;
import com.wifyee.greenfields.foodorder.FoodOrderStatusResponse;
import com.wifyee.greenfields.foodorder.FoodStatusDerailRequest;
import com.wifyee.greenfields.foodorder.FoodStatusDetailResponse;
import com.wifyee.greenfields.foodorder.GstOnFoodItemResponse;
import com.wifyee.greenfields.foodorder.MenuByParentSlugRequest;
import com.wifyee.greenfields.foodorder.MenuByParentSlugResponse;
import com.wifyee.greenfields.mapper.ModelMapper;
import com.wifyee.greenfields.models.requests.AddMoneyWallet;
import com.wifyee.greenfields.models.requests.AirtimeRequest;
import com.wifyee.greenfields.models.requests.BeneficiaryBean;
import com.wifyee.greenfields.models.requests.ClientBalanceRequest;
import com.wifyee.greenfields.models.requests.ClientBankTransferRequest;
import com.wifyee.greenfields.models.requests.ClientLogRequest;
import com.wifyee.greenfields.models.requests.ClientProfileUpdateRequest;
import com.wifyee.greenfields.models.requests.ClientResetPassCodeRequest;
import com.wifyee.greenfields.models.requests.ClientSendPassCodeRequest;
import com.wifyee.greenfields.models.requests.ClientStatusRequest;
import com.wifyee.greenfields.models.requests.ClientToClientMoneyTransferRequest;
import com.wifyee.greenfields.models.requests.DeductMoneyWallet;
import com.wifyee.greenfields.models.requests.GetClientProfileInfoRequest;
import com.wifyee.greenfields.models.requests.GetClientProfileRequest;
import com.wifyee.greenfields.models.requests.InstaMojoRequest;
import com.wifyee.greenfields.models.requests.KYCDocumentsBean;
import com.wifyee.greenfields.models.requests.LoginRequest;
import com.wifyee.greenfields.models.requests.MacAddressUpdate;
import com.wifyee.greenfields.models.requests.MedicineOrderModel;
import com.wifyee.greenfields.models.requests.MerchantRequest;
import com.wifyee.greenfields.models.requests.OffersCategory;
import com.wifyee.greenfields.models.requests.OperatorListRequest;
import com.wifyee.greenfields.models.requests.PayUPaymentGatewayRequest;
import com.wifyee.greenfields.models.requests.PlanCategory;
import com.wifyee.greenfields.models.requests.PlanPaymentRequest;
import com.wifyee.greenfields.models.requests.SendMoney;
import com.wifyee.greenfields.models.requests.SignupRequest;
import com.wifyee.greenfields.models.requests.SplitRequest;
import com.wifyee.greenfields.models.requests.UploadClientPictureRequest;
import com.wifyee.greenfields.models.requests.VerifyMobileNumber;
import com.wifyee.greenfields.models.response.AirtimeResponse;
import com.wifyee.greenfields.models.response.BeneficiaryResponse;
import com.wifyee.greenfields.models.response.ClientBalanceResponse;
import com.wifyee.greenfields.models.response.ClientBankTransferResponse;
import com.wifyee.greenfields.models.response.ClientLogResponse;
import com.wifyee.greenfields.models.response.ClientProfileUpdateResponse;
import com.wifyee.greenfields.models.response.ClientResetPassCodeResponse;
import com.wifyee.greenfields.models.response.ClientSendPassCodeResponse;
import com.wifyee.greenfields.models.response.ClientStatusResponse;
import com.wifyee.greenfields.models.response.ClientToClientMoneyTransferResponse;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.models.response.GetClientProfileInfoResponse;
import com.wifyee.greenfields.models.response.GetClientProfileResponse;
import com.wifyee.greenfields.models.response.LoginResponse;
import com.wifyee.greenfields.models.response.MacUpdateAddressResponse;
import com.wifyee.greenfields.models.response.MerchantPaymentResponse;
import com.wifyee.greenfields.models.response.NearOfferResponse;
import com.wifyee.greenfields.models.response.OTP_Response;
import com.wifyee.greenfields.models.response.OperatorListResponse;
import com.wifyee.greenfields.models.response.PayUPaymentGatewayResponse;
import com.wifyee.greenfields.models.response.SendMoneyResponse;
import com.wifyee.greenfields.models.response.SignupResponse;
import com.wifyee.greenfields.models.response.SplitResponse;
import com.wifyee.greenfields.models.response.UploadClientProfilePictureResponse;
import com.wifyee.greenfields.models.response.VerifyMobileNumberResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import timber.log.Timber;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * helper methods.
 */
public class MobicashIntentService extends IntentService {
    /**
     * Mac Address Stauts
     */
    private static final String TAG_PERFORM_MAC_UPDATE_STATUS = "com.mobicashindia.mobicash.services.tag.mac_update_status";
    private static final String ACTION_PERFORM_MAC_UPDATE_STATUS = "com.mobicashindia.mobicash.services.action.mac_update_status";
    private static final String PARAM_MAC_UPDATE_STATUS_REQUEST_MODEL = "mobicash.services.extra.client.mac_update_status.request.model";

    /**
     * Food Order Stauts
     */
    private static final String TAG_PERFORM_FOOD_ORDER_STATUS = "com.mobicashindia.mobicash.services.tag.food_order_status";
    private static final String ACTION_PERFORM_FOOD_ORDER_STATUS = "com.mobicashindia.mobicash.services.action.food_order_status";
    private static final String PARAM_FOOD_ORDER_STATUS_REQUEST_MODEL = "mobicash.services.extra.client.food_order_status.request.model";
    /**
     * Food Order Stauts Deatils
     */
    private static final String TAG_PERFORM_FOOD_ORDER_STATUS_DETAIL = "com.mobicashindia.mobicash.services.tag.food_order_status_detail";
    private static final String ACTION_PERFORM_FOOD_ORDER_STATUS_DETAIL = "com.mobicashindia.mobicash.services.action.food_order_status_detail";
    private static final String PARAM_FOOD_ORDER_STATUS_DETAIL_REQUEST_MODEL = "mobicash.services.extra.client.food_order_status_detail.request.model";

    private static final String TAG_PERFORM_OTP_STATUS_DETAIL = "com.mobicashindia.mobicash.services.tag.otp_status_detail";
    private static final String ACTION_PERFORM_OTP_STATUS_DETAIL = "com.mobicashindia.mobicash.services.action.otp_status_detail";
    private static final String PARAM_OTP_STATUS_DETAIL_REQUEST_MODEL = "mobicash.services.extra.client.otp_status_detail.request.model";

    private static final String TAG_PERFORM_CONFIRMATION_OTP_STATUS_DETAIL = "com.mobicashindia.mobicash.services.tag.confirmation_otp_status_detail";
    private static final String ACTION_PERFORM_CONFIRMATION_OTP_STATUS_DETAIL = "com.mobicashindia.mobicash.services.action.confirmation_otp_status_detail";
    private static final String PARAM_CONFIRMATION_OTP_STATUS_DETAIL_REQUEST_MODEL = "mobicash.services.extra.client.confirmation_otp_status_detail.request.model";
    /**
     * User sign up
     */
    private static final String TAG_PERFORM_USER_SIGN_UP = "com.mobicashindia.mobicash.services.tag.signup";
    private static final String ACTION_PERFORM_USER_SIGN_UP = "com.mobicashindia.mobicash.services.action.signup";
    private static final String PARAM_SIGNUP_REQUEST_MODEL = "mobicash.services.extra.client.signup.request.model";

    /**
     * Wi fi Plans
     */
    private static final String TAG_PERFORM_WI_FI_PLANS = "com.mobicashindia.mobicash.services.tag.wifiplans";
    private static final String ACTION_PERFORM_WI_FI = "com.mobicashindia.mobicash.services.action.wifi";
    private static final String PARAM_WIFI_PLANS_REQUEST_MODEL = "mobicash.services.extra.client.wifiplans.request.model";

    /**
     * Offers Plans
     */
    private static final String TAG_PERFORM_OFFERS= "com.mobicashindia.mobicash.services.tag.offers";
    private static final String ACTION_PERFORM_OFFERS= "com.mobicashindia.mobicash.services.action.offers";
    private static final String PARAM_WIFI_OFFERS_REQUEST_MODEL = "mobicash.services.extra.client.offers.request.model";


    /**
     * Verify phone number
     */
    private static final String TAG_PERFORM_VERIFY_MOBILE_NUMBER= "com.mobicashindia.mobicash.services.tag.verify_mobile_number";
    private static final String ACTION_PERFORM_VERIFY_MOBILE_NUMBER= "com.mobicashindia.mobicash.services.action.verify_mobile_number";
    private static final String PARAM_VERIFY_MOBILE_NUMBER_REQUEST_MODEL = "mobicash.services.extra.client.verify_mobile_number.request.model";
    /**
     * Send Money
     */
    private static final String TAG_PERFORM_SEND_MONEY = "com.mobicashindia.mobicash.services.tag.sendmoney";
    private static final String ACTION_PERFORM_SEND_MONEY = "com.mobicashindia.mobicash.services.action.sendmoney";
    private static final String PARAM_SEND_MONEY_REQUEST_MODEL = "mobicash.services.extra.client.sendmoney.request.model";

    /**
     * Add Beneficiary
     */
    private static final String TAG_ADD_BENEFICIARY = "com.mobicashindia.mobicash.services.tag.addbeneficiary";
    private static final String ACTION_PERFORM_ADD_BENEFICIARY = "com.mobicashindia.mobicash.services.action.addbeneficiary";
    private static final String PARAM_ADD_BENEFICIARY_REQUEST_MODEL = "mobicash.services.extra.client.addbeneficiary.request.model";

    /**
     * Add KYC
     */
    private static final String TAG_ADD_KYC = "com.mobicashindia.mobicash.services.tag.addkyc";
    private static final String ACTION_PERFORM_ADD_KYC = "com.mobicashindia.mobicash.services.action.addkyc";
    private static final String PARAM_ADD_KYC_REQUEST_MODEL = "mobicash.services.extra.client.addkyc.request.model";

    /**
     * Wi fi Plans
     */
    private static final String TAG_PERFORM_WI_FI_PLANS_PAYMENT = "com.mobicashindia.mobicash.services.tag.wifiplanPayments";
    private static final String ACTION_PERFORM_WI_FI_PAYMENT = "com.mobicashindia.mobicash.services.action.wifiPayments";
    private static final String PARAM_WIFI_PLANS_PAYMENT_REQUEST_MODEL = "mobicash.services.extra.client.wifiplansPayment.request.model";

    /**
     * Insta Mojo Payment gateway
     */
    private static final String TAG_PERFORM_INSTA_MOJO_PAYMENT = "com.mobicashindia.mobicash.services.tag.instamojoPayments";
    private static final String ACTION_PERFORM_INSTA_MOJO_PAYMENT = "com.mobicashindia.mobicash.services.action.instamojoPayments";
    private static final String PARAM_INSTA_MOJO_PAYMENT_REQUEST_MODEL = "mobicash.services.extra.client.instamojoPayment.request.model";

    /**
     * IntentService can perform, e.g. ACTION_USER_LOGIN
     */
    private static final String TAG_PERFORM_USER_LOGIN = "com.mobicashindia.mobicash.services.tag.login";
    private static final String ACTION_PERFORM_USER_LOGIN = "com.mobicashindia.mobicash.services.action.login";
    private static final String PARAM_LOGIN_REQUEST_MODEL = "mobicash.services.extra.client.login.request.model";

    /**
     * IntentService can perform, e.g. ACTION_USER_SPLIT_MONEY
     */
    private static final String TAG_PERFORM_USER_SPLIT = "com.mobicashindia.mobicash.services.tag.split";
    private static final String ACTION_PERFORM_USER_SPLIT = "com.mobicashindia.mobicash.services.action.split";
    private static final String PARAM_SPLIT_REQUEST_MODEL = "mobicash.services.extra.client.split.request.model";

    /**
     * balance transfer - CTC
     */
    private static final String TAG_PERFORM_CLIENT_TO_CLIENT_MONEY_TRANSFER = "com.mobicashindia.mobicash.services.tag.client_to_client_money_transfer";
    private static final String ACTION_PERFORM_CLIENT_TO_CLIENT_MONEY_TRANSFER = "com.mobicashindia.mobicash.services.action.client_to_client_money_transfer";
    private static final String PARAM_CLIENT_TO_CLIENT_MONEY_TRANSFER_REQUEST_MODEL = "mobicash.services.extra.client_to_client_money_transfer.request.model";

    /**
     * client status
     */
    private static final String TAG_PERFORM_CLIENT_STATUS = "com.mobicashindia.mobicash.services.tag.client.status";
    private static final String ACTION_PERFORM_CLIENT_STATUS = "com.mobicashindia.mobicash.services.action.client.status";
    private static final String PARAM_CLIENT_STATUS_REQUEST_MODEL = "mobicash.services.extra.client.status.request.model";

    /**
     * check balance
     */
    private static final String TAG_PERFORM_CLIENT_BALANCE = "com.mobicashindia.mobicash.services.tag.client.balance";
    private static final String ACTION_PERFORM_CLIENT_BALANCE = "com.mobicashindia.mobicash.services.action.client.balance";
    private static final String PARAM_CLIENT_BALANCE_REQUEST_MODEL = "mobicash.services.extra.client.balance.request.model";

    /**
     * airtime recharge
     */
    private static final String TAG_PERFORM_AIRTIME_RECHARGE = "com.mobicashindia.mobicash.services.tag.airtime_recharge";
    private static final String ACTION_PERFORM_AIRTIME_RECHARGE = "com.mobicashindia.mobicash.services.action.airtime_recharge";
    private static final String PARAM_AIRTIME_REQUEST_MODEL = "mobicash.services.extra.airtime.request.model";
    private static final String TRANSACTION_ID="transactionId";

    /**
     * airtime recharge
     */
    private static final String TAG_PERFORM_OPERATOR_LIST = "com.mobicashindia.mobicash.services.tag.operator_list.";
    private static final String ACTION_PERFORM_OPERATOR_LIST = "com.mobicashindia.mobicash.services.action.operator_list";
    private static final String PARAM_OPERATOR_LIST_REQUEST_MODEL = "mobicash.services.extra.operator.list.request.model";

    /**
     * Client log
     */
    private static final String TAG_PERFORM_CLIENT_LOG = "com.mobicashindia.mobicash.services.tag.client_log";
    private static final String ACTION_PERFORM_CLIENT_LOG = "com.mobicashindia.mobicash.services.action.client_log";
    private static final String PARAM_CLIENT_LOG_REQUEST_MODEL = "mobicash.services.extra.client_log.request.model";

    /**
     * GET Client Profile. : Used for showing client details
     */
    private static final String TAG_PERFORM_GET_CLIENT_PROFILE = "com.mobicashindia.mobicash.services.tag.get_client_profile";
    private static final String ACTION_PERFORM_GET_CLIENT_PROFILE = "com.mobicashindia.mobicash.services.action.get_client_profile";
    private static final String PARAM_GET_CLIENT_PROFILE_REQUEST_MODEL = "mobicash.services.extra.get_client_profile.request.model";

    /**
     * Post Client Profile : Used for updating the profile
     */
    private static final String TAG_PERFORM_CLIENT_PROFILE_UPDATE = "com.mobicashindia.mobicash.services.tag.client_profile_update";
    private static final String ACTION_PERFORM_CLIENT_PROFILE_UPDATE = "com.mobicashindia.mobicash.services.action.client_profile_update";
    private static final String PARAM_CLIENT_PROFILE_UPDATE_REQUEST_MODEL = "mobicash.services.extra.client_profile_update.request.model";
    /**
     * Client Profile info :  Used for showing client details on Home screen
     */
    private static final String TAG_PERFORM_GET_CLIENT_PROFILE_INFO = "com.mobicashindia.mobicash.services.tag.get_client_profile_info";
    private static final String ACTION_PERFORM_GET_CLIENT_PROFILE_INFO = "com.mobicashindia.mobicash.services.action.get_client_profile_info";
    private static final String PARAM_GET_CLIENT_PROFILE_INFO_REQUEST_MODEL = "mobicash.services.extra.get_client_profile_info.request.model";

    /**
     * Client Bank transfer :  Used for Bank transfer screen. NEFT / IMPS service
     */
    private static final String TAG_PERFORM_CLIENT_BANK_TRANSFER = "com.mobicashindia.mobicash.services.tag.client_bank_transfer";
    private static final String ACTION_PERFORM_CLIENT_BANK_TRANSFER = "com.mobicashindia.mobicash.services.action.client_bank_transfer";
    private static final String PARAM_CLIENT_BANK_TRANSFER_REQUEST_MODEL = "mobicash.services.extra.client_bank_transfer.request.model";
    /**
     * Client Deduct Money in Wallet :
     */
    private static final String TAG_PERFORM_DEDUCT_MONEY_WALLET= "com.mobicashindia.mobicash.services.tag.deduct_money_wallet";
    private static final String ACTION_PERFORM_DEDUCT_MONEY_WALLET= "com.mobicashindia.mobicash.services.deduct_money_wallet";
    private static final String PARAM_DEDUCT_MONEY_WALLET_REQUEST_MODEL = "mobicash.services.extra.deduct_money_wallet.request.model";

    /**
     * Client Add Money in Wallet :
     */
    private static final String TAG_PERFORM_ADD_MONEY_WALLET= "com.mobicashindia.mobicash.services.tag.add_money_wallet";
    private static final String ACTION_PERFORM_ADD_MONEY_WALLET= "com.mobicashindia.mobicash.services.add_money_wallet";
    private static final String PARAM_ADD_MONEY_WALLET_REQUEST_MODEL = "mobicash.services.extra.add_money_wallet.request.model";

    /**
     * Client Medicine Order :
     */
    private static final String TAG_PERFORM_MEDICINE_ORDER= "com.mobicashindia.mobicash.services.tag.medicine_order";
    private static final String ACTION_PERFORM_MEDICINE_ORDER= "com.mobicashindia.mobicash.services.medicine_order";
    private static final String PARAM_MEDICINE_ORDER_REQUEST_MODEL = "mobicash.services.extra.medicine_order.request.model";

    /**
     * Client Bank transfer :  Used for Send Passcode screen.
     */
    private static final String TAG_PERFORM_CLIENT_SEND_PASSCODE = "com.mobicashindia.mobicash.services.tag.client_send_passcode";
    private static final String ACTION_PERFORM_CLIENT_SEND_PASSCODE = "com.mobicashindia.mobicash.services.action.client_send_passcode";
    private static final String PARAM_CLIENT_SEND_PASSCODE_REQUEST_MODEL = "mobicash.services.extra.client_send_passcode.request.model";
    /**
     * Client To Client Wallet Balance Transfer
     */
    private static final String TAG_PERFORM_CLIENT_TO_CLIENT_WALLET= "com.mobicashindia.mobicash.services.tag.client_client_to_wallet";
    private static final String ACTION_PERFORM_CLIENT_TO_CLIENT_WALLET = "com.mobicashindia.mobicash.services.action.client_client_to_wallet";
    private static final String PARAM_CLIENT_TO_CLIENT_WALLET_REQUEST_MODEL = "mobicash.services.extra.client_client_to_wallet.request.model";
    /**
     * Client Bank transfer :  Used for Reset Passcode screen.
     */
    private static final String TAG_PERFORM_CLIENT_RESET_PASSCODE = "com.mobicashindia.mobicash.services.tag.client_reset_passcode";
    private static final String ACTION_PERFORM_CLIENT_RESET_PASSCODE = "com.mobicashindia.mobicash.services.action.client_reset_passcode";
    private static final String PARAM_CLIENT_RESET_PASSCODE_REQUEST_MODEL = "mobicash.services.extra.client_reset_passcode.request.model";
    /**
     * Client Bank transfer :  Used for Upload Client Profile Picture
     */
    private static final String TAG_PERFORM_UPLOAD_CLIENT_PROFILE_PICTURE = "com.mobicashindia.mobicash.services.tag.upload_client_profile_picture";
    private static final String ACTION_PERFORM_UPLOAD_CLIENT_PROFILE_PICTURE = "com.mobicashindia.mobicash.services.action.upload_client_profile_picture";
    private static final String PARAM_UPLOAD_CLIENT_PROFILE_PICTURE_REQUEST_MODEL = "mobicash.services.extra.upload_client_profile_picture.request.model";

    /**
     * Client PayU Payment Gateway  :  Used for Client PayU Payment Gateway
     */
    private static final String TAG_PERFORM_CLIENT_PAYU_PAYMENT_GATEWAY = "com.mobicashindia.mobicash.services.tag.client_payu_payment_gateway";
    private static final String ACTION_PERFORM_CLIENT_PAYU_PAYMENT_GATEWAY = "com.mobicashindia.mobicash.services.action.client_payu_payment_gateway";
    private static final String PARAM_CLIENT_PAYU_PAYMENT_GATEWAY_REQUEST_MODEL = "mobicash.services.extra.client_payu_payment_gateway.request.model";

    /**
     *
     *   /**
     * Check User Payment
     */
    private static final String TAG_PERFORM_CLIENT_PAYMENT_SUCCESS = "com.mobicashindia.mobicash.services.tag.merchant.payment";
    private static final String ACTION_PERFORM_CLIENT_PAYMENT = "com.mobicashindia.mobicash.services.action.merchant.payment";
    private static final String PARAM_CLIENT_PAYMENT_REQUEST_MODEL = "mobicash.services.extra.client.merchant.payment.request.model";
    /**
     * BANK LIST
     */

    private static final String ACTION_GET_BANK_LIST = "com.mobicashindia.mobicash.services.action.bank.list";

    /**
     * CITY LIST
     */

    private static final String ACTION_GET_CITY_LIST = "com.mobicashindia.mobicash.services.action.city.list";

    /**
     * State LIST
     */

    private static final String ACTION_GET_STATE_LIST = "com.mobicashindia.mobicash.services.action.state.list";

    /**
     * Wifyee registration action
     */
    private static final String TAG_PERFORM_WIFYEE_USER_REGISTRATION = "com.mobicashindia.mobicash.services.tag.wifee.reg";
    private static final String ACTION_PERFORM_WIFYEE_REGISTRATION = "com.mobicashindia.mobicash.services.action.wifyee.reg";
    private static final String PARAM_SIGNUP_REQUEST_MODEL_WIFYEE = "mobicash.services.extra.client.signup.request.model.wifyee";

    /**
     * Wifyee connection action
     */
    private static final String TAG_PERFORM_WIFYEE_USER_CONNECTION = "com.mobicashindia.mobicash.services.tag.wifee.connection";
    private static final String ACTION_PERFORM_WIFYEE_CONNECTION = "com.mobicashindia.mobicash.services.action.wifyee.connection";
    private static final String PARAM_CONN_REQUEST_MODEL_WIFYEE = "mobicash.services.extra.client.conn.request.model.wifyee";

    /**
     * FoodOder List
     */
    private static final String TAG_PERFORM_FOODORDER_LIST = "com.mobicashindia.mobicash.services.tag.foodorder_list.";
    private static final String ACTION_PERFORM_FOODORDER_LIST = "com.mobicashindia.mobicash.services.action.foodorder_list";
    private static final String PARAM_FOODORDER_LIST_REQUEST_MODEL = "mobicash.services.extra.foodorder.list.request.model";

    /**
     * FoodOder  BY Merchant List
     */
    private static final String TAG_PERFORM_FOODORDER_BMERCHANT_LIST = "com.mobicashindia.mobicash.services.tag.foodorder__bymerchant_list.";
    private static final String ACTION_PERFORM_FOODORDER_BYMERCHANT_LIST = "com.mobicashindia.mobicash.services.action.foodorder_bymerchant_list";
    private static final String PARAM_FOODORDER_BYMERCHANT_LIST_REQUEST_MODEL = "mobicash.services.extra.foodorder_bymerchant.list.request.model";

    private static final String TAG_PERFORM_FOODORDER_BYPARENTSLUG_LIST = "com.mobicashindia.mobicash.services.tag.foodorder_byparentslug_list.";
    private static final String ACTION_PERFORM_FOODORDER_BYPARENTSLUG_LIST = "com.mobicashindia.mobicash.services.action.foodorder_byparentsug_list";
    private static final String PARAM_FOODORDER_BYPARENTSLUG_LIST_REQUEST_MODEL = "mobicash.services.extra.foodorder_byparentslug.list.request.model";


    /**
     * ALLFoodOder List
     */
    private static final String TAG_PERFORM_ALLFOODORDER_LIST = "com.mobicashindia.mobicash.services.tag.allfoodorder_list.";
    private static final String ACTION_PERFORM_ALLFOODORDER_LIST = "com.mobicashindia.mobicash.services.action.allfoodorder_list";
    private static final String PARAM_AllFOODORDER_LIST_REQUEST_MODEL = "mobicash.services.extra.allfoodorder.list.request.model";
    /**GST ON FOOD **/

    private static final String TAG_PERFORM_GSTONFOOD_LIST = "com.mobicashindia.mobicash.services.tag.gstonfood_list.";
    private static final String ACTION_PERFORM_GSTONFOOD_LIST = "com.mobicashindia.mobicash.services.action.gstonfood_list";
    private static final String PARAM_GSTONFOOD_LIST_REQUEST_MODEL = "mobicash.services.extra.gstonfood.list.request.model";


    private static final String EXTRA_PARAM5 = "com.wifyee.greenfields.dairyorder.extra.PARAM5";
    private static final String EXTRA_PARAM6 = "com.wifyee.greenfields.dairyorder.extra.PARAM6";
    private static final String EXTRA_PARAM7 = "com.wifyee.greenfields.dairyorder.extra.PARAM7";
    private static final String EXTRA_PARAM8 = "com.wifyee.greenfields.dairyorder.extra.PARAM8";
    private static final String EXTRA_PARAM9 = "com.wifyee.greenfields.dairyorder.extra.PARAM9";
    private static final String EXTRA_PARAM10 = "com.wifyee.greenfields.dairyorder.extra.PARAM10";
    private static final String EXTRA_PARAM11 = "com.wifyee.greenfields.dairyorder.extra.PARAM11";
    private static final String EXTRA_PARAM12 = "com.wifyee.greenfields.dairyorder.extra.PARAM12";
    private static final String EXTRA_PARAM13 = "com.wifyee.greenfields.dairyorder.extra.PARAM13";
    /**h
     * SubMit all Food Request
     */
    private static final String TAG_PERFORM_FOODORDER_SUBMIT = "com.mobicashindia.mobicash.services.tag.foodOrder.submit";
    private static final String ACTION_PERFORM_FOODORDER_SUBMIT = "com.mobicashindia.mobicash.services.action.foodOrder.submit";
    private static final String PARAM_CLIENT_FOODORDER_SUBMIT_REQUEST_MODEL = "mobicash.services.extra.client_foodOrder_submit.request.model";

    private static final String TAG_PERFORM_NERAR_OFFERBY_SUBMIT = "com.mobicashindia.mobicash.services.tag.NERAR_OFFERBY";
    private static final String ACTION_PERFORM_NERAR_OFFERBY_SUBMIT = "com.mobicashindia.mobicash.services.action.NERAR_OFFERBY";
    private static final String PARAM_CLIENT_NERAR_OFFERBY_SUBMIT_REQUEST_MODEL = "mobicash.services.extra.NERAR_OFFERBY.request.model";

    private static Context ctx = null;

    public MobicashIntentService() {
        super("MobicashIntentService");
    }

    public static void startActionUserSignUp(Context context, SignupRequest signupRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_USER_SIGN_UP);
        intent.putExtra(PARAM_SIGNUP_REQUEST_MODEL, signupRequest);
        context.startService(intent);
    }
    public static void startActionUserConfirmationOtp(Context context, OTP_Response signupRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_CONFIRMATION_OTP_STATUS_DETAIL);
        intent.putExtra(PARAM_CONFIRMATION_OTP_STATUS_DETAIL_REQUEST_MODEL, signupRequest);
        context.startService(intent);
    }

    public static void startActionGstONFoodItem(Context context) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_GSTONFOOD_LIST);
        intent.putExtra(PARAM_GSTONFOOD_LIST_REQUEST_MODEL,"");
        context.startService(intent);
    }
    public static void startActionAllFoodOrderList(Context context,FoodOrderRequest foodOrderRequest) {
        ctx = context;
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_ALLFOODORDER_LIST);
        intent.putExtra(PARAM_AllFOODORDER_LIST_REQUEST_MODEL,foodOrderRequest);
        context.startService(intent);
    }
    public static void startActionFoodMerchantByLocation(Context context, AddressRequest addressRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_FOODORDER_BYMERCHANT_LIST);
        intent.putExtra(PARAM_FOODORDER_BYMERCHANT_LIST_REQUEST_MODEL, addressRequest);
        context.startService(intent);
    }

    public static void startActionMacUpdateRequest(Context context, MacAddressUpdate mMacAddressUpdate) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_MAC_UPDATE_STATUS);
        intent.putExtra(PARAM_MAC_UPDATE_STATUS_REQUEST_MODEL, mMacAddressUpdate);
        context.startService(intent);
    }
    public static void startActionSendFoodRequest(Context context,
                                                  CartFoodOderRequest cartFoodOderRequest,
                                                  String location,String latitude,String longitude,
                                                  String complete_add,String discount_amt, String claimType,
                                                  String voucherId, String voucherNo,String refNo) {
        ctx = context;
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_FOODORDER_SUBMIT);
        intent.putExtra(PARAM_CLIENT_FOODORDER_SUBMIT_REQUEST_MODEL, cartFoodOderRequest);
        intent.putExtra(EXTRA_PARAM5, location);
        intent.putExtra(EXTRA_PARAM6, latitude);
        intent.putExtra(EXTRA_PARAM7, longitude);
        intent.putExtra(EXTRA_PARAM8, complete_add);
        intent.putExtra(EXTRA_PARAM9, discount_amt);
        intent.putExtra(EXTRA_PARAM10, claimType);
        intent.putExtra(EXTRA_PARAM11, voucherId);
        intent.putExtra(EXTRA_PARAM12, voucherNo);
        intent.putExtra(EXTRA_PARAM13, refNo);
        context.startService(intent);
    }
    public static void startActionFoodOrderList(Context context, FoodOrderRequest foodOrderRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_FOODORDER_LIST);
        intent.putExtra(PARAM_FOODORDER_LIST_REQUEST_MODEL, foodOrderRequest);
        context.startService(intent);
    }
    public static void startActionFoodOrderListByParentSlug(Context context, MenuByParentSlugRequest menuByParentSlugRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_FOODORDER_BYPARENTSLUG_LIST);
        intent.putExtra(PARAM_FOODORDER_BYPARENTSLUG_LIST_REQUEST_MODEL, menuByParentSlugRequest);
        context.startService(intent);
    }


    public static void startActionSendMoneyToClient(Context context, SendMoney sendMoney) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_CLIENT_TO_CLIENT_WALLET);
        intent.putExtra(PARAM_CLIENT_TO_CLIENT_WALLET_REQUEST_MODEL, sendMoney);
        context.startService(intent);
    }

    /**
     * start action wifyee registration
     * @param context
     * @param parcel
     */

    public static void startActionWifyeeUserRegistration(Context context,  String parcel) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_WIFYEE_REGISTRATION);
        intent.putExtra(PARAM_SIGNUP_REQUEST_MODEL_WIFYEE, parcel);
        context.startService(intent);
    }

    /**
     * start action wifyee connection
     * @param context
     * @param parcel
     */

    public static void startActionWifyeeUserConnection(Context context,  String parcel) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_WIFYEE_CONNECTION);
        intent.putExtra(PARAM_CONN_REQUEST_MODEL_WIFYEE, parcel);
        context.startService(intent);
    }

    /**
     * start action wifyee connection
     * @param context
     * @param parcel
     */

    public static void startActionSendMoney(Context context,  SendMoney parcel) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_SEND_MONEY);
        intent.putExtra(PARAM_SEND_MONEY_REQUEST_MODEL, parcel);
        context.startService(intent);
    }

    /**
     * start action wifyee connection  plans
     * @param context
     * @param parcel
     */

    public static void startActionWifyeePLans(Context context,  PlanCategory parcel) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_WI_FI);
        intent.putExtra(PARAM_WIFI_PLANS_REQUEST_MODEL, parcel);
        context.startService(intent);
    }

    public static void startActionWifyeeOffers(Context context,  OffersCategory parcel) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_OFFERS);
        intent.putExtra(PARAM_WIFI_OFFERS_REQUEST_MODEL, parcel);
        context.startService(intent);
    }

    public static void startActionVerifyMobileNumber(Context context,  VerifyMobileNumber mVerifyMobileNumber) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_VERIFY_MOBILE_NUMBER);
        intent.putExtra(PARAM_VERIFY_MOBILE_NUMBER_REQUEST_MODEL, mVerifyMobileNumber);
        context.startService(intent);
    }

    // Split Money Request
    public static void startActionUserSplitMoney(Context context, SplitRequest splitRequest) {
        Timber.d(" Starting User Split service through Intent");
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_USER_SPLIT);
        intent.putExtra(PARAM_SPLIT_REQUEST_MODEL, splitRequest);
        context.startService(intent);
    }

    public static void startActionUserLogin(Context context, LoginRequest loginRequest) {
        Timber.d(" Starting User Login service through Intent");
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_USER_LOGIN);
        intent.putExtra(PARAM_LOGIN_REQUEST_MODEL, loginRequest);
        context.startService(intent);
    }
    //Plan Purchase
    public static void startActionPlanPurchase(Context context, PlanPaymentRequest planPaymentRequest) {
        Timber.d(" Starting User Payment service through Intent");
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_WI_FI_PAYMENT);
        intent.putExtra(PARAM_WIFI_PLANS_PAYMENT_REQUEST_MODEL, planPaymentRequest);
        context.startService(intent);
    }
    //Payment Gateway of InstaMojo
    public static void startActionInstaMojoPayment(Context context, InstaMojoRequest instaMojoPaymentRequest) {
        Timber.d(" Starting User Payment service through Intent");
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_INSTA_MOJO_PAYMENT);
        intent.putExtra(PARAM_INSTA_MOJO_PAYMENT_REQUEST_MODEL, instaMojoPaymentRequest);
        context.startService(intent);
    }
    public static void startActionClientPaymentDetails(Context context, MerchantRequest merchantRequest) {
        Timber.d(" Starting User Client payment service through Intent");
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_CLIENT_PAYMENT);
        intent.putExtra(PARAM_CLIENT_PAYMENT_REQUEST_MODEL, merchantRequest);
        context.startService(intent);
    }

    //Deduct Money Wallet
    public static void startActionDeductMoneyWallet(Context context, DeductMoneyWallet deductMoneyWalletRequest) {
        Timber.d(" Starting User Add Wallet Money service through Intent");
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_DEDUCT_MONEY_WALLET);
        intent.putExtra(PARAM_DEDUCT_MONEY_WALLET_REQUEST_MODEL, deductMoneyWalletRequest);
        context.startService(intent);
    }

    //Medicine Order
    public static void startActionMedicineOrder(Context context, MedicineOrderModel medicineOrderModel) {
        Timber.d(" Starting User Add Wallet Money service through Intent");
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_MEDICINE_ORDER);
        intent.putExtra(PARAM_MEDICINE_ORDER_REQUEST_MODEL, medicineOrderModel);
        context.startService(intent);
    }
    //Add Money Wallet
    public static void startActionAddMoneyWallet(Context context, AddMoneyWallet addMoneyWalletRequest) {
        Timber.d(" Starting User Add Wallet Money service through Intent");
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_ADD_MONEY_WALLET);
        intent.putExtra(PARAM_ADD_MONEY_WALLET_REQUEST_MODEL, addMoneyWalletRequest);
        context.startService(intent);
    }

    //TODO:Call below function from Activity after validation of senderPhoneNumber, receiverPhoneNumber, amount.
    public static void startActionClientToClientMoneyTransfer(Context context, ClientToClientMoneyTransferRequest clientToClientMoneyTransferRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_CLIENT_TO_CLIENT_MONEY_TRANSFER);
        intent.putExtra(PARAM_CLIENT_TO_CLIENT_MONEY_TRANSFER_REQUEST_MODEL, clientToClientMoneyTransferRequest);
        context.startService(intent);
    }


    //TODO:Call below function from Activity after validation of phoneNumber, passcode.
    public static void startActionClientStatus(Context context, ClientStatusRequest clientStatusRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_CLIENT_STATUS);
        intent.putExtra(PARAM_CLIENT_STATUS_REQUEST_MODEL, clientStatusRequest);
        context.startService(intent);
    }


    //TODO:Call below function from Activity after validation of phoneNumber, passcode.
    public static void startActionClientBalance(Context context, ClientBalanceRequest clientBalanceRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_CLIENT_BALANCE);
        intent.putExtra(PARAM_CLIENT_BALANCE_REQUEST_MODEL, clientBalanceRequest);
        context.startService(intent);
    }


    public static void startActionAirtimeRecharge(Context context, AirtimeRequest airtimeRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_AIRTIME_RECHARGE);
        intent.putExtra(PARAM_AIRTIME_REQUEST_MODEL, airtimeRequest);
        context.startService(intent);
    }

    public static void startActionAirtimeRecharge(Context context, AirtimeRequest airtimeRequest,String transactionId) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_AIRTIME_RECHARGE);
        intent.putExtra(PARAM_AIRTIME_REQUEST_MODEL, airtimeRequest);
        intent.putExtra(TRANSACTION_ID,transactionId);
        context.startService(intent);
    }

    public static void startActionOperatorList(Context context, OperatorListRequest operatorListRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_OPERATOR_LIST);
        intent.putExtra(PARAM_OPERATOR_LIST_REQUEST_MODEL, operatorListRequest);
        context.startService(intent);
    }

    public static void startActionClientLog(Context context, ClientLogRequest clientLogRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_CLIENT_LOG);
        intent.putExtra(PARAM_CLIENT_LOG_REQUEST_MODEL, clientLogRequest);
        context.startService(intent);
    }

    public static void startActionClientProfileUpdate(Context context, ClientProfileUpdateRequest clientProfileUpdateRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_CLIENT_PROFILE_UPDATE);
        intent.putExtra(PARAM_CLIENT_PROFILE_UPDATE_REQUEST_MODEL, clientProfileUpdateRequest);
        context.startService(intent);
    }

    public static void startActionGetClientProfile(Context context, GetClientProfileRequest getClientProfileRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_GET_CLIENT_PROFILE);
        intent.putExtra(PARAM_GET_CLIENT_PROFILE_REQUEST_MODEL, getClientProfileRequest);
        context.startService(intent);
    }

    public static void startActionGetClientProfileInfo(Context context, GetClientProfileInfoRequest getClientProfileInfoRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_GET_CLIENT_PROFILE_INFO);
        intent.putExtra(PARAM_GET_CLIENT_PROFILE_INFO_REQUEST_MODEL, getClientProfileInfoRequest);
        context.startService(intent);
    }

    //TODO:Call below function from Activity after validation of  public String clientmobile, clientId, pincode, transferType, accountNo, IFSC, bankName, branchName, amount, firstName, lastName, beneficiaryMobile, hash.
    public static void startActionClientBankTransfer(Context context, ClientBankTransferRequest clientBankTransferRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_CLIENT_BANK_TRANSFER);
        intent.putExtra(PARAM_CLIENT_BANK_TRANSFER_REQUEST_MODEL, clientBankTransferRequest);
        context.startService(intent);
    }

    public static void startActionClientSendPasscode(Context context, ClientSendPassCodeRequest clientSendPassCodeRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_CLIENT_SEND_PASSCODE);
        intent.putExtra(PARAM_CLIENT_SEND_PASSCODE_REQUEST_MODEL, clientSendPassCodeRequest);
        context.startService(intent);
    }

    public static void startActionClientResetPasscode(Context context, ClientResetPassCodeRequest clientResetPassCodeRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_CLIENT_RESET_PASSCODE);
        intent.putExtra(PARAM_CLIENT_RESET_PASSCODE_REQUEST_MODEL, clientResetPassCodeRequest);
        context.startService(intent);
    }

    public static void startActionUploadClientProfilePicture(Context context, UploadClientPictureRequest uploadClientPictureRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_UPLOAD_CLIENT_PROFILE_PICTURE);
        intent.putExtra(PARAM_UPLOAD_CLIENT_PROFILE_PICTURE_REQUEST_MODEL, uploadClientPictureRequest);
        context.startService(intent);
    }

    public static void startActionPayUPaymentGateway(Context context, PayUPaymentGatewayRequest payUPaymentGatewayRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_CLIENT_PAYU_PAYMENT_GATEWAY);
        intent.putExtra(PARAM_CLIENT_PAYU_PAYMENT_GATEWAY_REQUEST_MODEL, payUPaymentGatewayRequest);
        context.startService(intent);
    }

    public static void startActionAddBeneficiary(Context context, BeneficiaryBean beneficiaryBean) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_ADD_BENEFICIARY);
        intent.putExtra(PARAM_ADD_BENEFICIARY_REQUEST_MODEL, beneficiaryBean);
        context.startService(intent);
    }

    public static void startActionUploadKyc(Context context, ArrayList<KYCDocumentsBean> kycDocumentsBeanArrayList, File identityImagePath, File addressImagePath) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_ADD_KYC);
        intent.putExtra(PARAM_ADD_KYC_REQUEST_MODEL, kycDocumentsBeanArrayList);
        intent.putExtra("IdImagePath", identityImagePath);
        intent.putExtra("AddressImagePath", addressImagePath);
        context.startService(intent);
    }

    public static void startActionGetBankList(Context context) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_GET_BANK_LIST);
        context.startService(intent);
    }

    public static void startActionGetCityList(Context context) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_GET_CITY_LIST);
        context.startService(intent);
    }
    public static void startActionGetOfferNerar(Context context) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_NERAR_OFFERBY_SUBMIT);
        context.startService(intent);
    }

    public static void startActionGetStateList(Context context) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_GET_STATE_LIST);
        context.startService(intent);
    }

    public static void startActionGetFoodOrderStatus(Context context, FoodOrderStatusRequest foodOrderStatusRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_FOOD_ORDER_STATUS);
        intent.putExtra(PARAM_FOOD_ORDER_STATUS_REQUEST_MODEL, foodOrderStatusRequest);
        context.startService(intent);
    }
    public static void startActionGetFoodOrderStatusDetails(Context context, FoodStatusDerailRequest foodStatusDerailRequest) {
        Intent intent = new Intent(context, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_FOOD_ORDER_STATUS_DETAIL);
        intent.putExtra(PARAM_FOOD_ORDER_STATUS_DETAIL_REQUEST_MODEL, foodStatusDerailRequest);
        context.startService(intent);
    }

    public static void startActionCallOTPDetails(Context mContext, String clientMobile) {
        Intent intent = new Intent(mContext, MobicashIntentService.class);
        intent.setAction(ACTION_PERFORM_OTP_STATUS_DETAIL);
        intent.putExtra(PARAM_OTP_STATUS_DETAIL_REQUEST_MODEL, clientMobile);
        mContext.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PERFORM_USER_SIGN_UP.equals(action)) {
                handleActionUserSignUp(intent);
            }else if (ACTION_PERFORM_MAC_UPDATE_STATUS.equals(action)) {
                handleActionMacAddressUpated(intent);
            }else if (ACTION_PERFORM_OTP_STATUS_DETAIL.equals(action)) {
                handleActionUserOTP(intent);
            }else if (ACTION_PERFORM_CONFIRMATION_OTP_STATUS_DETAIL.equals(action)) {
                handleActionUserConfirmationOTP(intent);
            } else if(ACTION_PERFORM_WIFYEE_REGISTRATION.equals(action)){
                handleActionWifyeeUserSignUp(intent);
            }else if(ACTION_PERFORM_WIFYEE_CONNECTION.equals(action)){
                handleActionWifyeeUserConnection(intent);
            } else if (ACTION_PERFORM_USER_LOGIN.equals(action)) {
                handleActionUserLogin(intent);
            } else if (ACTION_PERFORM_CLIENT_TO_CLIENT_MONEY_TRANSFER.equals(action)) {
                handleActionClientToClientMoneyTransfer(intent);
            } else if (ACTION_PERFORM_CLIENT_STATUS.equals(action)) {
                handleActionClientStatus(intent);
            } else if (ACTION_PERFORM_CLIENT_BALANCE.equals(action)) {
                handleActionClientBalance(intent);
            } else if (ACTION_PERFORM_AIRTIME_RECHARGE.equals(action)) {
                handleActionAirtimeRecharge(intent);
            } else if (ACTION_PERFORM_OPERATOR_LIST.equals(action)) {
                handleActionOperatorList(intent);
            } else if (ACTION_PERFORM_CLIENT_LOG.equals(action)) {
                handleActionClientLog(intent);
            } else if (ACTION_PERFORM_CLIENT_PROFILE_UPDATE.equals(action)) {
                handleActionClientProfileUpdate(intent);
            } else if (ACTION_PERFORM_GET_CLIENT_PROFILE.equals(action)) {
                handleActionGetClientProfile(intent);
            } else if (ACTION_PERFORM_GET_CLIENT_PROFILE_INFO.equals(action)) {
                handleActionGetClientProfileInfo(intent);
            } else if (ACTION_PERFORM_CLIENT_BANK_TRANSFER.equals(action)) {
                handleActionClientBankTransfer(intent);
            } else if (ACTION_PERFORM_CLIENT_SEND_PASSCODE.equals(action)) {
                handleActionClientSendPasscode(intent);
            } else if (ACTION_PERFORM_CLIENT_RESET_PASSCODE.equals(action)) {
                handleActionClientResetPasscode(intent);
            } else if (ACTION_PERFORM_UPLOAD_CLIENT_PROFILE_PICTURE.equals(action)) {
                handleActionUploadProfilePicture(intent);
            }
            else if (ACTION_PERFORM_MEDICINE_ORDER.equals(action)) {
                handleActionUploadMedicine(intent);
            }
            else if (ACTION_PERFORM_CLIENT_PAYU_PAYMENT_GATEWAY.equals(action))
            {
                handleActionClientPayUPaymentGateway(intent);
            }else if (ACTION_PERFORM_ADD_BENEFICIARY.equals(action)) {
                handleActionAddBeneficiary(intent);
            }else if (ACTION_PERFORM_ADD_KYC.equals(action)) {
                handleActionAddKYC(intent);
            }
            else if (ACTION_GET_BANK_LIST.equals(action)) {
                handleActionGetBankList(intent);
            }
            else if (ACTION_GET_CITY_LIST.equals(action)) {
                handleActionGetCityList(intent);
            }
            else if (ACTION_PERFORM_NERAR_OFFERBY_SUBMIT.equals(action)) {
                handleActionNear_Offer(intent);
            }

            else if (ACTION_GET_STATE_LIST.equals(action)) {
                handleActionGetStateList(intent);
            }
            else if (ACTION_PERFORM_WI_FI.equals(action)) {
                handleActionWiFiPlans(intent);
            }
            else if (ACTION_PERFORM_OFFERS.equals(action)) {
                handleActionWiFiOffers(intent);
            }
            else if (ACTION_PERFORM_WI_FI_PAYMENT.equals(action)) {
                handleActionWiFiPlansPurchase(intent);
            }

            else if (ACTION_PERFORM_INSTA_MOJO_PAYMENT.equals(action)) {
                handleActionInstaMojoPayment(intent);
            }
            else if (ACTION_PERFORM_DEDUCT_MONEY_WALLET.equals(action)) {
                handleActionDeductWalletBalance(intent);
            }
            else if (ACTION_PERFORM_ADD_MONEY_WALLET.equals(action)) {
                handleActionAddWalletBalance(intent);
            }
            else if (ACTION_PERFORM_USER_SPLIT.equals(action)) {
                handleActionSplitBalance(intent);
            }
            else if (ACTION_PERFORM_CLIENT_PAYMENT.equals(action)) {
                handleActionGetClientPayment(intent);
            }
            else if (ACTION_PERFORM_SEND_MONEY.equals(action)) {
                handleActionGetSendMoney(intent);
            }
            else if (ACTION_PERFORM_CLIENT_TO_CLIENT_WALLET.equals(action)) {
                handleActionSendMoneyCLientToWallet(intent);
            }
            else if (ACTION_PERFORM_FOODORDER_LIST.equals(action)) {
                handleActionFoodorderList(intent);
            }else if (ACTION_PERFORM_FOODORDER_BYMERCHANT_LIST.equals(action)) {
                handleActionFoodorderListBylocation(intent);
            }
            else if (ACTION_PERFORM_FOODORDER_SUBMIT.equals(action)) {
                handleActionFoodorderListByMerchant(intent);
            }
            else if (ACTION_PERFORM_ALLFOODORDER_LIST.equals(action)) {
                handleActionAllFoodorderList(intent);
            }
            else if (ACTION_PERFORM_GSTONFOOD_LIST.equals(action)) {
                handleActionGSTOnFoodorder(intent);
            }
            else if (ACTION_PERFORM_FOODORDER_BYPARENTSLUG_LIST.equals(action)) {
                handleActionFoodorderListByParentSlug(intent);
            }

            else if(ACTION_PERFORM_FOOD_ORDER_STATUS.equals(action)){
                handleActionFoodOrderStatus(intent);
            }
            else if(ACTION_PERFORM_FOOD_ORDER_STATUS_DETAIL.equals(action)){
                handleActionFoodOrderStatusDetails(intent);
            }else if(ACTION_PERFORM_VERIFY_MOBILE_NUMBER.equals(action))
            {
                handleActionVerifyMobileNumber(intent);
            }
        }
    }

    private void handleActionUserOTP(Intent intent) {
        String  mClientMobile = (String) intent.getSerializableExtra(PARAM_OTP_STATUS_DETAIL_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile",mClientMobile);
            jsonObject.put("user_type","client");

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                . writeTimeout(3, TimeUnit.MINUTES)
                .build();

        AndroidNetworking.post(NetworkConstant.SEND_OTP_AUTHENTICATION)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_OTP_STATUS_DETAIL)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("-Miss OTP-",response.toString());
                        Timber.e("called onResponse of User payment API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                OTP_Response mOTP_Response = ModelMapper.transformJSONObjectToUserOTpResponse(response);
                               /* String code=response.getString("code");
                                String timeFrom=response.getString("timeFrom");
                                String mobile=response.getString("mobile");*/
                                Timber.d("Got Success handleActionUserOTP...");
                                Timber.d("handleActionUserOTP = > JSONObject response ==>" + new Gson().toJson(response));
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mOTP_Response);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_OTP_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }

                            else {
                                Timber.d("Got failure in PaymentResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionUserOTP = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_OTP_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of User Payment API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());
                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionUserOTP = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_USER_OTP_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }
    private void handleActionUserConfirmationOTP(Intent intent) {
        OTP_Response  mOTPOtp_response = (OTP_Response) intent.getSerializableExtra(PARAM_CONFIRMATION_OTP_STATUS_DETAIL_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile",mOTPOtp_response.mobile);
            jsonObject.put("code",mOTPOtp_response.code);
            jsonObject.put("timeFrom",mOTPOtp_response.timefrom);
            jsonObject.put("userId", mOTPOtp_response.userId);
            jsonObject.put("userType","client");

            Log.e("in authenticate",jsonObject.toString());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                . writeTimeout(3, TimeUnit.MINUTES)
                .build();
        //AndroidNetworking.initialize(getApplicationContext(),okHttpClient);
        //System.out.println("handleActionUserConfirmationOTP Request"+jsonObject);
        AndroidNetworking.post(NetworkConstant.SEND_OTP_AUTHENTICATION_COMPLETE)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_CONFIRMATION_OTP_STATUS_DETAIL)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of User payment API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                // OTP_Response mOTP_Response = ModelMapper.transformJSONObjectToUserOTpResponse(response);
                               /* String code=response.getString("code");
                                String timeFrom=response.getString("timeFrom");
                                String mobile=response.getString("mobile");*/
                                Timber.d("Got Success handleActionUserOTP...");
                                Timber.d("handleActionUserOTP = > JSONObject response ==>" + new Gson().toJson(response));
                                Intent broadcastIntent = new Intent();
                                //broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_CONFIRMATION_OTP_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }

                            else {
                                Timber.d("Got failure in PaymentResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionUserOTP = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_CONFIRMATION_OTP_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of User Payment API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());
                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionUserOTP = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_USER_CONFIRMATION_OTP_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }

    //Handle Action Insta Mojo Payments
    private void handleActionInstaMojoPayment(Intent intent)
    {
        InstaMojoRequest mInstaMojoPaymentGatewayRequest = (InstaMojoRequest) intent.getSerializableExtra(PARAM_INSTA_MOJO_PAYMENT_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginType",mInstaMojoPaymentGatewayRequest.getLoginType());
            jsonObject.put("amount",mInstaMojoPaymentGatewayRequest.getAmount());
            jsonObject.put("buyerMobile",mInstaMojoPaymentGatewayRequest.getBuyerMobile());
            jsonObject.put("buyerEmail",mInstaMojoPaymentGatewayRequest.getBuyerEmail());
            jsonObject.put("responseUrl",mInstaMojoPaymentGatewayRequest.getResponseUrl());
            jsonObject.put("buyerName",mInstaMojoPaymentGatewayRequest.getBuyerName());
        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                . writeTimeout(5, TimeUnit.MINUTES)
                .build();

        AndroidNetworking.post(NetworkConstant.INSTA_MOJO_PAYMENT_GATEWAY_REQUEST)
                .addJSONObjectBody(jsonObject)
                .setOkHttpClient(okHttpClient)
                .setTag(TAG_PERFORM_INSTA_MOJO_PAYMENT)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of User payment API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0)
                            {
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_INSTA_MOJO_PAYMENT_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in PaymentResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionUserpayment = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_INSTA_MOJO_PAYMENT_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of User Payment API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());
                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionPayment = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_USER_INSTA_MOJO_PAYMENT_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }

    //Handle Action Upload medicine
    private void handleActionUploadMedicine(Intent intent)
    {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                . writeTimeout(5, TimeUnit.MINUTES)
                .build();

        MedicineOrderModel mUploadMedicineRequest = (MedicineOrderModel) intent.getSerializableExtra(PARAM_MEDICINE_ORDER_REQUEST_MODEL);
        AndroidNetworking.upload(NetworkConstant.USER_MEDICINE_UPLOAD_END_POINT)
                .addMultipartFile("priscriptionDocument", mUploadMedicineRequest.priscriptionDocument)
                .addMultipartParameter("userId", mUploadMedicineRequest.getUserId())
                .addMultipartParameter("userType", mUploadMedicineRequest.getUserType())
                .addMultipartParameter("lotLoc", mUploadMedicineRequest.getLotLoc()).
                addMultipartParameter("longLoc",mUploadMedicineRequest.getLongLoc())
                .setTag(TAG_PERFORM_MEDICINE_ORDER)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .setExecutor(Executors.newSingleThreadExecutor()) // setting an executor to get response or completion on that executor thread
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                        Timber.e("bytesUploaded ====> " + bytesUploaded);
                        Timber.e("totalBytes ====> " + totalBytes);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of Upload Client Profile Picture API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                //UploadClientProfilePictureResponse mUploadClientProfilePictureResponse = ModelMapper.transformJSONObjectToUploadClientProfilePictureResponse(response);
                                Timber.d("Got Success UploadMedicineOrderResponse...");
                                Timber.d("handleActionUploadMedicine = > JSONObject response ==>" + new Gson().toJson(response));
                                //  Timber.d("handleActionUploadMedicine = > UploadMedicineOrderResponse mUploadMedicineOrderResponse ==>" + new Gson().toJson(mUploadClientProfilePictureResponse));
                                Intent broadcastIntent = new Intent();
                                //  broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mUploadClientProfilePictureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_UPLOAD_MEDICINE_ORDER_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in handleActionUploadMedicineResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionUploadMedicinePicture = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_UPLOAD_MEDICINE_ORDER_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of handleActionUploadMedicine API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());
                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionUploadMedicine = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_UPLOAD_MEDICINE_ORDER_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }
    //Handle Action Add Kyc
    private void handleActionAddKYC(Intent intent)
    {
        File idPath= (File) intent.getSerializableExtra("IdImagePath");
        File addressPath= (File) intent.getSerializableExtra("AddressImagePath");
        ArrayList<KYCDocumentsBean> mUploadAddKycRequest = (ArrayList<KYCDocumentsBean>) intent.getSerializableExtra(PARAM_ADD_KYC_REQUEST_MODEL);
        JSONArray itemSelectedJson = new JSONArray();
        try {
            for (int i = 0; i < mUploadAddKycRequest.size(); i++) {
                JSONObject jsonObjectKyc = new JSONObject();
                jsonObjectKyc.put("AID", mUploadAddKycRequest.get(i).getAID());
                jsonObjectKyc.put("CUSTOMER_MOBILE", mUploadAddKycRequest.get(i).getCUSTOMER_MOBILE());
                jsonObjectKyc.put("CUSTOMER_NAME", mUploadAddKycRequest.get(i).getCUSTOMER_NAME());
                jsonObjectKyc.put("OP", mUploadAddKycRequest.get(i).getOP());
                jsonObjectKyc.put("ST", mUploadAddKycRequest.get(i).getST());
                jsonObjectKyc.put("documentFile", mUploadAddKycRequest.get(i).getDocumentFile());
                jsonObjectKyc.put("documentName", mUploadAddKycRequest.get(i).getDocumentName());
                jsonObjectKyc.put("documentType", mUploadAddKycRequest.get(i).getDocumentType());
                jsonObjectKyc.put("proofNumber", mUploadAddKycRequest.get(i).getProofNumber());
                itemSelectedJson.put(jsonObjectKyc);
            }
            // jsonObject.put(ResponseAttributeConstants.SPLIT_USER_PARTICIPIENTS, itemSelectedJson);
            Log.e("Getting Json Value", "Getting Json Object Value" + itemSelectedJson);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                . writeTimeout(5, TimeUnit.MINUTES)
                .build();

        AndroidNetworking.upload(NetworkConstant.ADD_KYC_TO_SERVER)
                .addMultipartFile(ResponseAttributeConstants.ADD_KYC_IDENTITY_IMAGE, idPath)
                .addMultipartFile(ResponseAttributeConstants.ADD_KYC_ADDRESS_IMAGE,addressPath)
                .addMultipartParameter(ResponseAttributeConstants.ADD_KYC_DOCUMENTS,itemSelectedJson.toString())
                .setTag(TAG_ADD_KYC)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .setExecutor(Executors.newSingleThreadExecutor()) // setting an executor to get response or completion on that executor thread
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                        Timber.e("bytesUploaded ====> " + bytesUploaded);
                        Timber.e("totalBytes ====> " + totalBytes);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of Upload Client Add Kyc API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                // UploadClientProfilePictureResponse mUploadClientProfilePictureResponse = ModelMapper.transformJSONObjectToUploadClientProfilePictureResponse(response);
                                Timber.d("Got Success ClientADDKycResponse...");
                                Timber.d("handleActionADDKyc = > JSONObject response ==>" + new Gson().toJson(response));
                                //  Timber.d("handleActionUploadADDKyc = > UploadClientADDKycResponse mUploadClientADDKycResponse ==>" + new Gson().toJson(mUploadClientProfilePictureResponse));
                                Intent broadcastIntent = new Intent();
                                // broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mUploadClientProfilePictureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_UPLOAD_ADD_KYC_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in UploadClientAddKYCResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionUploadADDKycPicture = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_UPLOAD_CLIENT_ADD_KYC_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Upload KYC API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionUploadKYC = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_UPLOAD_CLIENT_ADD_KYC_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }

    //Handle Ordering Food with GST
    private void handleActionGSTOnFoodorder(Intent paramData) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.KEYWORD, "food");
        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                . writeTimeout(5, TimeUnit.MINUTES)
                .build();

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.PARAM_GSTONFOOD_LIST_REQUEST_MODEL)
                .addJSONObjectBody(jsonObject)
                .setOkHttpClient(okHttpClient)
                .setTag(TAG_PERFORM_GSTONFOOD_LIST)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of Gst food item List API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                GstOnFoodItemResponse mGstOnFoodItemResponse = ModelMapper.transformJSONObjectToGstOnFoodOrderResponse(response);
                                Timber.d("Got Success FoodOrderResponse...");

                                Timber.d("handleActionGSTOnFoodorder = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionGSTOnFoodorder = > FoodOrderResponse FoodOrderResponse ==>" + new Gson().toJson(mGstOnFoodItemResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mGstOnFoodItemResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_GSTONFOOD_LIST_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in FoodOrderResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionGSTOnFoodorder = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_GSTONFOOD_LIST_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Foododer List API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionOperatorList = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_GSTONFOOD_LIST_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });

    }
    private void handleActionAllFoodorderList(Intent paramData) {
        FoodOrderRequest mFoodOrderListRequest = (FoodOrderRequest) paramData.getSerializableExtra(PARAM_AllFOODORDER_LIST_REQUEST_MODEL);
        String url = "";
        JSONObject jsonObject = new JSONObject();
        try {
            if (mFoodOrderListRequest.flag.equalsIgnoreCase("voucher")){
                url = NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.PARAM_GET_FOOD_ITEM_BY_VOUCHER_ID;
                jsonObject.put(ResponseAttributeConstants.VOUCHER_ID, mFoodOrderListRequest.voucherId);
            }else {
                url = NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.PARAM_ALLFOODODER_LIST_REQUEST_MODEL;
                jsonObject.put(ResponseAttributeConstants.MERCHANTID, mFoodOrderListRequest.FoodType);
            }
        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();

        AndroidNetworking.post(url)
                .addJSONObjectBody(jsonObject)
                .setOkHttpClient(okHttpClient)
                .setTag(TAG_PERFORM_ALLFOODORDER_LIST)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("All_FOOD_LIST",response.toString());
                        Timber.e("called onResponse of User FoodOder List API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                FoodOrderResponse mFoodOderListResponse = ModelMapper.transformJSONObjectToFoodOrderResponse(response);
                                Timber.d("Got Success FoodOrderResponse...");

                                Timber.d("handleActionFoodorderList = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionFoodorderList = > FoodOrderResponse FoodOrderResponse ==>" + new Gson().toJson(mFoodOderListResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFoodOderListResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_ALLFOODODER_LIST_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in FoodOrderResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionFoodorderList = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_ALLFOODORDER_LIST_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Foododer List API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionOperatorList = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_ALLFOODORDER_LIST_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }

    private void handleActionFoodOrderStatusDetails(Intent paramData) {
        FoodStatusDerailRequest mFoodOrderStatusRequest = (FoodStatusDerailRequest) paramData.getSerializableExtra(PARAM_FOOD_ORDER_STATUS_DETAIL_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.ORDER_ID, mFoodOrderStatusRequest.orderID);


        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                . writeTimeout(5, TimeUnit.MINUTES)
                .build();

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.PARAM_FOODORDER_STATUS_DETAILS_LIST_REQUEST_MODEL)
                .addJSONObjectBody(jsonObject)
                .setOkHttpClient(okHttpClient)
                .setTag(TAG_PERFORM_FOOD_ORDER_STATUS_DETAIL)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of User handleActionFoodOrderStatusDetails  API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                FoodStatusDetailResponse mFoodStatusDetailResponse = ModelMapper.transformJSONObjectToFoodorderStatusDetailResponse(response);
                                Timber.d("Got Success handleActionFoodOrderStatusDetails...");
                                Timber.d("handleActionFoodOrderStatusDetails = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionFoodOrderStatusDetails = > FoodOrderResponse FoodOrderResponse ==>" + new Gson().toJson(response));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFoodStatusDetailResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_FOODODER_STATUS_DEATILS_LIST_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in handleActionFoodOrderStatusDetails...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionFoodOrderStatusDetails = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_FOODORDER_STATUS_DETAILS_LIST_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Foododer List API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionOperatorList = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_FOODORDER_STATUS_LIST_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });

    }

    //Handle Food order Status
    private void handleActionFoodOrderStatus(Intent paramData) {
        FoodOrderStatusRequest mFoodOrderStatusRequest = (FoodOrderStatusRequest) paramData.getSerializableExtra(PARAM_FOOD_ORDER_STATUS_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.USERTYPE, mFoodOrderStatusRequest.UserType);
            jsonObject.put(ResponseAttributeConstants.USERID,mFoodOrderStatusRequest.UserId);

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                . writeTimeout(5, TimeUnit.MINUTES)
                .build();

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.PARAM_FOODORDER_STATUS_LIST_REQUEST_MODEL)
                .addJSONObjectBody(jsonObject)
                .setOkHttpClient(okHttpClient)
                .setTag(TAG_PERFORM_FOOD_ORDER_STATUS)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of User FoodOderStatus  API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                FoodOrderStatusResponse mAddressResponse = ModelMapper.transformJSONObjectToSubmitFoodorderStatusResponse(response);
                                Timber.d("Got Success handleActionFoodOrderStatus...");
                                Timber.d("handleActionFoodOrderStatus = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionFoodOrderStatus = > FoodOrderResponse FoodOrderResponse ==>" + new Gson().toJson(response));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mAddressResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_FOODODER_STATUS_LIST_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in handleActionFoodOrderStatus...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionFoodOrderStatus = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_FOODORDER_STATUS_LIST_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Foododer List API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionOperatorList = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_FOODORDER_STATUS_LIST_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });

    }
    //Handle Food Order By Merchant
    private void handleActionFoodorderListByMerchant(Intent paramData) {
        CartFoodOderRequest mCartFoodOrderRequest = (CartFoodOderRequest) paramData.getSerializableExtra(PARAM_CLIENT_FOODORDER_SUBMIT_REQUEST_MODEL);
        String location = paramData.getStringExtra(EXTRA_PARAM5);
        String lat = paramData.getStringExtra(EXTRA_PARAM6);
        String lng = paramData.getStringExtra(EXTRA_PARAM7);
        String complete_add = paramData.getStringExtra(EXTRA_PARAM8);
        String discountAmt = paramData.getStringExtra(EXTRA_PARAM9);
        String claimType = paramData.getStringExtra(EXTRA_PARAM10);
        String voucherId = paramData.getStringExtra(EXTRA_PARAM11);
        String voucherNo = paramData.getStringExtra(EXTRA_PARAM12);
        String ref_no = paramData.getStringExtra(EXTRA_PARAM13);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.ORDERID, mCartFoodOrderRequest.orderId);
            //  jsonObject.put(ResponseAttributeConstants.ITEM,mCartFoodOrderRequest.items);
            JSONArray array=new JSONArray();

            for(int i=0;i<mCartFoodOrderRequest.items.size();i++){
                JSONObject obj=new JSONObject();
                try {
                    obj.put("item_id",mCartFoodOrderRequest.items.get(i).id);
                    obj.put("quantity",mCartFoodOrderRequest.items.get(i).quantity);
                    obj.put("amount",mCartFoodOrderRequest.items.get(i).amount);
                    Log.e("(JSON)",obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  array.put(obj);
                jsonObject.put(ResponseAttributeConstants.ITEM,array.put(obj));
            }
            jsonObject.put(ResponseAttributeConstants.USER_ID,mCartFoodOrderRequest.userId);
            jsonObject.put(ResponseAttributeConstants.MERCHANT_ID, mCartFoodOrderRequest.merchantId);
            jsonObject.put(ResponseAttributeConstants.USER_TYPE, mCartFoodOrderRequest.userType);
            jsonObject.put(ResponseAttributeConstants.ORDERPRICE,mCartFoodOrderRequest.orderPrice);
            jsonObject.put(ResponseAttributeConstants.ORDER_DATE_TIME,mCartFoodOrderRequest.orderDateTime);
            jsonObject.put(ResponseAttributeConstants.PAY_MODE,mCartFoodOrderRequest.payment_mode);
            jsonObject.put(ResponseAttributeConstants.TUV_ID,mCartFoodOrderRequest.tuvId);
            jsonObject.put(ResponseAttributeConstants.MER_CREDIT_ID,mCartFoodOrderRequest.mcId);
            jsonObject.put(ResponseAttributeConstants.LOCATION,location);
            jsonObject.put(ResponseAttributeConstants.LAT,lat);
            jsonObject.put(ResponseAttributeConstants.LNG,lng);
            jsonObject.put(ResponseAttributeConstants.COMPLETE_ADD,complete_add);
            jsonObject.put(ResponseAttributeConstants.DISCOUNT_AMT,discountAmt);
            jsonObject.put(ResponseAttributeConstants.CLAIM_TYPE,claimType);
            jsonObject.put(ResponseAttributeConstants.VOUCHER_ID,voucherId);
            jsonObject.put(ResponseAttributeConstants.VOUCHER_NO,voucherNo);
            jsonObject.put(ResponseAttributeConstants.FOOD_WIFYEE_COMMISION,mCartFoodOrderRequest.wifyeeCommision);
            jsonObject.put(ResponseAttributeConstants.FOOD_DIST_COMMISION,mCartFoodOrderRequest.distCommision);
            jsonObject.put(ResponseAttributeConstants.GST_AMOUNT,mCartFoodOrderRequest.gstAmount);
            jsonObject.put(ResponseAttributeConstants.DELIVERY_AMT,mCartFoodOrderRequest.deliveryFee);
            jsonObject.put(ResponseAttributeConstants.SUB_TOTAL,String.valueOf(mCartFoodOrderRequest.subTotal));
            if(mCartFoodOrderRequest.payment_mode.equalsIgnoreCase("wallet")){
                jsonObject.put(ResponseAttributeConstants.MOBILE_NUMBER,LocalPreferenceUtility.getUserMobileNumber(ctx));
                jsonObject.put(ResponseAttributeConstants.PIN_CODE,MobicashUtils.md5(LocalPreferenceUtility.getUserPassCode(ctx)));
                jsonObject.put(ResponseAttributeConstants.DESCRIPTION,"food_order");
            }if(mCartFoodOrderRequest.payment_mode.equalsIgnoreCase("online")){
                jsonObject.put(ResponseAttributeConstants.RefId,ref_no);
            }


            Log.e("order param",jsonObject.toString());
        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                . writeTimeout(5, TimeUnit.MINUTES)
                .build();

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.PARAM_FOODORDERLIST_BYMERCHANT_LIST_REQUEST_MODEL)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_FOODORDER_SUBMIT)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of User FoodOder By Location List API.");
                        Log.e("food_res",response.toString());
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                CartFoodOrderResponse mAddressResponse = ModelMapper.transformJSONObjectToSubmitFoodorderResponse(response);
                                Timber.d("Got Success handleActionFoodorderListByMerchant...");
                                Timber.d("handleActionFoodorderListByMerchant = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionFoodorderListByMerchant = > FoodOrderResponse FoodOrderResponse ==>" + new Gson().toJson(response));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mAddressResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_FOODODER_BYMERCHANT_LIST_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in handleActionFoodorderListByMerchant...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionFoodorderListByMerchant = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_FOODORDER_BYMERCHANT_LIST_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Foododer List API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionOperatorList = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_FOODORDER_BYPARENTSLUG_LIST_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });

    }
    //Handle Food Order by Location
    private void handleActionFoodorderListBylocation(Intent paramData) {
        AddressRequest mAddressRequest = (AddressRequest) paramData.getSerializableExtra(PARAM_FOODORDER_BYMERCHANT_LIST_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.LATITUDE, mAddressRequest.latitude);
            jsonObject.put(ResponseAttributeConstants.LONGITUDE,mAddressRequest.longitude);
            jsonObject.put(ResponseAttributeConstants.ZIP_CODE,mAddressRequest.pincode);
            //jsonObject.put(ResponseAttributeConstants.DISTANCE,mAddressRequest.distance);
        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }

        Log.e("jsn",jsonObject.toString());
        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.PARAM_FOODORDER_BYMERCHANT_LIST_REQUEST_MODEL)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_FOODORDER_BMERCHANT_LIST)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of User FoodOder By Location List API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                AddressResponse mAddressResponse = ModelMapper.transformJSONObjectToMenuByLocationResponse(response);
                                Timber.d("Got Success handleActionFoodorderListBylocation...");

                                Timber.d("handleActionFoodorderListBylocation = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionFoodorderListBylocation = > FoodOrderResponse FoodOrderResponse ==>" + new Gson().toJson(response));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mAddressResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_FOODODER_BYMERCHANT_LIST_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in MenuByParentSlugResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionFoodorderListBylocation = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_FOODORDER_BYMERCHANT_LIST_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Foododer List API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());
                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionOperatorList = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_FOODORDER_BYPARENTSLUG_LIST_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });

    }
    //Handle Food OrderList Parent Slug
    private void handleActionFoodorderListByParentSlug(Intent paramData) {
        MenuByParentSlugRequest mMenuByParentSlugRequest = (MenuByParentSlugRequest) paramData.getSerializableExtra(PARAM_FOODORDER_BYPARENTSLUG_LIST_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.CATEGORY, mMenuByParentSlugRequest.Category);
        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.PARAM_FOODORDER_BYPARENTSLUG_LIST_REQUEST_MODEL)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_FOODORDER_BYPARENTSLUG_LIST)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of User FoodOder By ParentSlug List API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                MenuByParentSlugResponse mMenuByParentSlugResponse = ModelMapper.transformJSONObjectToMenuByParentSlugResponse(response);
                                Timber.d("Got Success MenuByParentSlugResponse...");

                                Timber.d("handleActionFoodorderListByParentSlug = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionFoodorderListByParentSlug = > FoodOrderResponse FoodOrderResponse ==>" + new Gson().toJson(response));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mMenuByParentSlugResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_FOODODER_BYPARENTSLUG_LIST_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in MenuByParentSlugResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionFoodorderListByParentSlug = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_FOODORDER_BYPARENTSLUG_LIST_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Foododer List API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionOperatorList = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_FOODORDER_BYPARENTSLUG_LIST_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });

    }
    //Handle Food Order List
    private void handleActionFoodorderList(Intent paramData) {
        FoodOrderRequest mFoodOrderListRequest = (FoodOrderRequest) paramData.getSerializableExtra(PARAM_FOODORDER_LIST_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.CATEGORY, mFoodOrderListRequest.FoodType);
            jsonObject.put(ResponseAttributeConstants.MERCHANTID, mFoodOrderListRequest.MerchantId);
        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.PARAM_FOODODER_LIST_REQUEST_MODEL)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_FOODORDER_LIST)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("--VegNonVeg--",response.toString());
                        Timber.e("called onResponse of User FoodOder List API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                FoodOrderResponse mFoodOderListResponse = ModelMapper.transformJSONObjectToVegOrNonVegFoodOrderResponse(response);
                                Timber.d("Got Success FoodOrderResponse...");

                                Timber.d("handleActionFoodorderList = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionFoodorderList = > FoodOrderResponse FoodOrderResponse ==>" + new Gson().toJson(mFoodOderListResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFoodOderListResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_FOODODER_LIST_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in FoodOrderResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionFoodorderList = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_FOODORDER_LIST_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Foododer List API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionOperatorList = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_FOODORDER_LIST_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });

    }


    //Call Client to Client Wallet
    private void handleActionSendMoneyCLientToWallet(Intent intent)
    {
        SendMoney mSendMoneyRequest = (SendMoney) intent.getSerializableExtra(PARAM_CLIENT_TO_CLIENT_WALLET_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("clientsender",mSendMoneyRequest.getClientsender());
            jsonObject.put("clientreceiver",mSendMoneyRequest.getClientreceiver());
            jsonObject.put("amount",mSendMoneyRequest.getAmount());
            jsonObject.put("hash",mSendMoneyRequest.getHash());
        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        AndroidNetworking.post(NetworkConstant.SEND_MONEY_CLIENT_TO_CLIENT_API)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_CLIENT_TO_CLIENT_WALLET)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of Client Payment API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                Timber.d("Got Success MerchantPaymentResponse...");
                                Timber.d("handleActionClientPaymen = > JSONObject response ==>" + new Gson().toJson(response));
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.setAction(NetworkConstant.STATUS_SEND_MONEY_CLIENT_WALLET_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionPayment = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_SEND_CLIENT_WALLET_MONEY_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(MobicashIntentService.this,"You have Insufficient Balance",Toast.LENGTH_SHORT).show();
                        // handle error
                        Timber.e("called onError of Payment API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionPayment = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_SEND_CLIENT_WALLET_MONEY_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }

    //Call Send Money To Client Api
    private void handleActionGetSendMoney(Intent paramData)
    {
        SendMoney mSendMoneyRequest = (SendMoney) paramData.getSerializableExtra(PARAM_SEND_MONEY_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("clientsender",mSendMoneyRequest.getClientsender());
            jsonObject.put("clientreceiver",mSendMoneyRequest.getClientreceiver());
            jsonObject.put("amount",mSendMoneyRequest.getAmount());
            jsonObject.put("hash",mSendMoneyRequest.getHash());
        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        AndroidNetworking.post(NetworkConstant.SEND_MONEY_TO_CLIENT_API)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_SEND_MONEY)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of Client Payment API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0)
                            {
                                SendMoneyResponse clientSendMoneyResponse = ModelMapper.transformJSONObjectToSendMoneyResponse(response);
                                Timber.d("Got Success MerchantPaymentResponse...");
                                Timber.d("handleActionClientPaymen = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionClientPaymen = > ClientPaymenResponse MerchantPaymentResponse ==>" + new Gson().toJson(clientSendMoneyResponse));
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, clientSendMoneyResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_SEND_MONEY_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionPayment = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_SEND_MONEY_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(MobicashIntentService.this,"You have Insufficient Balance",Toast.LENGTH_SHORT).show();
                        // handle error
                        Timber.e("called onError of Payment API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionPayment = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_SEND_MONEY_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }
    //Add Value Client to merchant
    //call Client Payment  Api
    private void handleActionGetClientPayment(Intent paramData) {
        MerchantRequest mMerchnatRequest = (MerchantRequest) paramData.getSerializableExtra(PARAM_CLIENT_PAYMENT_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.MOBILE_NUMBER, mMerchnatRequest.mobile);
            jsonObject.put(ResponseAttributeConstants.MERCHANT_ID, mMerchnatRequest.merchantId);
            jsonObject.put(ResponseAttributeConstants.AMOUNT, mMerchnatRequest.amount);
            jsonObject.put(ResponseAttributeConstants.HASH, mMerchnatRequest.hash);
        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_CLIENT_PAYMENT_END_POINT)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_CLIENT_PAYMENT_SUCCESS)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of Client Payment API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                MerchantPaymentResponse merchantPaymentResponse = ModelMapper.transformJSONObjectToMerchantPaymentResponse(response);
                                Timber.d("Got Success MerchantPaymentResponse...");
                                Timber.d("handleActionClientPaymen = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionClientPaymen = > ClientPaymenResponse MerchantPaymentResponse ==>" + new Gson().toJson(merchantPaymentResponse));
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, merchantPaymentResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_MERCHANT_PAYMENT_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in MerchantPaymentResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionMerchantPayment = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_MERCHANT_PAYMENT_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        Toast.makeText(MobicashIntentService.this,"You have Insufficient Balance",Toast.LENGTH_SHORT).show();
                        // handle error
                        Timber.e("called onError of MerchantPayment API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());
                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionMerchantPayment = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_CLIENT_RESET_PASSCODE_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }

    //Handle User Sign Up
    private void handleActionUserSignUp(Intent paramData) {
        SignupRequest mSignupRequest = (SignupRequest) paramData.getSerializableExtra(PARAM_SIGNUP_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.HASH, mSignupRequest.hash);
            jsonObject.put(ResponseAttributeConstants.CLIENT_MOBILE_PARAMETER, mSignupRequest.clientmobile);
            jsonObject.put(ResponseAttributeConstants.EMAIL, mSignupRequest.email);
            jsonObject.put(ResponseAttributeConstants.FIRST_NAME, mSignupRequest.firstname);
            jsonObject.put(ResponseAttributeConstants.LAST_NAME, mSignupRequest.lastname);
            jsonObject.put(ResponseAttributeConstants.PIN_CODE, mSignupRequest.pincode);
            jsonObject.put(ResponseAttributeConstants.MAC, MobicashUtils.getMacAddress(this));
            //  jsonObject.put(ResponseAttributeConstants.MAC, MobicashUtils.getMacAddress(this));
            jsonObject.put(ResponseAttributeConstants.CUSTOMER_AADDRESS, mSignupRequest.custAddess);
            jsonObject.put(ResponseAttributeConstants.CUSTOMER_TITLE, mSignupRequest.customerTitle);
            /// jsonObject.put(ResponseAttributeConstants.CUSTOMER_DOB, mSignupRequest.customerDOB);

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }

        Log.e("regJson",jsonObject.toString());

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                . writeTimeout(3, TimeUnit.MINUTES)
                .build();
        //AndroidNetworking.initialize(getApplicationContext(),okHttpClient);

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_SIGNUP_END_POINT)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_USER_SIGN_UP)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of User SignUp API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                SignupResponse mSignupResponse = ModelMapper.transformJSONObjectToSignupResponse(response);
                                Timber.d("Got Success SignupResponse...");
                                Timber.w("handleActionUserSignUp = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.w("handleActionUserSignUp = > SignupResponse mSignupResponse ==>" + new Gson().toJson(mSignupResponse));
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mSignupResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_SIGNUP_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);

                            } else {
                                Timber.d("Got failure in SignupResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionUserSignUp = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_SIGNUP_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of User SignUp API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());
                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionUserSignUp = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_USER_SIGNUP_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });

    }

    //Handle Mac Address update
    private void handleActionMacAddressUpated(Intent paramData) {
        MacAddressUpdate mMacAddressUpdate = (MacAddressUpdate) paramData.getSerializableExtra(PARAM_MAC_UPDATE_STATUS_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.HASH, mMacAddressUpdate.hash);
            jsonObject.put(ResponseAttributeConstants.MOBILE_NUMBER_PLAN, mMacAddressUpdate.mobileNumbers);
            jsonObject.put(ResponseAttributeConstants.MAC_ADDRESSS, MobicashUtils.getMacAddress(this));

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                . writeTimeout(3, TimeUnit.MINUTES)
                .build();

        AndroidNetworking.post( NetworkConstant.USER_MAC_ADDRESS_END_POINT)
                .addJSONObjectBody(jsonObject)
                .setOkHttpClient(okHttpClient)
                .setTag(TAG_PERFORM_MAC_UPDATE_STATUS)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of User SignUp API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                MacUpdateAddressResponse mMacUpdateAddressResponse = ModelMapper.transformJSONObjectToMACUpdateResponse(response);
                                Timber.d("Got Success handleActionMacAddressUpated...");
                                Timber.w("handleActionMacAddressUpated = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.w("handleActionMacAddressUpated = > handleActionMacAddressUpated handleActionMacAddressUpated ==>" + new Gson().toJson(mMacUpdateAddressResponse));
                                System.out.println("mSignupResponse:"+mMacUpdateAddressResponse);
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mMacUpdateAddressResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_MAC_ADDRESS_UPDATE_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);

                            } else {
                                Timber.d("Got failure in handleActionMacAddressUpated...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionMacAddressUpated = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_MAC_ADDRESS_UPDATE_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of User SignUp API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());
                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionMacAddressUpated = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_MAC_ADDRESS_UPDATE_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });

    }

    /**
     * start wifyee registration request
     * @param paramData
     */
    private void handleActionWifyeeUserSignUp(Intent paramData) {
        String parcel = paramData.getStringExtra(PARAM_SIGNUP_REQUEST_MODEL_WIFYEE);
        try {
            AndroidNetworking.post(WifiConstant.WIFYEE_REG_BASE_URL)
                    .addJSONObjectBody(new JSONObject(parcel))
                    .setTag(TAG_PERFORM_WIFYEE_USER_REGISTRATION)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("AfterReg2",response.toString());
                            Timber.e("called onResponse of wifee User SignUp API.");
                            try {
                                if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                    Timber.d("Got Success wifee SignupResponse...");
                                    Timber.w("handleActionUserSignUp = > JSONObject response ==>" + new Gson().toJson(response));
                                } else {
                                    Timber.d("Got failure in wifyee SignupResponse...");
                                }
                            } catch (JSONException e) {
                                Timber.e("JSONException Caught.  Message : " + e.getMessage());
                            }

                        }
                        @Override
                        public void onError(ANError error) {
                            // handle error
                            Timber.e("called onError of Wifyee User SignUp API.");
                            Timber.e("Error Message : " + error.getMessage());
                            Timber.e("Error code : " + error.getErrorCode());
                            Timber.e("Error Body : " + error.getErrorBody());
                            Timber.e("Error Detail : " + error.getErrorDetail());
                            Timber.w("handleActionUserSignUp = > FailureResponse  ==>" + new Gson().toJson(error));
                        }
                    });
        } catch (JSONException e) {
            Toast.makeText(this,"Fatal Json Exception",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * start wifyee connection request
     * @param paramData
     */
    private void handleActionWifyeeUserConnection(Intent paramData) {
        String parcel = paramData.getStringExtra(PARAM_CONN_REQUEST_MODEL_WIFYEE);
        try {
            AndroidNetworking.post(WifiConstant.WIFYEE_CONNECTION_BASE_URL)
                    .addJSONObjectBody(new JSONObject(parcel))
                    .setTag(TAG_PERFORM_WIFYEE_USER_CONNECTION)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Timber.e("called onResponse of wifee User  connection API.");
                            try {
                                if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                    Timber.w("handleActionWifyeeUserConnection = > JSONObject response ==>" + new Gson().toJson(response));
                                    Intent broadcastIntent = new Intent();
                                    broadcastIntent.setAction(NetworkConstant.STATUS_USER_WIFYEE_CONNECTION_SUCCESS);
                                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);

                                } else {
                                    Timber.d("Got failure in wifyee connection...");
                                    Intent broadcastIntent = new Intent();
                                    broadcastIntent.setAction(NetworkConstant.STATUS_USER_WIFYEE_CONNECTION_FAIL);
                                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                                }
                            } catch (JSONException e) {
                                Timber.e("JSONException Caught.  Message : " + e.getMessage());
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            Timber.e("called onError of Wifyee User connection API.");
                            Timber.e("Error Message : " + error.getMessage());
                            Timber.e("Error code : " + error.getErrorCode());
                            Timber.e("Error Body : " + error.getErrorBody());
                            Timber.e("Error Detail : " + error.getErrorDetail());
                            Intent broadcastIntent = new Intent();
                            broadcastIntent.setAction(NetworkConstant.STATUS_USER_WIFYEE_CONNECTION_FAIL);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);

                            Timber.w("handleActionWifyeeUserConnection = > FailureResponse  ==>" + new Gson().toJson(error));
                        }
                    });
        } catch (JSONException e) {
        }
    }

    //Handle payment Success of Plans
    private void handleActionWiFiPlansPurchase(Intent paramData) {
        PlanPaymentRequest mplanPaymentGatewayRequest = (PlanPaymentRequest) paramData.getSerializableExtra(PARAM_WIFI_PLANS_PAYMENT_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.PLAN_ID_ ,mplanPaymentGatewayRequest.plan_id);
            jsonObject.put(ResponseAttributeConstants.PLAN_PAID_AMOUNT, mplanPaymentGatewayRequest.paid_amount);
            jsonObject.put(ResponseAttributeConstants.PLAN_DATE_TIME, mplanPaymentGatewayRequest.paid_on);
            jsonObject.put(ResponseAttributeConstants.PLAN_TAX, mplanPaymentGatewayRequest.paid_tax);
            jsonObject.put(ResponseAttributeConstants.INVOICE_ID, mplanPaymentGatewayRequest.invoice_id);
            jsonObject.put(ResponseAttributeConstants.MAC_ADDRESS, mplanPaymentGatewayRequest.macAddress);
            jsonObject.put(ResponseAttributeConstants.MOBILE_NUMBER_PLAN,mplanPaymentGatewayRequest.mobile);

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.WI_FI_PAYMENT_END_POINT)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_WI_FI_PLANS_PAYMENT)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of User payment API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0)
                            {
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_WI_FI_PAYMENT_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in PaymentResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionUserpayment = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_WI_FI_PAYMENT_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of User Payment API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());
                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionPayment = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_USER_WI_FI_PAYMENT_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }

    //Handle Deduct Wallet Balance Success via food ordering
    private void handleActionDeductWalletBalance(Intent paramData) {
        DeductMoneyWallet madeductMoneyWalletRequest = (DeductMoneyWallet) paramData.getSerializableExtra(PARAM_DEDUCT_MONEY_WALLET_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.ADD_WALLET_AMOUNT ,madeductMoneyWalletRequest.amount);
            jsonObject.put(ResponseAttributeConstants.ADD_WALLET_PINCODE ,MobicashUtils.md5(madeductMoneyWalletRequest.pincode));
            jsonObject.put(ResponseAttributeConstants.ADD_WALLET_CLIENT ,madeductMoneyWalletRequest.clientMobile);
            jsonObject.put(ResponseAttributeConstants.ADD_WALLET_DESCRIPTION ,madeductMoneyWalletRequest.description);
            // jsonObject.put(ResponseAttributeConstants.ADD_WALLET_STATUS ,maddMoneyWalletRequest.status);
            // jsonObject.put(ResponseAttributeConstants.ADD_WALLET_TXN_ID ,maddMoneyWalletRequest.txnId);
            jsonObject.put(ResponseAttributeConstants.ADD_WALLET_HASH ,madeductMoneyWalletRequest.hash);
        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_CLIENT_DEDUCT_MONEY_WALLET)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_DEDUCT_MONEY_WALLET)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of Deduct Money Wallet API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                if(response!= null) {
                                    DeductMoneyWalletResponse deductMoneyWalletResponse = ModelMapper.transformJSONObjectToDeductMoneyPaymentResponse(response);
                                    Intent broadcastIntent = new Intent();
                                    broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, deductMoneyWalletResponse);
                                    broadcastIntent.setAction(NetworkConstant.STATUS_DEDUCTMONEY_WALLET_SUCCESS);
                                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                                }
                                else{
                                    Intent broadcastIntent = new Intent();
                                    broadcastIntent.setAction(NetworkConstant.STATUS_DEDUCTMONEY_WALLET_SUCCESS);
                                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                                }

                            } else {
                                Timber.d("Got failure in Deduct Money Wallet Response...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionDeduct Money Wallet  = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_DEDUCT_MONEY_WALLET_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of User Deduct Money Wallet .");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleDeduct Money Wallet  = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_DEDUCT_MONEY_WALLET_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }

    //Handle Add Wallet Balance Success
    private void handleActionAddWalletBalance(Intent paramData) {
        AddMoneyWallet maaddMoneyWalletRequest = (AddMoneyWallet) paramData.getSerializableExtra(PARAM_ADD_MONEY_WALLET_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.ADD_WALLET_AMOUNT ,maaddMoneyWalletRequest.amount);
            jsonObject.put(ResponseAttributeConstants.ADD_WALLET_PINCODE ,MobicashUtils.md5(maaddMoneyWalletRequest.pincode));
            jsonObject.put(ResponseAttributeConstants.ADD_WALLET_RECIEVER ,maaddMoneyWalletRequest.clientreceiver);
            jsonObject.put(ResponseAttributeConstants.ADD_WALLET_STATUS ,maaddMoneyWalletRequest.status);
            jsonObject.put(ResponseAttributeConstants.ADD_WALLET_TXN_ID ,maaddMoneyWalletRequest.txnId);
            jsonObject.put(ResponseAttributeConstants.ADD_WALLET_HASH ,maaddMoneyWalletRequest.hash);
        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_CLIENT_ADD_MONEY_WALLET_END_POINT)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_ADD_MONEY_WALLET)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("WalletJSON",response.toString());
                        Timber.e("called onResponse of Add Money Wallet API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.setAction(NetworkConstant.STATUS_ADD_MONEY_WALLET_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in Add Money Wallet Response...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionAdd Money Wallet  = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_ADD_MONEY_WALLET_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of User Add Money Wallet .");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleAdd Money Wallet  = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_ADD_MONEY_WALLET_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }

    //Handle user Login
    private void handleActionUserLogin(Intent paramData) {
        LoginRequest mLoginRequest = (LoginRequest) paramData.getSerializableExtra(PARAM_LOGIN_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.CLIENT_MOBILE_PARAMETER, mLoginRequest.clientmobile);
            jsonObject.put(ResponseAttributeConstants.PIN_CODE, mLoginRequest.pincode);
            jsonObject.put(ResponseAttributeConstants.HASH, mLoginRequest.hash);
            jsonObject.put(ResponseAttributeConstants.MAC, MobicashUtils.getMacAddress(this));
        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_LOGIN_END_POINT)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_USER_LOGIN)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("LOGIN RES",response.toString());
                        Timber.e("called onResponse of User Login API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                LoginResponse mLoginResponse = ModelMapper.transformJSONObjectToLoginResponse(response);
                                Timber.d("Got Success LoginResponse...");
                                Timber.d("handleActionUserLogin = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionUserLogin = > LoginResponse mLoginResponse ==>" + new Gson().toJson(mLoginResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mLoginResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_LOGIN_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in LoginResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionUserLogin = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_LOGIN_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of User Login API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionUserLogin = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_USER_LOGIN_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }
    //Handle Use Split money
    private void handleActionSplitBalance(Intent paramData) {
        SplitRequest mSplitRequest = (SplitRequest) paramData.getSerializableExtra(PARAM_SPLIT_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        JSONArray itemSelectedJson = new JSONArray();
        try {
            jsonObject.put(ResponseAttributeConstants.SPLIT_USER_ID, mSplitRequest.getUserId());
            jsonObject.put(ResponseAttributeConstants.SPLIT_USER_AMOUNT, mSplitRequest.getAmount());
            jsonObject.put(ResponseAttributeConstants.SPLIT_USER_REFERENCE, mSplitRequest.getReferenceNumber());
            jsonObject.put(ResponseAttributeConstants.SPLIT_USER_NUMBER_PARTICIPIENT, mSplitRequest.getNumberOfParticipents());
            try {
                for (int i = 0; i < mSplitRequest.getParticipents().size(); i++) {
                    JSONObject splitJSON = new JSONObject();
                    splitJSON.put(ResponseAttributeConstants.SPLIT_USER_PARTICIPIENT_MOBILE, mSplitRequest.getParticipents().get(i).getParticipentMobile());
                    splitJSON.put(ResponseAttributeConstants.SPLIT_USER_PARTICIPIENT_EMAIL, mSplitRequest.getParticipents().get(i).getParticipentEmail());
                    itemSelectedJson.put(splitJSON);
                    Log.e("Getting Value", "Getting Array Value" + itemSelectedJson);
                }
                jsonObject.put(ResponseAttributeConstants.SPLIT_USER_PARTICIPIENTS, itemSelectedJson);
                Log.e("Getting Json Value", "Getting Json Object Value" + jsonObject);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }


        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_SPLIT_END_POINT)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_USER_LOGIN)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of User Split API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                SplitResponse mSplitResponse = ModelMapper.transformJSONObjectToSplitResponse(response);
                                Timber.d("Got Success SplitResponse...");

                                Timber.d("handleActionUserSplit  = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionUserSplit  = > Split Response mSplit Response ==>" + new Gson().toJson(mSplitResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mSplitResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_SPLIT_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in Split Response...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionUserSplit = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_SPLIT_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of User Split API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionUserSplit = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_USER_SPLIT_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }
    //Handle Action Clint to Client Money Transfer
    private void handleActionClientToClientMoneyTransfer(Intent paramData) {
        ClientToClientMoneyTransferRequest clientToClientMoneyTransferRequest = (ClientToClientMoneyTransferRequest) paramData.getSerializableExtra(PARAM_CLIENT_TO_CLIENT_MONEY_TRANSFER_REQUEST_MODEL);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.CLIENT_SENDER, clientToClientMoneyTransferRequest.senderMobileNumber);
            jsonObject.put(ResponseAttributeConstants.CLIENT_RECEIVER, clientToClientMoneyTransferRequest.receiverMobileNumber);
            jsonObject.put(ResponseAttributeConstants.AMOUNT, clientToClientMoneyTransferRequest.amount);
            jsonObject.put(ResponseAttributeConstants.HASH, clientToClientMoneyTransferRequest.hash);

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_CLIENT_TO_CLIENT_MONEY_TRANSFER_END_POINT)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_CLIENT_TO_CLIENT_MONEY_TRANSFER)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of User clientToClientMoneyTransfer API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                ClientToClientMoneyTransferResponse mClientToClientMoneyTransferResponse = ModelMapper.transformJSONObjectToClientToClientMoneyTransferResponse(response);
                                Timber.d("Got Success ClientToClientMoneyTransferResponse...");

                                Timber.d("handleActionClientToClientMoneyTransfer = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionClientToClientMoneyTransfer = > mClientToClientMoneyTransferResponse  ==>" + new Gson().toJson(mClientToClientMoneyTransferResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mClientToClientMoneyTransferResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_CLIENT_TO_CLIENT_MONEY_TRANSFER_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in ClientToClientMoneyTransferResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionClientToClientMoneyTransfer = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_CLIENT_TO_CLIENT_MONEY_TRANSFER_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of User clientToClientMoneyTransfer API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionClientToClientMoneyTransfer = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_USER_CLIENT_TO_CLIENT_MONEY_TRANSFER_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }
    //Handle Action Client Status
    private void handleActionClientStatus(Intent paramData) {
        ClientStatusRequest mClientStatusRequest = (ClientStatusRequest) paramData.getSerializableExtra(PARAM_CLIENT_STATUS_REQUEST_MODEL);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.CLIENT_MOBILE_PARAMETER, mClientStatusRequest.clientmobile);
            jsonObject.put(ResponseAttributeConstants.PIN_CODE, mClientStatusRequest.pincode);
            jsonObject.put(ResponseAttributeConstants.HASH, mClientStatusRequest.hash);

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_CLIENT_STATUS_END_POINT)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_CLIENT_STATUS)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of User ClientStatus API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                ClientStatusResponse mClientStatusResponse = ModelMapper.transformJSONObjectToClientStatusResponse(response);
                                Timber.d("Got Success ClientStatusResponse...");

                                Timber.d("handleActionClientStatus = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionClientStatus = > ClientStatusResponse mClientStatusResponse ==>" + new Gson().toJson(mClientStatusResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mClientStatusResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_CLIENT_STATUS_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in ClientStatusResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionClientStatus = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_CLIENT_STATUS_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of User ClientStatus  API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionClientStatus = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_USER_CLIENT_STATUS_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }
    //Handle Action Client Balance
    private void handleActionClientBalance(Intent paramData) {
        ClientBalanceRequest mClientBalanceRequest = (ClientBalanceRequest) paramData.getSerializableExtra(PARAM_CLIENT_BALANCE_REQUEST_MODEL);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.CLIENT_MOBILE_PARAMETER, mClientBalanceRequest.clientmobile);
            jsonObject.put(ResponseAttributeConstants.PIN_CODE, mClientBalanceRequest.pincode);
            jsonObject.put(ResponseAttributeConstants.HASH, mClientBalanceRequest.hash);

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_CLIENT_BALANCE_END_POINT)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_CLIENT_BALANCE)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of ClientBalance API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                ClientBalanceResponse mClientBalanceResponse = ModelMapper.transformJSONObjectToClientBalanceResponse(response);
                                Timber.d("Got Success ClientBalanceResponse...");

                                Timber.d("handleActionClientBalance = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionClientBalance = > ClientBalanceResponse mClientBalanceResponse ==>" + new Gson().toJson(mClientBalanceResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mClientBalanceResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_CLIENT_BALANCE_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in ClientBalanceResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionClientBalance = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_CLIENT_BALANCE_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of ClientBalance API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionClientBalance = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_USER_CLIENT_BALANCE_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });

    }
    //Handle Action Airtime Recharge Details
    private void handleActionAirtimeRecharge(Intent paramData) {
        AirtimeRequest mAirtimeRequest = (AirtimeRequest) paramData.getSerializableExtra(PARAM_AIRTIME_REQUEST_MODEL);
        String transactionID=paramData.getStringExtra(TRANSACTION_ID);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.CLIENT_MOBILE_PARAMETER, mAirtimeRequest.clientmobile);
            jsonObject.put(ResponseAttributeConstants.PIN_CODE, MobicashUtils.md5(mAirtimeRequest.pincode));
            jsonObject.put(ResponseAttributeConstants.NUMBER, mAirtimeRequest.number);
            jsonObject.put(ResponseAttributeConstants.AMOUNT, mAirtimeRequest.amount);
            jsonObject.put(ResponseAttributeConstants.CYCLE_NUMBER, mAirtimeRequest.cyclenumber);
            jsonObject.put(ResponseAttributeConstants.OPERATOR, mAirtimeRequest.operator);
            jsonObject.put(ResponseAttributeConstants.TYPE, mAirtimeRequest.type);
            if(transactionID!=null) {
                jsonObject.put(ResponseAttributeConstants.TRANSACTION_ID, transactionID);
            }
            jsonObject.put(ResponseAttributeConstants.HASH, mAirtimeRequest.hash);

            //Log.e("Prepaid",jsonObject.toString());

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_CLIENT_AIRTIME_END_POINT)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_AIRTIME_RECHARGE)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of User Airtime API.");
                        Log.e("Prepaid",response.toString());
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                AirtimeResponse mAirtimeResponse = ModelMapper.transformJSONObjectToAirtimeResponse(response);
                                Timber.d("Got Success AirtimeResponse...");

                                Timber.d("handleActionAirtimeRecharge = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionAirtimeRecharge = > AirtimeResponse mAirtimeResponse ==>" + new Gson().toJson(mAirtimeResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mAirtimeResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_CLIENT_AIRTIME_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in AirtimeResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionAirtimeRecharge = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_CLIENT_AIRTIME_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        //Timber.e("Error Message : " + error.getMessage());
                        Timber.e("called onError of User Airtime API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionAirtimeRecharge = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_USER_CLIENT_AIRTIME_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }
    //Handle Action OperatorList
    private void handleActionOperatorList(Intent paramData) {
        OperatorListRequest mOperatorListRequest = (OperatorListRequest) paramData.getSerializableExtra(PARAM_OPERATOR_LIST_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.MODULE_TYPE, mOperatorListRequest.moduleType);
            jsonObject.put(ResponseAttributeConstants.RECHARGE_TYPE, mOperatorListRequest.rechargeType);

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.PARAM_OPERATOR_LIST_REQUEST_MODEL)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_OPERATOR_LIST)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of User Operator List API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                OperatorListResponse mOperatorListResponse = ModelMapper.transformJSONObjectToOperatorListAPIResponse(response);
                                Timber.d("Got Success OperatorListResponse...");
                                Timber.d("handleActionOperatorList = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionOperatorList = > OperatorListResponse mOperatorListResponse ==>" + new Gson().toJson(mOperatorListResponse));
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mOperatorListResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_OPERATOR_LIST_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in OperatorListResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionOperatorList = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_OPERATOR_LIST_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Operator List API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionOperatorList = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_OPERATOR_LIST_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });

    }
    //Handle Action Client Log
    private void handleActionClientLog(Intent paramData) {
        ClientLogRequest mClientLogRequest = (ClientLogRequest) paramData.getSerializableExtra(PARAM_CLIENT_LOG_REQUEST_MODEL);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.CLIENT_ID, mClientLogRequest.clientId);
            jsonObject.put(ResponseAttributeConstants.PIN_CODE, MobicashUtils.md5(mClientLogRequest.pincode));
            jsonObject.put(ResponseAttributeConstants.HASH, mClientLogRequest.hash);

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_CLIENT_LOG_END_POINT)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_CLIENT_LOG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of Client log API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                ClientLogResponse mClientLogResponse = ModelMapper.transformJSONObjectToClientLogResponse(response);
                                Timber.d("Got Success ClientLogResponse...");

                                Timber.d("handleActionClientLog = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionClientLog = > ClientLogResponse mClientLogResponse ==>" + new Gson().toJson(mClientLogResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mClientLogResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_CLIENT_LOG_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in ClientLogResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionClientLog = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_USER_CLIENT_LOG_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Client log API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionClientLog = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_USER_CLIENT_LOG_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });


    }
    //Handle Action Profile Update
    private void handleActionClientProfileUpdate(Intent paramData) {
        ClientProfileUpdateRequest mClientProfileUpdateRequest = (ClientProfileUpdateRequest) paramData.getSerializableExtra(PARAM_CLIENT_PROFILE_UPDATE_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.CLIENT_ID, mClientProfileUpdateRequest.clientId);
            jsonObject.put(ResponseAttributeConstants.PIN_CODE, mClientProfileUpdateRequest.pincode);
            jsonObject.put(ResponseAttributeConstants.HASH, mClientProfileUpdateRequest.hash);
            jsonObject.put(ResponseAttributeConstants.FIRST_NAME, mClientProfileUpdateRequest.firstname);
            jsonObject.put(ResponseAttributeConstants.LAST_NAME, mClientProfileUpdateRequest.lastname);
            jsonObject.put(ResponseAttributeConstants.USER_NAME, mClientProfileUpdateRequest.username);
            jsonObject.put(ResponseAttributeConstants.ADDRESS, mClientProfileUpdateRequest.address);
            jsonObject.put(ResponseAttributeConstants.ZIP_CODE, mClientProfileUpdateRequest.zipcode);
            jsonObject.put(ResponseAttributeConstants.CITY, mClientProfileUpdateRequest.city);
            jsonObject.put(ResponseAttributeConstants.COUNTRY, mClientProfileUpdateRequest.country);
            jsonObject.put(ResponseAttributeConstants.LANGUAGE, mClientProfileUpdateRequest.language);
            jsonObject.put(ResponseAttributeConstants.EMAIL, mClientProfileUpdateRequest.email);
            jsonObject.put(ResponseAttributeConstants.DOB, mClientProfileUpdateRequest.dob);
            jsonObject.put(ResponseAttributeConstants.ADHAAR, mClientProfileUpdateRequest.adhaar);
            jsonObject.put(ResponseAttributeConstants.PAN, mClientProfileUpdateRequest.pan);

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                . writeTimeout(5, TimeUnit.MINUTES)
                .build();

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_CLIENT_PROFILE_UPDATE_END_POINT)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_CLIENT_PROFILE_UPDATE)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of Client Profile Update API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                ClientProfileUpdateResponse mClientProfileUpdateResponse = ModelMapper.transformJSONObjectToClientProfileUpdateResponse(response);
                                Timber.d("Got Success ClientProfileUpdateResponse...");

                                Timber.d("handleActionClientProfileUpdate = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionClientProfileUpdate = > ClientProfileUpdateResponse mClientProfileUpdateResponse ==>" + new Gson().toJson(mClientProfileUpdateResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mClientProfileUpdateResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_CLIENT_PROFILE_UPDATE_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in ClientProfileUpdateResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionClientProfileUpdate = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_CLIENT_PROFILE_UPDATE__FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Client Profile Update API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionClientProfileUpdate = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_CLIENT_PROFILE_UPDATE__FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });


    }
    //Handle Action Client Profile
    private void handleActionGetClientProfile(Intent paramData) {
        GetClientProfileRequest mGetClientProfileRequest = (GetClientProfileRequest) paramData.getSerializableExtra(PARAM_GET_CLIENT_PROFILE_REQUEST_MODEL);
        AndroidNetworking.get(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_GET_CLIENT_PROFILE_END_POINT)
                .addQueryParameter(NetworkConstant.PARAM_CLIENT_ID, mGetClientProfileRequest.clientId)
                .addQueryParameter(NetworkConstant.PARAM_HASH, mGetClientProfileRequest.hash)
                .setTag(TAG_PERFORM_GET_CLIENT_PROFILE)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of Get Client Profile API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                GetClientProfileResponse mGetClientProfileResponse = ModelMapper.transformJSONObjectToGetClientProfileResponse(response);
                                Timber.d("Got Success GetClientProfileResponse...");

                                Timber.d("handleActionGetClientProfile = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionGetClientProfile = > GetClientProfileResponse mGetClientProfileResponse ==>" + new Gson().toJson(mGetClientProfileResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mGetClientProfileResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_GET_CLIENT_PROFILE_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in GetClientProfileResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionGetClientProfile = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_GET_CLIENT_PROFILE_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Get Client Profile API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionGetClientProfile = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_GET_CLIENT_PROFILE_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });

    }
    //Handle Action Client Profile Info
    private void handleActionGetClientProfileInfo(Intent paramData) {
        GetClientProfileInfoRequest mGetClientProfileInfoRequest = (GetClientProfileInfoRequest) paramData.getSerializableExtra(PARAM_GET_CLIENT_PROFILE_INFO_REQUEST_MODEL);

        if (mGetClientProfileInfoRequest.clientId == null) {
            Timber.e("mGetClientProfileInfoRequest.clientId is null");
            return;
        }
        if (mGetClientProfileInfoRequest.pincode == null) {
            Timber.e("mGetClientProfileInfoRequest.pincode is null");
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(mGetClientProfileInfoRequest.clientId);
        builder.append(MobicashUtils.md5(mGetClientProfileInfoRequest.pincode));
        try {
            mGetClientProfileInfoRequest.hash = MobicashUtils.getSha1(builder.toString());
        } catch (NoSuchAlgorithmException e) {
            Timber.e(" NoSuchAlgorithmException occured. message : " + e.getMessage());
        }
        AndroidNetworking.get(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_GET_CLIENT_PROFILE_INFO_END_POINT)
                .addQueryParameter(NetworkConstant.PARAM_CLIENT_ID, mGetClientProfileInfoRequest.clientId)
                .addQueryParameter(NetworkConstant.PARAM_HASH, mGetClientProfileInfoRequest.hash)
                .setTag(TAG_PERFORM_GET_CLIENT_PROFILE_INFO)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of Get Client Profile Info API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                GetClientProfileInfoResponse mGetClientProfileInfoResponse = ModelMapper.transformJSONObjectTomGetClientProfileInfoResponse(response);
                                Timber.d("Got Success GetClientProfileInfoResponse...");
                                Timber.d("handleActionGetClientProfileInfo = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionGetClientProfileInfo = > GetClientProfileInfoResponse mGetClientProfileInfoResponse ==>" + new Gson().toJson(mGetClientProfileInfoResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mGetClientProfileInfoResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_GET_CLIENT_PROFILE_INFO_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in GetClientProfileInfoResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionGetClientProfileInfo = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_GET_CLIENT_PROFILE_INFO_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Get Client Profile Info API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());
                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionGetClientProfileInfo = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_GET_CLIENT_PROFILE_INFO_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }
    //Handle Action Client Bank Transfer
    private void handleActionClientBankTransfer(Intent paramData) {
        ClientBankTransferRequest mClientBankTransferRequest = (ClientBankTransferRequest) paramData.getSerializableExtra(PARAM_CLIENT_BANK_TRANSFER_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.TRANSFER_USER_MOBILE, mClientBankTransferRequest.userMobile);
            jsonObject.put(ResponseAttributeConstants.TRANSFER_USER_AGENT_CODE, mClientBankTransferRequest.agentCode);
            jsonObject.put(ResponseAttributeConstants.TRANSFER_USER_BENIFICARY_MOBILE, mClientBankTransferRequest.beneficiaryMobile);
            jsonObject.put(ResponseAttributeConstants.TRANSFER_USER_ID, mClientBankTransferRequest.userId);
            jsonObject.put(ResponseAttributeConstants.TRANSFER_USER_TYPE, mClientBankTransferRequest.userType);
            jsonObject.put(ResponseAttributeConstants.TRANSFER_USER_TERMINAL, "");
            jsonObject.put(ResponseAttributeConstants.PIN_CODE, mClientBankTransferRequest.pincode);
            jsonObject.put(ResponseAttributeConstants.TRANSFER_TYPE, mClientBankTransferRequest.transferType);
            jsonObject.put(ResponseAttributeConstants.ACCOUNT_NO, mClientBankTransferRequest.accountNo);
            jsonObject.put(ResponseAttributeConstants.IFSC, mClientBankTransferRequest.IFSC);
            jsonObject.put(ResponseAttributeConstants.BANK_NAME, mClientBankTransferRequest.bankName);
            jsonObject.put(ResponseAttributeConstants.BRANCH_NAME, mClientBankTransferRequest.branchName);
            jsonObject.put(ResponseAttributeConstants.AMOUNT, mClientBankTransferRequest.amount);
            jsonObject.put(ResponseAttributeConstants.FIRST_NAME_TRANSFER, mClientBankTransferRequest.firstName);
            jsonObject.put(ResponseAttributeConstants.LAST_NAME_TRANSFER, mClientBankTransferRequest.lastName);
            jsonObject.put(ResponseAttributeConstants.BENEFICIARY_MOBILE, mClientBankTransferRequest.beneficiaryMobile);
            jsonObject.put(ResponseAttributeConstants.COMMENTS,mClientBankTransferRequest.comments);
            jsonObject.put(ResponseAttributeConstants.EMAIL_ID,mClientBankTransferRequest.emailId);
            jsonObject.put(ResponseAttributeConstants.HASH, mClientBankTransferRequest.hash);
            jsonObject.put(ResponseAttributeConstants.TRANSFER_USER_BENIFICARY_ID, mClientBankTransferRequest.beneficiaryID);
            jsonObject.put(ResponseAttributeConstants.TRANSFER_USER_KYC_STATUS, mClientBankTransferRequest.kycStatus);
        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_CLIENT_BANK_TRANSFER)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_CLIENT_BANK_TRANSFER)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of Client Bank Transfer API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS)!= 0) {
                                ClientBankTransferResponse mClientBankTransferResponse = ModelMapper.transformJSONObjectToClientBankTransferResponse(response);
                                Timber.d("Got Success ClientBankTransferResponse...");
                                Timber.d("handleActionClientBankTransfer = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionClientBankTransfer = > ClientBankTransferResponse mClientBankTransferResponse ==>" + new Gson().toJson(mClientBankTransferResponse));
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mClientBankTransferResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_CLIENT_BANK_TRANSFER_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in ClientBankTransferResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionClientBankTransfer = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_CLIENT_BANK_TRANSFER_FAIl);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Client Bank Transfer API.");
                        Timber.e("Error Message : " +error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());
                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionClientBankTransfer = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_CLIENT_BANK_TRANSFER_FAIl);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });

    }

    //Handle Action Add Beneficiary
    private void handleActionAddBeneficiary(Intent paramData) {
        BeneficiaryBean mbeneBeneficiaryBean = (BeneficiaryBean) paramData.getSerializableExtra(PARAM_ADD_BENEFICIARY_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.CUSTOMER_USER_ID, mbeneBeneficiaryBean.getUser_id());
            jsonObject.put(ResponseAttributeConstants.USER_TYPE, mbeneBeneficiaryBean.getUserType());
            jsonObject.put(ResponseAttributeConstants.CUSTOMER_TITLE, mbeneBeneficiaryBean.getCustomerTitle());
            jsonObject.put(ResponseAttributeConstants.AGENT_CODE, mbeneBeneficiaryBean.getAgentCode());
            jsonObject.put(ResponseAttributeConstants.CUSTOMER_MOBILE, mbeneBeneficiaryBean.getCustomerMobile());
            jsonObject.put(ResponseAttributeConstants.BENEFICIARY_FIRST_NAME, mbeneBeneficiaryBean.getBenFirstName());
            jsonObject.put(ResponseAttributeConstants.CUSTOMER_FIRST_NAME, mbeneBeneficiaryBean.getCustomerFname());
            jsonObject.put(ResponseAttributeConstants.CUSTOMER_LAST_NAME, mbeneBeneficiaryBean.getCustomerLname());
            jsonObject.put(ResponseAttributeConstants.CUSTOMER_EMAIL, mbeneBeneficiaryBean.getCustomerEmail());
            jsonObject.put(ResponseAttributeConstants.STATE, mbeneBeneficiaryBean.getState());
            jsonObject.put(ResponseAttributeConstants.CITY, mbeneBeneficiaryBean.getCity());
            jsonObject.put(ResponseAttributeConstants.BENEFICIARY_BANK_NAME, mbeneBeneficiaryBean.getBenBankName());
            jsonObject.put(ResponseAttributeConstants.BENEFICIARY_IFSC_CODE, mbeneBeneficiaryBean.getBenIfscCode());
            jsonObject.put(ResponseAttributeConstants.BENEFICIARY_MOBILE, mbeneBeneficiaryBean.getBenMobileNumber());
            jsonObject.put(ResponseAttributeConstants.BENEFICIARY_BRANCH_NAME, mbeneBeneficiaryBean.getBenBranchName());
            jsonObject.put(ResponseAttributeConstants.BENEFICIARY_MOBILE_NUMBER, mbeneBeneficiaryBean.getBenMobileNumber());
            jsonObject.put(ResponseAttributeConstants.BENEFICIARY_LAST_NAME, mbeneBeneficiaryBean.getBenLastName());
            jsonObject.put(ResponseAttributeConstants.BENEFICIARY_ACCOUNT_NUMBER, mbeneBeneficiaryBean.getBenAccountNumber());
            jsonObject.put(ResponseAttributeConstants.PINCODE, mbeneBeneficiaryBean.getPincode());
            jsonObject.put(ResponseAttributeConstants.CUSTOMER_AADDRESS, mbeneBeneficiaryBean.getCustAddess());
            jsonObject.put(ResponseAttributeConstants.SOURCE, mbeneBeneficiaryBean.getSource());
            jsonObject.put(ResponseAttributeConstants.TYPE, mbeneBeneficiaryBean.getType());
            jsonObject.put("KYC", mbeneBeneficiaryBean.getKYC());
            jsonObject.put("KYCType", mbeneBeneficiaryBean.getKYCType());
            jsonObject.put("addarVerificationCode", mbeneBeneficiaryBean.getAddarVerificationCode());
            jsonObject.put(ResponseAttributeConstants.CUSTOMER_DOB, mbeneBeneficiaryBean.getCustomerDOB());
            jsonObject.put(ResponseAttributeConstants.CUSTOMER_ALETRNATE_MOBILE, mbeneBeneficiaryBean.getCustomerAltMobile());

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_CLIENT_ADD_BENEFICIARY_END_POINT)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_ADD_BENEFICIARY)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of Add Beneficiary API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                BeneficiaryResponse beneficiaryResponse = ModelMapper.transformJSONObjectToBeneficiaryResponse(response);
                                Timber.d("Got Success Add BeneficiaryCodeResponse...");

                                Timber.d("handleActionClientAddBeneficiary = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionClientAddBeneficiary = > Add BeneficiaryCodeResponse mClientSendAdd BeneficiaryResponse ==>" + new Gson().toJson(beneficiaryResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, beneficiaryResponse);
                                broadcastIntent.setAction(NetworkConstant.ADD_BENEFICIARY_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in ClientAdd BeneficiaryResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionClientAdd Beneficiary = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.ADD_BENEFICIARY_FAILURE);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Client Add Beneficiary API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionAddBeneficiary = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.ADD_BENEFICIARY_FAILURE);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }

    //Handle Action Clinet Send passcode
    private void handleActionClientSendPasscode(Intent paramData) {
        ClientSendPassCodeRequest mClientSendPassCodeRequest = (ClientSendPassCodeRequest) paramData.getSerializableExtra(PARAM_CLIENT_SEND_PASSCODE_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.CLIENT_MOBILE_PARAMETER, mClientSendPassCodeRequest.clientmobile);
        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_CLIENT_SEND_PASSCODE_END_POINT)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_CLIENT_SEND_PASSCODE)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of Client Send Passcode API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                ClientSendPassCodeResponse mClientSendPassCodeResponse = ModelMapper.transformJSONObjectToClientSendPasscodeResponse(response);
                                Timber.d("Got Success ClientSendPassCodeResponse...");

                                Timber.d("handleActionClientSendPasscode = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionClientSendPasscode = > ClientSendPassCodeResponse mClientSendPassCodeResponse ==>" + new Gson().toJson(mClientSendPassCodeResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mClientSendPassCodeResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_CLIENT_SEND_PASSCODE_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in ClientSendPassCodeResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionClientSendPasscode = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_CLIENT_SEND_PASSCODE_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Client Send Passcode API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionClientSendPasscode = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_CLIENT_SEND_PASSCODE_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });

    }
    //Handle Action Client Reset Passcode
    private void handleActionClientResetPasscode(Intent paramData) {
        ClientResetPassCodeRequest mClientResetPassCodeRequest = (ClientResetPassCodeRequest) paramData.getSerializableExtra(PARAM_CLIENT_RESET_PASSCODE_REQUEST_MODEL);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.CLIENT_ID, mClientResetPassCodeRequest.clientId);
            jsonObject.put(ResponseAttributeConstants.NEW_PIN_CODE, mClientResetPassCodeRequest.newPinCode);
            jsonObject.put(ResponseAttributeConstants.PIN_CODE, mClientResetPassCodeRequest.pinCode);
            jsonObject.put(ResponseAttributeConstants.HASH, mClientResetPassCodeRequest.hash);

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }


        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_CLIENT_RESET_PASSCODE_END_POINT)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_CLIENT_RESET_PASSCODE)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of Client Reset Passcode API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                ClientResetPassCodeResponse mClientResetPassCodeResponse = ModelMapper.transformJSONObjectToClientResetPasscodeResponse(response);
                                Timber.d("Got Success ClientResetPassCodeResponse...");

                                Timber.d("handleActionClientResetPasscode = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionClientResetPasscode = > ClientResetPassCodeResponse mClientResetPassCodeResponse ==>" + new Gson().toJson(mClientResetPassCodeResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mClientResetPassCodeResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_CLIENT_RESET_PASSCODE_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in ClientResetPassCodeResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionClientResetPasscode = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_CLIENT_RESET_PASSCODE_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Client Reset Passcode API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionClientResetPasscode = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_CLIENT_RESET_PASSCODE_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }
    //Handle Action Upload Profile Picture
    private void handleActionUploadProfilePicture(Intent paramData) {
        UploadClientPictureRequest mUploadClientPictureRequest = (UploadClientPictureRequest) paramData.getSerializableExtra(PARAM_UPLOAD_CLIENT_PROFILE_PICTURE_REQUEST_MODEL);
        AndroidNetworking.upload(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_CLIENT_UPDATE_CLIENT_PROFILE_PICTURE_END_POINT)
                .addMultipartFile(ResponseAttributeConstants.PICTURE, mUploadClientPictureRequest.picture)
                .addMultipartParameter(ResponseAttributeConstants.CLIENT_ID, mUploadClientPictureRequest.clientId)
                .addMultipartParameter(ResponseAttributeConstants.PIN_CODE, MobicashUtils.md5(mUploadClientPictureRequest.pincode))
                .addMultipartParameter(ResponseAttributeConstants.HASH, mUploadClientPictureRequest.hash)
                .setTag(TAG_PERFORM_UPLOAD_CLIENT_PROFILE_PICTURE)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(new OkHttpClient())
                .setExecutor(Executors.newSingleThreadExecutor()) // setting an executor to get response or completion on that executor thread
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                        Timber.e("bytesUploaded ====> " + bytesUploaded);
                        Timber.e("totalBytes ====> " + totalBytes);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of Upload Client Profile Picture API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0)
                            {
                                UploadClientProfilePictureResponse mUploadClientProfilePictureResponse = ModelMapper.transformJSONObjectToUploadClientProfilePictureResponse(response);
                                Timber.d("Got Success ClientResetPassCodeResponse...");

                                Timber.d("handleActionUploadProfilePicture = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionUploadProfilePicture = > UploadClientProfilePictureResponse mUploadClientProfilePictureResponse ==>" + new Gson().toJson(mUploadClientProfilePictureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mUploadClientProfilePictureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_UPLOAD_CLIENT_PROFILE_PICTURE_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in UploadClientProfilePictureResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionUploadProfilePicture = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_UPLOAD_CLIENT_PROFILE_PICTURE_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Upload Client Profile Picture API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionUploadProfilePicture = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_UPLOAD_CLIENT_PROFILE_PICTURE_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }
    //Handle Action PayUPaymentGateway
    private void handleActionClientPayUPaymentGateway(Intent paramData) {
        PayUPaymentGatewayRequest mPayUPaymentGatewayRequest = (PayUPaymentGatewayRequest) paramData.getSerializableExtra(PARAM_CLIENT_PAYU_PAYMENT_GATEWAY_REQUEST_MODEL);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.AMOUNT, mPayUPaymentGatewayRequest.amount);
            jsonObject.put(ResponseAttributeConstants.FIRST_NAME, mPayUPaymentGatewayRequest.firstname);
            jsonObject.put(ResponseAttributeConstants.LAST_NAME, mPayUPaymentGatewayRequest.lastname);
            jsonObject.put(ResponseAttributeConstants.EMAIL, mPayUPaymentGatewayRequest.email);
            jsonObject.put(ResponseAttributeConstants.PHONE, mPayUPaymentGatewayRequest.phone);
            jsonObject.put(ResponseAttributeConstants.PRODUCT_INFO, mPayUPaymentGatewayRequest.productinfo);
            jsonObject.put(ResponseAttributeConstants.MERCHANT_ID, mPayUPaymentGatewayRequest.merchantId);
            jsonObject.put(ResponseAttributeConstants.PG, mPayUPaymentGatewayRequest.pg);
            jsonObject.put(ResponseAttributeConstants.ADDRESS1, mPayUPaymentGatewayRequest.address1);
            jsonObject.put(ResponseAttributeConstants.CITY, mPayUPaymentGatewayRequest.city);
            jsonObject.put(ResponseAttributeConstants.STATE, mPayUPaymentGatewayRequest.state);
            jsonObject.put(ResponseAttributeConstants.COUNTRY, mPayUPaymentGatewayRequest.country);
            jsonObject.put(ResponseAttributeConstants.ZIP_CODE, mPayUPaymentGatewayRequest.zipcode);
            jsonObject.put(ResponseAttributeConstants.USER_ID, mPayUPaymentGatewayRequest.userId);
            jsonObject.put(ResponseAttributeConstants.USER_TYPE, mPayUPaymentGatewayRequest.userType);
            jsonObject.put(ResponseAttributeConstants.BANK_CODE, mPayUPaymentGatewayRequest.bankcode);
            jsonObject.put(ResponseAttributeConstants.CCNAME, mPayUPaymentGatewayRequest.ccname);
            jsonObject.put(ResponseAttributeConstants.TRANX_ID,mPayUPaymentGatewayRequest.tranxid);
            jsonObject.put(ResponseAttributeConstants.CCNUM, mPayUPaymentGatewayRequest.ccnum);
            jsonObject.put(ResponseAttributeConstants.CCVV, mPayUPaymentGatewayRequest.ccvv);
            jsonObject.put(ResponseAttributeConstants.REDIRECT_URL, mPayUPaymentGatewayRequest.redirectURL);
            jsonObject.put(ResponseAttributeConstants.CCEXPMON, mPayUPaymentGatewayRequest.ccexpmon);
            jsonObject.put(ResponseAttributeConstants.CCEXPYR, mPayUPaymentGatewayRequest.ccexpyr);

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_CLIENT_PAYU_PAYMENT_GATEWAY_END_POINT)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_CLIENT_PAYU_PAYMENT_GATEWAY)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of Client PayU Payment Gateway API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                PayUPaymentGatewayResponse mPayUPaymentGatewayResponse = ModelMapper.transformJSONObjectToPayUPaymentGatewayResponse(response);
                                Timber.d("Got Success PayUPaymentGatewayResponse...");
                                Timber.d("handleActionClientPayUPaymentGateway = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionClientPayUPaymentGateway = > PayUPaymentGatewayResponse mPayUPaymentGatewayResponse ==>" + new Gson().toJson(mPayUPaymentGatewayResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mPayUPaymentGatewayResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_PAYU_PAYMENT_GATEWAY_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in PayUPaymentGatewayResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionClientPayUPaymentGateway = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_PAYU_PAYMENT_GATEWAY_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Client  PayU Payment Gateway API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionClientPayUPaymentGateway = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_PAYU_PAYMENT_GATEWAY_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }
    //Handle Action Bank List
    private void handleActionGetBankList(Intent paramData) {

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_CLIENT_BANK_LIST_END_POINT)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of bank list API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {

                                Timber.d("Got Success Bank List Response...");

                                Timber.d("handleActionGetBankList = > JSONObject response ==>" + new Gson().toJson(response));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, ModelMapper.getBankList(response));
                                broadcastIntent.setAction(NetworkConstant.BANK_LIST_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in PayUPaymentGatewayResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionClientPayUPaymentGateway = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.BANK_LIST_FAILURE);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Client  PayU Payment Gateway API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionClientPayUPaymentGateway = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.BANK_LIST_FAILURE);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }

    //Handle City List
    private void handleActionGetCityList(Intent paramData)
    {

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.USER_CLIENT_CITY_LIST_END_POINT)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of city list API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {

                                Timber.d("Got Success city List Response...");

                                Timber.d("handleActionGetcityList = > JSONObject response ==>" + new Gson().toJson(response));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, ModelMapper.getCityList(response));
                                broadcastIntent.setAction(NetworkConstant.CITY_LIST_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in City List ApiResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handlecityGateway = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.CITY_LIST_FAILURE);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Client  City List Api.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionCity List Api = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.CITY_LIST_FAILURE);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }


    //Handle City List
    private void handleActionGetStateList(Intent paramData)
    {

        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.BENEFICIARY_CLIENT_CITY_LIST_END_POINT)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of city list API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {

                                Timber.d("Got Success city List Response...");

                                Timber.d("handleActionGetStateList = > JSONObject response ==>" + new Gson().toJson(response));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, ModelMapper.getStateList(response));
                                broadcastIntent.setAction(NetworkConstant.STATE_LIST_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in City List ApiResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handlecityGateway = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATE_LIST_FAILURE);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Client  City List Api.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionCity List Api = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATE_LIST_FAILURE);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }

    //Handle Action wifi Plans
    private void handleActionWiFiPlans(Intent paramData) {
        JSONObject jsonObject = new JSONObject();
        PlanCategory planCategory= (PlanCategory) paramData.getSerializableExtra(PARAM_WIFI_PLANS_REQUEST_MODEL);
        try {
            jsonObject.put(ResponseAttributeConstants.PLANS_CATEGORY, planCategory.plan_category);

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }

        AndroidNetworking.post(NetworkConstant.PLANS_URL)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of get Plan List.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                Timber.d("Got Success Plan List Response...");
                                Timber.d("handleActionGetPlanList = > JSONObject response ==>" + new Gson().toJson(response));
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, ModelMapper.getPlanList(response));
                                broadcastIntent.setAction(NetworkConstant.PLAN_LIST_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in Plan ListResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionPlanList = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.PLAN_LIST_FAILURE);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of   Plan List API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionPlan List= > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.PLAN_LIST_FAILURE);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }

    //Handle Action wifi Plans
    private void handleActionWiFiOffers(Intent paramData) {
        JSONObject jsonObject = new JSONObject();
        OffersCategory offersCategoryCategory= (OffersCategory) paramData.getSerializableExtra(PARAM_WIFI_OFFERS_REQUEST_MODEL);
        try {
            jsonObject.put("lat", offersCategoryCategory.lat);
            jsonObject.put("longt", offersCategoryCategory.longt);
            jsonObject.put("hash", offersCategoryCategory.hash);

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        AndroidNetworking.post(NetworkConstant.OFFERS_URL)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of get Offers List.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                Timber.d("Got Success Offers List Response...");
                                Timber.d("handleActionGetOffersList = > JSONObject response ==>" + new Gson().toJson(response));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, ModelMapper.getOfferList(response));
                                broadcastIntent.setAction(NetworkConstant.OFFERS_LIST_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in Offers ListResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionOffersList = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.OFFERS_LIST_FAILURE);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of   Offers List API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionOffers List= > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.OFFERS_LIST_FAILURE);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }

    //Verify the Mobile Number before buy plans
    private void handleActionVerifyMobileNumber(Intent paramData) {
        JSONObject jsonObject = new JSONObject();
        VerifyMobileNumber mverifyMobileNumber= (VerifyMobileNumber) paramData.getSerializableExtra(PARAM_VERIFY_MOBILE_NUMBER_REQUEST_MODEL);
        try {
            jsonObject.put("req_action", "USER_EXIST");
            jsonObject.put("req_customercode", "95629568");
            //jsonObject.put("req_username", "8287316489@wifyee");
            jsonObject.put("req_cellphone", mverifyMobileNumber.req_username);
            jsonObject.put("req_username",   mverifyMobileNumber.req_username+"@wifyee");
            jsonObject.put("req_country", "91");
            jsonObject.put("req_apptype", "91");

        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        AndroidNetworking.post(NetworkConstant.WIFYEE_HOTSPOT_VERFIFY_MOBILE_USER)
                .addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_VERIFY_MOBILE_NUMBER)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse to verfiy phone number.");
                        try {
                            if (!response.getString("ret_message").equalsIgnoreCase("null")) {
                                Timber.d("Got Success verify phone number...");
                                Timber.d("handleActionVerifyMobileNumber = > JSONObject response ==>" + new Gson().toJson(response));
                                VerifyMobileNumberResponse mVerifyMobileNumberResponse = ModelMapper.transformJSONObjectToVerifyMobileNumberResponse(response);
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mVerifyMobileNumberResponse);
                                broadcastIntent.setAction(NetworkConstant.VERIFY_MOBILE_NUMBER_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in Verify phone number Response...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionVerifyMobileNumber = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.VERIFY_MOBILE_NUMBER_FAILURE);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of   verify phone number API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionVerifyMobileNumber List= > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.VERIFY_MOBILE_NUMBER_FAILURE);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });
    }
    //Call Api for Products Selling
    private void handleActionNear_Offer(Intent paramData) {
        //CartFoodOderRequest mCartFoodOrderRequest = (CartFoodOderRequest) paramData.getSerializableExtra(PARAM_CLIENT_FOODORDER_SUBMIT_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
       /* try {
            jsonObject.put("begin", "0");
            jsonObject.put("end", "100");
            jsonObject.put("sort", "ASC");

            //jsonObject.put("sort", LocalPreferenceUtility.getUserCode(context));
        } catch (Exception ex) {
        }*/
        AndroidNetworking.get(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.SELLING_ITEMS_MERCHANT)
                //.addJSONObjectBody(jsonObject)
                .setTag(TAG_PERFORM_NERAR_OFFERBY_SUBMIT)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of User FoodOder By Location List API.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                NearOfferResponse mNearOfferResponse = ModelMapper.transformJSONObjectToNearOfferResponse(response);
                                Timber.d("Got Success handleActionFoodorderListByMerchant...");
                                Timber.d("handleActionFoodorderListByMerchant = > JSONObject response ==>" + new Gson().toJson(response));
                                Timber.d("handleActionFoodorderListByMerchant = > FoodOrderResponse FoodOrderResponse ==>" + new Gson().toJson(response));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mNearOfferResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_OFFER_NEAR_LIST_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in handleActionFoodorderListByMerchant...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionFoodorderListByMerchant = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(NetworkConstant.STATUS_OFFER_NEAR_LIST_FAIL);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of Foododer List API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionOperatorList = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(NetworkConstant.STATUS_OFFER_NEAR_LIST_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });

    }
}
