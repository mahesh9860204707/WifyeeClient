package com.wifyee.greenfields.dairyorder;

import java.util.UUID;

public class DairyNetworkConstant {

    public static final String DAIRY_MERCHANT_MAIN_LIST_TYPE = "12";
    public static final String BASE_URL_DAIRY = "http://45.249.108.75";



    /**
     * dairy service broadcast status list constant
     */
    public static final String DAIRY_MERCHANT_LIST_STATUS_SUCCESS = "merchant_list_status_success";
    public static final String DAIRY_MERCHANT_LIST_STATUS_FAILURE = "merchant_list_status_failure";
    public static final String REQUEST_PARAM_MAIN_LIST = "/api/merchantapi.php?request=getmerchantlist";

    /**
     * List dairy item broadcast status
     */

    public static final String DAIRY_LIST_ITEM_STATUS_SUCCESS = "merchant_list_item_status_success";
    public static final String DAIRY_LIST_ITEM_STATUS_FAILURE = "merchant_list_item_status_failure";
    public static final String REQUEST_PARAM_LIST_ITEM = "/api/dairyusersellingitemapi.php?request=dairylistSellingItems";
    //public static final String REQUEST_LIST_ITEM = "/api/usersellingitemapi.php?request=listSellingItems&userId=";
    public static final String REQUEST_LIST_ITEM = "/api/usersellingitemapi.php?request=listSellingItems";

    //public static final String DAIRY_ADD_ORDER_URL = "http://45.249.108.75/api/dairy_order.php?request=addorder";
    public static final String ADD_ORDER_URL = BASE_URL_DAIRY+"/api/wifyee_order.php?request=addorder";
    public static final String DAIRY_ADD_ORDER_STATUS_SUCCESS = "merchant_add_order_status_success";
    public static final String DAIRY_ADD_ORDER_STATUS_FAILURE = "merchant_add_order_status_failure";

    public static final String PAYMENT_MODE_COD = "cod";
    public static final String PAYMENT_MODE_WALLET = "wallet";
    public static final String PAYMENT_MODE_INSTAMOJO = "instamojo";
    public static final String PAYMENT_MODE_ONLINE = "online";
    public static final String PAYMENT_MODE_VOUCHER= "voucher";


    public static final String CHECK_ITEM_QUANTITY = "/api/usersellingitemapi.php?request=checkmultipleitemqty";
    public static final String CHECK_ITEM_INDIDUALY= "/api/usersellingitemapi.php?request=checkitemqty&itemId=";


    /**
     * get client wallet balance
     */
    public static final String WALLET_BALANCE_URL = "http://wifyeepay.com/api/clientcheckonlybalanceapi.php?request=clientBalanceCheck";
    public static final String WALLET_PARAM = "clientmobile";
    public static final String DAIRY_WALLET_BALANCE_STATUS_SUCCESS = "merchant_wallet_balance_success";
    public static final String DAIRY_WALLET_BALANCE_STATUS_FAILURE = "merchant_wallet_balance_failure";

    /**
     * payment response URL
     */
    public static final String PAYMENT_RESPONSE_URL = " http://wifyeepay.com/payments/app_response.php";
    public static final String MEDICINE_PAYMENT_RESPONSE_URL = "http://wifyeepay.com/payments/common/instamojoAppResponse.php";
    public static final String WEBHOOK = "http://wifyeepay.com/payments/instamojo_webhooks/online_transaction_wk.php";

    /**
     * get merchant list
     */

    public static final String MAIN_CATEGORY_LIST_ITEM_SUCCESS ="main_category_list_item_success";
    public static final String MAIN_CATEGORY_LIST_ITEM_FAIL ="main_category_list_item_fail";
    public static final String GET_MERCHAN_TYPE_LIST_END_POINT="/api/merchantenrollmentapi.php?request=merchantTypeList";
    public static final String ID="id";
    public static final String MERCHANTTYPE = "merchantType";

    /**
     * payment url
     */

    public static final String PAYMENT_URL = "http://www.wifyeepay.com/payments/rest/WifyeePayments.php?request=payment_request";
    public static final String MEDICINE_PAYMENT_URL = "http://wifyeepay.com/payments/rest/instaMojoWifyeePayment.php?request=payment_request";

