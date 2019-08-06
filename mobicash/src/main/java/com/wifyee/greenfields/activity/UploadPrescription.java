package com.wifyee.greenfields.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.PhotoPopupWindow;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.dairyorder.DairyItemListActivity;
import com.wifyee.greenfields.dairyorder.DairyMerchantListModel;
import com.wifyee.greenfields.dairyorder.DairyNetworkConstant;
import com.wifyee.greenfields.dairyorder.JSONParser;
import com.wifyee.greenfields.foodorder.AddressRequest;
import com.wifyee.greenfields.mapper.ModelMapper;
import com.wifyee.greenfields.models.response.FailureResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import timber.log.Timber;

public class UploadPrescription extends AppCompatActivity {

    private RelativeLayout rl_camera,rl_gallery,rl_remove,rl_upload_prescription;
    private Button submit;
    public final int REQUEST_CAMERA = 0;
    public final int SELECT_FILE = 1;
    private ImageView img,icNotHere,icCamera,icGallery;
    private Bitmap bitmap;
    private String userID="",longitude="",latitude="";
    private SweetAlertDialog pDialog;
    private Toolbar mToolbar;
    private CardView cardView;
    private TextView txtNotHere,txtDetailNotHere,medicalName,medicalAddress,txt_prescription_upload;
    private LinearLayout llPartner;
    private CircleImageView medicalImage;
    private String currentStatus,merIdentyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_medicine_preciption);

        rl_camera = findViewById(R.id.rl_camera);
        rl_gallery = findViewById(R.id.rl_gallery);
        img = findViewById(R.id.ic_image_medicine);
        submit = findViewById(R.id.submit_medicine);
        cardView = findViewById(R.id.cardView);
        icNotHere = findViewById(R.id.ic_not_here);
        icCamera = findViewById(R.id.ic_camera);
        icGallery = findViewById(R.id.ic_gallery);
        txtNotHere = findViewById(R.id.txt_we_are_not);
        txtDetailNotHere = findViewById(R.id.txt_detail_we_are_not);
        medicalName = findViewById(R.id.medical_name);
        medicalAddress = findViewById(R.id.medical_address);
        rl_remove = findViewById(R.id.rl_remove);
        rl_upload_prescription = findViewById(R.id.rl_upload_prescription);
        llPartner = findViewById(R.id.ll_partner);
        medicalImage = findViewById(R.id.medical_image);
        TextView txtUpload = findViewById(R.id.txt_upload);
        TextView txtPlease = findViewById(R.id.txt_please);
        TextView txtCamera = findViewById(R.id.txt_camera);
        TextView txtGallery = findViewById(R.id.txt_gallery);
        TextView txtRemove = findViewById(R.id.title);
        txt_prescription_upload = findViewById(R.id.txt_prescription_upload);
        TextView txtMedicalPartner = findViewById(R.id.txt_medical_partner);

        userID= LocalPreferenceUtility.getUserCode(UploadPrescription.this);
        latitude = LocalPreferenceUtility.getLatitude(UploadPrescription.this);
        longitude = LocalPreferenceUtility.getLongitude(UploadPrescription.this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbarTitle = mToolbar.findViewById(R.id.toolbar_title);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.secondaryPrimary), PorterDuff.Mode.SRC_ATOP);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });
        }

        toolbarTitle.setTypeface(Fonts.getSemiBold(this));
        txtNotHere.setTypeface(Fonts.getSemiBold(this));
        txtDetailNotHere.setTypeface(Fonts.getRegular(this));
        txtUpload.setTypeface(Fonts.getSemiBold(this));
        txtPlease.setTypeface(Fonts.getRegular(this));
        txtCamera.setTypeface(Fonts.getRegular(this));
        txtGallery.setTypeface(Fonts.getRegular(this));
        txtRemove.setTypeface(Fonts.getRegular(this));
        txt_prescription_upload.setTypeface(Fonts.getRegular(this));
        txtMedicalPartner.setTypeface(Fonts.getSemiBold(this));
        medicalName.setTypeface(Fonts.getSemiBold(this));
        medicalAddress.setTypeface(Fonts.getRegular(this));
        submit.setTypeface(Fonts.getSemiBold(this));

        rl_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });

        rl_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
            }
        });

        medicalList();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PhotoPopupWindow(UploadPrescription.this,view,"",bitmap);

            }
        });

        rl_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmap = null;
                img.setImageBitmap(null);
                rl_upload_prescription.setVisibility(View.GONE);
                submit.setBackgroundColor(getResources().getColor(R.color.log_gray_text_color));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmap!=null){
                    pDialog = new SweetAlertDialog(UploadPrescription.this, SweetAlertDialog.PROGRESS_TYPE)
                            .setTitleText("Please wait...");
                    pDialog.show();
                    pDialog.setCancelable(false);
                    Log.e("URL",NetworkConstant.USER_MEDICINE_UPLOAD_END_POINT);

                    VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                            NetworkConstant.USER_MEDICINE_UPLOAD_END_POINT,
                            new Response.Listener<NetworkResponse>() {
                                @Override
                                public void onResponse(NetworkResponse response) {
                                    String resultResponse = new String(response.data);
                                    Log.e("Response", resultResponse);
                                    try {
                                        JSONObject object = new JSONObject(resultResponse);
                                        if (object.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                            pDialog.setTitleText("Success!")
                                                    .setContentText(object.getString(ResponseAttributeConstants.MSG))
                                                    .setConfirmText("OK")
                                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    bitmap = null;
                                                    img.setImageBitmap(null);
                                                    rl_upload_prescription.setVisibility(View.GONE);
                                                    submit.setBackgroundColor(getResources().getColor(R.color.log_gray_text_color));
                                                    onPause();
                                                }
                                            });

                                        } else {
                                            pDialog.setTitleText("Failed! Please Try Again.")
                                                    .setContentText(object.getString(ResponseAttributeConstants.MSG))
                                                    .setConfirmText("OK")
                                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e(getClass().getName(),error.toString());
                                    pDialog.setTitleText("Error!")
                                            .setContentText(error.toString())
                                            .setConfirmText("OK")
                                            .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                }
                            }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("userId", userID);
                            params.put("userType", "client");
                            params.put("lotLoc", latitude);
                            params.put("longLoc", longitude);
                            params.put("mer_idt_id", merIdentyId);

                            return params;
                        }

                        @Override
                        protected Map<String, DataPart> getByteData() {
                            Map<String, DataPart> params = new HashMap<>();
                            params.put("priscriptionDocument", new DataPart(String.valueOf(System.currentTimeMillis()) + ".jpeg",
                                        getFileDataFromDrawable(bitmap)));
                            return params;
                        }
                        /*@Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "charset=utf-8");
                            return headers;
                        }*/
                    };

                    Volley.newRequestQueue(getApplicationContext()).add(volleyMultipartRequest);
                    int socketTimeout = 50000;
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    volleyMultipartRequest.setRetryPolicy(policy);
                }else {
                    Toast.makeText(getApplicationContext(),"Please upload prescription",Toast.LENGTH_LONG).show();
                }
            }
        });

        TransitionManager.beginDelayedTransition(llPartner);
    }

    private void medicalList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ResponseAttributeConstants.MERCHANT_TYPE, "7");
            jsonObject.put(ResponseAttributeConstants.LATITUDE, latitude);
            jsonObject.put(ResponseAttributeConstants.LONGITUDE, longitude);
            jsonObject.put(ResponseAttributeConstants.ZIP_CODE, LocalPreferenceUtility.getCurrentPincode(UploadPrescription.this));

            Log.e("merchantList",jsonObject.toString());
        } catch (JSONException e) {
        }

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                . writeTimeout(5, TimeUnit.MINUTES)
                .build();

        AndroidNetworking.post(DairyNetworkConstant.BASE_URL_DAIRY + DairyNetworkConstant.REQUEST_PARAM_MAIN_LIST)
                .addJSONObjectBody(jsonObject)
                .setOkHttpClient(okHttpClient)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("=Medicine-Merchant=",response.toString());
                        try {
                            if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                //ArrayList<DairyMerchantListModel> item = JSONParser.getDairyMerchantList(response);
                                JSONArray itemArray = response.getJSONArray(DairyNetworkConstant.DATA);
                                if (itemArray.length()>0){
                                    icNotHere.setVisibility(View.GONE);
                                    txtNotHere.setVisibility(View.GONE);
                                    txtDetailNotHere.setVisibility(View.GONE);
                                    cardView.setVisibility(View.VISIBLE);
                                    submit.setVisibility(View.VISIBLE);
                                    llPartner.setVisibility(View.VISIBLE);

                                    int length = itemArray.length();
                                    for(int i=0;i<length;i++){
                                        JSONObject dataObject = itemArray.getJSONObject(0);
                                        merIdentyId = dataObject.getString(DairyNetworkConstant.MER_IDT_ID);
                                        if(dataObject.has(DairyNetworkConstant.MER_COMPANY)){
                                            medicalName.setText(dataObject.getString(DairyNetworkConstant.MER_COMPANY));
                                        }
                                        if(dataObject.has(DairyNetworkConstant.MER_IDT_ADDRESS)){
                                            medicalAddress.setText(dataObject.getString(DairyNetworkConstant.MER_IDT_ADDRESS));
                                        }
                                        if(dataObject.has(DairyNetworkConstant.MER_IMAGE)){
                                            RequestOptions options = new RequestOptions()
                                                    .centerCrop()
                                                    .placeholder(R.drawable.ic_medical_plus)
                                                    .error(R.drawable.ic_medical_plus)
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL);

                                            Glide.with(UploadPrescription.this).load(dataObject.getString(DairyNetworkConstant.MER_IMAGE))
                                                    .apply(options)
                                                    .into(medicalImage);

                                            /*Glide.with(UploadPrescription.this).asBitmap()
                                                    .load(dataObject.getString(DairyNetworkConstant.MER_IMAGE))
                                                    .listener(new RequestListener<Bitmap>() {
                                                        @Override
                                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                                            medicalImage.setBackgroundResource(R.drawable.ic_medical_plus);
                                                            return false;
                                                        }

                                                        @Override
                                                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                                            medicalImage.setImageBitmap(resource);
                                                            return false;
                                                        }
                                                    })
                                                    .into(medicalImage);*/
                                        }
                                        if(dataObject.has(DairyNetworkConstant.MER_CURRENT_STATUS)){
                                            currentStatus = dataObject.getString(DairyNetworkConstant.MER_CURRENT_STATUS);
                                            if(currentStatus.equals("0")) {
                                                submit.setText("Currently Unavailable");
                                                rl_camera.setEnabled(false);
                                                rl_gallery.setEnabled(false);
                                                rl_camera.setAlpha(0.5f);
                                                rl_gallery.setAlpha(0.5f);
                                                /*Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Currently Unavailable", Snackbar.LENGTH_INDEFINITE);
                                                View snackbarView = snack.getView();
                                                snackbarView.setBackgroundColor(Color.parseColor("#CC00CC"));
                                                snack.show();*/
                                            }
                                        }
                                    }

                                }else {
                                    icNotHere.setVisibility(View.VISIBLE);
                                    txtNotHere.setVisibility(View.VISIBLE);
                                    txtDetailNotHere.setVisibility(View.VISIBLE);
                                    cardView.setVisibility(View.GONE);
                                    submit.setVisibility(View.GONE);
                                    llPartner.setVisibility(View.GONE);
                                }

                            } else {
                                Timber.d("Got failure in SignupResponse...");
                                FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(response.toString());
                                Timber.w("handleActionUserSignUp = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));

                            }
                        } catch (JSONException e) {
                            Timber.e("JSONException Caught.  Message : " + e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Timber.e("called onError of User dairy order API.");
                        Timber.e("Error Message : " + error.getMessage());
                        Timber.e("Error code : " + error.getErrorCode());
                        Timber.e("Error Body : " + error.getErrorBody());
                        Timber.e("Error Detail : " + error.getErrorDetail());
                        FailureResponse mFailureResponse = ModelMapper.transformErrorResponseToFailureResponse(error.getErrorBody());
                        Timber.w("handleActionUserSignUp = > FailureResponse  ==>" + new Gson().toJson(mFailureResponse));
                    }
                });

    }



    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult", "requestCode " + requestCode + ", resultCode " + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                bitmap = (Bitmap) data.getExtras().get("data");
                img.setImageBitmap(bitmap);
                rl_upload_prescription.setVisibility(View.VISIBLE);
                submit.setBackgroundColor(getResources().getColor(R.color.secondaryPrimary));
            }
            else if(requestCode == SELECT_FILE && data != null && data.getData() != null){
                Uri imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    img.setImageBitmap(bitmap);
                    rl_upload_prescription.setVisibility(View.VISIBLE);
                    submit.setBackgroundColor(getResources().getColor(R.color.secondaryPrimary));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if ( pDialog!=null && pDialog.isShowing() ){
            pDialog.dismiss();
        }
    }
}
