package com.wifyee.greenfields.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.adapters.OrderAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.foodorder.ModelMapper;
import com.wifyee.greenfields.interfaces.FragmentInterface;
import com.wifyee.greenfields.models.OrderModel;
import com.wifyee.greenfields.models.requests.GetClientProfileRequest;
import com.wifyee.greenfields.models.response.FailureResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import timber.log.Timber;

public class OrderFragment extends Fragment implements FragmentInterface {

    private OrderAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<OrderModel> listArray = new ArrayList<>();
    OrderModel[] order;
    private ProgressDialog progressDialog;
    LinearLayout ll;

    public OrderFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_fragment, container, false);

        recyclerView = view.findViewById(R.id.order_recycler);
        ll = view.findViewById(R.id.empty_view_containers);


        return view;
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext(), ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIcon(0);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    protected void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }

    private void dataLoad(){
        if(listArray!=null){
            listArray.clear();
        }
        Log.e("in data","in load");
        showProgressDialog();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.USERID, LocalPreferenceUtility.getUserCode(getContext()));
        } catch (JSONException e) {
            Timber.e("JSONException. message : " + e.getMessage());
        }
        AndroidNetworking.post(NetworkConstant.MOBICASH_BASE_URL_TESTING + NetworkConstant.PARAM_MYORDER)
            .addJSONObjectBody(jsonObject)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject object) {
                    Log.e("--RESPONSE--",object.toString());
                    Timber.e("called onResponse of All Order.");
                    cancelProgressDialog();
                    try {
                        if (object.getInt(ResponseAttributeConstants.STATUS) != 0) {
                            JSONArray jsonArray = object.getJSONArray("orders");
                            order = new OrderModel[jsonArray.length()];

                            for (int i=0; i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                order[i] = new OrderModel(
                                        jsonObject.getString("order_id"),
                                        jsonObject.getString("order_amount"),
                                        jsonObject.getString("order_on"),
                                        jsonObject.getString("merchant_id"),
                                        jsonObject.getString("emp_name"),
                                        jsonObject.getString("task_id"),
                                        jsonObject.getString("merchant_type")
                                );
                                listArray.add(order[i]);
                            }
                            recyclerView.getRecycledViewPool().clear();
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(),
                                    "Something went wrong. PLease try again",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Timber.e("JSONException Caught.  Message : " + e.getMessage());
                    }
                }

                @Override
                public void onError(ANError error) {
                    cancelProgressDialog();
                    // handle error
                    Timber.e("called onError of User Invoice API.");
                    Timber.e("Error Message : " + error.getMessage());
                    Timber.e("Error code : " + error.getErrorCode());
                    Timber.e("Error Body : " + error.getErrorBody());
                    Timber.e("Error Detail : " + error.getErrorDetail());

                    FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                    Timber.w("UserInvoice = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                }
            });
    }

    @Override
    public void fragmentBecameVisible() {
        //Log.e("test","TestFragment");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new OrderAdapter(getActivity(), listArray);
        recyclerView.setAdapter(adapter);
        dataLoad();
    }
}