    public static final String DATA = "data";
    public static final String MER_ID = "mer_id";
    public static final String MER_NAME = "mer_name";
    public static final String MER_COMPANY = "mer_company";
    public static final String MER_TYPE = "mer_type";
    public static final String MER_IMAGE = "merchant_profile_image";
    public static final String MER_IDT_ID = "idt_id";
    public static final String MER_IDT_NAME = "idt_name";
    public static final String MER_IDT_ROLE = "idt_role";
    public static final String MER_IDT_EMAIL= "idt_email";
    public static final String MER_IDT_ADDRESS= "idt_address";
    public static final String MER_IDT_CITY= "idt_city";
    public static final String MER_IDT_ZIP_CODE= "idt_zipcode";
    public static final String MER_IDT_FAX= "idt_fax";
    public static final String MER_IDT_NATIONALITY= "idt_nationality";
    public static final String MER_IDT_PHONE= "idt_contactphone";
    public static final String MER_IDT_ALT_PHONE= "idt_alternatephone";
    public static final String MER_CURRENT_STATUS= "current_status";

    /**
     * dairy list item
     */

    public static final String MERCHANT_ID = "merchantId";
    public static final String ITEM_ID = "itemId";
    public static final String ITEM_NAME= "itemName";
    public static final String ITEM_PRICE= "itemPrice";
    public static final String ITEM_TYPE= "itemtype";
    //public static final String ITEM_QUALITY= "quality";
    public static final String ITEM_QUALITY= "item_clearification";
    public static final String ITEM_DISCOUNT_TYPE= "discountType";
    public static final String ITEM_DISCOUNT= "discount";
    public static final String ITEM_DISCOUNT_AMT= "discount_amount";
    public static final String ITEM_MOB_COMMISSION= "mobicash_commission";
    public static final String ITEM_TYPE_COMMISSION= "commission_type";
    public static final String ITEM_QUENTITY= "quentity";
    public static final String ITEM_STATUS= "status";
    public static final String ITEM_CREATE_DATE= "createdDate";
    public static final String ITEM_UNIT= "unit";
    public static final String ITEM_UNIT_QTY= "unit_quantity";
    public static final String ITEM_UPDATED_DATE= "updateddDate";
    public static final String ITEM_IMAGE_OBJ= "imageObj";
    public static final String ITEM_IMAGE_ID= "imageId";
    public static final String ITEM_IMAGE_PATH= "imagePath";

    /***
     * Add order
     */

    public static final String ORDER_ID = "orderId";
    public static final String ORDER_ITEMS = "items";
    public static final String ORDER_ITEM_ID ="item_id";
    public static final String ORDER_QUANTITY = "quantity";
    public static final String ORDER_AMOUNT = "amount";
    public static final String ORD_ORDER_AMT = "order_amount";
    public static final String ORDER_USER_ID = "userId";
    public static final String ORDER_GST_AMT = "gst_amount";
    public static final String ORDER_DELIVERY_AMT = "delivery_amount";
    public static final String ORDER_CLAIM_TYPE = "claim_type";
    public static final String ORDER_ON = "order_on";
    public static final String ORDER_USER_TYPE = "userType";
    public static final String ORDER_PRICE ="orderPrice";
    public static final String ORDER_DATE_TIME = "orderDateTime";
    public static final String ORDER_LAT = "usa_lat";
    public static final String ORDER_LONG ="usa_long";
    public static final String ORDER_LOCATION ="usa_geo_address";
    public static final String ORDER_COMPLETE_ADD ="usa_complete_address";
    public static final String ORDER_DISCOUNT_AMT ="discount_amt";
    public static final String ORDER_PAYMENT_MODE ="payment_mode";
    public static final String ORDER_MERCHANT_ID = "merchantId";
    public static final String ORDER_MOB_NUMBER = "moblile";
    public static final String ORDER_MOBILE_NUMBER = "mobile";
    public static final String ORDER_DESCRIPTION = "description";
    public static final String ORDER_PIN = "pincode";
    public static final String SUBSCRIPTION_FROM_DATE = "subscription_from_date";
    public static final String SUBSCRIPTION_TO_DATE = "subscription_to_date";
    public static final String SUBSCRIPTION_PER_DAY = "subscription_perday_qty";
    public static final String CLAIM_TYPE = "claim_type";
    public static final String VOUCHER_ID = "voucher_id";
    public static final String VOUCHER_NO = "voucher_no";
    public static final String ORDER_REF_ID = "ref_id";
    public static final String TUV_ID = "tuv_id";

    public static String getUniqueId(){
        return UUID.randomUUID().toString();
    }


}

