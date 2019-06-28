package com.wifyee.greenfields.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;

import java.util.Arrays;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindString(R.string.tv_coming_soon)
    String mCommingSoonMessage;

    @BindString(R.string.feature_is_not_available_message)
    String mFeatureIsNotAvailableMessage;

    private Context mContext = null;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ButterKnife.bind(this);
        mContext = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolBarTitle = mToolbar.findViewById(R.id.toolbar_title);
        toolBarTitle.setTypeface(Fonts.getSemiBold(this));
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.secondaryPrimary), PorterDuff.Mode.SRC_ATOP);

            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });
        }

        final ListView listView = (ListView) findViewById(R.id.list);
        final String[] values = new String[]{getString(R.string.list_item_change_password),
                getString(R.string.list_item_identity),
                getString(R.string.list_item_authorized_phones_for_this_account),
                getString(R.string.list_item_supplementary_information),
                getString(R.string.list_item_notifications),
                getString(R.string.list_item_payment),
                getString(R.string.list_item_account)
        };

        adapter= new ArrayAdapter<String>(this,R.layout.setting_item_layout, values)
        {
            @Override
            public View getView(int pos, View convertView, android.view.ViewGroup parent) {
                View v = convertView;

                if (v == null) {
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.setting_item_layout, null);
                    TextView tv1 = v.findViewById(R.id.text_view);
                    tv1.setText(Arrays.toString(values));
                    tv1.setTypeface(Fonts.getRegular(SettingActivity.this));
                }
                return super.getView(pos, v, parent);
            }
        };

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);
                if (itemValue.equals(getString(R.string.list_item_account))) {
                    showCommingSoonDialog();
                    /*startActivity(IntentFactory.createAccountSettingActivity(getApplicationContext()));
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);*/
                } else if (itemValue.equals(getString(R.string.list_item_supplementary_information))) {
                    showCommingSoonDialog();
                   /* startActivity(IntentFactory.createSupplementaryInfoSettingActivity(getApplicationContext()));
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);*/
                } else if (itemValue.equals(getString(R.string.list_item_change_password))) {
                    startActivity(IntentFactory.createChangePasswordSettingActivity(getApplicationContext()));
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                } else if (itemValue.equals(getString(R.string.list_item_payment))) {
                    showCommingSoonDialog();
                    /*startActivity(IntentFactory.createPaymentSettingActivity(getApplicationContext()));
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);*/
                } else if (itemValue.equals(getString(R.string.list_item_authorized_phones_for_this_account))) {
                    showCommingSoonDialog();
                    /*startActivity(IntentFactory.createAuthorizedPhoneSettingActivity(getApplicationContext()));
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);*/
                } else if (itemValue.equals(getString(R.string.list_item_identity))) {
                    showCommingSoonDialog();
                    /*startActivity(IntentFactory.createIdentitySettingActivity(getApplicationContext()));
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);*/
                } else if (itemValue.equals(getString(R.string.list_item_notifications))) {
                    showCommingSoonDialog();
                  /*  startActivity(IntentFactory.createNotificationSettingActivity(getApplicationContext()));
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);*/
                }
            }

        });
    }

    public void showCommingSoonDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder
                .setTitle(mCommingSoonMessage)
                .setMessage(mFeatureIsNotAvailableMessage)
                .setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
}
