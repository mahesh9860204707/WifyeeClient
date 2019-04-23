package com.wifyee.greenfields.dairyorder;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.foodorder.AddressRequest;
import com.wifyee.greenfields.mapper.ModelMapper;
import com.wifyee.greenfields.models.response.FailureResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class DairyProductIntentService extends IntentService {

    private static final String ACTION_GET_MERCHANT_LIST = "com.wifyee.greenfields.dairyorder.action.GET_MERCHANT_LIST";
    private static final String ACTION_GET_ITEM_LIST = "com.wifyee.greenfields.dairyorder.action.ITEM_LIST";
    private static final String ACTION_ADD_ORDER = "com.wifyee.greenfields.dairyorder.action.ADD_ORDER";
    private static final String ACTION_WALLET_BALANCE = "com.wifyee.greenfields.dairyorder.action.WALLET_BALANCE";
    private static final String ACTION_PERFORM_MERCHANT_TYPE_LIST = "com.mobicashindia.mobicash.services.action.merchant.merchanttypelist";
    private static final String TAG_PERFORM_MERCHANT_TYPE_LIST = "com.mobicashindia.mobicash.services.tag.merchant.merchanttypelist";
    private static final String PARAM_BYMERCHANT_LIST_REQUEST_MODEL = "com.mobicashindia.mobicash.services.bymerchant.list.request.model";


    private static final String EXTRA_PARAM1 = "com.wifyee.greenfields.dairyorder.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.wifyee.greenfields.dairyorder.extra.PARAM2";
    private static final String EXTRA_PARAM3 = "com.wifyee.greenfields.dairyorder.extra.PARAM3";
    private static final String EXTRA_PARAM4 = "com.wifyee.greenfields.dairyorder.extra.PARAM4";
    private static final String EXTRA_PARAM5 = "com.wifyee.greenfields.dairyorder.extra.PARAM5";
    private static final String EXTRA_PARAM6 = "com.wifyee.greenfields.dairyorder.extra.PARAM6";
    private static final String EXTRA_PARAM7 = "com.wifyee.greenfields.dairyorder.extra.PARAM7";
    private static final String EXTRA_PARAM8 = "com.wifyee.greenfields.dairyorder.extra.PARAM8";

    private static Context ctx = null;

    public DairyProductIntentService() {
        super("DairyProductIntentService");
    }

    /**
     * START REQUEST WALLET BALANCE
     */

    public static void  startActionGetWalletBalance(Context context,String mobileNumber) {
        Intent intent = new Intent(context, DairyProductIntentService.class);
        intent.putExtra(EXTRA_PARAM1,mobileNumber);
        intent.setAction(ACTION_WALLET_BALANCE);
        context.startService(intent);
    }

    /**
     * main category list
     * @param context
     */
    public static void startActionMerchantTypeList(Context context) {
        Timber.d(" Starting Merchant Type List service through Intent");
        Intent intent = new Intent(context, DairyProductIntentService.class);
        intent.setAction(ACTION_PERFORM_MERCHANT_TYPE_LIST);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void startActionGetMerchantList(Context context, AddressRequest addressRequest) {
        Intent intent = new Intent(context, DairyProductIntentService.class);
        intent.setAction(ACTION_GET_MERCHANT_LIST);
        intent.putExtra(PARAM_BYMERCHANT_LIST_REQUEST_MODEL,addressRequest);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action list dairy item with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void startActionListDairyItem(Context context,String merId) {
        Intent intent = new Intent(context, DairyProductIntentService.class);
        intent.putExtra(EXTRA_PARAM1,merId);
        intent.setAction(ACTION_GET_ITEM_LIST);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action list dairy item with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */

    public static void startActionAddOrder(Context context, ArrayList<PlaceOrderData> orderItem,
                                           String totalAmount, String paymentMode,String refId,String location, String lat, String lng, String complete_add) {
        ctx = context;
        Intent intent = new Intent(context, DairyProductIntentService.class);
        intent.putParcelableArrayListExtra(EXTRA_PARAM1,orderItem);
        intent.putExtra(EXTRA_PARAM2,totalAmount);
        intent.putExtra(EXTRA_PARAM3,paymentMode);
        intent.putExtra(EXTRA_PARAM4, LocalPreferenceUtility.getUserPassCode(context));
        intent.putExtra("ref_id",refId);
        intent.putExtra(EXTRA_PARAM5,location);
        intent.putExtra(EXTRA_PARAM6,lat);
        intent.putExtra(EXTRA_PARAM7,lng);
        intent.putExtra(EXTRA_PARAM8,complete_add);
        intent.setAction(ACTION_ADD_ORDER);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_MERCHANT_LIST.equals(action)) {
                handleActionGetDairyMerchantList(intent);
            } else if (ACTION_GET_ITEM_LIST.equals(action)) {
                handleActionGetListItem(intent);
            } else if(ACTION_ADD_ORDER.equals(action)){
                handleActionAddOrder(intent);
            }else if(ACTION_WALLET_BALANCE.equals(action)){
                handleGetWalletBalance(intent);
            }else if (ACTION_PERFORM_MERCHANT_TYPE_LIST.equals(action)) {
                handleActionGetMerchantTypeList(intent);
            }
        }
    }

    /**
     * Handle action get dairy merchant list in the provided background thread with the provided
     * parameters.
     */
    private void handleGetWalletBalance(Intent intent) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(DairyNetworkConstant.WALLET_PARAM, intent.getStringExtra(EXTRA_PARAM1));
        } catch (JSONException e) {
        }
        AndroidNetworking.post(DairyNetworkConstant.WALLET_BALANCE_URL)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of client balance.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                String balance = "";
                                if(response.has("clientBalance")){
                                    balance = response.getString("clientBalance");
                                }
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, balance);
                                broadcastIntent.setAction(DairyNetworkConstant.DAIRY_WALLET_BALANCE_STATUS_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in clientBalance...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionUserSignUp = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(DairyNetworkConstant.DAIRY_WALLET_BALANCE_STATUS_FAILURE);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of User clientBalance API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());
                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionUserSignUp = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(DairyNetworkConstant.DAIRY_WALLET_BALANCE_STATUS_FAILURE);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });

    }

    //Handle Merchant CurrencyList
    private void handleActionGetMerchantTypeList(Intent paramData) {
        AndroidNetworking.get(NetworkConstant.MOBICASH_BASE_URL_TESTING + DairyNetworkConstant.GET_MERCHAN_TYPE_LIST_END_POINT)
                .setTag(TAG_PERFORM_MERCHANT_TYPE_LIST)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Timber.e("called onResponse of Merchant TYPE API.");
                        if (response!=null) {
                            ArrayList<MainCategoryModel> dataList = JSONParser.getMainCategoryListItem(response);
                            Timber.d("Got Success Merchant TYPE Response...");
                            Timber.d("handleActionMerchant TYPE = > JSONObject response ==>" + new Gson().toJson(response));
                            Intent broadcastIntent = new Intent();
                            broadcastIntent.putParcelableArrayListExtra(NetworkConstant.EXTRA_DATA, dataList);
                            broadcastIntent.setAction(DairyNetworkConstant.MAIN_CATEGORY_LIST_ITEM_SUCCESS);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                        } else {
                            Timber.d("Got failure inMerchant TYpe Response...");
                            FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                            Timber.w("handleActionGetMerchantTypeList = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                            Intent broadcastIntent = new Intent();
                            broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                            broadcastIntent.setAction(DairyNetworkConstant.MAIN_CATEGORY_LIST_ITEM_FAIL);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError ofhandleActionGetMerchantTypeList API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());

                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionGetMerchantTypeList = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(DairyNetworkConstant.MAIN_CATEGORY_LIST_ITEM_FAIL);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });

    }


    /**
     * Handle action get dairy merchant list in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGetDairyMerchantList(Intent intent) {
        AddressRequest mAddressRequest = (AddressRequest) intent.getSerializableExtra(PARAM_BYMERCHANT_LIST_REQUEST_MODEL);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.MERCHANT_TYPE, mAddressRequest.productId);
            jsonObject.put(ResponseAttributeConstants.LATITUDE, mAddressRequest.latitude);
            jsonObject.put(ResponseAttributeConstants.LONGITUDE,mAddressRequest.longitude);

        } catch (JSONException e) {
        }
        AndroidNetworking.post(DairyNetworkConstant.BASE_URL_DAIRY + DairyNetworkConstant.REQUEST_PARAM_MAIN_LIST)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("=Dairy-Merchant=",response.toString());
                        Timber.e("called onResponse of dairy order.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                ArrayList<DairyMerchantListModel> item = JSONParser.getDairyMerchantList(response);
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putParcelableArrayListExtra(NetworkConstant.EXTRA_DATA, item);
                                broadcastIntent.setAction(DairyNetworkConstant.DAIRY_MERCHANT_LIST_STATUS_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in SignupResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionUserSignUp = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(DairyNetworkConstant.DAIRY_MERCHANT_LIST_STATUS_FAILURE);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of User dairy order API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());
                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionUserSignUp = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(DairyNetworkConstant.DAIRY_MERCHANT_LIST_STATUS_FAILURE);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });

    }

    /**
     * Handle action get dairy merchant list in the provided background thread with the provided
     * parameters.
     */
    private void handleActionAddOrder(Intent intent) {
        JSONObject obj = null;
        String paymentMode = intent.getStringExtra(EXTRA_PARAM3);
        String pinCode = intent.getStringExtra(EXTRA_PARAM4);
        String location = intent.getStringExtra(EXTRA_PARAM5);
        String lat = intent.getStringExtra(EXTRA_PARAM6);
        String lng = intent.getStringExtra(EXTRA_PARAM7);
        String complete_add = intent.getStringExtra(EXTRA_PARAM8);
        try {
            obj = JSONBuilder.getAddOrderJSon(ctx,intent.<PlaceOrderData>getParcelableArrayListExtra(EXTRA_PARAM1)
                    ,intent.getStringExtra(EXTRA_PARAM2), paymentMode,pinCode,intent.getStringExtra("ref_id"),
                    location,lat,lng,complete_add);
            Log.e("JSON",obj.toString());
        } catch (Exception e) {
            obj = new JSONObject();
        }
        AndroidNetworking.post(DairyNetworkConstant.ADD_ORDER_URL)
                .addJSONObjectBody(obj)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Timber.e("called onResponse of dairy add order.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
//                                ArrayList<DairyMerchantListModel> item = JSONParser.getDairyMerchantList(response);
                               Intent broadcastIntent = new Intent();
                               // broadcastIntent.putParcelableArrayListExtra(NetworkConstant.EXTRA_DATA, item);
                                broadcastIntent.setAction(DairyNetworkConstant.DAIRY_ADD_ORDER_STATUS_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in dairy add order...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionUserSignUp = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(DairyNetworkConstant.DAIRY_ADD_ORDER_STATUS_FAILURE);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of User dairy order API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());
                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionUserSignUp = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(DairyNetworkConstant.DAIRY_ADD_ORDER_STATUS_FAILURE);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });

    }

    /**
     * Handle action get dairy item list in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGetListItem(Intent intent) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", intent.getStringExtra(EXTRA_PARAM1));
        } catch (JSONException e) {
        }
        AndroidNetworking.get(DairyNetworkConstant.BASE_URL_DAIRY + DairyNetworkConstant.REQUEST_LIST_ITEM+intent.getStringExtra(EXTRA_PARAM1))
                //.addJSONObjectBody(jsonObject)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("--item--",response.toString());
                        Timber.e("called onResponse of dairy list itemorder.");
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                ArrayList<DairyProductListItem> item = JSONParser.getDairyListItem(response);
                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putParcelableArrayListExtra(NetworkConstant.EXTRA_DATA, item);
                                broadcastIntent.setAction(DairyNetworkConstant.DAIRY_LIST_ITEM_STATUS_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            } else {
                                Timber.d("Got failure in SignupResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionUserSignUp = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                                Intent broadcastIntent = new Intent();
                                broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                                broadcastIntent.setAction(DairyNetworkConstant.DAIRY_LIST_ITEM_STATUS_FAILURE);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of User dairy order API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());
                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionUserSignUp = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(NetworkConstant.EXTRA_DATA, mFailureResponse);
                        broadcastIntent.setAction(DairyNetworkConstant.DAIRY_LIST_ITEM_STATUS_FAILURE);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
                    }
                });

    }
}
