package com.wifyee.greenfields.dairyorder;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIInterface {
    @Headers("Content-Type: application/json")
    @POST("/api/merchantapi.php?request=getmerchantlist")
    Call<DairyMerchantListModel> doGetDairyMerchantList(@Body JSONObject body);
}
