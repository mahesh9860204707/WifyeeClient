package com.wifyee.greenfields.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.adapters.SplitListAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.models.requests.Participent;
import com.wifyee.greenfields.models.requests.SplitRequest;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.models.response.SplitListResponse;
import com.wifyee.greenfields.models.response.SplitResponse;
import com.wifyee.greenfields.services.MobicashIntentService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class SplitMoneyActivity extends BaseActivity implements View.OnClickListener
{

    @BindView(R.id.generate_split)
    ImageView generateImg;
    @BindView(R.id.recycler_view_split_list)
    RecyclerView recyclerView;
    private String[] counting={"Select Pay By user","2","3","4","5","6"};
    private Spinner payBySpinner;
    private int unique_number;
    private String refrence="";
    private String selectedValue;
    private Context mContext=null;
    private SplitRequest splitRequest;
    private ArrayList<Participent> participentRequestArray;
    private Participent participentRequest;
    private String strFirstEmail,strSecondEmail,strThirdEmail,strFourEmail,strFiveEmail,strSixEmail;
    private String strFirstMobileNumber,strSecondMobileNumber,strThirdMobileNumber,strFourMobileNumber,strFiveMobileNumber,strSixMobileNumber;
    private LinearLayout linearLayout;
    private AutoCompleteTextView et_first_mobile_number,et_second_mobile_number,et_third_mobile_number,et_nine_mobile_number,et_ten_mobile_number,
            et_four_mobile_number,et_five_mobile_number,et_six_mobile_number,et_seven_mobile_number,et_eight_mobile_number;

    private EditText et_first_email_id,et_second_email_id,et_third_email_id,et_four_email_id,et_five_email_id,et_six_email_id,
            et_seven_email_id,et_eight_email_id,et_nine_email_id,et_ten_email_id;
    private ArrayAdapter<String> adapter;
    // Store contacts values in these arraylist
    public static ArrayList<String> phoneValueArr = new ArrayList<String>();
    public static ArrayList<String> nameValueArr = new ArrayList<String>();
    private String userID="";
    private SplitListResponse splitListResponse;
    private ArrayList<SplitListResponse> splitListResponseArrayList=new ArrayList<>();
    private ImageButton back;

    /**
     * List of actions supported.
     */
    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_USER_SPLIT_FAIL,
            NetworkConstant.STATUS_USER_SPLIT_SUCCESS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_money);
        ButterKnife.bind(this);
        back= (ImageButton) findViewById(R.id.toolbar_back);
        mContext = this;
        generateImg.setOnClickListener(this);
        unique_number=MobicashUtils.generateTransactionId();
        refrence= String.valueOf(unique_number);
        userID=LocalPreferenceUtility.getUserCode(this);
        showProgressDialog();
        callApiSplitList(this,userID);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });
    }
   //Call Api List Split
    private void callApiSplitList(final Context context,String userID)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,NetworkConstant.USER_SPLIT_LIST_POINT+userID  ,null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Getting Response", response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                        JSONArray splitMoney = response.getJSONArray("splitMoneyRequestRecords");
                        // getting json objects from Ingredients json array
                        for(int j=0; j<splitMoney.length(); j++)
                        {
                            JSONObject json = splitMoney.getJSONObject(j);
                            String reference=json.getString("splitMoneyReferenceNumber");
                            String amount=json.getString("splitMoneyAmount");
                            String paidByUser=json.getString("splitMoneyNumberOfParticipents");
                            String status=json.getString("splitMoneyStatus");
                            String createdDate=json.getString("splitMoneyCreatedDate");
                            String splitID=json.getString("splitMoneyId");
                            splitListResponse=new SplitListResponse();
                            splitListResponse.setSplitMoneyAmount(amount);
                            splitListResponse.setSplitMoneyCreatedDate(createdDate);
                            splitListResponse.setSplitMoneyNumberOfParticipents(paidByUser);
                            splitListResponse.setSplitMoneyReferenceNumber(reference);
                            splitListResponse.setSplitMoneyStatus(status);
                            splitListResponse.setSplitMoneyId(splitID);
                            splitListResponseArrayList.add(splitListResponse);
                            //Toast.makeText(context, json.getString("splitMoneyAmount").toString(), Toast.LENGTH_LONG).show();
                        }
                        bindAdapterSplitList(splitListResponseArrayList);
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
                cancelProgressDialog();


            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                Log.d("params_SPlit", String.valueOf(params));
                cancelProgressDialog();
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setShouldCache(false);
    }
  //Bind Adapter
    private void bindAdapterSplitList(ArrayList<SplitListResponse> splitListResponseArrayList) {
        SplitListAdapter adapter = new SplitListAdapter(this,splitListResponseArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        cancelProgressDialog();
    }

    @Override
    public void onClick(View v) {
        if(v==generateImg)
        {
            showPopupGenerate(this);
        }
    }
    //Call Popup
    private void showPopupGenerate(final Activity activity) {
        final Dialog layout = new Dialog(activity);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layout.setContentView(R.layout.generate_split_money_popup);
        // Set dialog title
        layout.setTitle("Generate Split Invoice");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        // Read contact data and add data to ArrayAdapter
        // ArrayAdapter used by AutoCompleteTextView
        readContactData();
        Button generate = (Button) layout.findViewById(R.id.generate);
        Button cancel = (Button) layout.findViewById(R.id.cancel_action);
        final EditText amount= (EditText) layout.findViewById(R.id.amount);
        linearLayout= (LinearLayout) layout.findViewById(R.id.linear_add_layout);
        final TextView refrenceNum = (TextView) layout.findViewById(R.id.reference_number);
         et_first_mobile_number= (AutoCompleteTextView)layout.findViewById(R.id.et_first_mobile_number);
         et_second_mobile_number= (AutoCompleteTextView)layout.findViewById(R.id.et_second_mobile_number);
         et_third_mobile_number= (AutoCompleteTextView)layout.findViewById(R.id.et_third_mobile_number);
         et_four_mobile_number= (AutoCompleteTextView)layout.findViewById(R.id.et_four_mobile_number);
         et_five_mobile_number= (AutoCompleteTextView)layout.findViewById(R.id.et_five_mobile_number);
         et_six_mobile_number= (AutoCompleteTextView)layout.findViewById(R.id.et_six_mobile_number);
         et_seven_mobile_number= (AutoCompleteTextView)layout.findViewById(R.id.et_seven_mobile_number);
         et_eight_mobile_number= (AutoCompleteTextView)layout.findViewById(R.id.et_eight_mobile_number);
         et_nine_mobile_number= (AutoCompleteTextView)layout.findViewById(R.id.et_nine_mobile_number);
         et_ten_mobile_number= (AutoCompleteTextView)layout.findViewById(R.id.et_ten_mobile_number);

         et_first_email_id= (EditText)layout.findViewById(R.id.et_first_email_id);
         et_second_email_id= (EditText)layout.findViewById(R.id.et_second_email_id);
         et_third_email_id= (EditText)layout.findViewById(R.id.et_third_email_id);
         et_four_email_id= (EditText)layout.findViewById(R.id.et_four_email_id);
         et_five_email_id= (EditText)layout.findViewById(R.id.et_five_email_id);
         et_six_email_id= (EditText)layout.findViewById(R.id.et_six_email_id);
         et_seven_email_id= (EditText)layout.findViewById(R.id.et_seven_email_id);
         et_eight_email_id= (EditText)layout.findViewById(R.id.et_eight_email_id);
         et_nine_email_id= (EditText)layout.findViewById(R.id.et_nine_email_id);
         et_ten_email_id= (EditText)layout.findViewById(R.id.et_ten_email_id);

        refrenceNum.setText(refrence);
        payBySpinner= (Spinner) layout.findViewById(R.id.pay_by_no_user);
        bindArrayAdapter(counting);
        payBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedValue=parent.getSelectedItem().toString();
                bindEditTextRunTime(selectedValue);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        layout.show();
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        strFirstMobileNumber=et_first_mobile_number.getText().toString();
                        strSecondMobileNumber=et_second_mobile_number.getText().toString();
                        strThirdMobileNumber=et_third_mobile_number.getText().toString();
                        strFourMobileNumber=et_four_mobile_number.getText().toString();
                        strFiveMobileNumber=et_five_mobile_number.getText().toString();
                        strSixMobileNumber=et_six_mobile_number.getText().toString();

                        strFirstEmail=et_first_email_id.getText().toString();
                        strSecondEmail=et_second_email_id.getText().toString();
                        strThirdEmail=et_third_email_id.getText().toString();
                        strFourEmail=et_four_email_id.getText().toString();
                        strFiveEmail=et_five_email_id.getText().toString();
                        strSixEmail=et_six_email_id.getText().toString();

                    if(strFirstMobileNumber.length()==0&&strSecondMobileNumber.length()==0&&strThirdMobileNumber.length()==0
                            &&strFourMobileNumber.length()==0&&strFiveMobileNumber.length()==0&&strSixMobileNumber.length()==0)
                    {
                        Toast.makeText(activity, "Please fill Mobile Number", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(amount.getText().toString().length()==0)
                    {
                        Toast.makeText(activity, "Please fill Amount", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    splitRequest=new SplitRequest();
                    participentRequest=new Participent();
                    participentRequestArray=new ArrayList<Participent>();
                    splitRequest.setUserId(LocalPreferenceUtility.getUserCode(SplitMoneyActivity.this));
                    splitRequest.setNumberOfParticipents(selectedValue);
                    splitRequest.setReferenceNumber(refrenceNum.getText().toString());
                    splitRequest.setAmount(amount.getText().toString());
                    participentRequest.setParticipentMobile(strFirstMobileNumber);
                    participentRequestArray.add(participentRequest);
                    participentRequest=new Participent();
                    participentRequest.setParticipentMobile(strSecondMobileNumber);
                    participentRequestArray.add(participentRequest);
                    if(!strThirdMobileNumber.equals(""))
                    {
                        participentRequest.setParticipentMobile(strThirdMobileNumber);
                    }
                    if(!strFourMobileNumber.equals("")) {
                        participentRequest.setParticipentMobile(strFourMobileNumber);
                    }
                    if(!strFiveMobileNumber.equals("")) {
                        participentRequest.setParticipentMobile(strFiveMobileNumber);
                    }
                    if(!strSixMobileNumber.equals("")) {
                        participentRequest.setParticipentMobile(strSixMobileNumber);
                    }
                    participentRequest.setParticipentEmail(strFirstEmail);
                    participentRequest.setParticipentEmail(strSecondEmail);
                    if(!strThirdEmail.equals(""))
                    {
                        participentRequest.setParticipentEmail(strThirdEmail);
                    }
                    if(!strFourEmail.equals("")) {
                        participentRequest.setParticipentEmail(strFourEmail);
                    }
                    if(!strFiveEmail.equals("")) {
                        participentRequest.setParticipentEmail(strFiveEmail);
                    }
                    if(!strSixEmail.equals("")) {
                        participentRequest.setParticipentEmail(strSixEmail);
                    }
                   // participentRequestArray.add(participentRequest);
                    splitRequest.setParticipents(participentRequestArray);
                    MobicashIntentService.startActionUserSplitMoney(mContext, splitRequest);
                    overridePendingTransition(R.anim.enter_from_left,R.anim.enter_from_right);
                    layout.dismiss();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                 //   finish();
                    layout.dismiss();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
    }
    //Getting Contact List
    private void readContactData()
    {
        try {
            /*********** Reading Contacts Name And Number **********/

            String phoneNumber = "";
            ContentResolver cr = getBaseContext()
                    .getContentResolver();

            //Query to get contact name

            Cursor cur = cr
                    .query(ContactsContract.Contacts.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

            // If data data found in contacts
            if (cur.getCount() > 0) {

                Log.i("AutocompleteContacts", "Reading   contacts........");

                int k = 0;
                String name = "";

                while (cur.moveToNext()) {

                    String id = cur
                            .getString(cur
                                    .getColumnIndex(ContactsContract.Contacts._ID));
                    name = cur
                            .getString(cur
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    //Check contact have phone number
                    if (Integer
                            .parseInt(cur
                                    .getString(cur
                                            .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                        //Create query to get phone number by contact id
                        Cursor pCur = cr
                                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                + " = ?",
                                        new String[]{id},
                                        null);
                        int j = 0;

                        while (pCur
                                .moveToNext()) {
                            // Sometimes get multiple data
                            if (j == 0) {
                                // Get Phone number
                                phoneNumber = "" + pCur.getString(pCur
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                // Add contacts names to adapter
                                adapter.add(phoneNumber);
                                // Add ArrayList names to adapter
                                phoneValueArr.add(phoneNumber.toString());
                                nameValueArr.add(name.toString());

                                j++;
                                k++;
                            }
                        }  // End while loop
                        pCur.close();
                    } // End if

                }  // End while loop

            } // End Cursor value check
            cur.close();


        } catch (Exception e) {
            Log.i("AutocompleteContacts", "Exception : " + e);
        }


    }

    private void bindEditTextRunTime(String selectedValue)
    {

        if(selectedValue.equals("2"))
        {
            linearLayout.setVisibility(View.VISIBLE);
            et_first_mobile_number.setVisibility(View.VISIBLE);
            et_first_email_id.setVisibility(View.VISIBLE);
            et_second_email_id.setVisibility(View.VISIBLE);
            et_second_mobile_number.setVisibility(View.VISIBLE);
            et_third_email_id.setVisibility(View.GONE);
            et_third_mobile_number.setVisibility(View.GONE);
            et_four_email_id.setVisibility(View.GONE);
            et_four_mobile_number.setVisibility(View.GONE);
            et_five_email_id.setVisibility(View.GONE);
            et_five_mobile_number.setVisibility(View.GONE);
            et_six_email_id.setVisibility(View.GONE);
            et_six_mobile_number.setVisibility(View.GONE);
            et_first_mobile_number.setThreshold(1);
            et_second_mobile_number.setThreshold(1);
           /* et_third_mobile_number.setThreshold(1);
            et_four_mobile_number.setThreshold(1);
            et_five_mobile_number.setThreshold(1);
            et_six_mobile_number.setThreshold(1);
            et_seven_mobile_number.setThreshold(1);
            et_eight_mobile_number.setThreshold(1);
            et_nine_mobile_number.setThreshold(1);
            et_ten_mobile_number.setThreshold(1);*/
            //Set adapter to AutoCompleteTextView
            et_first_mobile_number.setAdapter(adapter);
            et_second_mobile_number.setAdapter(adapter);
           /* et_third_mobile_number.setAdapter(adapter);
            et_four_mobile_number.setAdapter(adapter);
            et_five_mobile_number.setAdapter(adapter);
            et_six_mobile_number.setAdapter(adapter);
            et_seven_mobile_number.setAdapter(adapter);
            et_eight_mobile_number.setAdapter(adapter);
            et_nine_mobile_number.setAdapter(adapter);
            et_ten_mobile_number.setAdapter(adapter);*/

        }
        else if(selectedValue.equals("3"))
        {
            linearLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            et_first_mobile_number.setVisibility(View.VISIBLE);
            et_first_email_id.setVisibility(View.VISIBLE);
            et_second_email_id.setVisibility(View.VISIBLE);
            et_second_mobile_number.setVisibility(View.VISIBLE);
            et_third_email_id.setVisibility(View.VISIBLE);
            et_third_mobile_number.setVisibility(View.VISIBLE);
            et_four_email_id.setVisibility(View.GONE);
            et_four_mobile_number.setVisibility(View.GONE);
            et_five_email_id.setVisibility(View.GONE);
            et_five_mobile_number.setVisibility(View.GONE);
            et_six_email_id.setVisibility(View.GONE);
            et_six_mobile_number.setVisibility(View.GONE);
            et_first_mobile_number.setThreshold(1);
            et_second_mobile_number.setThreshold(1);
            et_third_mobile_number.setThreshold(1);
            et_first_mobile_number.setAdapter(adapter);
            et_second_mobile_number.setAdapter(adapter);
            et_third_mobile_number.setAdapter(adapter);
        }
        else if(selectedValue.equals("4"))
        {
            linearLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            et_first_mobile_number.setVisibility(View.VISIBLE);
            et_first_email_id.setVisibility(View.VISIBLE);
            et_second_email_id.setVisibility(View.VISIBLE);
            et_second_mobile_number.setVisibility(View.VISIBLE);
            et_third_email_id.setVisibility(View.VISIBLE);
            et_third_mobile_number.setVisibility(View.VISIBLE);
            et_four_email_id.setVisibility(View.VISIBLE);
            et_four_mobile_number.setVisibility(View.VISIBLE);
            et_five_email_id.setVisibility(View.GONE);
            et_five_mobile_number.setVisibility(View.GONE);
            et_six_email_id.setVisibility(View.GONE);
            et_six_mobile_number.setVisibility(View.GONE);
            et_first_mobile_number.setThreshold(1);
            et_second_mobile_number.setThreshold(1);
            et_third_mobile_number.setThreshold(1);
            et_four_mobile_number.setThreshold(1);
            et_first_mobile_number.setAdapter(adapter);
            et_second_mobile_number.setAdapter(adapter);
            et_third_mobile_number.setAdapter(adapter);
            et_four_mobile_number.setAdapter(adapter);
        }
        else if(selectedValue.equals("5"))
        {
            linearLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            et_first_mobile_number.setVisibility(View.VISIBLE);
            et_first_email_id.setVisibility(View.VISIBLE);
            et_second_email_id.setVisibility(View.VISIBLE);
            et_second_mobile_number.setVisibility(View.VISIBLE);
            et_third_email_id.setVisibility(View.VISIBLE);
            et_third_mobile_number.setVisibility(View.VISIBLE);
            et_four_email_id.setVisibility(View.VISIBLE);
            et_four_mobile_number.setVisibility(View.VISIBLE);
            et_five_email_id.setVisibility(View.VISIBLE);
            et_five_mobile_number.setVisibility(View.VISIBLE);
            et_six_email_id.setVisibility(View.GONE);
            et_six_mobile_number.setVisibility(View.GONE);
            et_first_mobile_number.setThreshold(1);
            et_second_mobile_number.setThreshold(1);
            et_third_mobile_number.setThreshold(1);
            et_four_mobile_number.setThreshold(1);
            et_five_mobile_number.setThreshold(1);
            et_first_mobile_number.setAdapter(adapter);
            et_second_mobile_number.setAdapter(adapter);
            et_third_mobile_number.setAdapter(adapter);
            et_four_mobile_number.setAdapter(adapter);
            et_five_mobile_number.setAdapter(adapter);
        }
        else if(selectedValue.equals("6"))
        {
            linearLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            et_first_mobile_number.setVisibility(View.VISIBLE);
            et_first_email_id.setVisibility(View.VISIBLE);
            et_second_email_id.setVisibility(View.VISIBLE);
            et_second_mobile_number.setVisibility(View.VISIBLE);
            et_third_email_id.setVisibility(View.VISIBLE);
            et_third_mobile_number.setVisibility(View.VISIBLE);
            et_four_email_id.setVisibility(View.VISIBLE);
            et_four_mobile_number.setVisibility(View.VISIBLE);
            et_five_email_id.setVisibility(View.VISIBLE);
            et_five_mobile_number.setVisibility(View.VISIBLE);
            et_six_email_id.setVisibility(View.VISIBLE);
            et_six_mobile_number.setVisibility(View.VISIBLE);
            et_first_mobile_number.setThreshold(1);
            et_second_mobile_number.setThreshold(1);
            et_third_mobile_number.setThreshold(1);
            et_four_mobile_number.setThreshold(1);
            et_five_mobile_number.setThreshold(1);
            et_six_mobile_number.setThreshold(1);
            et_first_mobile_number.setAdapter(adapter);
            et_second_mobile_number.setAdapter(adapter);
            et_third_mobile_number.setAdapter(adapter);
            et_four_mobile_number.setAdapter(adapter);
            et_five_mobile_number.setAdapter(adapter);
            et_six_mobile_number.setAdapter(adapter);
        }

    }

    private void bindArrayAdapter(String[] counting) {

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, counting);
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        payBySpinner.setAdapter(spinnerArrayAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(splitStatusReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(splitStatusReceiver, new IntentFilter(action));
        }

    }

    /**
     * Handling broadcast event for user login .
     */
    private BroadcastReceiver splitStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            cancelProgressDialog();
            try {
                if (action.equals(NetworkConstant.STATUS_USER_SPLIT_SUCCESS)) {

                    showSuccessDialog("Success Split Money");
                    SplitResponse mSplitResponse = (SplitResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    Timber.d("STATUS_USER_SPLIT_SUCCESS = > SPLIT Response  ==>" + new Gson().toJson(mSplitResponse));
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                    finish();

                } else if (action.equals(NetworkConstant.STATUS_USER_SPLIT_FAIL)) {

                    FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if (failureResponse != null) {
                        Timber.d("STATUS_USER_SPLIT_FAIL = > failureResponse  ==>" + new Gson().toJson(failureResponse));
                    }
                    if (failureResponse != null && failureResponse.msg != null) {
                        showErrorDialog(failureResponse.msg);
                    } else {
                        String errorMessage = getString(R.string.error_message);
                        showErrorDialog(errorMessage);
                    }

                }
            } catch (Exception e) {
                Timber.e(" Exception caught in SPLITStatusReceiver " + e.getMessage());
            }
        }
    };


    private void showSuccessDialog(final String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder
                .setTitle("Success")
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
    }
}
