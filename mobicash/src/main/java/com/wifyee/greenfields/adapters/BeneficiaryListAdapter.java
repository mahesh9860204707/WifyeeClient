package com.wifyee.greenfields.adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.wifyee.greenfields.activity.DeleteBeneficiaryOTPActivity;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.interfaces.OnClickListenerAddBeneficiary;
import com.wifyee.greenfields.models.requests.BeneficiaryBean;
import com.wifyee.greenfields.models.response.SplitListResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Amit on 27-12-2016.
 */

public class BeneficiaryListAdapter extends RecyclerView.Adapter<BeneficiaryListAdapter.LogItemViewHolder> {

    private List<BeneficiaryBean> mBeneficiaryBeanList;
    private final LayoutInflater mLayoutInflater;
    private Context mContext;
    public OnClickListenerAddBeneficiary ClickListener;
    private ProgressDialog progressDialog;
    private String beneID="";
    private String accountNumber="";

    public BeneficiaryListAdapter(Context context, List<BeneficiaryBean> mBeneficiarybeanList,OnClickListenerAddBeneficiary listener) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mBeneficiaryBeanList = mBeneficiarybeanList;
        this.ClickListener = listener;
    }

    @Override
    public LogItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.beneficiary_list_item, parent, false);
        return new LogItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LogItemViewHolder holder, final int position)
    {
        final BeneficiaryBean beneficiaryItem = mBeneficiaryBeanList.get(position);
        String accNumber="";
        accNumber=beneficiaryItem.getBenAccountNumber();
        removeMiddleChars(accNumber);
       /* StringBuilder builder=new StringBuilder(accNumber.length());
        for(int i=0;i<accNumber.length();i++) {
            builder.append("*");
        }*/
        holder.mName.setText(beneficiaryItem.getBenFirstName());
        holder.mNickName.setText(beneficiaryItem.getBenLastName());
        holder.mAccountNumber.setText(accountNumber);
        holder.mBankName.setText(beneficiaryItem.getBenBankName());
        holder.mMobile.setText(beneficiaryItem.getBenMobileNumber());
        holder.mIfscCode.setText(beneficiaryItem.getBenIfscCode());
        holder.mSerialNumber.setText(String.valueOf(position));
     /* holder.mUpdateBenef.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(ClickListener!=null)
              {
                ClickListener.seClickListener(mBeneficiaryBeanList.get(position));
              }
          }
      });*/
        holder.mDeleteBene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showPopUpDeleteBene(mContext,mBeneficiaryBeanList.get(position).getUser_id(),mBeneficiaryBeanList.get(position).getBeneficiary_id());
            }
        });
    }

    //remove middle character
    public String removeMiddleChars(String string) {
        int word = string.length();
        String start = string.substring(0, word / 2);
        String end = string.substring((word / 2) + 1 , string.length());
        accountNumber=start + end;
        return start + end;

    }
    //Delete Beneficiary
    private void showPopUpDeleteBene(final Context context, final String user_Id, final String beneID)
    {
        final Dialog layout = new Dialog(context);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layout.setContentView(R.layout.showpopup_delete_bene);
        // Set dialog title
        layout.setTitle("Delete Beneficiary");
        Button yes = (Button) layout.findViewById(R.id.yes);
        Button no = (Button) layout.findViewById(R.id.no);
        layout.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showProgressDialog();
                    callOTPApi(context,beneID);
                    layout.dismiss();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
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
   //Call otp Api
    private void callOTPApi(final Context context, final String beneID)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", LocalPreferenceUtility.getUserCode(context));
            jsonObject.put("userType","Client");
            jsonObject.put("agentCode","RS00789");
            jsonObject.put("customerMobile",LocalPreferenceUtility.getUserMobileNumber(context));
            jsonObject.put("requestFor","BENDELETE");

            StringBuilder sb = new StringBuilder(LocalPreferenceUtility.getUserCode(context));
            sb.append("Client");
            sb.append("RS00789");
            sb.append(LocalPreferenceUtility.getUserMobileNumber(context));
            sb.append("BENDELETE");
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
                    if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                        String  requestCode = response.getString("responseCode");
                        callActivityVerifyOtp(context,requestCode,beneID);
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
        context.startActivity(new Intent(context, DeleteBeneficiaryOTPActivity.class).putExtra("requestCode",code).putExtra("benId",benId));
    }

    @Override
    public int getItemCount() {
        return (mBeneficiaryBeanList != null) ? mBeneficiaryBeanList.size() : 0;
    }

    public void cleartLogItemCollection() {
        mBeneficiaryBeanList.clear();
    }

    public void setLogItemCollection(List<BeneficiaryBean> benItemCollection) {
        for (BeneficiaryBean benItem : benItemCollection) {
            mBeneficiaryBeanList.add(0, benItem);
        }
        notifyDataSetChanged();
    }

    public class LogItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.serial_number)
        public TextView mSerialNumber;

        @BindView(R.id.name)
        public TextView mName;

        @BindView(R.id.nick_name)
        public TextView mNickName;

        @BindView(R.id.mobile)
        public TextView mMobile;

        @BindView(R.id.bank_name)
        public TextView mBankName;

        @BindView(R.id.account_number)
        public TextView mAccountNumber;

        @BindView(R.id.ifsc_code)
        public TextView mIfscCode;

        @BindView(R.id.status)
        public TextView mStatus;

        @BindView(R.id.is_verified)
        public TextView mIsVerified;

       /* @BindView(R.id.edit_beneficiary)
        public ImageView mUpdateBenef;
*/
        @BindView(R.id.delete_beneficiary)
        public ImageView mDeleteBene;

        public LogItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * show profile upload progress dialog
     */
    protected void showProgressDialog() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            progressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            progressDialog = new ProgressDialog(mContext);
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


