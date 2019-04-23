package com.wifyee.greenfields.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.adapters.PlanListAdapter;
import com.wifyee.greenfields.adapters.PromoCodeListAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.PaymentConstants;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.interfaces.OnClickListener;
import com.wifyee.greenfields.models.requests.PlanCategory;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.models.response.PlanDataSummary;
import com.wifyee.greenfields.models.response.PlanDetails;
import com.wifyee.greenfields.models.response.PromoCodeBean;
import com.wifyee.greenfields.services.MobicashIntentService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;


public class PromoCodeFragment extends Fragment  {

    private View view;
    private RecyclerView mRecyclerView;
    private PromoCodeBean promoCodeBean;
    private ArrayList<PromoCodeBean> promoCodeBeanArrayList=new ArrayList<>();
    public  ProgressDialog progressDialog = null;
    public PromoCodeFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static PromoCodeFragment getinstance() {
        return new PromoCodeFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_promo_code, container, false);
        final Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        showProgressDialog();
        callApiForPromocode(getActivity());
        mRecyclerView=(RecyclerView)view.findViewById(R.id.recyclerView_promo_code);
        // Set the toolbar as action bar
        getActivity().setTitle("Promo Code");
        // For back button in tool bar handling.
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void showErrorDialog(String errorMessage) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(errorMessage);
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
    }
    //Bind RecyclerView for Promocode
    private void bindRecylcerAdapter(List<PromoCodeBean> promoCodeBeanArrayList) {
        PromoCodeListAdapter adapter = new PromoCodeListAdapter(promoCodeBeanArrayList,getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
    }

    private void showProgressDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            progressDialog = new ProgressDialog(getActivity());
        }
        progressDialog.setMessage("Please wait...");
        progressDialog.setIcon(0);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    //Call Api for data usage
    private void callApiForPromocode(final Context context)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, NetworkConstant.USER_PROMO_CODDE ,null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Getting Response", response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                        cancelProgressDialog();
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            promoCodeBean = new PromoCodeBean();
                            JSONObject promoJsonObject=jsonArray.getJSONObject(i);
                            String promoName = promoJsonObject.getString("promoName");
                            String promoCode = promoJsonObject.getString("promoCode");
                            String promoDesc = promoJsonObject.getString("promoDesc");
                            String type = promoJsonObject.getString("type");
                            String discountType = promoJsonObject.getString("discountType");
                            String discount = promoJsonObject.getString("discount");
                            String maxDiscount = promoJsonObject.getString("maxDiscount");
                            String startDate = promoJsonObject.getString("startDate");
                            String endDate = promoJsonObject.getString("endDate");
                            String userType = promoJsonObject.getString("userType");
                            String createdDate = promoJsonObject.getString("createdDate");
                            String planId = promoJsonObject.getString("planId");
                            String status = promoJsonObject.getString("status");
                            promoCodeBean.setPromoName(promoName);
                            promoCodeBean.setPromoCode(promoCode);
                            promoCodeBean.setPromoDesc(promoDesc);
                            promoCodeBean.setType(type);
                            promoCodeBean.setDiscountType(discountType);
                            promoCodeBean.setDiscount(discount);
                            promoCodeBean.setMaxDiscount(maxDiscount);
                            promoCodeBean.setStartDate(startDate);
                            promoCodeBean.setEndDate(endDate);
                            promoCodeBean.setUserType(userType);
                            promoCodeBean.setCreatedDate(createdDate);
                            promoCodeBean.setPlanId(planId);
                            promoCodeBean.setStatus(status);
                            promoCodeBeanArrayList.add(promoCodeBean);
                        }
                        bindRecylcerAdapter(promoCodeBeanArrayList);
                    }
                    else {
                        cancelProgressDialog();
                        Toast.makeText(context, "Some Issue in Getting response", Toast.LENGTH_SHORT).show();
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


    //Cancel Progress dialog
    private void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }

}
