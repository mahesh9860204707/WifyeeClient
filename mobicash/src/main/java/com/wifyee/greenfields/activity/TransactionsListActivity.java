package com.wifyee.greenfields.activity;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.adapters.BeneficiaryListAdapter;
import com.wifyee.greenfields.adapters.TransactionListAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.models.requests.BeneficiaryBean;
import com.wifyee.greenfields.models.requests.TransactionBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TransactionsListActivity extends BaseActivity implements View.OnClickListener
{
    private EditText et_from_date,et_to_date;
    private RecyclerView recyclerViewTransaction;
    private Button submitTransaction;
    private int mYear, mMonth, mDay;
    private int flag=0;
    private TransactionBean transactionBean;
    private ArrayList<TransactionBean> transactionBeanArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions_list);
        bindUI();
    }

    private void bindUI()
    {
        et_from_date=(EditText)findViewById(R.id.et_from_date);
        et_to_date=(EditText)findViewById(R.id.et_to_date);
        recyclerViewTransaction=(RecyclerView) findViewById(R.id.recyclerView_trans);
        submitTransaction=(Button) findViewById(R.id.btn_submit);
        submitTransaction.setOnClickListener(this);
        et_from_date.setOnClickListener(this);
        et_to_date.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if(v==submitTransaction) {
            showProgressDialog();
            callTransactionListApi(this,et_from_date.getText().toString(),et_to_date.getText().toString());
        }else if(v==et_from_date)
        {
            flag=0;
            showDatePicker(flag);
        }else if(v==et_to_date)
        {
            flag=1;
            showDatePicker(flag);
        }

    }
//Call Transaction List api
    private void callTransactionListApi(final TransactionsListActivity context, String fromdate, String toDate)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("agentCode","RS00789");
            jsonObject.put("userId", LocalPreferenceUtility.getUserCode(context));
            jsonObject.put("userType","Client");
            jsonObject.put("userMobile",LocalPreferenceUtility.getUserMobileNumber(context));
            jsonObject.put("fromDate",fromdate);
            jsonObject.put("toDate",toDate);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, NetworkConstant.USER_TRANSACTIONS_LIST_END_POINT ,jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Getting Response", response.toString());
                try {
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                        JSONArray jsonArray=response.getJSONArray("data");
                        for(int i=0;i<jsonArray.length();i++) {
                            transactionBean=new TransactionBean();
                            JSONObject benJsonObject=jsonArray.getJSONObject(i);
                            String transactionId=benJsonObject.getString("TXN_ID");
                            String transactionDate=benJsonObject.getString("TRANSACTION_DATE");
                            String transactionStatus=benJsonObject.getString("TRANSACTION_STATUS");
                            String transactionAmount=benJsonObject.getString("TRANSFER_AMOUNT");
                            String benBeneficiaryMobile=benJsonObject.getString("BENE_MOBILENO");
                            String transactionAccount=benJsonObject.getString("BANK_ACCOUNTNO");
                            String transactionIFSCCode=benJsonObject.getString("BANKIFSC_CODE");
                            String transactionReference=benJsonObject.getString("CUSTOMER_REFERENCE_NO");

                            transactionBean.setACCOUNT_NUMBER(transactionAccount);
                            transactionBean.setAMOUNT(transactionAmount);
                            transactionBean.setBENEFICIARY_MOBILE(benBeneficiaryMobile);
                            transactionBean.setIFSC_CODE(transactionIFSCCode);
                            transactionBean.setREFERENCE_NO(transactionReference);
                            transactionBean.setTXN_ID(transactionId);
                            transactionBean.setTRANSACTION_DATE(transactionDate);
                            transactionBean.setSTATUS(transactionStatus);
                            transactionBeanArrayList.add(transactionBean);
                            cancelProgressDialog();
                        }
                        bindListAdapter(transactionBeanArrayList);
                    }
                    else {
                        cancelProgressDialog();
                        Toast.makeText(context, "Some Issue in Getting Transaction List", Toast.LENGTH_SHORT).show();
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
  //Binding Recycler view
    private void bindListAdapter(ArrayList<TransactionBean> transactionBeanArrayList)
    {
        TransactionListAdapter adapter = new TransactionListAdapter(this,transactionBeanArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        recyclerViewTransaction.setLayoutManager(linearLayoutManager);
        recyclerViewTransaction.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTransaction.setAdapter(adapter);
        cancelProgressDialog();
    }

    //Set Date Picker
    private void showDatePicker(final int flag) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                        Date chosenDate = cal.getTime();
                        SimpleDateFormat format=new SimpleDateFormat("dd-MM-yyyy");
                        String datetime = format.format(chosenDate);
                        if(flag==0) {
                            et_from_date.setText(datetime);
                        }
                        else if(flag==1) {
                            et_to_date.setText(datetime);
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
