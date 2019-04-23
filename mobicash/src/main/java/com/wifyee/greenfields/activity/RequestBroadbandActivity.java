package com.wifyee.greenfields.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;

public class RequestBroadbandActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private ImageButton back;
    private TextView txtAddress,txtName,txtMobile,txtEmail,txtPlandetails;
    private EditText etExactAddress;
    private Button submit;
    private String strName="",strMobile="",strEmail="",strAddress="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_broadband);
        strName= LocalPreferenceUtility.getUserLastName(this);
        strMobile= LocalPreferenceUtility.getUserMobileNumber(this);
        strEmail= LocalPreferenceUtility.getUserEmail(this);
        strAddress= LocalPreferenceUtility.getClientAddress(this);
        bindView();
        mToolbar = (Toolbar) findViewById(R.id.mtoolbar);
        back = (ImageButton) findViewById(R.id.toolbar_back);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });
        }
    }
   //Bind View
    private void bindView()
    {
        txtAddress=(TextView)findViewById(R.id.txtadress);
        txtAddress.setText("Address="+" "+strAddress);
        txtName=(TextView)findViewById(R.id.txtname);
        txtName.setText("Name="+" "+strName);
        txtMobile=(TextView)findViewById(R.id.txtmobile);
        txtMobile.setText("Mobile="+" "+strMobile);
        txtEmail=(TextView)findViewById(R.id.txtemail);
        txtEmail.setText("Email="+" "+strEmail);
        txtPlandetails=(TextView)findViewById(R.id.broadband_plans);
        txtPlandetails.setOnClickListener(this);
        etExactAddress=(EditText) findViewById(R.id.exact_address);
        submit=(Button) findViewById(R.id.submit_button);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
       if(v==txtPlandetails)
       {
           startActivity(new Intent(this,BroadBandListActivity.class));
           this.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
       }else if(v==submit)
       {
           if(etExactAddress.getText().toString().length()==0)
           {
               Toast.makeText(this, "Fill Current exact Address", Toast.LENGTH_SHORT).show();
               return;
           }

       }

    }
}
