package com.wifyee.greenfields.mapper;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.foodorder.AddressResponse;
import com.wifyee.greenfields.foodorder.CartFoodOrderResponse;
import com.wifyee.greenfields.foodorder.CategoryList;
import com.wifyee.greenfields.foodorder.DeductMoneyWalletResponse;
import com.wifyee.greenfields.foodorder.FoodOderList;
import com.wifyee.greenfields.foodorder.FoodOrderResponse;
import com.wifyee.greenfields.foodorder.FoodOrderStatusResponse;
import com.wifyee.greenfields.foodorder.FoodStatusDetailResponse;
import com.wifyee.greenfields.foodorder.GstOnFoodItemResponse;
import com.wifyee.greenfields.foodorder.ItemsDeatils;
import com.wifyee.greenfields.foodorder.MenuByParentSlugResponse;
import com.wifyee.greenfields.foodorder.OrderStatusDetailList;
import com.wifyee.greenfields.foodorder.OrdersList;
import com.wifyee.greenfields.foodorder.Restaurant;
import com.wifyee.greenfields.foodorder.VegORNonVegList;
import com.wifyee.greenfields.models.requests.VerifyMobileNumber;
import com.wifyee.greenfields.models.response.AirtimeResponse;
import com.wifyee.greenfields.models.response.BankListResponseModel;
import com.wifyee.greenfields.models.response.BankTransferResponse;
import com.wifyee.greenfields.models.response.BankTransferStatus;
import com.wifyee.greenfields.models.response.BeneficiaryResponse;
import com.wifyee.greenfields.models.response.CityResponse;
import com.wifyee.greenfields.models.response.ClientBalanceResponse;
import com.wifyee.greenfields.models.response.ClientBankTransferResponse;
import com.wifyee.greenfields.models.response.ClientLogResponse;
import com.wifyee.greenfields.models.response.ClientProfile;
import com.wifyee.greenfields.models.response.ClientProfileUpdateResponse;
import com.wifyee.greenfields.models.response.ClientResetPassCodeResponse;
import com.wifyee.greenfields.models.response.ClientSendPassCodeResponse;
import com.wifyee.greenfields.models.response.ClientStatusResponse;
import com.wifyee.greenfields.models.response.ClientToClientMoneyTransferResponse;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.models.response.GetClientProfile;
import com.wifyee.greenfields.models.response.GetClientProfileInfo;
import com.wifyee.greenfields.models.response.GetClientProfileInfoResponse;
import com.wifyee.greenfields.models.response.GetClientProfileResponse;
import com.wifyee.greenfields.models.response.GetProfile;
import com.wifyee.greenfields.models.response.GetProfileInfo;
import com.wifyee.greenfields.models.response.LogItem;
import com.wifyee.greenfields.models.response.LogList;
import com.wifyee.greenfields.models.response.LoginResponse;
import com.wifyee.greenfields.models.response.MacUpdateAddressResponse;
import com.wifyee.greenfields.models.response.MerchantPaymentResponse;
import com.wifyee.greenfields.models.response.NearOfferList;
import com.wifyee.greenfields.models.response.NearOfferResponse;
import com.wifyee.greenfields.models.response.OTP_Response;
import com.wifyee.greenfields.models.response.OfferImageObj_ResponseList;
import com.wifyee.greenfields.models.response.OffersItem;
import com.wifyee.greenfields.models.response.OperatorListResponse;
import com.wifyee.greenfields.models.response.PayUPaymentGatewayResponse;
import com.wifyee.greenfields.models.response.PlanDetails;
import com.wifyee.greenfields.models.response.Profile;
import com.wifyee.greenfields.models.response.SendMoneyResponse;
import com.wifyee.greenfields.models.response.SignupResponse;
import com.wifyee.greenfields.models.response.SplitResponse;
import com.wifyee.greenfields.models.response.UploadClientProfilePictureResponse;
import com.wifyee.greenfields.models.response.VerifyMobileNumberResponse;
import com.wifyee.greenfields.models.response.operators.Airtime;
import com.wifyee.greenfields.models.response.operators.DthList;
import com.wifyee.greenfields.models.response.operators.Electricity;
import com.wifyee.greenfields.models.response.operators.ElectricityOrGas;
import com.wifyee.greenfields.models.response.operators.Gas;
import com.wifyee.greenfields.models.response.operators.Generate;
import com.wifyee.greenfields.models.response.operators.ICashCard;
import com.wifyee.greenfields.models.response.operators.Landline;
import com.wifyee.greenfields.models.response.operators.MoneyTransfer;
import com.wifyee.greenfields.models.response.operators.OperatorDetails;
import com.wifyee.greenfields.models.response.operators.OperatorList;
import com.wifyee.greenfields.models.response.operators.Postpaid;
import com.wifyee.greenfields.models.response.operators.Prepaid;
import com.wifyee.greenfields.models.response.operators.Transfer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class ModelMapper {

    public static AddressResponse transformJSONObjectToMenuByLocationResponse(JSONObject response) {
        AddressResponse getAddressResponse = null;
        try {
            if (response != null) {
                getAddressResponse = new AddressResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    getAddressResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.RESTAURANT))
                    getAddressResponse.restaurant = transformJSONArrayToMerchantIDList(new JSONArray(response.getString(ResponseAttributeConstants.RESTAURANT)));


                if (response.has(ResponseAttributeConstants.MSG))
                    getAddressResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToMenuByLocationResponse. Message : " + e.getMessage());
        }
        return getAddressResponse;
    }
    public static MenuByParentSlugResponse transformJSONObjectToMenuByParentSlugResponse(JSONObject response) {
        MenuByParentSlugResponse getMenuByParentSlugResponse = null;
        try {
            if (response != null) {
                getMenuByParentSlugResponse = new MenuByParentSlugResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    getMenuByParentSlugResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.CATEGORY))
                    getMenuByParentSlugResponse.categoryList = transformJSONArrayToCategoryList(new JSONArray(response.getString(ResponseAttributeConstants.CATEGORY)));


                if (response.has(ResponseAttributeConstants.MSG))
                    getMenuByParentSlugResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToFoodOrderResponse. Message : " + e.getMessage());
        }
        return getMenuByParentSlugResponse;
    }
    // GSTOnFoodOder List
    public static GstOnFoodItemResponse transformJSONObjectToGstOnFoodOrderResponse(JSONObject response) {
        GstOnFoodItemResponse getGstOnFoodItemResponse= null;
        try {
            if (response != null) {
                getGstOnFoodItemResponse = new GstOnFoodItemResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    getGstOnFoodItemResponse.status = response.getString(ResponseAttributeConstants.STATUS);
                if (response.has(ResponseAttributeConstants.GST))
                    getGstOnFoodItemResponse.gst =  response.getString(ResponseAttributeConstants.GST);
                if (response.has(ResponseAttributeConstants.MSG))
                    getGstOnFoodItemResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToGstOnFoodOrderResponse. Message : " + e.getMessage());
        }
        return getGstOnFoodItemResponse;
    }
    // FoodOder List
    public static FoodOrderResponse transformJSONObjectToFoodOrderResponse(JSONObject response) {
        FoodOrderResponse getFoodOderResponse = null;
        try {
            if (response != null) {
                getFoodOderResponse = new FoodOrderResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    getFoodOderResponse.status = response.getString(ResponseAttributeConstants.STATUS);
                if (response.has(ResponseAttributeConstants.ITEM))
                    getFoodOderResponse.FoodOderList = transformJSONArrayToFoodOderList(new JSONArray(response.getString(ResponseAttributeConstants.ITEM)));
                if (response.has(ResponseAttributeConstants.MSG))
                    getFoodOderResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToFoodOrderResponse. Message : " + e.getMessage());
        }
        return getFoodOderResponse;
    }
    public static FoodStatusDetailResponse transformJSONObjectToFoodorderStatusDetailResponse(JSONObject response) {
        FoodStatusDetailResponse getFoodStatusDetailResponse = null;
        try {
            if (response != null) {
                getFoodStatusDetailResponse = new FoodStatusDetailResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    getFoodStatusDetailResponse.status = response.getString(ResponseAttributeConstants.STATUS);
                if (response.has(ResponseAttributeConstants.ORDERS))
                    //  getFoodStatusDetailResponse.orderStatusDetailList = transformJSONArrayToFoodOrderStatusDeatilsOderList(new JSONArray(response.getString(ResponseAttributeConstants.ORDERS)));
                    getFoodStatusDetailResponse.orderStatusDetailList = transformJSONArrayToFoodOrderStatusDeatilsOderList(new JSONObject(response.getString(ResponseAttributeConstants.ORDERS)));
                if (response.has(ResponseAttributeConstants.MSG))
                    getFoodStatusDetailResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToFoodOrderResponse. Message : " + e.getMessage());
        }
        return getFoodStatusDetailResponse;
    }
    public static OrderStatusDetailList transformJSONArrayToFoodOrderStatusDeatilsOderList(JSONObject response) {
        OrderStatusDetailList mOrderStatusDetailList = null;
        try {
            if (response != null) {
                mOrderStatusDetailList = new OrderStatusDetailList();
                if (response.has(ResponseAttributeConstants.ORDER_ID))
                    mOrderStatusDetailList.order_id = response.getString(ResponseAttributeConstants.ORDER_ID);

                if (response.has(ResponseAttributeConstants.ORDER_AMOUNT))
                    mOrderStatusDetailList.order_amount = response.getString(ResponseAttributeConstants.ORDER_AMOUNT);

                if (response.has(ResponseAttributeConstants.ORDER_ON))
                    mOrderStatusDetailList.order_on = response.getString(ResponseAttributeConstants.ORDER_ON);

                if (response.has(ResponseAttributeConstants.ORDER_STATUS))
                    mOrderStatusDetailList.order_status = response.getString(ResponseAttributeConstants.ORDER_STATUS);

              /*  if (response.has(ResponseAttributeConstants.ITEM))
                    mOrderStatusDetailList.orderStatusItemsDetailList = transformJSONArrayToFoodOrderStatusItemsDeatilsOderList(new JSONArray(response.getString(ResponseAttributeConstants.ORDERS)));
            */    if (response.has(ResponseAttributeConstants.ITEM))
                    mOrderStatusDetailList.orderStatusItemsDetailList =transformJSONArrayToFoodOrderStatusDeatilsOderList(new JSONArray(response.getString(ResponseAttributeConstants.ITEM)));

            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToAgentAirtimeLog. Message : " + e.getMessage());
        }
        return mOrderStatusDetailList;
    }


    public static DeductMoneyWalletResponse transformJSONObjectToDeductMoneyPaymentResponse(JSONObject response) {
        DeductMoneyWalletResponse deductMoneyWalletResponse = null;
        try {
            if (response != null) {
                deductMoneyWalletResponse = new DeductMoneyWalletResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    deductMoneyWalletResponse.status = response.getString(ResponseAttributeConstants.STATUS);
                if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                    deductMoneyWalletResponse.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);
                if (response.has(ResponseAttributeConstants.CLIENT_NEW_BALANCE))
                    deductMoneyWalletResponse.clientNewBalance = response.getString(ResponseAttributeConstants.CLIENT_NEW_BALANCE);
                if (response.has(ResponseAttributeConstants.CLIENT_ID))
                    deductMoneyWalletResponse.clientId = response.getString(ResponseAttributeConstants.CLIENT_ID);
                if (response.has(ResponseAttributeConstants.CLIENT_MOBILE))
                    deductMoneyWalletResponse.clientMobile = response.getString(ResponseAttributeConstants.CLIENT_MOBILE);
                if (response.has(ResponseAttributeConstants.CLIENTMOBICASHLOGID))
                    deductMoneyWalletResponse.clientMobicashLogId = response.getString(ResponseAttributeConstants.CLIENTMOBICASHLOGID);
                if (response.has(ResponseAttributeConstants.CLIENT_TRANSACTIONDATE))
                    deductMoneyWalletResponse.clientTransactionDate = response.getString(ResponseAttributeConstants.CLIENT_TRANSACTIONDATE);
                if (response.has(ResponseAttributeConstants.MSG))
                    deductMoneyWalletResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToDeductMoneyPaymentResponse. Message : " + e.getMessage());
        }
        return deductMoneyWalletResponse;
    }

    public static List<ItemsDeatils> transformJSONArrayToFoodOrderStatusDeatilsOderList(JSONArray response) {
        List<ItemsDeatils> foodOrderItemsDetail = new ArrayList<>();
        try {
            ItemsDeatils Item_ordersList;
            for (int index = 0; index < response.length(); index++) {
                JSONObject json_data = response.getJSONObject(index);
                Item_ordersList = transformToFoodOderStatusItemsDetailListItem(json_data);
                if (Item_ordersList != null) {
                    foodOrderItemsDetail.add(Item_ordersList);
                }
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONArrayToFoodOrderStatusDeatilsOderList. Message : " + e.getMessage());

        }
        return foodOrderItemsDetail;
    }

    private static ItemsDeatils transformToFoodOderStatusItemsDetailListItem(JSONObject response) {

        ItemsDeatils foodOderItems = null;
        try {
            if (response != null) {
                foodOderItems = new ItemsDeatils();

                if (response.has(ResponseAttributeConstants.ITEMNAME))
                    foodOderItems.Name = response.getString(ResponseAttributeConstants.ITEMNAME);

                if (response.has(ResponseAttributeConstants.ITEMQUANTITY))
                    foodOderItems.quantity = response.getString(ResponseAttributeConstants.ITEMQUANTITY);

                if (response.has(ResponseAttributeConstants.ITEMAMOUNT))
                    foodOderItems.amount = response.getString(ResponseAttributeConstants.ITEMAMOUNT);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformToFoodOderStatusItem. Message : " + e.getMessage());
        }
        return foodOderItems;
    }
    public static FoodOrderStatusResponse transformJSONObjectToSubmitFoodorderStatusResponse(JSONObject response) {
        FoodOrderStatusResponse getFoodOderStatusResponse = null;
        try {
            if (response != null) {
                getFoodOderStatusResponse = new FoodOrderStatusResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    getFoodOderStatusResponse.status = response.getString(ResponseAttributeConstants.STATUS);
                if (response.has(ResponseAttributeConstants.ORDERS))
                    getFoodOderStatusResponse.orderStatusList = transformJSONArrayToFoodOrderStatusOderList(new JSONArray(response.getString(ResponseAttributeConstants.ORDERS)));

                if (response.has(ResponseAttributeConstants.MSG))
                    getFoodOderStatusResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToFoodOrderResponse. Message : " + e.getMessage());
        }
        return getFoodOderStatusResponse;
    }

    public static CartFoodOrderResponse transformJSONObjectToSubmitFoodorderResponse(JSONObject response) {
        CartFoodOrderResponse getFoodOderResponse = null;
        try {
            if (response != null) {
                getFoodOderResponse = new CartFoodOrderResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    getFoodOderResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.MSG))
                    getFoodOderResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToFoodOrderResponse. Message : " + e.getMessage());
        }
        return getFoodOderResponse;
    }

    public static NearOfferResponse transformJSONObjectToNearOfferResponse(JSONObject response) {
        NearOfferResponse getFoodOderResponse = null;
        try {
            if (response != null) {
                getFoodOderResponse = new NearOfferResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    getFoodOderResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.MSG))
                    getFoodOderResponse.msg = response.getString(ResponseAttributeConstants.MSG);

                if (response.has(ResponseAttributeConstants.OFFERS_DATA))
                    getFoodOderResponse.mNearOfferListList = transformJSONArrayToNearOffer_List(new JSONArray(response.getString(ResponseAttributeConstants.OFFERS_DATA)));
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToFoodOrderResponse. Message : " + e.getMessage());
        }
        return getFoodOderResponse;
    }
    public static List<NearOfferList> transformJSONArrayToNearOffer_List(JSONArray response) {
        List<NearOfferList> foodOderLists = new ArrayList<>();
        try {
            NearOfferList tmpVegOrNonVeg;
            for (int index = 0; index < response.length(); index++) {
                JSONObject json_data = response.getJSONObject(index);
                tmpVegOrNonVeg = transformToNearOfferListItem(json_data);
                if (tmpVegOrNonVeg != null) {
                    foodOderLists.add(tmpVegOrNonVeg);
                }
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONArrayToFoodOderList. Message : " + e.getMessage());

        }
        return foodOderLists;
    }
    private static NearOfferList transformToNearOfferListItem(JSONObject response) {
       // ArrayList VegOrNonvegFoodModelsList=new ArrayList();
        NearOfferList foodOderItem = null;
        try {
            if (response != null) {
                foodOderItem = new NearOfferList();
                if (response.has(ResponseAttributeConstants.MERCHANTID))
                    foodOderItem.itemId = response.getString("itemId");
                foodOderItem.itemName= response.getString("itemName");
                foodOderItem.itemPrice = response.getString("itemPrice");
                foodOderItem.discountType = response.getString("discountType");
                foodOderItem.discount = response.getString("discount");
                foodOderItem.mobicash_commission = response.getString("mobicash_commission");
                foodOderItem.commission_type = response.getString("commission_type");
                foodOderItem.quentity = response.getString("quentity");
                foodOderItem.status = response.getString("status");
                foodOderItem.createdDate = response.getString("createdDate");
                foodOderItem.updateddDate = response.getString("updateddDate");
                foodOderItem.mOfferImageObj_responseLists= transformJSONArrayToNearOfferImage_List(new JSONArray(response.getString(ResponseAttributeConstants.OFFERS_DATA)));

            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformToFoodOderItem. Message : " + e.getMessage());
        }
        return foodOderItem;
    }
    public static List<OfferImageObj_ResponseList> transformJSONArrayToNearOfferImage_List(JSONArray response) {
        List<OfferImageObj_ResponseList> foodOderLists = new ArrayList<>();
        try {
            OfferImageObj_ResponseList tmpVegOrNonVeg;
            for (int index = 0; index < response.length(); index++) {
                JSONObject json_data = response.getJSONObject(index);
                tmpVegOrNonVeg = transformToNearOfferListImageItem(json_data);
                if (tmpVegOrNonVeg != null) {
                    foodOderLists.add(tmpVegOrNonVeg);
                }
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONArrayToFoodOderList. Message : " + e.getMessage());

        }
        return foodOderLists;
    }
    private static OfferImageObj_ResponseList transformToNearOfferListImageItem(JSONObject response) {
        // ArrayList VegOrNonvegFoodModelsList=new ArrayList();
        OfferImageObj_ResponseList foodOderItem = null;
        try {
            if (response != null) {
                foodOderItem = new OfferImageObj_ResponseList();
                if (response.has(ResponseAttributeConstants.MERCHANTID))
                foodOderItem.imageId = response.getString("imageId");
                foodOderItem.imagePath= response.getString("imagePath");

            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformToFoodOderItem. Message : " + e.getMessage());
        }
        return foodOderItem;
    }
    public static FoodOrderResponse transformJSONObjectToVegOrNonVegFoodOrderResponse(JSONObject response) {
        FoodOrderResponse getFoodOderResponse = null;
        try {
            if (response != null) {
                getFoodOderResponse = new FoodOrderResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    getFoodOderResponse.status = response.getString(ResponseAttributeConstants.STATUS);
                if (response.has(ResponseAttributeConstants.ITEM))
                    getFoodOderResponse.VegOrNonVegFoodOderList = transformJSONArrayToVegOrNonVegFoodOderList(new JSONArray(response.getString(ResponseAttributeConstants.ITEM)));
                if (response.has(ResponseAttributeConstants.MSG))
                    getFoodOderResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToFoodOrderResponse. Message : " + e.getMessage());
        }
        return getFoodOderResponse;
    }
    public static List<Restaurant> transformJSONArrayToMerchantIDList(JSONArray response) {
        List<Restaurant> categoryLists = new ArrayList<>();
        try {
            Restaurant tmpCategoryList;
            for (int index = 0; index < response.length(); index++) {
                JSONObject json_data = response.getJSONObject(index);
                tmpCategoryList = transformToRestaurantListItem(json_data);
                if (tmpCategoryList != null) {
                    categoryLists.add(tmpCategoryList);
                }
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONArrayToFoodOderList. Message : " + e.getMessage());

        }
        return categoryLists;
    }
    public static List<CategoryList> transformJSONArrayToCategoryList(JSONArray response) {
        List<CategoryList> categoryLists = new ArrayList<>();
        try {
            CategoryList tmpCategoryList;
            for (int index = 0; index < response.length(); index++) {
                JSONObject json_data = response.getJSONObject(index);
                tmpCategoryList = transformToCategoryListItem(json_data);
                if (tmpCategoryList != null) {
                    categoryLists.add(tmpCategoryList);
                }
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONArrayToFoodOderList. Message : " + e.getMessage());

        }
        return categoryLists;
    }
    private static Restaurant transformToRestaurantListItem(JSONObject response) {
        Restaurant categoryListItem = null;
        try {
            if (response != null) {
                categoryListItem = new Restaurant();

                if (response.has(ResponseAttributeConstants.MERCHANTID))
                    categoryListItem.merchant_id = response.getString(ResponseAttributeConstants.MERCHANTID);

                if (response.has(ResponseAttributeConstants.RESTAURANRAME))
                    categoryListItem.restaurant_name = response.getString(ResponseAttributeConstants.RESTAURANRAME);

                if (response.has(ResponseAttributeConstants.LOGO))
                    categoryListItem.logo = response.getString(ResponseAttributeConstants.LOGO);

                if (response.has(ResponseAttributeConstants.CURRENT_STATUS))
                    categoryListItem.status = response.getString(ResponseAttributeConstants.CURRENT_STATUS);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformToRestaurantListItem. Message : " + e.getMessage());
        }
        return categoryListItem;
    }
    private static CategoryList transformToCategoryListItem(JSONObject response) {
        CategoryList categoryListItem = null;
        try {
            if (response != null) {
                categoryListItem = new CategoryList();

                if (response.has(ResponseAttributeConstants.CATEGORYID))
                    categoryListItem.categoryId = response.getString(ResponseAttributeConstants.CATEGORYID);

                if (response.has(ResponseAttributeConstants.CATEGORYNAME))
                    categoryListItem.categoryName = response.getString(ResponseAttributeConstants.CATEGORYNAME);

                if (response.has(ResponseAttributeConstants.CATEGORYSLUG))
                    categoryListItem.categorySlug = response.getString(ResponseAttributeConstants.CATEGORYSLUG);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformToFoodOderItem. Message : " + e.getMessage());
        }
        return categoryListItem;
    }

    public static List<FoodOderList> transformJSONArrayToFoodOderList(JSONArray response) {
        List<FoodOderList> foodOderLists = new ArrayList<>();
        try {
            FoodOderList tmpAirtimeLog;
            for (int index = 0; index < response.length(); index++) {
                JSONObject json_data = response.getJSONObject(index);
                tmpAirtimeLog = transformToFoodOderItem(json_data);
                if (tmpAirtimeLog != null) {
                    foodOderLists.add(tmpAirtimeLog);
                }
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONArrayToFoodOderList. Message : " + e.getMessage());

        }
        return foodOderLists;
    }
    public static List<OrdersList> transformJSONArrayToFoodOrderStatusOderList(JSONArray response) {
        List<OrdersList> foodOrderStautsLists = new ArrayList<>();
        try {
            OrdersList ordersList;
            for (int index = 0; index < response.length(); index++) {
                JSONObject json_data = response.getJSONObject(index);
                ordersList = transformToFoodOderStatusItem(json_data);
                if (ordersList != null) {
                    foodOrderStautsLists.add(ordersList);
                }
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONArrayToFoodOrderStatusOderList. Message : " + e.getMessage());

        }
        return foodOrderStautsLists;
    }
    private static OrdersList transformToFoodOderStatusItem(JSONObject response) {
        ArrayList OrdersList=new ArrayList();
        OrdersList foodOderItem = null;
        try {
            if (response != null) {
                foodOderItem = new OrdersList();

                if (response.has(ResponseAttributeConstants.ORDER_ID))
                    foodOderItem.order_id = response.getString(ResponseAttributeConstants.ORDER_ID);

                if (response.has(ResponseAttributeConstants.ORDER_AMOUNT))
                    foodOderItem.order_amount = response.getString(ResponseAttributeConstants.ORDER_AMOUNT);

                if (response.has(ResponseAttributeConstants.ORDER_ON))
                    foodOderItem.order_on = response.getString(ResponseAttributeConstants.ORDER_ON);

                if (response.has(ResponseAttributeConstants.ORDER_STATUS))
                    foodOderItem.order_status = response.getString(ResponseAttributeConstants.ORDER_STATUS);
                if (response.has(ResponseAttributeConstants.UPDATED_ON))
                    foodOderItem.updated_on = response.getString(ResponseAttributeConstants.UPDATED_ON);
                if (response.has(ResponseAttributeConstants.PAY_MODE))
                    foodOderItem.payment_mode = response.getString(ResponseAttributeConstants.PAY_MODE);



            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformToFoodOderStatusItem. Message : " + e.getMessage());
        }
        return foodOderItem;
    }
    public static List<VegORNonVegList> transformJSONArrayToVegOrNonVegFoodOderList(JSONArray response) {
        List<VegORNonVegList> foodOderLists = new ArrayList<>();
        try {
            VegORNonVegList tmpVegOrNonVeg;
            for (int index = 0; index < response.length(); index++) {
                JSONObject json_data = response.getJSONObject(index);
                tmpVegOrNonVeg = transformToVegOrNonVegFoodOderItem(json_data);
                if (tmpVegOrNonVeg != null) {
                    foodOderLists.add(tmpVegOrNonVeg);
                }
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONArrayToFoodOderList. Message : " + e.getMessage());

        }
        return foodOderLists;
    }
    private static VegORNonVegList transformToVegOrNonVegFoodOderItem(JSONObject response) {
        ArrayList VegOrNonvegFoodModelsList = new ArrayList();
        VegORNonVegList foodOderItem = null;
        try {
            if (response != null) {
                foodOderItem = new VegORNonVegList();
                if (response.has(ResponseAttributeConstants.MERCHANTID))
                    foodOderItem.merchantId = response.getString(ResponseAttributeConstants.MERCHANTID);

                if (response.has(ResponseAttributeConstants.ITEMID))
                    foodOderItem.itemId = response.getString(ResponseAttributeConstants.ITEMID);

                if (response.has(ResponseAttributeConstants.FOODNAME))
                    foodOderItem.name = response.getString(ResponseAttributeConstants.FOODNAME);

                if (response.has(ResponseAttributeConstants.FOOD_PRICE))
                    foodOderItem.price = response.getString(ResponseAttributeConstants.FOOD_PRICE);

                if (response.has(ResponseAttributeConstants.FOOD_DESCRIPTION))
                    foodOderItem.description = response.getString(ResponseAttributeConstants.FOOD_DESCRIPTION);

                if (response.has(ResponseAttributeConstants.FOOD_IMAGE))
                    /*foodOderItem.foodImage = response.getString(ResponseAttributeConstants.FOOD_IMAGE);*/
                    foodOderItem.foodImage = response.getString(ResponseAttributeConstants.FOOD_IMAGE);

                VegOrNonvegFoodModelsList.add(new VegORNonVegList(response.getString(ResponseAttributeConstants.CATEGORY),response.getString(ResponseAttributeConstants.MERCHANTID),response.getString(ResponseAttributeConstants.FOODNAME),response.getString(ResponseAttributeConstants.FOOD_PRICE),response.getString(ResponseAttributeConstants.FOOD_DESCRIPTION),
                        response.getString(ResponseAttributeConstants.FOOD_IMAGE),false));
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformToFoodOderItem. Message : " + e.getMessage());
        }
        return foodOderItem;
    }
    private static FoodOderList transformToFoodOderItem(JSONObject response) {
        ArrayList FoodModelsList=new ArrayList();
        FoodOderList foodOderItem = null;
        try {
            if (response != null) {
                foodOderItem = new FoodOderList();

                if (response.has(ResponseAttributeConstants.ITEMID))
                    foodOderItem.itemID = response.getString(ResponseAttributeConstants.ITEMID);

                if (response.has(ResponseAttributeConstants.FOODNAME))
                    foodOderItem.name = response.getString(ResponseAttributeConstants.FOODNAME);

                if (response.has(ResponseAttributeConstants.FOOD_PRICE))
                    foodOderItem.price = response.getString(ResponseAttributeConstants.FOOD_PRICE);

                if (response.has(ResponseAttributeConstants.FOOD_DESCRIPTION))
                    foodOderItem.description = response.getString(ResponseAttributeConstants.FOOD_DESCRIPTION);

                if (response.has(ResponseAttributeConstants.FOOD_QUANTITY))
                    foodOderItem.quantiy = response.getString(ResponseAttributeConstants.FOOD_QUANTITY);

                if (response.has(ResponseAttributeConstants.FOOD_IMAGE))
                    /*foodOderItem.foodImage = response.getString(ResponseAttributeConstants.FOOD_IMAGE);*/
                    foodOderItem.foodImage = response.getString(ResponseAttributeConstants.FOOD_IMAGE);

                if (response.has(ResponseAttributeConstants.FOOD_DISCOUNT_PRICE))
                    foodOderItem.discountPrice = response.getString(ResponseAttributeConstants.FOOD_DISCOUNT_PRICE);

                if (response.has(ResponseAttributeConstants.FOOD_CATEGORY))
                    foodOderItem.category = response.getString(ResponseAttributeConstants.FOOD_CATEGORY);

                if (response.has(ResponseAttributeConstants.FOOD_WIFYEE_COMMISION))
                    foodOderItem.wifyeeCommision = response.getString(ResponseAttributeConstants.FOOD_WIFYEE_COMMISION);

                if (response.has(ResponseAttributeConstants.FOOD_DIST_COMMISION))
                    foodOderItem.distCommision = response.getString(ResponseAttributeConstants.FOOD_DIST_COMMISION);

                FoodModelsList.add(new FoodOderList(
                        response.getString(ResponseAttributeConstants.MERCHANTID),
                        response.getString(ResponseAttributeConstants.FOODNAME),
                        response.getString(ResponseAttributeConstants.FOOD_PRICE),
                        response.getString(ResponseAttributeConstants.FOOD_DESCRIPTION),
                        response.getString(ResponseAttributeConstants.FOOD_QUANTITY),
                        response.getString(ResponseAttributeConstants.FOOD_IMAGE),
                        response.getString(ResponseAttributeConstants.FOOD_DISCOUNT_PRICE),
                        response.getString(ResponseAttributeConstants.FOOD_CATEGORY),
                        false,
                        response.getString(ResponseAttributeConstants.FOOD_WIFYEE_COMMISION),
                        response.getString(ResponseAttributeConstants.FOOD_DIST_COMMISION)));
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformToFoodOderItem. Message : " + e.getMessage());
        }
        return foodOderItem;
    }
    public static MacUpdateAddressResponse transformJSONObjectToMACUpdateResponse(JSONObject response) {
        MacUpdateAddressResponse mMacUpdateAddressResponse = null;
        try {
            if (response != null) {
                mMacUpdateAddressResponse = new MacUpdateAddressResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    mMacUpdateAddressResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.MSG))
                    mMacUpdateAddressResponse.msg = response.getString(ResponseAttributeConstants.MSG);



            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToMACUpdateResponse. Message : " + e.getMessage());
        }
        return mMacUpdateAddressResponse;
    }
    /**
     * Client Signup API....
     */
    public static SignupResponse transformJSONObjectToSignupResponse(JSONObject response) {
        SignupResponse signupResponse = null;
        try {
            if (response != null) {
                signupResponse = new SignupResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    signupResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                    signupResponse.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);

                if (response.has(ResponseAttributeConstants.CLIENT_ID))
                    signupResponse.clientId = response.getString(ResponseAttributeConstants.CLIENT_ID);

                if (response.has(ResponseAttributeConstants.CLIENT_MOBILE))
                    signupResponse.clientMobile = response.getString(ResponseAttributeConstants.CLIENT_MOBILE);

                if (response.has(ResponseAttributeConstants.PIN_CODE))
                    signupResponse.pincode = response.getString(ResponseAttributeConstants.PIN_CODE);

                if (response.has(ResponseAttributeConstants.ENROLLED_DATE))
                    signupResponse.enrolledDate = response.getString(ResponseAttributeConstants.ENROLLED_DATE);

                if (response.has(ResponseAttributeConstants.MSG))
                    signupResponse.msg = response.getString(ResponseAttributeConstants.MSG);

            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToSignupResponse. Message : " + e.getMessage());
        }
        return signupResponse;
    }

    /**
     * Merchant Payment Response ....
     */
    public static MerchantPaymentResponse transformJSONObjectToMerchantPaymentResponse(JSONObject response) {
        MerchantPaymentResponse merchantPaymentResponse = null;
        try {
            if (response != null) {
                merchantPaymentResponse = new MerchantPaymentResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    merchantPaymentResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                    merchantPaymentResponse.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);

                if (response.has(ResponseAttributeConstants.MSG))
                    merchantPaymentResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToClientResetPasscodeResponse. Message : " + e.getMessage());
        }
        return merchantPaymentResponse;
    }

    /**
     * Merchant Payment Response ....
     */
    public static SendMoneyResponse transformJSONObjectToSendMoneyResponse(JSONObject response) {
        SendMoneyResponse sendMoneyResponse = null;
        try {
            if (response != null) {
                sendMoneyResponse = new SendMoneyResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    sendMoneyResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                    sendMoneyResponse.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);

                if (response.has(ResponseAttributeConstants.MSG))
                    sendMoneyResponse.msg = response.getString(ResponseAttributeConstants.MSG);

                if (response.has(ResponseAttributeConstants.RECEIVER_MOBILE))
                    sendMoneyResponse.receiverMobile = response.getString(ResponseAttributeConstants.RECEIVER_MOBILE);

                if (response.has(ResponseAttributeConstants.SENDER_MOBILE))
                    sendMoneyResponse.senderMobile = response.getString(ResponseAttributeConstants.SENDER_MOBILE);

                if (response.has(ResponseAttributeConstants.RECEIVER_TYPE))
                    sendMoneyResponse.receiverType = response.getString(ResponseAttributeConstants.RECEIVER_TYPE);
                if (response.has(ResponseAttributeConstants.AMOUNT))
                    sendMoneyResponse.amount = response.getString(ResponseAttributeConstants.AMOUNT);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToClientToClientWallet. Message : " + e.getMessage());
        }
        return sendMoneyResponse;
    }

    /**
     * Client Login API....
     */
    public static LoginResponse transformJSONObjectToLoginResponse(JSONObject response) {
        LoginResponse loginResponse = null;
        try {
            if (response != null) {
                loginResponse = new LoginResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    loginResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                    loginResponse.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);

                if (response.has(ResponseAttributeConstants.CLIENT_STATUS))
                    loginResponse.clientStatus = response.getString(ResponseAttributeConstants.CLIENT_STATUS);

                if (response.has(ResponseAttributeConstants.CLIENT_ID))
                    loginResponse.clientId = response.getString(ResponseAttributeConstants.CLIENT_ID);

                if (response.has(ResponseAttributeConstants.CLIENT_MOBILE))
                    loginResponse.clientMobile = response.getString(ResponseAttributeConstants.CLIENT_MOBILE);

                if (response.has(ResponseAttributeConstants.LOGIN_DATE))
                    loginResponse.loginDate = response.getString(ResponseAttributeConstants.LOGIN_DATE);

                if (response.has(ResponseAttributeConstants.MSG))
                    loginResponse.msg = response.getString(ResponseAttributeConstants.MSG);

                if (response.has(ResponseAttributeConstants.PIN_CODE))
                    loginResponse.passCode = response.getString(ResponseAttributeConstants.PIN_CODE);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToLoginResponse. Message : " + e.getMessage());
        }
        return loginResponse;
    }

    // Call Split Response


    /**
     * Client Login API....
     */
    public static SplitResponse transformJSONObjectToSplitResponse(JSONObject response) {
        SplitResponse splitResponse = null;
        try {
            if (response != null) {
                splitResponse = new SplitResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    splitResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.LAST_INSERTED))
                    splitResponse.lastInsertId = response.getString(ResponseAttributeConstants.LAST_INSERTED);

                if (response.has(ResponseAttributeConstants.MSG))
                    splitResponse.msg = response.getString(ResponseAttributeConstants.MSG);

            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToLoginResponse. Message : " + e.getMessage());
        }
        return splitResponse;
    }

    /**
     * Client To Client Money Transfer API....
     */
    public static ClientToClientMoneyTransferResponse transformJSONObjectToClientToClientMoneyTransferResponse(JSONObject response) {
        ClientToClientMoneyTransferResponse clientToClientMoneyTransferResponse = null;
        try {
            if (response != null) {
                clientToClientMoneyTransferResponse = new ClientToClientMoneyTransferResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    clientToClientMoneyTransferResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                    clientToClientMoneyTransferResponse.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);

                if (response.has(ResponseAttributeConstants.RECEIVER_MOBILE))
                    clientToClientMoneyTransferResponse.receiverMobile = response.getString(ResponseAttributeConstants.RECEIVER_MOBILE);

                if (response.has(ResponseAttributeConstants.SENDER_MOBILE))
                    clientToClientMoneyTransferResponse.senderMobile = response.getString(ResponseAttributeConstants.SENDER_MOBILE);

                if (response.has(ResponseAttributeConstants.AMOUNT))
                    clientToClientMoneyTransferResponse.amount = response.getString(ResponseAttributeConstants.AMOUNT);

                if (response.has(ResponseAttributeConstants.TRANSACTION_DATE))
                    clientToClientMoneyTransferResponse.transactionDate = response.getString(ResponseAttributeConstants.TRANSACTION_DATE);

                if (response.has(ResponseAttributeConstants.MSG))
                    clientToClientMoneyTransferResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToClientToClientMoneyTransferResponse. Message : " + e.getMessage());
        }
        return clientToClientMoneyTransferResponse;
    }

    /**
     * Client Status API....
     */
    public static ClientStatusResponse transformJSONObjectToClientStatusResponse(JSONObject response) {
        ClientStatusResponse clientStatusResponse = null;
        try {
            if (response != null) {
                clientStatusResponse = new ClientStatusResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    clientStatusResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                    clientStatusResponse.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);

                if (response.has(ResponseAttributeConstants.CLIENT_STATUS))
                    clientStatusResponse.clientStatus = response.getString(ResponseAttributeConstants.CLIENT_STATUS);

                if (response.has(ResponseAttributeConstants.CLIENT_ID))
                    clientStatusResponse.clientId = response.getString(ResponseAttributeConstants.CLIENT_ID);

                if (response.has(ResponseAttributeConstants.CLIENT_MOBILE))
                    clientStatusResponse.clientMobile = response.getString(ResponseAttributeConstants.CLIENT_MOBILE);

                if (response.has(ResponseAttributeConstants.LOGIN_DATE))
                    clientStatusResponse.loginDate = response.getString(ResponseAttributeConstants.LOGIN_DATE);

                if (response.has(ResponseAttributeConstants.MSG))
                    clientStatusResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToClientStatusResponse. Message : " + e.getMessage());
        }
        return clientStatusResponse;
    }


    /**
     * Client Balance API....
     */
    public static ClientBalanceResponse transformJSONObjectToClientBalanceResponse(JSONObject response) {
        ClientBalanceResponse clientBalanceResponse = null;
        try {
            if (response != null) {
                clientBalanceResponse = new ClientBalanceResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    clientBalanceResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                    clientBalanceResponse.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);

                if (response.has(ResponseAttributeConstants.CLIENT_BALANCE))
                    clientBalanceResponse.clientBalance = response.getString(ResponseAttributeConstants.CLIENT_BALANCE);

                if (response.has(ResponseAttributeConstants.CLIENT_ID))
                    clientBalanceResponse.clientId = response.getString(ResponseAttributeConstants.CLIENT_ID);

                if (response.has(ResponseAttributeConstants.CLIENT_MOBILE))
                    clientBalanceResponse.clientMobile = response.getString(ResponseAttributeConstants.CLIENT_MOBILE);

                if (response.has(ResponseAttributeConstants.REQ_DATE))
                    clientBalanceResponse.reqDate = response.getString(ResponseAttributeConstants.REQ_DATE);

                if (response.has(ResponseAttributeConstants.MSG))
                    clientBalanceResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToClientBalanceResponse. Message : " + e.getMessage());
        }
        return clientBalanceResponse;
    }


    /**
     * Client Airtime API....
     */
    public static AirtimeResponse transformJSONObjectToAirtimeResponse(JSONObject response) {
        AirtimeResponse airtimeResponse = null;
        try {
            if (response != null) {
                airtimeResponse = new AirtimeResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    airtimeResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                    airtimeResponse.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);

                if (response.has(ResponseAttributeConstants.MODULE_TYPE))
                    airtimeResponse.moduleType = response.getString(ResponseAttributeConstants.MODULE_TYPE);

                if (response.has(ResponseAttributeConstants.NUMBER))
                    airtimeResponse.number = response.getString(ResponseAttributeConstants.NUMBER);

                if (response.has(ResponseAttributeConstants.AMOUNT))
                    airtimeResponse.amount = response.getString(ResponseAttributeConstants.AMOUNT);

                if (response.has(ResponseAttributeConstants.RECHARGE_STATUS))
                    airtimeResponse.rechargeStatus = response.getString(ResponseAttributeConstants.RECHARGE_STATUS);

                if (response.has(ResponseAttributeConstants.REFERENCE_ID))
                    airtimeResponse.referenceID = response.getString(ResponseAttributeConstants.REFERENCE_ID);

                if (response.has(ResponseAttributeConstants.TXN_DATE))
                    airtimeResponse.txnDate = response.getString(ResponseAttributeConstants.TXN_DATE);

                if (response.has(ResponseAttributeConstants.MSG))
                    airtimeResponse.msg = response.getString(ResponseAttributeConstants.MSG);

                if (response.has(ResponseAttributeConstants.RECHARGE_ID))
                    airtimeResponse.rechargeID = response.getString(ResponseAttributeConstants.RECHARGE_ID);

            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToAirtimeResponse. Message : " + e.getMessage());
        }
        return airtimeResponse;
    }

    /**
     * Client Operator List API....
     */
    public static OperatorListResponse transformJSONObjectToOperatorListAPIResponse(JSONObject response) {
        OperatorListResponse operatorListResponse = null;
        try {
            if (response != null) {
                operatorListResponse = new OperatorListResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    operatorListResponse.status = response.getString(ResponseAttributeConstants.STATUS);
                if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                    operatorListResponse.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);

                if (response.has(ResponseAttributeConstants.FORMAT))
                    operatorListResponse.format = response.getString(ResponseAttributeConstants.FORMAT);

                if (response.has(ResponseAttributeConstants.REQ_DATE))
                    operatorListResponse.reqDate = response.getString(ResponseAttributeConstants.REQ_DATE);

                if (response.has(ResponseAttributeConstants.MSG))
                    operatorListResponse.msg = response.getString(ResponseAttributeConstants.MSG);

                if (response.has(ResponseAttributeConstants.OPERATOR_LIST))
                    operatorListResponse.operatorList = ModelMapper.transformJSONArrayToOperatorList(new JSONArray(response.getString(ResponseAttributeConstants.OPERATOR_LIST)));
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToOperatorListAPIResponse. Message : " + e.getMessage());
        }
        return operatorListResponse;
    }

    private static OperatorList transformJSONArrayToOperatorList(JSONArray jsonArray) {
        OperatorList operatorList = null;
        JSONObject response = null;
        try {
            if (jsonArray.length() != 0) {
                operatorList = new OperatorList();
                for (int index = 0; index < jsonArray.length(); index++) {
                    response = jsonArray.getJSONObject(index);
                    if (response.has(ResponseAttributeConstants.AIRTIME)) {
                        operatorList.airtimeList = transformJSONArrayToAirtime(new JSONArray(response.getString(ResponseAttributeConstants.AIRTIME)));
                    } else if (response.has(ResponseAttributeConstants.ELECTRICITY_GAS)) {
                        operatorList.electricityGasList = transformJSONArrayToElectricityOrGas(new JSONArray(response.getString(ResponseAttributeConstants.ELECTRICITY_GAS)));
                    } else if (response.has(ResponseAttributeConstants.ICASHCARD)) {
                        operatorList.icashcardList = transformIJSONArrayToCashCard(new JSONArray(response.getString(ResponseAttributeConstants.ICASHCARD)));
                    } else if (response.has(ResponseAttributeConstants.MONEY_TRANSFER)) {
                        operatorList.moneyTransferList = transformJSONArrayToMoneyTransfer(new JSONArray(response.getString(ResponseAttributeConstants.MONEY_TRANSFER)));
                    }
                }
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONArrayToOperatorList.Message : " + e.getMessage());
        } catch (JsonSyntaxException e) {
            Timber.e("JsonSyntaxException while transformJSONArrayToOperatorList.Message : " + e.getMessage());
        }
        return operatorList;
    }

    private static Airtime transformJSONArrayToAirtime(JSONArray jsonArray) {

        Airtime airtime = null;
        JSONObject response = null;
        try {
            if (jsonArray.length() != 0) {
                airtime = new Airtime();

                for (int index = 0; index < jsonArray.length(); index++) {
                    response = jsonArray.getJSONObject(index);

                    if (response.has(ResponseAttributeConstants.PREPAID)) {
                        airtime.prepaid = transformJSONArrayToPrepaid(new JSONArray(response.getString(ResponseAttributeConstants.PREPAID)));
                    } else if (response.has(ResponseAttributeConstants.DTH)) {
                        airtime.dthList = transformJSONArrayToDthList(new JSONArray(response.getString(ResponseAttributeConstants.DTH)));
                    } else if (response.has(ResponseAttributeConstants.POSTPAID)) {
                        airtime.postpaidList = transformJSONArrayToPostpaid(new JSONArray(response.getString(ResponseAttributeConstants.POSTPAID)));
                    } else if (response.has(ResponseAttributeConstants.LANDLINE)) {
                        airtime.landlineList = transformJSONArrayToLandline(new JSONArray(response.getString(ResponseAttributeConstants.LANDLINE)));
                    }
                }
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONArrayToAirtime. Message : " + e.getMessage());
        } catch (JsonSyntaxException e) {
            Timber.e("JsonSyntaxException while transformJSONArrayToAirtime. Message : " + e.getMessage());
        }
        return airtime;
    }

    // Common for AIRTIME and ICashCard mapping
    private static Prepaid transformJSONArrayToPrepaid(JSONArray response) {
        Prepaid prepaid = null;
        try {
            if (response != null) {
                prepaid = new Prepaid();
                prepaid.operatorDetailsList = transformJSONArrayToOperatorDetailsList(response);
            }
        } catch (Exception e) {
            Timber.e("JSONException while transformJSONArrayToPrepaid. Message : " + e.getMessage());
        }
        return prepaid;
    }

    private static DthList transformJSONArrayToDthList(JSONArray response) {
        DthList dthList = null;
        try {
            if (response != null) {
                dthList = new DthList();
                dthList.operatorDetailsList = transformJSONArrayToOperatorDetailsList(response);
            }
        } catch (Exception e) {
            Timber.e("JSONException while transformJSONArrayToDthList. Message : " + e.getMessage());
        }
        return dthList;
    }

    private static Postpaid transformJSONArrayToPostpaid(JSONArray response) {
        Postpaid postpaid = null;
        try {
            if (response != null) {
                postpaid = new Postpaid();
                postpaid.operatorDetailsList = transformJSONArrayToOperatorDetailsList(response);
            }

        } catch (Exception e) {
            Timber.e("JSONException while transformJSONArrayToPostpaid. Message : " + e.getMessage());
        }
        return postpaid;
    }

    private static Landline transformJSONArrayToLandline(JSONArray response) {
        Landline landline = null;
        try {
            if (response != null) {
                landline = new Landline();
                landline.operatorDetailsList = transformJSONArrayToOperatorDetailsList(response);
            }
        } catch (Exception e) {
            Timber.e("JSONException while transformJSONArrayToLandline. Message : " + e.getMessage());
        }
        return landline;
    }

    private static ElectricityOrGas transformJSONArrayToElectricityOrGas(JSONArray jsonArray) {
        ElectricityOrGas electricityOrGas = null;
        JSONObject response = null;
        try {
            if (jsonArray.length() != 0) {
                electricityOrGas = new ElectricityOrGas();

                for (int index = 0; index < jsonArray.length(); index++) {
                    response = jsonArray.getJSONObject(index);
                    if (response.has(ResponseAttributeConstants.ELECTRICITY)) {
                        electricityOrGas.electricityList = transformJSONArrayToElectricity(new JSONArray(response.getString(ResponseAttributeConstants.ELECTRICITY)));
                    } else if (response.has(ResponseAttributeConstants.GAS)) {
                        electricityOrGas.gasList = transformJSONArrayToGas(new JSONArray(response.getString(ResponseAttributeConstants.GAS)));
                    }
                }
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONArrayToElectricityOrGas. Message : " + e.getMessage());
        } catch (JsonSyntaxException e) {
            Timber.e("JsonSyntaxException while transformJSONArrayToElectricityOrGas. Message : " + e.getMessage());
        }
        return electricityOrGas;
    }

    private static Electricity transformJSONArrayToElectricity(JSONArray response) {
        Electricity electricity = null;
        try {
            if (response != null) {
                electricity = new Electricity();
                electricity.operatorDetailsList = transformJSONArrayToOperatorDetailsList(response);
            }
        } catch (Exception e) {
            Timber.e("JSONException while transformJSONArrayToElectricity. Message : " + e.getMessage());
        }
        return electricity;
    }

    private static Gas transformJSONArrayToGas(JSONArray response) {
        Gas gas = null;
        try {
            if (response != null) {
                gas = new Gas();
                gas.operatorDetailsList = transformJSONArrayToOperatorDetailsList(response);
            }
        } catch (Exception e) {
            Timber.e("JSONException while transformJSONArrayToGas. Message : " + e.getMessage());
        }
        return gas;
    }


    private static ICashCard transformIJSONArrayToCashCard(JSONArray jsonArray) {

        ICashCard iCashCard = null;
        JSONObject response = null;
        try {
            if (jsonArray.length() != 0) {
                iCashCard = new ICashCard();

                for (int index = 0; index < jsonArray.length(); index++) {
                    response = jsonArray.getJSONObject(index);
                    if (response.has(ResponseAttributeConstants.GENERATE)) {
                        iCashCard.generateList = transformJSONArrayToGenerate(new JSONArray(response.getString(ResponseAttributeConstants.GENERATE)));
                    } else if (response.has(ResponseAttributeConstants.PREPAID)) {
                        iCashCard.prepaidList = transformJSONArrayToPrepaid(new JSONArray(response.getString(ResponseAttributeConstants.PREPAID)));
                    }
                }
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformIJSONArrayToCashCard. Message : " + e.getMessage());
        } catch (JsonSyntaxException e) {
            Timber.e("JsonSyntaxException while transformIJSONArrayToCashCard. Message : " + e.getMessage());
        }
        return iCashCard;
    }

    private static Generate transformJSONArrayToGenerate(JSONArray response) {
        Generate generate = null;
        try {
            if (response != null) {
                generate = new Generate();
                generate.operatorDetailsList = transformJSONArrayToOperatorDetailsList(response);
            }
        } catch (Exception e) {
            Timber.e("JSONException while transformJSONArrayToGas. Message : " + e.getMessage());
        }
        return generate;
    }


    private static MoneyTransfer transformJSONArrayToMoneyTransfer(JSONArray jsonArray) {
        MoneyTransfer moneyTransfer = null;
        JSONObject response = null;
        try {
            if (jsonArray.length() != 0) {
                moneyTransfer = new MoneyTransfer();
                for (int index = 0; index < jsonArray.length(); index++) {
                    response = jsonArray.getJSONObject(index);
                    if (response.has(ResponseAttributeConstants.TRANSFER)) {
                        moneyTransfer.transferList = transformJSONArrayToTransfer(new JSONArray(response.getString(ResponseAttributeConstants.TRANSFER)));
                    }
                }
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONArrayToMoneyTransfer. Message : " + e.getMessage());
        } catch (JsonSyntaxException e) {
            Timber.e("JsonSyntaxException while transformJSONArrayToMoneyTransfer. Message : " + e.getMessage());
        }
        return moneyTransfer;
    }


    private static Transfer transformJSONArrayToTransfer(JSONArray response) {
        Transfer transfer = null;
        try {
            if (response != null) {
                transfer = new Transfer();
                transfer.operatorDetailsList = transformJSONArrayToOperatorDetailsList(response);
            }
        } catch (Exception e) {
            Timber.e("Exception while transformJSONArrayToTransfer. Message : " + e.getMessage());
        }
        return transfer;
    }


    private static List<OperatorDetails> transformJSONArrayToOperatorDetailsList(JSONArray jsonArray) {
        List<OperatorDetails> operatorDetailsList = new ArrayList<>();
        try {
            OperatorDetails tmpOperatorDetails;

            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject json_data = jsonArray.getJSONObject(index);
                tmpOperatorDetails = transformJSONObjectToOperatorDetails(json_data);

                if (tmpOperatorDetails != null) {
                    operatorDetailsList.add(tmpOperatorDetails);
                }
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONArrayToOperatorDetailsList. Message : " + e.getMessage());

        }
        return operatorDetailsList;
    }

    private static OperatorDetails transformJSONObjectToOperatorDetails(JSONObject response) {
        OperatorDetails operatorDetails = null;
        try {
            if (response != null) {
                operatorDetails = new OperatorDetails();

                if (response.has(ResponseAttributeConstants.OPERATOR_NAME))
                    operatorDetails.operatorName = response.getString(ResponseAttributeConstants.OPERATOR_NAME);

                if (response.has(ResponseAttributeConstants.OPERATOR_CODE))
                    operatorDetails.operatorCode = response.getString(ResponseAttributeConstants.OPERATOR_CODE);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToOperatorDetails. Message : " + e.getMessage());
        }

        return operatorDetails;
    }

    /**
     * Client Logs API....
     */
    public static ClientLogResponse transformJSONObjectToClientLogResponse(JSONObject response) {
        ClientLogResponse clientLogResponse = null;
        try {
            if (response != null) {
                clientLogResponse = new ClientLogResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    clientLogResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                    clientLogResponse.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);

                if (response.has(ResponseAttributeConstants.CLIENT_ID))
                    clientLogResponse.clientId = response.getString(ResponseAttributeConstants.CLIENT_ID);

                if (response.has(ResponseAttributeConstants.LOG_LIST))
                    clientLogResponse.logList = transformJSONObjectToLogList(new JSONObject(response.getString(ResponseAttributeConstants.LOG_LIST)));

                if (response.has(ResponseAttributeConstants.FORMAT))
                    clientLogResponse.format = response.getString(ResponseAttributeConstants.FORMAT);

                if (response.has(ResponseAttributeConstants.MSG))
                    clientLogResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToClientLogResponse. Message : " + e.getMessage());
        }
        return clientLogResponse;
    }

    public static LogList transformJSONObjectToLogList(JSONObject response) {
        Log.e("ProfileModelMapper",response.toString());
        LogList logList = null;
        try {
            if (response != null) {
                logList = new LogList();
                if (response.has(ResponseAttributeConstants.MOBICASH_CLIENT_ID))
                    logList.mobicashClientId = response.getString(ResponseAttributeConstants.MOBICASH_CLIENT_ID);
                if (response.has(ResponseAttributeConstants.MOBICASH_CLIENT_LOG))
                    logList.mobicashClientLogItem = transformJSONArrayToClientLogs(new JSONArray(response.getString(ResponseAttributeConstants.MOBICASH_CLIENT_LOG)));

            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToLogList. Message : " + e.getMessage());
        }
        return logList;
    }

    private static List<LogItem>  transformJSONArrayToClientLogs(JSONArray jsonArray) {
        List<LogItem> logItems = new ArrayList<>();
        try {
            LogItem tmpLogItem;

            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject json_data = jsonArray.getJSONObject(index);
                tmpLogItem = transformToLogItem(json_data);

                if (tmpLogItem != null) {
                    logItems.add(tmpLogItem);
                }
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONArrayToClientLogs. Message : " + e.getMessage());

        }
        return logItems;
    }

    private static LogItem transformToLogItem(JSONObject response) {
        LogItem logItem = new LogItem();
        try {
            if (response != null) {
                logItem = new LogItem();
                if (response.has(ResponseAttributeConstants.LOG_ID))
                    logItem.logId = response.getString(ResponseAttributeConstants.LOG_ID);

                if (response.has(ResponseAttributeConstants.LOG_DATE))
                    logItem.logDate = response.getString(ResponseAttributeConstants.LOG_DATE);

                if (response.has(ResponseAttributeConstants.LOG_TYPE))
                    logItem.logType = response.getString(ResponseAttributeConstants.LOG_TYPE);

                if (response.has(ResponseAttributeConstants.LOG_DEBIT))
                    logItem.logDebit = response.getString(ResponseAttributeConstants.LOG_DEBIT);

                if (response.has(ResponseAttributeConstants.LOG_CREDIT))
                    logItem.logCredit = response.getString(ResponseAttributeConstants.LOG_CREDIT);

                if (response.has(ResponseAttributeConstants.LOG_BALANCE))
                    logItem.logBalance = response.getString(ResponseAttributeConstants.LOG_BALANCE);

                if (response.has(ResponseAttributeConstants.LOG_DESCRIPTION))
                    logItem.logDescription = response.getString(ResponseAttributeConstants.LOG_DESCRIPTION);

                if (response.has(ResponseAttributeConstants.LOG_FEES))
                    logItem.logFees = response.getString(ResponseAttributeConstants.LOG_FEES);

                if (response.has(ResponseAttributeConstants.LOG_ACCOUNT))
                    logItem.logAccount = response.getString(ResponseAttributeConstants.LOG_ACCOUNT);

            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformToLogItem. Message : " + e.getMessage());
        }
        return logItem;
    }


    /**
     * Client Profile Update API....
     */
    public static ClientProfileUpdateResponse transformJSONObjectToClientProfileUpdateResponse(JSONObject response) {
        ClientProfileUpdateResponse clientProfileUpdateResponse = null;
        try {
            if (response != null) {
                clientProfileUpdateResponse = new ClientProfileUpdateResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    clientProfileUpdateResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                    clientProfileUpdateResponse.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);

                if (response.has(ResponseAttributeConstants.CLIENT_ID))
                    clientProfileUpdateResponse.clientId = response.getString(ResponseAttributeConstants.CLIENT_ID);

                if (response.has(ResponseAttributeConstants.CLIENT_PROFILE))
                    clientProfileUpdateResponse.clientProfile = transformJSONObjectToClientProfile(new JSONObject(response.getString(ResponseAttributeConstants.CLIENT_PROFILE)));

                if (response.has(ResponseAttributeConstants.CLIENT_PROFILE_STATE))
                    clientProfileUpdateResponse.clientProfileState = response.getString(ResponseAttributeConstants.CLIENT_PROFILE_STATE);

                if (response.has(ResponseAttributeConstants.FORMAT))
                    clientProfileUpdateResponse.format = response.getString(ResponseAttributeConstants.FORMAT);

                if (response.has(ResponseAttributeConstants.MSG))
                    clientProfileUpdateResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }


        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToClientProfileUpdateResponse. Message : " + e.getMessage());
        }
        return clientProfileUpdateResponse;
    }

    public static ClientProfile transformJSONObjectToClientProfile(JSONObject response) {
        ClientProfile clientProfile = null;
        try {
            if (response != null) {
                clientProfile = new ClientProfile();
                if (response.has(ResponseAttributeConstants.MOBICASH_CLIENT_ID))
                    clientProfile.mobicashClientId = response.getString(ResponseAttributeConstants.MOBICASH_CLIENT_ID);
                if (response.has(ResponseAttributeConstants.MOBICASH_CLIENT_PROFILE))
                    clientProfile.mobicashClientProfile = transformToProfileItem(new JSONObject(response.getString(ResponseAttributeConstants.MOBICASH_CLIENT_PROFILE)));

            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToClientProfile. Message : " + e.getMessage());
        }
        return clientProfile;
    }

    private static Profile transformToProfileItem(JSONObject response) {
        Profile profile = new Profile();
        try {
            if (response != null) {
                profile = new Profile();
                if (response.has(ResponseAttributeConstants.CLIENT_FIRST_NAME))
                    profile.clientFirstName = response.getString(ResponseAttributeConstants.CLIENT_FIRST_NAME);

                if (response.has(ResponseAttributeConstants.CLIENT_LAST_NAME))
                    profile.clientLastName = response.getString(ResponseAttributeConstants.CLIENT_LAST_NAME);

                if (response.has(ResponseAttributeConstants.CLIENT_USER_NAME))
                    profile.clientUserName = response.getString(ResponseAttributeConstants.CLIENT_USER_NAME);

                if (response.has(ResponseAttributeConstants.CLIENT_ADDRESS))
                    profile.clientAddress = response.getString(ResponseAttributeConstants.CLIENT_ADDRESS);

                if (response.has(ResponseAttributeConstants.CLIENT_CITY))
                    profile.clientCity = response.getString(ResponseAttributeConstants.CLIENT_CITY);

                if (response.has(ResponseAttributeConstants.CLIENT_ZIP_CODE))
                    profile.clientZipcode = response.getString(ResponseAttributeConstants.CLIENT_ZIP_CODE);

                if (response.has(ResponseAttributeConstants.CLIENT_COUNTRY))
                    profile.clientCountry = response.getString(ResponseAttributeConstants.CLIENT_COUNTRY);

                if (response.has(ResponseAttributeConstants.CLIENT_LANGUAGE))
                    profile.clientLanguage = response.getString(ResponseAttributeConstants.CLIENT_LANGUAGE);

                if (response.has(ResponseAttributeConstants.CLIENT_EMAIL))
                    profile.clientEmail = response.getString(ResponseAttributeConstants.CLIENT_EMAIL);

                if (response.has(ResponseAttributeConstants.CLIENT_DOB))
                    profile.clientDob = response.getString(ResponseAttributeConstants.CLIENT_DOB);

                if (response.has(ResponseAttributeConstants.CLIENT_ADHAAR))
                    profile.clientAdhaar = response.getString(ResponseAttributeConstants.CLIENT_ADHAAR);

                if (response.has(ResponseAttributeConstants.CLIENT_PAN))
                    profile.clientPan = response.getString(ResponseAttributeConstants.CLIENT_PAN);

                if (response.has(ResponseAttributeConstants.PROFILE_PICTURE))
                    profile.profilePicture = response.getString(ResponseAttributeConstants.PROFILE_PICTURE);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformToProfileItem. Message : " + e.getMessage());
        }
        return profile;
    }

    /**
     * Get Client Profile  API...
     */
    public static GetClientProfileResponse transformJSONObjectToGetClientProfileResponse(JSONObject response) {
        GetClientProfileResponse getClientProfileResponse = null;
        try {
            if (response != null) {
                getClientProfileResponse = new GetClientProfileResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    getClientProfileResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                    getClientProfileResponse.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);

                if (response.has(ResponseAttributeConstants.CLIENT_ID))
                    getClientProfileResponse.clientId = response.getString(ResponseAttributeConstants.CLIENT_ID);

                if (response.has(ResponseAttributeConstants.CLIENT_PROFILE))
                    getClientProfileResponse.clientProfile = transformJSONObjectToGetClientProfile(new JSONObject(response.getString(ResponseAttributeConstants.CLIENT_PROFILE)));

                if (response.has(ResponseAttributeConstants.FORMAT))
                    getClientProfileResponse.format = response.getString(ResponseAttributeConstants.FORMAT);

                if (response.has(ResponseAttributeConstants.MSG))
                    getClientProfileResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }


        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToGetClientProfileResponse. Message : " + e.getMessage());
        }
        return getClientProfileResponse;
    }

    public static GetClientProfile transformJSONObjectToGetClientProfile(JSONObject response) {
        GetClientProfile getClientProfile = null;
        try {
            if (response != null) {
                getClientProfile = new GetClientProfile();
                if (response.has(ResponseAttributeConstants.MOBICASH_CLIENT_ID))
                    getClientProfile.mobicashClientId = response.getString(ResponseAttributeConstants.MOBICASH_CLIENT_ID);
                if (response.has(ResponseAttributeConstants.MOBICASH_CLIENT_PROFILE))
                    getClientProfile.mobicashClientProfile = transformToGetProfileItem(new JSONObject(response.getString(ResponseAttributeConstants.MOBICASH_CLIENT_PROFILE)));

            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToGetClientProfile. Message : " + e.getMessage());
        }
        return getClientProfile;
    }

    private static GetProfile transformToGetProfileItem(JSONObject response) {
        GetProfile getProfile = null;
        try {
            if (response != null) {
                getProfile = new GetProfile();
                if (response.has(ResponseAttributeConstants.CLIENT_FIRST_NAME))
                    getProfile.clientFirstName = response.getString(ResponseAttributeConstants.CLIENT_FIRST_NAME);

                if (response.has(ResponseAttributeConstants.CLIENT_LAST_NAME))
                    getProfile.clientLastName = response.getString(ResponseAttributeConstants.CLIENT_LAST_NAME);

                if (response.has(ResponseAttributeConstants.CLIENT_USER_NAME))
                    getProfile.clientUserName = response.getString(ResponseAttributeConstants.CLIENT_USER_NAME);

                if (response.has(ResponseAttributeConstants.CLIENT_ADDRESS))
                    getProfile.clientAddress = response.getString(ResponseAttributeConstants.CLIENT_ADDRESS);

                if (response.has(ResponseAttributeConstants.CLIENT_CITY))
                    getProfile.clientCity = response.getString(ResponseAttributeConstants.CLIENT_CITY);

                if (response.has(ResponseAttributeConstants.CLIENT_ZIP_CODE))
                    getProfile.clientZipcode = response.getString(ResponseAttributeConstants.CLIENT_ZIP_CODE);

                if (response.has(ResponseAttributeConstants.CLIENT_COUNTRY))
                    getProfile.clientCountry = response.getString(ResponseAttributeConstants.CLIENT_COUNTRY);

                if (response.has(ResponseAttributeConstants.CLIENT_LANGUAGE))
                    getProfile.clientLanguage = response.getString(ResponseAttributeConstants.CLIENT_LANGUAGE);

                if (response.has(ResponseAttributeConstants.CLIENT_EMAIL))
                    getProfile.clientEmail = response.getString(ResponseAttributeConstants.CLIENT_EMAIL);

                if (response.has(ResponseAttributeConstants.CLIENT_ADHAAR))
                    getProfile.clientAdhaar = response.getString(ResponseAttributeConstants.CLIENT_ADHAAR);

                if (response.has(ResponseAttributeConstants.CLIENT_PAN))
                    getProfile.clientPan = response.getString(ResponseAttributeConstants.CLIENT_PAN);

                if (response.has(ResponseAttributeConstants.CLIENT_DOB))
                    getProfile.clientDob = response.getString(ResponseAttributeConstants.CLIENT_DOB);
                if (response.has(ResponseAttributeConstants.PROFILE_PICTURE))
                    getProfile.profilePicture = response.getString(ResponseAttributeConstants.PROFILE_PICTURE);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformToGetProfileItem. Message : " + e.getMessage());
        }
        return getProfile;
    }


    /**
     * Get Client Profile Info API
     */
    public static GetClientProfileInfoResponse transformJSONObjectTomGetClientProfileInfoResponse(JSONObject response) {
        GetClientProfileInfoResponse getClientProfileInfoResponse = null;
        try {
            if (response != null) {
                getClientProfileInfoResponse = new GetClientProfileInfoResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    getClientProfileInfoResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                    getClientProfileInfoResponse.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);

                if (response.has(ResponseAttributeConstants.CLIENT_ID))
                    getClientProfileInfoResponse.clientId = response.getString(ResponseAttributeConstants.CLIENT_ID);

                if (response.has(ResponseAttributeConstants.CLIENT_PROFILE_INFO))
                    getClientProfileInfoResponse.clientProfileInfo = transformJSONObjectToGetClientProfileInfo(new JSONObject(response.getString(ResponseAttributeConstants.CLIENT_PROFILE_INFO)));

                if (response.has(ResponseAttributeConstants.FORMAT))
                    getClientProfileInfoResponse.format = response.getString(ResponseAttributeConstants.FORMAT);

                if (response.has(ResponseAttributeConstants.MSG))
                    getClientProfileInfoResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }


        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectTomGetClientProfileInfoResponse. Message : " + e.getMessage());
        }
        return getClientProfileInfoResponse;
    }

    public static GetClientProfileInfo transformJSONObjectToGetClientProfileInfo(JSONObject response) {
        GetClientProfileInfo getClientProfileInfo = null;
        try {
            if (response != null) {
                getClientProfileInfo = new GetClientProfileInfo();
                if (response.has(ResponseAttributeConstants.MOBICASH_CLIENT_ID))
                    getClientProfileInfo.mobicashClientId = response.getString(ResponseAttributeConstants.MOBICASH_CLIENT_ID);
                if (response.has(ResponseAttributeConstants.MOBICASH_CLIENT_PROFILE))
                    getClientProfileInfo.mobicashClientProfile = transformToGetProfileInfo(new JSONObject(response.getString(ResponseAttributeConstants.MOBICASH_CLIENT_PROFILE)));

            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToGetClientProfileInfo. Message : " + e.getMessage());
        }
        return getClientProfileInfo;
    }

    private static GetProfileInfo transformToGetProfileInfo(JSONObject response) {
        GetProfileInfo getProfileInfo = null;
        try {
            if (response != null) {
                getProfileInfo = new GetProfileInfo();
                if (response.has(ResponseAttributeConstants.CLIENT_WALLET_BALANCE))
                    getProfileInfo.clientWalletBalance = response.getString(ResponseAttributeConstants.CLIENT_WALLET_BALANCE);

                if (response.has(ResponseAttributeConstants.CLIENT_PROFILE_IMAGE))
                    getProfileInfo.clientProfileImage = response.getString(ResponseAttributeConstants.CLIENT_PROFILE_IMAGE);

                if (response.has(ResponseAttributeConstants.CLIENT_FIRST_NAME))
                    getProfileInfo.client_firstname = response.getString(ResponseAttributeConstants.CLIENT_FIRST_NAME);

                if (response.has(ResponseAttributeConstants.CLIENT_LAST_NAME))
                    getProfileInfo.client_lastname = response.getString(ResponseAttributeConstants.CLIENT_LAST_NAME);

                if (response.has(ResponseAttributeConstants.CLIENT_EMAIL_ID))
                    getProfileInfo.client_email = response.getString(ResponseAttributeConstants.CLIENT_EMAIL_ID);

                if (response.has(ResponseAttributeConstants.CLIENT_RESPONSE_ADDRESS))
                    getProfileInfo.custAddess = response.getString(ResponseAttributeConstants.CLIENT_RESPONSE_ADDRESS);

                if (response.has(ResponseAttributeConstants.CLIENT_RESPONSE_DOB))
                    getProfileInfo.customerDOB = response.getString(ResponseAttributeConstants.CLIENT_RESPONSE_DOB);

                if (response.has(ResponseAttributeConstants.CLIENT_TITLE))
                    getProfileInfo.customerTitle = response.getString(ResponseAttributeConstants.CLIENT_TITLE);

                if (response.has(ResponseAttributeConstants.CLIENT_IDENTITY_ID))
                    getProfileInfo.indentityId = response.getString(ResponseAttributeConstants.CLIENT_IDENTITY_ID);

                if (response.has(ResponseAttributeConstants.CLIENT_APPROVED_CREDIT_LIMIT))
                    getProfileInfo.clientApprovedCreditLimit = response.getString(ResponseAttributeConstants.CLIENT_APPROVED_CREDIT_LIMIT);

            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformToGetProfileInfo. Message : " + e.getMessage());
        }
        return getProfileInfo;
    }
    /**
     * Client Bank Transfer API....
     */
    public static ClientBankTransferResponse transformJSONObjectToClientBankTransferResponse(JSONObject response) {
        ClientBankTransferResponse clientBankTransferResponse = null;
        try {
            if (response != null) {
                clientBankTransferResponse = new ClientBankTransferResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    clientBankTransferResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                    clientBankTransferResponse.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);

                if (response.has(ResponseAttributeConstants.CLIENT_ID))
                    clientBankTransferResponse.clientId = response.getString(ResponseAttributeConstants.CLIENT_ID);

                if (response.has(ResponseAttributeConstants.BANK_TRANSFER_STATUS))
                    clientBankTransferResponse.bankTransferStatus = transformJSONObjectToBankTransferStatus(new JSONObject(response.getString(ResponseAttributeConstants.BANK_TRANSFER_STATUS)));

                if (response.has(ResponseAttributeConstants.FORMAT))
                    clientBankTransferResponse.format = response.getString(ResponseAttributeConstants.FORMAT);

                if (response.has(ResponseAttributeConstants.MSG))
                    clientBankTransferResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToClientBankTransferResponse. Message : " + e.getMessage());
        }
        return clientBankTransferResponse;
    }


    public static BankTransferStatus transformJSONObjectToBankTransferStatus(JSONObject response) {
        BankTransferStatus bankTransferStatus = null;
        try {
            if (response != null) {
                bankTransferStatus = new BankTransferStatus();
                if (response.has(ResponseAttributeConstants.MOBICASH_CLIENT_ID))
                    bankTransferStatus.mobicashClientId = response.getString(ResponseAttributeConstants.MOBICASH_CLIENT_ID);
                if (response.has(ResponseAttributeConstants.MOBICASH_CLIENT_BANK_TRANSFER_RESPONSE))
                    bankTransferStatus.mobicashClientBankTransferResponseList = transformJSONArrayToClientBankTransferResponseList(new JSONArray(response.getString(ResponseAttributeConstants.MOBICASH_CLIENT_BANK_TRANSFER_RESPONSE)));

            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToBankTransferStatus. Message : " + e.getMessage());
        }
        return bankTransferStatus;
    }


    public static List<BankTransferResponse> transformJSONArrayToClientBankTransferResponseList(JSONArray response) {
        List<BankTransferResponse> bankTransferResponseList = new ArrayList<>();
        try {
            BankTransferResponse tmpBankTransferResponseItem;

            for (int index = 0; index < response.length(); index++) {
                JSONObject json_data = response.getJSONObject(index);
                tmpBankTransferResponseItem = transformJSONArrayToClientBankTransferResponse(json_data);

                if (tmpBankTransferResponseItem != null) {
                    bankTransferResponseList.add(tmpBankTransferResponseItem);
                }
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONArrayToClientBankTransferResponseList. Message : " + e.getMessage());

        }
        return bankTransferResponseList;
    }

    private static BankTransferResponse transformJSONArrayToClientBankTransferResponse(JSONObject response) {
        BankTransferResponse bankTransferResponse = null;
        try {
            if (response != null) {
                bankTransferResponse = new BankTransferResponse();
                if (response.has(ResponseAttributeConstants.BANK_TRANSFER_TRANSACTION_DATE))
                    bankTransferResponse.bankTransferTransactionDate = response.getString(ResponseAttributeConstants.BANK_TRANSFER_TRANSACTION_DATE);
                if (response.has(ResponseAttributeConstants.BANK_TRANSFER_TRANSACTION_TYPE))
                    bankTransferResponse.bankTransferType = response.getString(ResponseAttributeConstants.BANK_TRANSFER_TRANSACTION_TYPE);
                if (response.has(ResponseAttributeConstants.BANK_TRANSFER_ACCOUNT_NO))
                    bankTransferResponse.bankTransferAccountNo = response.getString(ResponseAttributeConstants.BANK_TRANSFER_ACCOUNT_NO);
                if (response.has(ResponseAttributeConstants.BANK_TRANSFER_IFSC))
                    bankTransferResponse.bankTransferIFSC = response.getString(ResponseAttributeConstants.BANK_TRANSFER_IFSC);
                if (response.has(ResponseAttributeConstants.BANK_TRANSFER_BANK_NAME))
                    bankTransferResponse.bankTransferBankName = response.getString(ResponseAttributeConstants.BANK_TRANSFER_BANK_NAME);
                if (response.has(ResponseAttributeConstants.BANK_TRANSFER_BRANCH_NAME))
                    bankTransferResponse.bankTransferBranchName = response.getString(ResponseAttributeConstants.BANK_TRANSFER_BRANCH_NAME);
                if (response.has(ResponseAttributeConstants.BANK_TRANSFER_AMOUNT))
                    bankTransferResponse.bankTransferAmount = response.getString(ResponseAttributeConstants.BANK_TRANSFER_AMOUNT);
                if (response.has(ResponseAttributeConstants.BANK_TRANSFER_FEES))
                    bankTransferResponse.bankTransferFees = response.getString(ResponseAttributeConstants.BANK_TRANSFER_FEES);
                if (response.has(ResponseAttributeConstants.BANK_TRANSFER_FIRST_NAME))
                    bankTransferResponse.bankTransferFirstName = response.getString(ResponseAttributeConstants.BANK_TRANSFER_FIRST_NAME);
                if (response.has(ResponseAttributeConstants.BANK_TRANSFER_LAST_NAME))
                    bankTransferResponse.bankTransferLastName = response.getString(ResponseAttributeConstants.BANK_TRANSFER_LAST_NAME);
                if (response.has(ResponseAttributeConstants.BANK_TRANSFER_BENEFICIARY_MOBILE))
                    bankTransferResponse.bankTransferBeneficiaryMobile = response.getString(ResponseAttributeConstants.BANK_TRANSFER_BENEFICIARY_MOBILE);
                if (response.has(ResponseAttributeConstants.BANK_TRANSFER_STATUS_RESPONSE))
                    bankTransferResponse.bankTransferStatus = response.getString(ResponseAttributeConstants.BANK_TRANSFER_STATUS_RESPONSE);
                if (response.has(ResponseAttributeConstants.BANK_TRANSFER_REFERENCE_ID))
                    bankTransferResponse.bankTransferReferenceId = response.getString(ResponseAttributeConstants.BANK_TRANSFER_REFERENCE_ID);
                if (response.has(ResponseAttributeConstants.BANK_TRANSFER_TRANSACTION_ID))
                    bankTransferResponse.bankTransferTransactionId = response.getString(ResponseAttributeConstants.BANK_TRANSFER_TRANSACTION_ID);
                if (response.has(ResponseAttributeConstants.BANK_TRANSFER_DESCRIPTION))
                    bankTransferResponse.bankTransferDescription = response.getString(ResponseAttributeConstants.BANK_TRANSFER_DESCRIPTION);

            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONArrayToClientBankTransferResponse. Message : " + e.getMessage());
        }
        return bankTransferResponse;
    }

    /**
     * Client Send Passcode API....
     */
    public static ClientSendPassCodeResponse transformJSONObjectToClientSendPasscodeResponse(JSONObject response) {
        ClientSendPassCodeResponse clientSendPassCodeResponse = null;
        try {
            if (response != null) {
                clientSendPassCodeResponse = new ClientSendPassCodeResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    clientSendPassCodeResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                    clientSendPassCodeResponse.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);

                if (response.has(ResponseAttributeConstants.CLIENT_MOBILE))
                    clientSendPassCodeResponse.clientMobile = response.getString(ResponseAttributeConstants.CLIENT_MOBILE);

                if (response.has(ResponseAttributeConstants.SEND_STATUS))
                    clientSendPassCodeResponse.sendStatus = response.getString(ResponseAttributeConstants.SEND_STATUS);

                if (response.has(ResponseAttributeConstants.MSG))
                    clientSendPassCodeResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToClientSendPasscodeResponse. Message : " + e.getMessage());
        }
        return clientSendPassCodeResponse;
    }

    /**
     * Client Add beneficiary API....
     */
    public static BeneficiaryResponse transformJSONObjectToBeneficiaryResponse(JSONObject response) {
        BeneficiaryResponse beneficiaryResponse = null;
        try {
            if (response != null) {
                beneficiaryResponse = new BeneficiaryResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    beneficiaryResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.RESPONSE_BENEFICIARY_ID))
                    beneficiaryResponse.ben_id = response.getString(ResponseAttributeConstants.RESPONSE_BENEFICIARY_ID);

                if (response.has(ResponseAttributeConstants.MSG))
                    beneficiaryResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToAddBeneficiaryResponse. Message : " + e.getMessage());
        }
        return beneficiaryResponse;
    }

    /**
     * Client Reset Passcode API....
     */
    public static ClientResetPassCodeResponse transformJSONObjectToClientResetPasscodeResponse(JSONObject response) {
        ClientResetPassCodeResponse clientResetPassCodeResponse = null;
        try {
            if (response != null) {
                clientResetPassCodeResponse = new ClientResetPassCodeResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    clientResetPassCodeResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                    clientResetPassCodeResponse.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);

                if (response.has(ResponseAttributeConstants.CLIENT_ID))
                    clientResetPassCodeResponse.clientId = response.getString(ResponseAttributeConstants.CLIENT_ID);

                if (response.has(ResponseAttributeConstants.RESET_STATUS))
                    clientResetPassCodeResponse.resetStatus = response.getString(ResponseAttributeConstants.RESET_STATUS);

                if (response.has(ResponseAttributeConstants.MSG))
                    clientResetPassCodeResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToClientResetPasscodeResponse. Message : " + e.getMessage());
        }
        return clientResetPassCodeResponse;
    }
    public static OTP_Response transformJSONObjectToUserOTpResponse(JSONObject response) {
        OTP_Response mOTP_Response = null;
        try {
            if (response != null) {
                mOTP_Response = new OTP_Response();
                if (response.has(ResponseAttributeConstants.STATUS))
                    mOTP_Response.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                    mOTP_Response.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);

                if (response.has(ResponseAttributeConstants.CODE))
                    mOTP_Response.code = response.getString(ResponseAttributeConstants.CODE);

                if (response.has(ResponseAttributeConstants.TIMEFROM))
                    mOTP_Response.timefrom = response.getString(ResponseAttributeConstants.TIMEFROM);

                if (response.has(ResponseAttributeConstants.MOBILE_NUMBER))
                    mOTP_Response.mobile = response.getString(ResponseAttributeConstants.MOBILE_NUMBER);

                if (response.has(ResponseAttributeConstants.GENERATEOTPSENDSTATUS))
                    mOTP_Response.generateOTPSendStatus = response.getString(ResponseAttributeConstants.GENERATEOTPSENDSTATUS);

                if (response.has(ResponseAttributeConstants.USER_ID))
                    mOTP_Response.userId = response.getString(ResponseAttributeConstants.USER_ID);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToUploadClientProfilePictureResponse. Message : " + e.getMessage());
        }
        return mOTP_Response;
    }
    /**
     * Upload Client profile Picture API....
     */
    public static UploadClientProfilePictureResponse transformJSONObjectToUploadClientProfilePictureResponse(JSONObject response) {
        UploadClientProfilePictureResponse uploadClientProfilePictureResponse = null;
        try {
            if (response != null) {
                uploadClientProfilePictureResponse = new UploadClientProfilePictureResponse();
                if (response.has(ResponseAttributeConstants.STATUS))
                    uploadClientProfilePictureResponse.status = response.getString(ResponseAttributeConstants.STATUS);

                if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                    uploadClientProfilePictureResponse.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);

                if (response.has(ResponseAttributeConstants.CLIENT_ID))
                    uploadClientProfilePictureResponse.clientId = response.getString(ResponseAttributeConstants.CLIENT_ID);

                if (response.has(ResponseAttributeConstants.CLIENT_PROFILE_UPLOAD_STATE))
                    uploadClientProfilePictureResponse.clientProfileUploadState = response.getString(ResponseAttributeConstants.CLIENT_PROFILE_UPLOAD_STATE);

                if (response.has(ResponseAttributeConstants.MSG))
                    uploadClientProfilePictureResponse.msg = response.getString(ResponseAttributeConstants.MSG);
            }
        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToUploadClientProfilePictureResponse. Message : " + e.getMessage());
        }
        return uploadClientProfilePictureResponse;
    }
    public static VerifyMobileNumberResponse transformJSONObjectToVerifyMobileNumberResponse(JSONObject response) {
        VerifyMobileNumberResponse mVerifyMobileNumberResponse = null;
        try {
            if (response != null) {
                mVerifyMobileNumberResponse = new VerifyMobileNumberResponse();
                if (response.has(ResponseAttributeConstants.RET_CODE))
                    mVerifyMobileNumberResponse.ret_code = response.getString(ResponseAttributeConstants.RET_CODE);

                if (response.has(ResponseAttributeConstants.RET_MESSAGE))
                    mVerifyMobileNumberResponse.ret_message = response.getString(ResponseAttributeConstants.RET_MESSAGE);
            }

        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToVerifyMobileNumberResponse. Message : " + e.getMessage());
        }
        return mVerifyMobileNumberResponse;
    }

    /**
     * Client PayU Payment Gateway API....
     */
    public static PayUPaymentGatewayResponse transformJSONObjectToPayUPaymentGatewayResponse(JSONObject response) {
        PayUPaymentGatewayResponse payUPaymentGatewayResponse = null;
        try {
            if (response != null) {
                payUPaymentGatewayResponse = new PayUPaymentGatewayResponse();
                if (response.has(ResponseAttributeConstants.RESPONSE_STATUS))
                    payUPaymentGatewayResponse.responseStatus = response.getString(ResponseAttributeConstants.RESPONSE_STATUS);

                if (response.has(ResponseAttributeConstants.RESPONSE_CODE))
                    payUPaymentGatewayResponse.responseCode = response.getString(ResponseAttributeConstants.RESPONSE_CODE);

                if (response.has(ResponseAttributeConstants.RESPONSE_MSG))
                    payUPaymentGatewayResponse.ResponseMsg = response.getString(ResponseAttributeConstants.RESPONSE_MSG);

                if (response.has(ResponseAttributeConstants.REF_ID))
                    payUPaymentGatewayResponse.RefId = response.getString(ResponseAttributeConstants.REF_ID);

                if (response.has(ResponseAttributeConstants.CLI_IDT_ID))
                    payUPaymentGatewayResponse.cliIdtId = response.getString(ResponseAttributeConstants.CLI_IDT_ID);

                if (response.has(ResponseAttributeConstants.MER_IDT_ID))
                    payUPaymentGatewayResponse.merIdtId = response.getString(ResponseAttributeConstants.MER_IDT_ID);
            }

        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToPayUPaymentGatewayResponse. Message : " + e.getMessage());
        }
        return payUPaymentGatewayResponse;
    }
    /**
     * Client Plan List Details....
     */
    public static ArrayList<PlanDetails> getPlanList(JSONObject response) {
        ArrayList<PlanDetails> planListResponseModelsArray = new ArrayList<>();
        PlanDetails planDetails = null;
        try {
            if (response != null) {
                if (response.has(ResponseAttributeConstants.PLANS))
                {
                    JSONArray bankListArray = response.getJSONArray(ResponseAttributeConstants.PLANS);
                    int sizeOfBankListArray = bankListArray.length();
                    for (int i = 0; i < sizeOfBankListArray; i++) {
                        planDetails = new PlanDetails();
                        JSONObject planData = bankListArray.getJSONObject(i);
                        if(planData.has(ResponseAttributeConstants.PLAN_ID)){
                            planDetails.setPlanId(planData.getString(ResponseAttributeConstants.PLAN_ID));
                        }
                        if(planData.has(ResponseAttributeConstants.PLAN_COST)){
                            planDetails.setPlanCost(planData.getString(ResponseAttributeConstants.PLAN_COST));
                        }
                        if(planData.has(ResponseAttributeConstants.PLAN_TIME_BANK)){
                            planDetails.setPlanTimeBank(planData.getString(ResponseAttributeConstants.PLAN_TIME_BANK));
                        }
                        if(planData.has(ResponseAttributeConstants.PLAN_TRAFFIC_TOTAL)){
                            planDetails.setPlanTrafficTotal(planData.getString(ResponseAttributeConstants.PLAN_TRAFFIC_TOTAL));
                        }
                        if(planData.has(ResponseAttributeConstants.PLAN_NAME)){
                            planDetails.setPlanName(planData.getString(ResponseAttributeConstants.PLAN_NAME));
                        }
                        planListResponseModelsArray.add(planDetails);
                    }

                }
            }

        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToPayUPaymentGatewayResponse. Message : " + e.getMessage());
        }
        return planListResponseModelsArray;
    }
    /**
     * Client Offers List Details....
     */
    public static ArrayList<OffersItem> getOfferList(JSONObject response) {
        ArrayList<OffersItem> offerListResponseModelsArray = new ArrayList<>();
        OffersItem offerDetails = null;
        try {
            if (response != null)
            {
                if (response.has(ResponseAttributeConstants.OFFERS_DATA))
                {
                    JSONArray offersListArray = response.getJSONArray(ResponseAttributeConstants.OFFERS_DATA);
                    int sizeOfOfferListArray = offersListArray.length();
                    for (int i = 0; i <sizeOfOfferListArray; i++) {
                        offerDetails = new OffersItem();
                        JSONObject offerData = offersListArray.getJSONObject(i);

                        if (offerData.has(ResponseAttributeConstants.OFFERS_ID)) {
                            offerDetails.setOffer_id(offerData.getString(ResponseAttributeConstants.OFFERS_ID));
                        }
                        if (offerData.has(ResponseAttributeConstants.OFFERS_NAME)) {
                            offerDetails.setOffer_name(offerData.getString(ResponseAttributeConstants.OFFERS_NAME));
                        }
                        if (offerData.has(ResponseAttributeConstants.OFFERS_DISCOUNT)) {
                            offerDetails.setOffer_discount(offerData.getString(ResponseAttributeConstants.OFFERS_DISCOUNT));
                        }
                        if (offerData.has(ResponseAttributeConstants.OFFERS_TYPE)) {
                            offerDetails.setOffer_type(offerData.getString(ResponseAttributeConstants.OFFERS_TYPE));
                        }
                        if (offerData.has(ResponseAttributeConstants.OFFERS_VALIDITY)) {
                            offerDetails.setValid_upto(offerData.getString(ResponseAttributeConstants.OFFERS_VALIDITY));
                        }
                        if (offerData.has(ResponseAttributeConstants.OFFERS_MERCHANT_ID)) {
                            offerDetails.setMerchant_id(offerData.getString(ResponseAttributeConstants.OFFERS_MERCHANT_ID));
                        }
                        offerListResponseModelsArray.add(offerDetails);
                    }
                }

            }

        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToPayUPaymentGatewayResponse. Message : " + e.getMessage());
        }
        return offerListResponseModelsArray;
    }
    /**
     * Client Bank List Details....
     */
    public static ArrayList<BankListResponseModel> getBankList(JSONObject response) {
        ArrayList<BankListResponseModel> bankListResponseModelsArray = new ArrayList<>();
        BankListResponseModel bankListResponseModel = null;
        try {
            if (response != null) {
                if (response.has(ResponseAttributeConstants.BANK_LIST)) {
                    JSONArray bankListArray = response.getJSONArray(ResponseAttributeConstants.BANK_LIST);
                    int sizeOfBankListArray = bankListArray.length();
                    for (int i = 0; i < sizeOfBankListArray; i++) {
                        bankListResponseModel = new BankListResponseModel();
                        JSONObject bankData = bankListArray.getJSONObject(i);
                        if(bankData.has(ResponseAttributeConstants.BANK_CODE)){
                            bankListResponseModel.setBankCode(bankData.getString(ResponseAttributeConstants.BANK_CODE));
                        }
                        if(bankData.has(ResponseAttributeConstants.BANK_NAME)){
                            bankListResponseModel.setBankName(bankData.getString(ResponseAttributeConstants.BANK_NAME));
                        }
                        bankListResponseModelsArray.add(bankListResponseModel);
                    }

                }
            }

        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToPayUPaymentGatewayResponse. Message : " + e.getMessage());
        }
        return bankListResponseModelsArray;
    }

    /**
     * Client STATE List Details....
     */
    public static ArrayList<CityResponse> getStateList(JSONObject response) {
        ArrayList<CityResponse> bankListResponseModelsArray = new ArrayList<>();
        CityResponse cityListResponseModel = null;
        try {
            if (response != null) {
                if (response.getInt(ResponseAttributeConstants.STATUS)!= 0)
                {
                    JSONArray cityListArray = response.getJSONArray(ResponseAttributeConstants.STATE_LIST_VALUE);

                    int sizeOfCityListArray = cityListArray.length();
                    for (int i = 0; i < sizeOfCityListArray; i++) {
                        cityListResponseModel = new CityResponse();

                        JSONObject cityData = cityListArray.getJSONObject(i);
                        if(cityData.has(ResponseAttributeConstants.CITY_ID)){
                            cityListResponseModel.setId(cityData.getString(ResponseAttributeConstants.CITY_ID));
                        }
                        if(cityData.has(ResponseAttributeConstants.CITY_NAME)){
                            cityListResponseModel.setName(cityData.getString(ResponseAttributeConstants.CITY_NAME));
                        }
                        bankListResponseModelsArray.add(cityListResponseModel);
                    }

                }
            }

        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToPayUPaymentGatewayResponse. Message : " + e.getMessage());
        }
        return bankListResponseModelsArray;
    }

    /**
     * Client City List Details....
     */
    public static ArrayList<CityResponse> getCityList(JSONObject response) {
        ArrayList<CityResponse> bankListResponseModelsArray = new ArrayList<>();
        CityResponse cityListResponseModel = null;
        try {
            if (response != null) {
                if (response.has(ResponseAttributeConstants.CITY_LIST))
                {
                    JSONObject jsonObjectCity=response.getJSONObject(ResponseAttributeConstants.CITY_LIST);

                    JSONObject jsonObjectCityList=jsonObjectCity.getJSONObject(ResponseAttributeConstants.CITY_LIST_OBJECT);

                    JSONArray cityListArray = jsonObjectCityList.getJSONArray(ResponseAttributeConstants.CITY_LIST_VALUE);

                    int sizeOfCityListArray = cityListArray.length();
                    for (int i = 0; i < sizeOfCityListArray; i++) {
                        cityListResponseModel = new CityResponse();

                        JSONObject cityData = cityListArray.getJSONObject(i);
                        if(cityData.has(ResponseAttributeConstants.CITY_ID)){
                            cityListResponseModel.setId(cityData.getString(ResponseAttributeConstants.CITY_ID));
                        }
                        if(cityData.has(ResponseAttributeConstants.CITY_NAME)){
                            cityListResponseModel.setName(cityData.getString(ResponseAttributeConstants.CITY_NAME));
                        }
                        bankListResponseModelsArray.add(cityListResponseModel);
                    }

                }
            }

        } catch (JSONException e) {
            Timber.e("JSONException while transformJSONObjectToPayUPaymentGatewayResponse. Message : " + e.getMessage());
        }
        return bankListResponseModelsArray;
    }

    public static FailureResponse transformErrorResponseToFailureResponse(String errorbody) {
        FailureResponse failureResponse = null;
        try {
            if (errorbody != null) {

                Gson gson = new Gson();
                failureResponse = new FailureResponse();
                failureResponse = gson.fromJson(errorbody, FailureResponse.class); // deserializes json into target2
            }
        } catch (Exception e) {
            Timber.e("Exception while transformErrorResponseToFailureResponse. Message : " + e.getMessage());
        }
        return failureResponse;
    }


}
