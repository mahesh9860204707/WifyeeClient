package com.wifyee.greenfields.Intents;

import android.content.Context;
import android.content.Intent;

import com.wifyee.greenfields.activity.AccountSettingActivity;
import com.wifyee.greenfields.activity.AddMoneyActivity;
import com.wifyee.greenfields.activity.AuthorizedPhoneSettingActivity;
import com.wifyee.greenfields.activity.BroadBandActivity;
import com.wifyee.greenfields.activity.BusBookActivity;
import com.wifyee.greenfields.activity.ChangePasswordSettingActivity;
import com.wifyee.greenfields.activity.ChoosePaymentWalletActivity;
import com.wifyee.greenfields.activity.DTHRechargeActivity;
import com.wifyee.greenfields.dairyorder.OrderSummaryDetails;
import com.wifyee.greenfields.dairyorder.DairyProductActivity;
import com.wifyee.greenfields.activity.ElectricityBillPaymentActivity;
import com.wifyee.greenfields.activity.GasBillPaymentActivity;
import com.wifyee.greenfields.activity.LandlineRechargeActivity;
import com.wifyee.greenfields.activity.MobicashDashBoardActivity;
import com.wifyee.greenfields.activity.MoneyTransferActivity;
import com.wifyee.greenfields.activity.NotificationSettingActivity;
import com.wifyee.greenfields.activity.PayUBaseActivity;
import com.wifyee.greenfields.activity.PaymentSettingActivity;
import com.wifyee.greenfields.activity.PlanDetailsActivity;
import com.wifyee.greenfields.activity.PostPaidRechargeActivity;
import com.wifyee.greenfields.activity.PrePaidRechargeActivity;
import com.wifyee.greenfields.activity.PromoCodeApplyActivity;
import com.wifyee.greenfields.activity.RequestCreditActivity;
import com.wifyee.greenfields.activity.RequestMoneyActivity;
import com.wifyee.greenfields.activity.SellProductsListActivity;
import com.wifyee.greenfields.activity.SendMoneyActivity;
import com.wifyee.greenfields.activity.SendPassCodeActivity;
import com.wifyee.greenfields.activity.SettingActivity;
import com.wifyee.greenfields.activity.SignInBaseActivity;
import com.wifyee.greenfields.activity.SignUpActivity;
import com.wifyee.greenfields.activity.SplitMoneyActivity;
import com.wifyee.greenfields.activity.SupplementaryInfoSettingActivity;
import com.wifyee.greenfields.activity.TicketingActivity;
import com.wifyee.greenfields.activity.TransactionsListActivity;
import com.wifyee.greenfields.activity.UploadMedicinePrescriptionActivity;
import com.wifyee.greenfields.dairyorder.ProductsMainActivity;
import com.wifyee.greenfields.foodorder.AddToCartActivity;
import com.wifyee.greenfields.foodorder.FoodOrderListActivity;
import com.wifyee.greenfields.foodorder.FoodStatusActivity;
import com.wifyee.greenfields.foodorder.MerchantActivity;



/**
 * Created by sumanta on 12/13/16.
 */

public class IntentFactory {

    /**
     * create user login activity
     */

    public static Intent createUserLoginActivity(Context context) {
        Intent intent = new Intent(context, SignInBaseActivity.class);
        return intent;
    }


    public static Intent createFoodStatusActivity(Context context) {
        Intent intent = new Intent(context, FoodStatusActivity.class);
        return intent;
    }

    public static Intent createAddOderByMerchantFoodActivity(Context context) {
        Intent intent = new Intent(context, FoodOrderListActivity.class);
        return intent;
    }

    /**
     * create user enrollment activity
     */

    public static Intent createUserEnrollmentActivity(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);
        return intent;
    }
    /**
     * create user enrollment activity
     */

    public static Intent createAddTOCartActivity(Context context) {
        Intent intent = new Intent(context, AddToCartActivity.class);
        return intent;
    }


    /**
     * create dashboard activity
     */
    public static Intent createDashboardActivity(Context context) {
        Intent intent = new Intent(context, MobicashDashBoardActivity.class);
        return intent;
    }

    /**
     * create post paid activity
     */
    public static Intent createPostpaidActivity(Context context) {
        Intent intent = new Intent(context, PostPaidRechargeActivity.class);
        return intent;
    }

    /**
     * create pre paid activity
     */
    public static Intent createPrepaidActivity(Context context) {
        Intent intent = new Intent(context, PrePaidRechargeActivity.class);
        return intent;
    }

    /**
     * create land line activity
     */
    public static Intent createLandLineActivity(Context context) {
        Intent intent = new Intent(context, LandlineRechargeActivity.class);
        return intent;
    }

    /**
     * create dth activity
     */
    public static Intent createDthActivity(Context context) {
        Intent intent = new Intent(context, DTHRechargeActivity.class);
        return intent;
    }

    /**
     * create gas bill payment activity
     */
    public static Intent createGasBillPaymentActivity(Context context) {
        Intent intent = new Intent(context, GasBillPaymentActivity.class);
        return intent;
    }

    /**
     * create Broadband bill payment activity
     */
    public static Intent createBroadbandBillPaymentActivity(Context context) {
        Intent intent = new Intent(context, BroadBandActivity.class);
        return intent;
    }

    /**
     * create electricity bill payment activity
     */
    public static Intent createElectricityBillPaymentActivity(Context context) {
        Intent intent = new Intent(context, ElectricityBillPaymentActivity.class);
        return intent;
    }

    /**
     * create electricity bill payment activity
     */
    public static Intent createBookBusTicketActivity(Context context) {
        Intent intent = new Intent(context, BusBookActivity.class);
        return intent;
    }

    /**
     * create setting activity
     */
    public static Intent createSettingActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        return intent;
    }

    /**
     * create setting activity
     */
    public static Intent createAccountSettingActivity(Context context) {
        Intent intent = new Intent(context, AccountSettingActivity.class);
        return intent;
    }

    /**
     * create setting supplementary info
     */
    public static Intent createSupplementaryInfoSettingActivity(Context context) {
        Intent intent = new Intent(context, SupplementaryInfoSettingActivity.class);
        return intent;
    }

    /**
     * create change password
     */
    public static Intent createChangePasswordSettingActivity(Context context) {
        Intent intent = new Intent(context, ChangePasswordSettingActivity.class);
        return intent;
    }

    /**
     * create payment setting activity
     */
    public static Intent createPaymentSettingActivity(Context context) {
        Intent intent = new Intent(context, PaymentSettingActivity.class);
        return intent;
    }

    /**
     * create authorized phone setting activity
     */
    public static Intent createAuthorizedPhoneSettingActivity(Context context) {
        Intent intent = new Intent(context, AuthorizedPhoneSettingActivity.class);
        return intent;
    }


    /**
     * create authorized phone setting activity
     */
