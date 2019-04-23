package com.wifyee.greenfields.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.PaymentConstants;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.models.response.PromoCodeBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PromoCodeApplyActivity extends BaseActivity implements View.OnClickListener
{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_back)
    ImageButton back;
    @BindView(R.id.edit_applied_promo)
    EditText et_promo_code;
    @BindView(R.id.applied_button)
    Button aaplied;
    @BindView(R.id.skip_button)
    TextView skipButton;

    private String wiFypayment="";
    private String planId="";
    private String plandata="";
    private String planName="";
    private String description="";
    private String mobileNumberRecharge="";
    private String planCost="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_code_apply);
        ButterKnife.bind(this);
        wiFypayment = getIntent().getStringExtra("wifyee");
        planName = getIntent().getStringExtra("planName");
        planId = getIntent().getStringExtra("planId");
        plandata = getIntent().getStringExtra("planData");
        description = getIntent().getStringExtra("description");
        mobileNumberRecharge=getIntent().getStringExtra("mobileNumber");
        planCost = getIntent().getStringExtra(PaymentConstants.STRING_EXTRA);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        aaplied.setOnClickListener(this);
        skipButton.setOnClickListener(this);
        if (mToolbar!= null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
    if(v==aaplied) {
        if (et_promo_code.getText().toString().length() == 0 || et_promo_code.getText().toString() == null) {
            Toast.makeText(this, "Please fill Promocode", Toast.LENGTH_SHORT).show();
            return;
        }
        //Call Api for Applied Promo Code
        showProgressDialog();
        appliedPromoCodetoServer(et_promo_code.getText().toString(), this);
    }
    if(v==skipButton) {
        Intent paymentIntent = IntentFactory.createPayuBaseActivity(this);
        paymentIntent.putExtra(PaymentConstants.STRING_EXTRA,planCost);
        paymentIntent.putExtra("planName",planName);
        paymentIntent.putExtra("planId",planId);
        paymentIntent.putExtra("planData",plandata);
        paymentIntent.putExtra("wifyee","wiFyeepayment");
        paymentIntent.putExtra("mobileNumber",mobileNumberRecharge);
        paymentIntent.putExtra("description",description);
        startActivity(paymentIntent);
    }

    }
    //Call Api for applied promo code
    private void appliedPromoCodetoServer(String promoCode, final Context context)
    {
        StringBuilder builder=new StringBuilder(LocalPreferenceUtility.getUserCode(context));
        builder.append("Client");
        builder.append(planId);
        builder.append(promoCode);
        builder.append(planCost);
        String hash="";
        try {
            hash= MobicashUtils.getSha1(builder.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("userId",LocalPreferenceUtility.getUserCode(context));
            jsonObject.put("userType","Client");
            jsonObject.put("planId",planId);
            jsonObject.put("promocode",promoCode);
            jsonObject.put("planAmount",planCost);
            jsonObject.put("hash",hash);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetworkConstant.USER_PROMO_CODDE_APPLIED ,jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Getting Response", response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0)
                    {
                        cancelProgressDialog();
                        String messageResponse=response.getString("msg");
                        showPopupPlanPurchase(context,messageResponse);
                    }
                    else if(response.getInt(ResponseAttributeConstants.STATUS)==0)
                    {
                        String messageResponse=response.getString("msg");
                        showPopupPlanPurchase(context,messageResponse);
                    }
                    else {
                        cancelProgressDialog();
                        Toast.makeText(context, "Issue in Promo code Getting response", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley_error", String.valueOf(error));
                Toast.makeText(context,"response Error",Toast.LENGTH_SHORT).show();
                cancelProgressDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                Log.d("params", String.valueOf(params));
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setShouldCache(false);
    }

    private void showPopupPlanPurchase(final Context context, String messageResponse)
    {
        final Dialog layout = new Dialog(context);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layout.setContentView(R.layout.showpopup_success_applied_promo_code);
        // Set dialog title
        layout.setTitle("Success");
        Button yes = (Button) layout.findViewById(R.id.yes);
        Button no = (Button) layout.findViewById(R.id.no);
        TextView textInfo = (TextView) layout.findViewById(R.id.info);
        if(messageResponse.equals("success")) {
            textInfo.setText(messageResponse+" "+"Apllied Your Promocode Successfully,Do You Want To Continue Plan Purchase");
        }
        else{
            textInfo.setText(messageResponse+" "+"Does Not Apllied Your Promocode Do You Want To Continue Plan Purchase");
        }
        layout.show();
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent paymentIntent = IntentFactory.createPayuBaseActivity(context);
                    paymentIntent.putExtra(PaymentConstants.STRING_EXTRA,planCost);
                    paymentIntent.putExtra("planName",planName);
                    paymentIntent.putExtra("planId",planId);
                    paymentIntent.putExtra("planData",plandata);
                    paymentIntent.putExtra("wifyee","wiFyeepayment");
                    paymentIntent.putExtra("mobileNumber",mobileNumberRecharge);
                    paymentIntent.putExtra("description",description);
                    startActivity(paymentIntent);
                    layout.dismiss();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }
}
