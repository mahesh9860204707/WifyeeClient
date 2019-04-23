package com.wifyee.greenfields.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.adapters.SplitParticipentListAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.models.response.SplitParticipent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplitParticipientActivity extends BaseActivity
{
   private String splitId="";
    @BindView(R.id.recycler_view_split_participent_list)
    RecyclerView recyclerView;
    private SplitParticipent splitParticipent;
    private ArrayList<SplitParticipent> splitParticipentArrayList=new ArrayList<>();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_participient);
        ButterKnife.bind(this);
        mContext = this;
        splitId=getIntent().getStringExtra("splitId");
        showProgressDialog();
        callSplitMoneyApi(this,splitId);
    }
//Call Split Money Api
    private void callSplitMoneyApi(final Context context, String splitId)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, NetworkConstant.USER_PARTICIPENT_SPLIT_LIST_POINT+splitId  ,null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Getting Response", response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                        JSONArray splitMoney = response.getJSONArray("splitMoneyRequestParticipantRecords");
                        // getting json objects from Ingredients json array
                        for(int j=0; j<splitMoney.length(); j++)
                        {
                            JSONObject json = splitMoney.getJSONObject(j);
                            String reference=json.getString("splitMoneyParticipantUniqueReferenceNumber");
                            String amount=json.getString("splitMoneyParticipantParticipantAmount");
                            String mobile=json.getString("splitMoneyParticipantMobileNumber");
                            String email=json.getString("splitMoneyParticipantEmailAddress");
                            String status=json.getString("splitMoneyParticipantStatus");
                            String createdDate=json.getString("splitMoneyParticipantCreatedDate");
                            String splitID=json.getString("splitMoneyParticipantId");
                            splitParticipent=new SplitParticipent();
                            splitParticipent.setSplitMoneyParticipantCreatedDate(createdDate);
                            splitParticipent.setSplitMoneyParticipantParticipantAmount(amount);
                            splitParticipent.setSplitMoneyParticipantEmailAddress(email);
                            splitParticipent.setSplitMoneyParticipantId(splitID);
                            splitParticipent.setSplitMoneyParticipantMobileNumber(mobile);
                            splitParticipent.setSplitMoneyParticipantStatus(status);
                            splitParticipent.setSplitMoneyParticipantUniqueReferenceNumber(reference);
                            splitParticipentArrayList.add(splitParticipent);
                        }
                        bindAdapterSplitParticipentList(splitParticipentArrayList);
                    }
                    else
                    {
                        Toast.makeText(context, "Some Issue", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("SPlit_error", String.valueOf(error));
                Toast.makeText(context,"response Error",Toast.LENGTH_SHORT).show();


            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                Log.d("params_SPlit", String.valueOf(params));
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setShouldCache(false);
    }
    //Bind Adapter Class of participent List
    private void bindAdapterSplitParticipentList(ArrayList<SplitParticipent> splitParticipentArrayList) {
        SplitParticipentListAdapter adapter = new SplitParticipentListAdapter(this,splitParticipentArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        cancelProgressDialog();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
    }
}