//    public static Intent createIdentitySettingActivity(Context context) {
//        Intent intent = new Intent(context, IdentitySettingActivity.class);
//        return intent;
//    }

    /**
     * create notification setting activity
     */
    public static Intent createNotificationSettingActivity(Context context) {
        Intent intent = new Intent(context, NotificationSettingActivity.class);
        return intent;
    }

    /**
     * create money transfer activity
     */
    public static Intent createMoneyTransferActivity(Context context) {
        Intent intent = new Intent(context, MoneyTransferActivity.class);
        return intent;
    }

    /**
     * create Send money activity
     */
    public static Intent createSendMoneyActivity(Context context) {
        Intent intent = new Intent(context, SendMoneyActivity.class);
        return intent;
    }

    /**
     * create Request money activity
     */
    public static Intent createRequestMoneyActivity(Context context) {
        Intent intent = new Intent(context, RequestMoneyActivity.class);
        return intent;
    }

    /**
     * create Qr Code
     */
    public static Intent scanQRCodeActivity(Context context) {
        Intent intent = new Intent(context, ChoosePaymentWalletActivity.class);
        return intent;
    }

    /**
     * create send passcode activity
     */

    public static Intent createSendPasscodeActivity(Context context) {
        Intent intent = new Intent(context, SendPassCodeActivity.class);
        return intent;
    }

    /**
     * create send passcode activity
     */

    public static Intent createTicketingActivity(Context context) {
        Intent intent = new Intent(context, TicketingActivity.class);
        return intent;
    }


    /**
     * create payu payment base  activity
     */

    public static Intent createPayuBaseActivity(Context context) {
        Intent intent = new Intent(context, PayUBaseActivity.class);
        return intent;
    }

    /**
     * create payu payment base  activity
     */

    public static Intent createPromoCodeFillActivity(Context context) {
        Intent intent = new Intent(context, PromoCodeApplyActivity.class);
        return intent;
    }
    /**
     * create payu payment base  activity
     */
    public static Intent createAddMoneyActivity(Context context) {
        Intent intent = new Intent(context, AddMoneyActivity.class);
        return intent;
    }

    /**
     * create payu payment base  activity
     */
    public static Intent createRequestCreditActivity(Context context) {
        Intent intent = new Intent(context, RequestCreditActivity.class);
        return intent;
    }

    /**
     * create payu payment base  activity
     */

    public static Intent createSplitMoneyActivity(Context context) {
        Intent intent = new Intent(context, SplitMoneyActivity.class);
        return intent;
    }

    /**
     * create Transaction List activity
     */

    public static Intent createTransactionActivity(Context context) {
        Intent intent = new Intent(context, TransactionsListActivity.class);
        return intent;
    }

    /**
     * create WI FI Plans activity
     */

    public static Intent createWiFiPlansActivity(Context context) {
        Intent intent = new Intent(context, PlanDetailsActivity.class);
        return intent;
    }


    /**
     * create Selling Products activity
     */

    public static Intent createsellingProductsActivity(Context context) {
        Intent intent = new Intent(context, SellProductsListActivity.class);
        return intent;
    }


    /**
     * create Selling Products activity
     */

    public static Intent createMedicinePreciptionActivity(Context context) {
        Intent intent = new Intent(context, UploadMedicinePrescriptionActivity.class);
        return intent;
    }

    public static Intent createAddOderFoodActivity(Context context) {
        Intent intent = new Intent(context, MerchantActivity.class);
        return intent;
    }

    /**
     * create dairy product activity
     */
    public static Intent createDairyProductActivity(Context ctx){
        Intent intent = new Intent(ctx, DairyProductActivity.class);
        return intent;
    }

    /**
     * create  product gridactivity
     */
    public static Intent createProductActivity(Context ctx){
        Intent intent = new Intent(ctx, ProductsMainActivity.class);
        return intent;
    }

    /**
     * create  order summary  details
     */
    public static Intent createOrderSummaryDetailsActivity(Context ctx){
        Intent intent = new Intent(ctx, OrderSummaryDetails.class);
        return intent;
    }

}
