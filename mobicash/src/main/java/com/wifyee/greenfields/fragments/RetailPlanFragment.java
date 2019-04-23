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
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.PaymentConstants;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.interfaces.OnClickListener;
import com.wifyee.greenfields.models.requests.PlanCategory;
import com.wifyee.greenfields.models.requests.VerifyMobileNumber;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.models.response.PlanDataSummary;
import com.wifyee.greenfields.models.response.PlanDetails;
import com.wifyee.greenfields.models.response.VerifyMobileNumberResponse;
import com.wifyee.greenfields.services.MobicashIntentService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;


public class RetailPlanFragment extends Fragment implements View.OnClickListener ,OnClickListener {

    private View view;
    private RecyclerView mRecyclerView;
    private ImageView dataUsage;
    private PlanDataSummary planDataSummary;
    public  ProgressDialog progressDialog = null;
    private Button payUButton;
    private String plan_Name="";
    private String plan_Cost="";
    private String mobileNumber="";
    private EditText mobileRecharge;
    private String plan_ID="";
    private String plan_Data="";
    private PlanCategory planCategory;
    private Spinner spinnerPlan;
    private String planName="";
    private String planDetails []={"Select","Retail","Commercial","Residential"};
    /**
     * List of actions supported.
     */
    private String[] broadCastReceiverActionList = {
            NetworkConstant.PLAN_LIST_FAILURE,
            NetworkConstant.PLAN_LIST_SUCCESS,  NetworkConstant.VERIFY_MOBILE_NUMBER_SUCCESS,
            NetworkConstant.VERIFY_MOBILE_NUMBER_FAILURE
    };
    public RetailPlanFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static RetailPlanFragment getinstance() {

        return new RetailPlanFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_retail_plan, container, false);
        final Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        dataUsage = (ImageView)view.findViewById(R.id.view_data_usage);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.recyclerView);
        //   spinnerPlan=(Spinner) view.findViewById(R.id.planSpinner);
        //   spinnerPlan.setOnItemSelectedListener(this);
        payUButton=(Button) view.findViewById(R.id.payuMoney);
        payUButton.setOnClickListener(this);
        dataUsage.setOnClickListener(this);
        // Set the toolbar as action bar
        getActivity().setTitle("WiFi Plans");
        planCategory=new PlanCategory();
        planCategory.plan_category="retail";
        MobicashIntentService.startActionWifyeePLans(getActivity(),planCategory);
        // For back button in tool bar handling.
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });

       /* ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item, planDetails); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinnerPlan.setAdapter(spinnerArrayAdapter);*/
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
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(planStatusReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(planStatusReceiver, new IntentFilter(action));
        }

    }

    /**
     * Handling broadcast event for user login .
     */
    private BroadcastReceiver planStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            cancelProgressDialog();
            try {
                if (action.equals(NetworkConstant.PLAN_LIST_SUCCESS)) {
                    final  ArrayList<PlanDetails> mPlanResponse = (ArrayList<PlanDetails>) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    Timber.d("STATUS_PLAN_LIST_SUCCESS = > PlanListResponse  ==>" + new Gson().toJson(mPlanResponse));
                    bindRecylcerAdapter(mPlanResponse);
                    //overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                    //  finish();
                } else if (action.equals(NetworkConstant.PLAN_LIST_FAILURE)) {
                    FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if (failureResponse != null) {
                        Timber.d("STATUS_PLAN_LIST_FAIL = > failureResponse  ==>" + new Gson().toJson(failureResponse));
                    }
                    if (failureResponse != null && failureResponse.msg != null) {
                        showErrorDialog(failureResponse.msg);
                    } else {
                        String errorMessage = getString(R.string.error_message);
                        showErrorDialog(errorMessage);
                    }
                }
                if (action.equals(NetworkConstant.VERIFY_MOBILE_NUMBER_SUCCESS)) {
                    VerifyMobileNumberResponse mVerifyMobileNumberResponse=(VerifyMobileNumberResponse)intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if(mVerifyMobileNumberResponse!=null)
                    {
                        if(mVerifyMobileNumberResponse.ret_code.equalsIgnoreCase("0"))
                        {

                            Intent paymentIntent = IntentFactory.createPromoCodeFillActivity(getActivity());
                            paymentIntent.putExtra(PaymentConstants.STRING_EXTRA,plan_Cost);
                            paymentIntent.putExtra("planName",plan_Name);
                            paymentIntent.putExtra("planId",plan_ID);
                            paymentIntent.putExtra("planData",plan_Data);
                            paymentIntent.putExtra("wifyee","wiFyeepayment");
                            paymentIntent.putExtra("mobileNumber",mobileNumber);
                            paymentIntent.putExtra("description","Deduction from wallet");
                            startActivity(paymentIntent);

                        }else{
                            showErrorDialog("User Not Exist");
                        }
                    }
                } else if (action.equals(NetworkConstant.VERIFY_MOBILE_NUMBER_FAILURE)) {
                    FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if (failureResponse != null) {
                        Timber.d("STATUS_PLAN_LIST_FAIL = > failureResponse  ==>" + new Gson().toJson(failureResponse));
                    }
                    if (failureResponse != null && failureResponse.msg != null) {
                        showErrorDialog(failureResponse.msg);
                    } else {
                        String errorMessage = getString(R.string.error_message);
                        showErrorDialog(errorMessage);
                    }
                }
            } catch (Exception e) {
                Timber.e(" Exception caught in PlanListStatusReceiver " + e.getMessage());
            }
        }
    };

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
    //Bind RecyclerView for plans
    private void bindRecylcerAdapter(ArrayList<PlanDetails> planDetailsArrayList) {
        PlanListAdapter adapter = new PlanListAdapter(planDetailsArrayList,this,getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void seClickListener(String planID,String planCost,String planData,String planName) {
        plan_Cost=planCost;
        plan_Data=planData;
        plan_ID=planID;
        plan_Name=planName;
    }
    @Override
    public void onClick(View v) {

        if(v==dataUsage) {
            showProgressDialog();
            callApiForDataUsage(getActivity(), MobicashUtils.getMacAddress(getActivity()));
        }
        if(v==payUButton) {
            showPopupFillMobile(getActivity());
        }
    }

    //Show popup fill Mobile number
    private void showPopupFillMobile(final Activity activity)
    {
        final Dialog layout = new Dialog(activity);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        layout.setContentView(R.layout.showpopup_fill_mobile);
        // Set dialog title
        layout.setTitle("Fill Mobile");
        Button addMobile = (Button) layout.findViewById(R.id.add_mobile_number);
        mobileRecharge=(EditText) layout.findViewById(R.id.mobile_number_recharge);
        layout.show();
        addMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mobileNumber=mobileRecharge.getText().toString();
                    if(mobileNumber.equals("")||mobileNumber.length()==0)
                    {
                        Toast.makeText(activity, "Fill Mobile Number", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        VerifyMobileNumber mVerifyMobileNumber=new VerifyMobileNumber();
                        mVerifyMobileNumber.req_username=mobileNumber;
                        MobicashIntentService.startActionVerifyMobileNumber(getActivity(), mVerifyMobileNumber);
                        //callApiForVerifyMobileNumber(getActivity());

                    }
                    /*Intent paymentIntent = IntentFactory.createPromoCodeFillActivity(activity);
                    paymentIntent.putExtra(PaymentConstants.STRING_EXTRA,plan_Cost);
                    paymentIntent.putExtra("planName",plan_Name);
                    paymentIntent.putExtra("planId",plan_ID);
                    paymentIntent.putExtra("planData",plan_Data);
                    paymentIntent.putExtra("wifyee","wiFyeepayment");
                    paymentIntent.putExtra("mobileNumber",mobileNumber);
                    paymentIntent.putExtra("description","Deduction from wallet");
                    startActivity(paymentIntent);*/
                    //Add Sender
                    layout.dismiss();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });

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
    private void callApiForDataUsage(final Context context,final String macAddrr)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            //jsonObject.put("macAddress",macAddrr);
            jsonObject.put("mobileNumber", LocalPreferenceUtility.getUserMobileNumber(context));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetworkConstant.USER_VIEW_DATA_USAGE ,jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Getting Response", response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0)
                    {
                        planDataSummary=new PlanDataSummary();
                        JSONObject jsonObject1 =response.getJSONObject("current_plan");
                        String planName=jsonObject1.getString("plan_name");
                        String timeUsed=jsonObject1.getString("time_used");
                        String dataDownload=jsonObject1.getString("data_download");
                        String dataRemaining=jsonObject1.getString("data_remaining");
                        String dataTotal=jsonObject1.getString("data_total");
                        String timeRemaining=jsonObject1.getString("time_remaining");
                        String timeTotal=jsonObject1.getString("time_total");
                        planDataSummary.setData_download(dataDownload);
                        planDataSummary.setData_remaining(dataRemaining);
                        planDataSummary.setData_total(dataTotal);
                        planDataSummary.setPlan_name(planName);
                        planDataSummary.setTime_remaining(timeRemaining);
                        planDataSummary.setTime_used(timeUsed);
                        planDataSummary.setTime_total(timeTotal);
                        showDataUsageDialog(context,planDataSummary);
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

    private void showDataUsageDialog(Context activity, PlanDataSummary planDataSummary)
    {
        final Dialog layout = new Dialog(activity);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layout.setContentView(R.layout.showpopup_view_data);
        // Set dialog title
        layout.setTitle("Data Summary");
        TextView textDataDownload = (TextView) layout.findViewById(R.id.data_download);
        TextView textPlanName = (TextView) layout.findViewById(R.id.plan_name);
        TextView textDataRemaining = (TextView) layout.findViewById(R.id.data_remaining);
        TextView textTimeUsed = (TextView) layout.findViewById(R.id.time_used);
        TextView textDataTotal = (TextView) layout.findViewById(R.id.data_total);
        TextView textTimeRemaining = (TextView) layout.findViewById(R.id.time_remaing);
        TextView textTimeTotal= (TextView) layout.findViewById(R.id.time_total);
        textDataDownload.setText(planDataSummary.getData_download());
        textDataRemaining.setText(planDataSummary.getData_remaining());
        textDataTotal.setText(planDataSummary.getData_total());
        textPlanName.setText(planDataSummary.getPlan_name());
        textTimeUsed.setText(planDataSummary.getTime_used());
        textTimeRemaining.setText(planDataSummary.getTime_remaining());
        textTimeTotal.setText(planDataSummary.getTime_total());
        Button yes = (Button) layout.findViewById(R.id.yes);
        cancelProgressDialog();
        layout.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cancelProgressDialog();
                    layout.dismiss();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
    }
    //Cancel Progress dialog
    private void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }


  /*  @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        planName=parent.getSelectedItem().toString();
        Toast.makeText(getActivity(),"Selected Item"+planName,Toast.LENGTH_SHORT).show();
        callApiForPlan(planName);
    }
//Call Api For Plan List
    private void callApiForPlan(String planName)
    {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/
}
