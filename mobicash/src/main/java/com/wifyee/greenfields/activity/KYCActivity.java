package com.wifyee.greenfields.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.adapters.KycListAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.interfaces.OnClickListenerUpdateKYC;
import com.wifyee.greenfields.models.Example;
import com.wifyee.greenfields.models.requests.KYCBean;
import com.wifyee.greenfields.models.requests.KYCDocumentsBean;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.services.MobicashIntentService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class KYCActivity extends BaseActivity implements View.OnClickListener,OnClickListenerUpdateKYC
{
    private RecyclerView kycRecyclerView;
    private ArrayList<KYCDocumentsBean> kycDocumentsBeanArrayList=new ArrayList<>();
    private KYCDocumentsBean kycDocumentsBean;
    private LinearLayout layoutKyc;
    private int PICK_IMAGE_REQUEST = 1;
    private String strIdProofSelected="";
    private String strAddressProofSelected="";
    private int flag=0;
    private ImageView imageIdProofExt,imageAddressProofExt;
    private String imagePath="";
    private File fileIdImage,fileAddressImage;
    private String idProofArray[]={"SELECT ID","AADHAR CARD","PASSPORT","DRIVING LICENSE","VOTER ID CARD","PAN CARD"};
    private String addressProofArray[]={"SELECT ADDRESS PROOF","AADHAR CARD","RATION CARD","DRIVING LICENSE",
            "VOTER ID CARD","RENT AGREEMENT","SHOP LANDLINE BILL","SHOP ELECTRIC BILL(NOT MORE THAN 2 MONTHS OLD)"};
    /**
     * List of actions supported.
     */
    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_UPLOAD_ADD_KYC_SUCCESS,
            NetworkConstant.STATUS_UPLOAD_CLIENT_ADD_KYC_FAIL
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc);
        bindUi();
    }
    private void bindUi() {
        kycRecyclerView=(RecyclerView)findViewById(R.id.recycler_kyc);
        layoutKyc=(LinearLayout) findViewById(R.id.add_kyc);
        layoutKyc.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        showPopupAddKyc(this);
    }
    //Show Popup Add Kyc
    private void showPopupAddKyc(final KYCActivity activity) {
        final Dialog layout = new Dialog(activity);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layout.setContentView(R.layout.showpoup_add_kyc);
        // Set dialog title
        layout.setTitle("Add KYC");
        final Button next = (Button) layout.findViewById(R.id.button_next);
        final EditText etDocumentNumberIdProof = (EditText) layout.findViewById(R.id.etDocnumber_first);
        final Spinner spinnerIdProof = (Spinner) layout.findViewById(R.id.spinner_id_proof);
        //Bind array adapter of Spinner Id proof
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, idProofArray); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIdProof.setAdapter(spinnerArrayAdapter);
        //On item seleted Listener on Id proof
        spinnerIdProof.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strIdProofSelected=parent.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Event click listener on Button
        next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {
                    if(etDocumentNumberIdProof.getText().toString().length()==0) {
                        Toast.makeText(activity,"Enter Document Id Number",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    kycDocumentsBean=new KYCDocumentsBean();
                    kycDocumentsBeanArrayList.clear();
                    kycDocumentsBean.setAID("AMDNISPS001");
                    kycDocumentsBean.setDocumentType("199");
                    kycDocumentsBean.setCUSTOMER_MOBILE(LocalPreferenceUtility.getUserMobileNumber(activity));
                    kycDocumentsBean.setCUSTOMER_NAME(LocalPreferenceUtility.getUserFirstsName(activity));
                    kycDocumentsBean.setDocumentFile("identityImagePath");
                    kycDocumentsBean.setDocumentName(strIdProofSelected);
                    kycDocumentsBean.setOP("YPCNUC");
                    kycDocumentsBean.setST("CARDISSUANCE");
                    kycDocumentsBean.setProofNumber(etDocumentNumberIdProof.getText().toString());
                    kycDocumentsBeanArrayList.add(kycDocumentsBean);
                    showPopupSecondAddKyc(kycDocumentsBeanArrayList,KYCActivity.this);
                    layout.dismiss();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        layout.show();
    }
    //Second Popup for Add Kyc
    //Show Popup Add Kyc
    private void showPopupSecondAddKyc(final ArrayList<KYCDocumentsBean> kycBeanArrayList, final KYCActivity activity) {
        final Dialog layout = new Dialog(activity);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layout.setContentView(R.layout.showpoup_add_second_kyc);
        // Set dialog title
        layout.setTitle("Add KYC");
        final Button addKyc = (Button) layout.findViewById(R.id.add_kyc_popup);
        Button cancel = (Button) layout.findViewById(R.id.cancel_kyc);

        final EditText etDocumentNumberAddressProof = (EditText) layout.findViewById(R.id.etAddressDocnumber);
        final Spinner spinnerAddressProof = (Spinner) layout.findViewById(R.id.spinner_address_proof);

        //Bind array Adapter of spinner address proof
        ArrayAdapter<String> spinnerAddressArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, addressProofArray); //selected item will look like a spinner set from XML
        spinnerAddressArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAddressProof.setAdapter(spinnerAddressArrayAdapter);
        //On Item selected Listener on Address proof
        spinnerAddressProof.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strAddressProofSelected=parent.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Event click listener on Button
        addKyc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {
                    if(etDocumentNumberAddressProof.getText().toString().length()==0) {
                        Toast.makeText(activity,"Enter Document Address Proof",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    kycDocumentsBean=new KYCDocumentsBean();
                    kycDocumentsBean.setAID("AMDNISPS001");
                    kycDocumentsBean.setDocumentType("198");
                    kycDocumentsBean.setCUSTOMER_MOBILE(LocalPreferenceUtility.getUserMobileNumber(activity));
                    kycDocumentsBean.setCUSTOMER_NAME(LocalPreferenceUtility.getUserFirstsName(activity));
                    kycDocumentsBean.setDocumentFile("addressImagePath");
                    kycDocumentsBean.setDocumentName(strAddressProofSelected);
                    kycDocumentsBean.setOP("YPCNUC");
                    kycDocumentsBean.setST("CARDISSUANCE");
                    kycDocumentsBean.setProofNumber(etDocumentNumberAddressProof.getText().toString());
                   // showProgressDialog();

                    kycBeanArrayList.add(kycDocumentsBean);
                    //show Third Popup
                    showPopupThird(activity,kycBeanArrayList);
                    layout.dismiss();
                }
                catch (Exception ex) {
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
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        layout.show();
    }
  //show Popup Third
    private void showPopupThird(KYCActivity activity, final ArrayList<KYCDocumentsBean> kycBeanArrayList)
    {
        final Dialog layout = new Dialog(activity);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layout.setContentView(R.layout.showpoup_add_third_kyc);
        // Set dialog title
        layout.setTitle("Add KYC");
        final Button addKyc = (Button) layout.findViewById(R.id.add_kyc_popup);
        Button cancel = (Button) layout.findViewById(R.id.cancel_kyc);

        final Button buttonIdChoose = (Button) layout.findViewById(R.id.btn_choose_id);
        final Button buttonAddressChoose = (Button) layout.findViewById(R.id.btn_choose_address);

        buttonIdChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=0;
                showFileChooser(flag);
            }
        });

        buttonAddressChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=1;
                showFileChooser(flag);
            }
        });

        imageIdProofExt=(ImageView) layout.findViewById(R.id.file_extension_first);
        imageAddressProofExt=(ImageView) layout.findViewById(R.id.file_extension_second);

        addKyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobicashIntentService.startActionUploadKyc(KYCActivity.this,kycBeanArrayList,fileIdImage,fileAddressImage);
                bindAdapterKyc(KYCActivity.this,kycBeanArrayList);
                layout.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        layout.show();
    }
    //Binding Adpter of KYC
    private void bindAdapterKyc(Activity acttivity, ArrayList<KYCDocumentsBean> kycBeanArrayList) {
        KycListAdapter adapter = new KycListAdapter(acttivity,kycBeanArrayList,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(acttivity, OrientationHelper.VERTICAL, false);
        kycRecyclerView.setLayoutManager(linearLayoutManager);
        kycRecyclerView.setItemAnimator(new DefaultItemAnimator());
        kycRecyclerView.setAdapter(adapter);
    }
    //method to show file chooser
    private void showFileChooser(int flag) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                // Get the cursor
                if (selectedImage!= null) {
                    Cursor cursor=getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    Bitmap thumbnail = BitmapFactory.decodeFile(picturePath);
                    if(flag==0) {
                        imageIdProofExt.setImageBitmap(thumbnail);
                    }else if(flag==1) {
                        imageAddressProofExt.setImageBitmap(thumbnail);
                    }
                    saveImage(thumbnail);
                   /* String filePath = cursor.getString(columnIndex);
                    cursor.close();*/
                    // Set the Image in ImageView after decoding the String
                   /// mUserImageView.setImageBitmap(BitmapFactory.decodeFile(filePath));
                } else {
                    Timber.e("Selected profile picture is null");
                }

            } catch (Exception e) {
                Timber.e("Exception occured while selecting picture from gallary" + e.getMessage());
            }
        }
    }

    private String saveImage(Bitmap myBitmap)
    {
        if(flag==0) {
            imageIdProofExt.buildDrawingCache();
            myBitmap = imageIdProofExt.getDrawingCache();
        }else if(flag==1) {
            imageAddressProofExt.buildDrawingCache();
            myBitmap = imageAddressProofExt.getDrawingCache();
        }
        OutputStream fOut = null;
        try {
            File root = new File(Environment.getExternalStorageDirectory()+ File.separator + "KYCFolder" + File.separator);
            root.mkdirs();
            File sdImageMainDirectory = new File(root, currentDateFormat()+".jpg");
            fOut = new FileOutputStream(sdImageMainDirectory);
            if(flag==0) {
                imagePath=sdImageMainDirectory.getAbsolutePath();
                fileIdImage=new File(imagePath);
            }else if(flag==1) {
                imagePath=sdImageMainDirectory.getAbsolutePath();
                fileAddressImage=new File(imagePath);
            }
            Toast.makeText(this, "Correct end now", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error Occured. Please try again later."+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        try {
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            Toast.makeText(this, "Correct end now", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Exception" +e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return "";
    }

    private String currentDateFormat()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    @Override
    public void seClickListener(KYCBean kycBean) {
        //showPopupUpdateKyc(this,kycBean);
    }
    @Override
    public void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(operatorListReceiver, new IntentFilter(action));
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(operatorListReceiver);
    }
    /**
     * Handling broadcast event for user log .
     */
    private BroadcastReceiver operatorListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(NetworkConstant.STATUS_UPLOAD_ADD_KYC_SUCCESS)) {
//
            } else if (action != null && action.equals(NetworkConstant.STATUS_UPLOAD_CLIENT_ADD_KYC_FAIL)) {
                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);

            }
        }
    };

}
