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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.activity.AddBeneficiaryOTPActivity;
import com.wifyee.greenfields.activity.KYCActivity;
import com.wifyee.greenfields.adapters.BeneficiaryListAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.interfaces.OnClickListenerAddBeneficiary;
import com.wifyee.greenfields.models.requests.BeneficiaryBean;
import com.wifyee.greenfields.models.response.BeneficiaryResponse;
import com.wifyee.greenfields.models.response.CityResponse;
import com.wifyee.greenfields.services.MobicashIntentService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ManageBeneficiaryFragment extends Fragment implements View.OnClickListener ,OnClickListenerAddBeneficiary {

    private View view;
    private LinearLayout layooutAddBenef,layoutAddKyc;
    private RecyclerView recyclerViewBenef;
    public ProgressDialog progressDialog = null;
    public ArrayList<String>  stateList=new ArrayList<>();
    private String stateID="";
    private String stateName="";
    private String cityID="";
    private ArrayList<CityResponse> cityResponseArrayList=new ArrayList<>();
    private ArrayList<CityResponse>  sateCityResponseArrayList=new ArrayList<>();
    private CityResponse cityResponse;
    private ArrayList<String> statecityList=new ArrayList<>();
    private String beneficiaryID="";
    private Spinner spinBankState,spinBankCity;
    private BeneficiaryBean beneficiaryBean;
    private ArrayList<BeneficiaryBean> beneficiaryBeenList=new ArrayList<>();
    public ManageBeneficiaryFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(operatorListReceiver, new IntentFilter(action));
        }
        //showProgressDialog();
        MobicashIntentService.startActionGetStateList(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_manage_beneficiary, container, false);
        bindUi(view);
        //Call Api for beneficiaryList
        callApiBeneficiaryList(getActivity());
        return view;
    }
   //Call Api Beneficiary List
    private void callApiBeneficiaryList(final Context context)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", LocalPreferenceUtility.getUserCode(context));
            jsonObject.put("userType","Client");
            jsonObject.put("agentCode","RS00789");
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetworkConstant.USER_BENEFICIARY_LIST_END_POINT ,jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Getting Response", response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                        JSONObject jsonObject=response.getJSONObject("data");
                        JSONObject jsonData=jsonObject.getJSONObject("DATA");
                        JSONArray jsonArray=jsonData.getJSONArray("BENEFICIARY_DATA");
                        beneficiaryBeenList.clear();
                        for(int i=0;i<jsonArray.length();i++) {
                            beneficiaryBean=new BeneficiaryBean();
                            JSONObject benJsonObject=jsonArray.getJSONObject(i);
                            String benID=benJsonObject.getString("BENE_ID");
                            String benMobile=benJsonObject.getString("BENE_MOBILENO");
                            String benName=benJsonObject.getString("BENE_NAME");
                            String benNickName=benJsonObject.getString("BENE_NICKNAME");
                            String benBankName=benJsonObject.getString("BENE_BANKNAME");
                            String benBankAccount=benJsonObject.getString("BANK_ACCOUNTNO");
                            String benIFSCCode=benJsonObject.getString("BANKIFSC_CODE");
                            String benStatus=benJsonObject.getString("BENEVERIFIED_STATUS");
                            beneficiaryBean.setBenIfscCode(benIFSCCode);
                            beneficiaryBean.setBenAccountNumber(benBankAccount);
                            beneficiaryBean.setBenMobileNumber(benMobile);
                            beneficiaryBean.setBenFirstName(benName);
                            beneficiaryBean.setBenBankName(benBankName);
                            beneficiaryBean.setStatus(benStatus);
                            beneficiaryBean.setBeneficiary_id(benID);
                            beneficiaryBeenList.add(beneficiaryBean);
                        }
                        bindBeneFiciaryList(beneficiaryBeenList);
                    }
                    else {
                        cancelProgressDialog();
                        Toast.makeText(context, "Some Issue in Getting response", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    cancelProgressDialog();
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

    /**
     * List of actions supported.
     */
    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATE_LIST_SUCCESS,
            NetworkConstant.STATE_LIST_FAILURE,
            NetworkConstant.ADD_BENEFICIARY_SUCCESS,
            NetworkConstant.ADD_BENEFICIARY_FAILURE
    };

//Bind View
    private void bindUi(View view) {
        layooutAddBenef= (LinearLayout) view.findViewById(R.id.add_beneficiary);
        layoutAddKyc= (LinearLayout) view.findViewById(R.id.add_kyc);
        layooutAddBenef.setOnClickListener(this);
        layoutAddKyc.setOnClickListener(this);
        recyclerViewBenef= (RecyclerView) view.findViewById(R.id.recycler_beneficiary);
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
        cancelProgressDialog();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(operatorListReceiver);
    }
    /**
     * operator list and recharge status receiver
     */
    private BroadcastReceiver operatorListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionOperatorList = intent.getAction();
            if (actionOperatorList.equals(NetworkConstant.STATE_LIST_SUCCESS)) {
                ArrayList<CityResponse> cityListResponse = (ArrayList<CityResponse>) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (cityListResponse != null) {
                    cancelProgressDialog();
                    if (cityListResponse != null) {
                        //Bind Spinner Adapter
                        cityResponseArrayList.addAll(cityListResponse);
                        for(int i=0;i<cityListResponse.size();i++) {
                            stateList.add(cityResponseArrayList.get(i).getName());
                        }
                        //bindSpinnerAdapter(cityListResponse);
                        cancelProgressDialog();
                    } else {
                        cancelProgressDialog();
                    }
                } else {
                    cancelProgressDialog();
                }
            } else if (actionOperatorList.equals(NetworkConstant.STATE_LIST_FAILURE)) {
                cancelProgressDialog();
            }
            if(actionOperatorList.equals(NetworkConstant.ADD_BENEFICIARY_SUCCESS)) {
                BeneficiaryResponse getBeneficiaryResponse = (BeneficiaryResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if(getBeneficiaryResponse!=null) {
                    String beneficiaryId = getBeneficiaryResponse.ben_id;
                    beneficiaryID=beneficiaryId;
                    callSendOtpBeneficiary(context,beneficiaryID);
                    cancelProgressDialog();
                }
                cancelProgressDialog();
            }
            else if(actionOperatorList.equals(NetworkConstant.ADD_BENEFICIARY_FAILURE))
            {
                cancelProgressDialog();
            }
        }
    };
 //Call api for send OTP for Beneficiary
    private void callSendOtpBeneficiary(final Context context,final String beneficiaryId)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", LocalPreferenceUtility.getUserCode(context));
            jsonObject.put("userType","Client");
            jsonObject.put("agentCode","RS00789");
            jsonObject.put("customerMobile",LocalPreferenceUtility.getUserMobileNumber(context));
            jsonObject.put("requestFor","CUSTVERIFICATION");

            StringBuilder sb = new StringBuilder(LocalPreferenceUtility.getUserCode(context));
            sb.append("Client");
            sb.append("RS00789");
            sb.append(LocalPreferenceUtility.getUserMobileNumber(context));
            sb.append("CUSTVERIFICATION");
            try {
                String hashValue="";
                hashValue = MobicashUtils.getSha1(sb.toString());
                jsonObject.put("hash",hashValue);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetworkConstant.USER_SEND_BENEFICIARY_OTP_VERIFICATION_END_POINT ,jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e("Getting Response", response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0)
                    {
                        String  requestCode = response.getString("responseCode");
                        callActivityVerifyOtp(context,requestCode,beneficiaryId);
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
   //Call very Otp method
    private void callActivityVerifyOtp(Context context, String code, String benId) {
        startActivity(new Intent(context, AddBeneficiaryOTPActivity.class).putExtra("requestCode",code).putExtra("benId",benId));
    }

    //Bind Spinner Adapter
    private void bindSpinnerAdapter(final ArrayList<CityResponse> cityListResponse) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, stateList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinBankState.setAdapter(spinnerArrayAdapter);
        spinBankState.setPrompt("Select State");
    }

    @Override
    public void onClick(View v)
    {
        if(v==layooutAddBenef) {
            showPopupAddBenef(getActivity(), cityResponseArrayList);
        }
        else if(v==layoutAddKyc)
        {
            startActivity(new Intent(getActivity(), KYCActivity.class));
        }
    }
    //Show Popup Add Benefeciary
    private void showPopupAddBenef(final Activity activity,final ArrayList<CityResponse> cityResponseArrayList)
    {
        final Dialog layout = new Dialog(activity);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layout.setContentView(R.layout.showpoup_add_beneficiary);
        // Set dialog title
        layout.setTitle("Success");
        final Button addBeneficiary = (Button) layout.findViewById(R.id.add_beneficiary_ac);
        Button cancel = (Button) layout.findViewById(R.id.cancel);
        final EditText etBenefFirstName = (EditText) layout.findViewById(R.id.benef_first_name);
        final EditText etBenefLastName = (EditText) layout.findViewById(R.id.benef_last_name);
        final EditText etBenefAccNo = (EditText) layout.findViewById(R.id.benef_account_number);
        final EditText etBenefIfscCode = (EditText) layout.findViewById(R.id.benef_ifsc_code);
        final EditText etBenefMobileNo = (EditText) layout.findViewById(R.id.benef_mobile_number);
        final EditText etBeneBankName = (EditText) layout.findViewById(R.id.bank_name);
        final EditText etBranchName = (EditText) layout.findViewById(R.id.bank_branch_name);
        final EditText etBankAddress = (EditText) layout.findViewById(R.id.bank_address);
        spinBankState = (Spinner) layout.findViewById(R.id.bank_state);
        spinBankCity = (Spinner) layout.findViewById(R.id.bank_city);
        bindSpinnerAdapter(cityResponseArrayList);
        //Bind Spinner State Adapter
        spinBankState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateName=cityResponseArrayList.get(position).getName();
                stateID=cityResponseArrayList.get(position).getId();
                //Call Api for City within particular state
                callApiCityList(stateID,activity);
                Toast.makeText(getActivity(), "Selected State="+stateID, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Bind Spinner city Adapter
        spinBankCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityID=sateCityResponseArrayList.get(position).getName();
                Toast.makeText(getActivity(), "Selected City="+cityID, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addBeneficiary.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {
                    if(etBenefFirstName.getText().toString().length()==0)
                    {
                        Toast.makeText(activity,"Enter Beneficiary First Name",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(etBenefLastName.getText().toString().length()==0)
                    {
                        Toast.makeText(activity,"Enter Beneficiary Last Name",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(etBenefAccNo.getText().toString().length()==0)
                    {
                        Toast.makeText(activity,"Enter Beneficiary Account Number",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(etBenefIfscCode.getText().toString().length()==0)
                    {
                        Toast.makeText(activity,"Enter Beneficiary IFSC Code",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(etBenefMobileNo.getText().toString().length()==0)
                    {
                        Toast.makeText(activity,"Enter Beneficiary Mobile Number",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(etBeneBankName.getText().toString().length()==0)
                    {
                        Toast.makeText(activity,"Enter Beneficiary Bank Name",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(etBranchName.getText().toString().length()==0)
                    {
                        Toast.makeText(activity,"Enter Branch Name",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(etBankAddress.getText().toString().length()==0)
                    {
                        Toast.makeText(activity,"Enter Bank Address",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //Bind Adapter of Beneficiary
                    //bindAdapterBeneficiaryList();
                    beneficiaryBean=new BeneficiaryBean();
                    beneficiaryBean.setUser_id(LocalPreferenceUtility.getUserCode(activity));
                    beneficiaryBean.setUserType("Client");
                    beneficiaryBean.setAgentCode("RS00789");
                    beneficiaryBean.setCustomerMobile(LocalPreferenceUtility.getUserMobileNumber(activity));
                    beneficiaryBean.setCustomerLname(LocalPreferenceUtility.getUserLastName(activity));
                    beneficiaryBean.setBenFirstName(LocalPreferenceUtility.getUserFirstsName(activity));
                    beneficiaryBean.setCustomerEmail(LocalPreferenceUtility.getUserEmail(activity));
                    beneficiaryBean.setType("sender");
                    beneficiaryBean.setSource("app");
                    beneficiaryBean.setBenFirstName(etBenefFirstName.getText().toString());
                    beneficiaryBean.setBenLastName(etBenefLastName.getText().toString());
                    beneficiaryBean.setBenAccountNumber(etBenefAccNo.getText().toString());
                    beneficiaryBean.setBenBranchName(etBranchName.getText().toString());
                    beneficiaryBean.setBenMobileNumber(etBenefMobileNo.getText().toString());
                    beneficiaryBean.setBenIfscCode(etBenefIfscCode.getText().toString());
                    beneficiaryBean.setKYCType("MANUAL");
                    beneficiaryBean.setKYC("KYC");
                    beneficiaryBean.setState(stateName);
                    beneficiaryBean.setCity(cityID);
                    beneficiaryBean.setAddarVerificationCode("");
                    beneficiaryBean.setPincode(LocalPreferenceUtility.getUserPassCode(activity));
                    beneficiaryBean.setBenBankName(etBeneBankName.getText().toString());
                    beneficiaryBeenList.add(beneficiaryBean);
                    //Bind Beneficiary List
                  //  bindBeneFiciaryList(beneficiaryBeenList);
                 //   showProgressDialog();
                    MobicashIntentService.startActionAddBeneficiary(activity,beneficiaryBean);
                    layout.dismiss();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {
                    layout.dismiss();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });

        layout.show();
    }
    //Call Api City List
    private void callApiCityList(String stateID,final Context context) {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("stateId", stateID);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetworkConstant.USER_CLIENT_STATE_CITY_END_POINT ,jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("Getting Response", response.toString());
                    try {
                        if (response.getInt(ResponseAttributeConstants.STATUS) != 0)
                        {
                            JSONArray cityListArray = response.getJSONArray(ResponseAttributeConstants.STATE_LIST_VALUE);
                            int sizeOfCityListArray = cityListArray.length();
                            statecityList.clear();
                            sateCityResponseArrayList.clear();
                            for (int i = 0; i < sizeOfCityListArray; i++) {
                              CityResponse  cityResponse = new CityResponse();
                                JSONObject cityData = cityListArray.getJSONObject(i);
                                if(cityData.has(ResponseAttributeConstants.CITY_ID)){
                                    cityResponse.setId(cityData.getString(ResponseAttributeConstants.CITY_ID));
                                }
                                if(cityData.has(ResponseAttributeConstants.CITY_NAME)){
                                    cityResponse.setName(cityData.getString(ResponseAttributeConstants.CITY_NAME));
                                }
                               /* sateCityResponseArrayList.clear();*/
                                sateCityResponseArrayList.add(cityResponse);
                                sateCityResponseArrayList.size();
                                //statecityList.removeAll(sateCityResponseArrayList);
                            }
                            for(int j=0;j<sateCityResponseArrayList.size();j++) {
                                statecityList.add(sateCityResponseArrayList.get(j).getName());
                            }
                            //Bind City Adapter
                            bindSpinnerCityAdapter(statecityList);
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
    ///Bind City spinner Adapter
    private void bindSpinnerCityAdapter(ArrayList<String> sateCityResponseArrayList) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, sateCityResponseArrayList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinBankCity.setAdapter(spinnerArrayAdapter);
        spinBankCity.setPrompt("Select State");
    }

    ///Bind Adapter Beneficiary List
    private void bindBeneFiciaryList(ArrayList<BeneficiaryBean> beneficiaryBeenList) {
        BeneficiaryListAdapter adapter = new BeneficiaryListAdapter(getActivity(),beneficiaryBeenList,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        recyclerViewBenef.setLayoutManager(linearLayoutManager);
        recyclerViewBenef.setItemAnimator(new DefaultItemAnimator());
        recyclerViewBenef.setAdapter(adapter);
        cancelProgressDialog();
    }

    @Override
    public void seClickListener(BeneficiaryBean beneficiaryBean) {
        showPopupUpdateBene(getActivity(),beneficiaryBean);
    }
    //Update Beneficiary
    private void showPopupUpdateBene(final Activity activity,BeneficiaryBean beneficiaryBean)
    {
        final Dialog layout = new Dialog(activity);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layout.setContentView(R.layout.showpoup_update_beneficiary);
        // Set dialog title
        layout.setTitle("Success");
        Button addBeneficiary = (Button) layout.findViewById(R.id.add_beneficiary_ac);
        Button cancel = (Button) layout.findViewById(R.id.cancel);
        final EditText etBenefFirstName = (EditText) layout.findViewById(R.id.benef_first_name);
       // etBenefFirstName.setText(beneficiaryBean.getFirstname());
        final EditText etBenefLastName = (EditText) layout.findViewById(R.id.benef_last_name);
      //  etBenefLastName.setText(beneficiaryBean.getLastname());
        final EditText etBenefAccNo = (EditText) layout.findViewById(R.id.benef_account_number);
      //  etBenefAccNo.setText(beneficiaryBean.getAccountNumber());
        final EditText etBenefIfscCode = (EditText) layout.findViewById(R.id.benef_ifsc_code);
      //  etBenefIfscCode.setText(beneficiaryBean.getIfscCode());
        final EditText etBenefMobileNo = (EditText) layout.findViewById(R.id.benef_mobile_number);
     //   etBenefMobileNo.setText(beneficiaryBean.getBeneficiaryMobile());
        final EditText etBranchName = (EditText) layout.findViewById(R.id.bank_branch_name);
    //    etBranchName.setText(beneficiaryBean.getBranchName());
        final EditText etBankAddress = (EditText) layout.findViewById(R.id.bank_address);
      //  etBankAddress.setText(beneficiaryBean.getAddress());
        spinBankState = (Spinner) layout.findViewById(R.id.bank_state);
        final EditText etBankCity = (EditText) layout.findViewById(R.id.bank_city);
        layout.show();
        addBeneficiary.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {
                    if(etBenefFirstName.getText().toString().length()==0)
                    {
                        Toast.makeText(activity,"Enter Beneficiary First Name",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(etBenefLastName.getText().toString().length()==0)
                    {
                        Toast.makeText(activity,"Enter Beneficiary Last Name",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(etBenefAccNo.getText().toString().length()==0)
                    {
                        Toast.makeText(activity,"Enter Beneficiary Account Number",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(etBenefIfscCode.getText().toString().length()==0)
                    {
                        Toast.makeText(activity,"Enter Beneficiary IFSC Code",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(etBenefMobileNo.getText().toString().length()==0)
                    {
                        Toast.makeText(activity,"Enter Beneficiary Mobile Number",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(etBranchName.getText().toString().length()==0)
                    {
                        Toast.makeText(activity,"Enter Branch Name",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(etBankAddress.getText().toString().length()==0)
                    {
                        Toast.makeText(activity,"Enter Bank Address",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(etBankCity.getText().toString().length()==0)
                    {
                        Toast.makeText(activity,"Enter Bank City",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    layout.dismiss();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {
                    layout.dismiss();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });
    }

    /**
     * show text validation message.
     *
     * @param title
     * @param verificationFailedMessageString
     */
    private void showAlert(String title, String verificationFailedMessageString) {
        String message = verificationFailedMessageString;
        AlertDialog.Builder alertDialogBuilder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            alertDialogBuilder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            alertDialogBuilder = new AlertDialog.Builder(getActivity());
        }
        alertDialogBuilder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * show profile upload progress dialog
     */
    protected void showProgressDialog() {

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

    /**
     * cancel upload progress dialog
     */
    protected void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }
}
