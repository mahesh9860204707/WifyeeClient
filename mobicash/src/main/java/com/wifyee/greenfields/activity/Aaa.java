package com.wifyee.greenfields.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;

public class Aaa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_new);

        ImageView icSetting = findViewById(R.id.ic_setting);
        TextView favName = findViewById(R.id.com_name);
        final EditText tvMobileNo = findViewById(R.id.tv_phone_number);
        EditText tvPassword = findViewById(R.id.tv_password);
        TextView txtLogin = findViewById(R.id.txt_login);
        TextView txtNewUser = findViewById(R.id.txt_new_user);
        TextView txtSignUp = findViewById(R.id.txt_signup);
        TextView tvSendPasscode = findViewById(R.id.tv_send_passcode);
        TextInputLayout tilMobile = findViewById(R.id.til_mobile);
        TextInputLayout tilPassword = findViewById(R.id.til_password);
        Button btnLogin = findViewById(R.id.btn_login);

        favName.setTypeface(Fonts.getSemiBold(this));
        tvMobileNo.setTypeface(Fonts.getSemiBold(this));
        tvPassword.setTypeface(Fonts.getSemiBold(this));
        txtLogin.setTypeface(Fonts.getSemiBold(this));
        txtNewUser.setTypeface(Fonts.getSemiBold(this));
        txtSignUp.setTypeface(Fonts.getSemiBold(this));
        tvSendPasscode.setTypeface(Fonts.getSemiBold(this));
        tilMobile.setTypeface(Fonts.getRegular(this));
        tilPassword.setTypeface(Fonts.getRegular(this));
        btnLogin.setTypeface(Fonts.getSemiBold(this));

        tvMobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(tvMobileNo.getText().toString().length()==10){
                    tvMobileNo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_correct, 0);
                }else {
                    tvMobileNo.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        icSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), view);
                popup.inflate(R.menu.setting_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:

                                break;

                            case R.id.menu2:

                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }
}
