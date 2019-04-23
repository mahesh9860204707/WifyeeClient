package com.wifyee.greenfields.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.adapters.SpinerCityAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.models.response.CityResponse;
import com.wifyee.greenfields.services.MobicashIntentService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class BusBookActivity extends BaseActivity implements View.OnClickListener {

    private final int MINIMUM_PHONE_NUMBER_LENGHT = 4;
    private final int MAXIMUM_PHONE_NUMBER_LENGHT = 19;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.toolbar_back)
    ImageButton back;

    @BindView(R.id.from_bus)
    AutoCompleteTextView buttonFrom;

    @BindView(R.id.to_bus)
    AutoCompleteTextView buttonTo;

    @BindView(R.id.search)
    Button buttonSearch;

    @BindView(R.id.edit_text_mobile_number)
    EditText mobileEditText;

    @BindView(R.id.edit_text_date_of_journey)
     EditText departureDate;
    private String sourceID="";
    private String destinationID="";
    private String strDepartureDate="";
    private int mYear, mMonth, mDay, mHour, mMinute;
    private ArrayList<CityResponse> cityResponseArrayList=new ArrayList<>();
    /**
     * List of actions supported.
     */
    private String[] broadCastReceiverActionList = {
            NetworkConstant.CITY_LIST_SUCCESS,
            NetworkConstant.CITY_LIST_FAILURE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_book_page);
        ButterKnife.bind(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        buttonSearch.setOnClickListener(this);
        departureDate.setOnClickListener(this);
        if (mToolbar!= null) {
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
    @Override
    protected void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(operatorListReceiver, new IntentFilter(action));
        }
        showProgressDialog();
        MobicashIntentService.startActionGetCityList(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        cancelProgressDialog();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(operatorListReceiver);
    }
    /**
     * operator list and recharge status receiver
     */
    private BroadcastReceiver operatorListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionOperatorList = intent.getAction();
            if (actionOperatorList.equals(NetworkConstant.CITY_LIST_SUCCESS))
            {
                ArrayList<CityResponse> cityListResponse = (ArrayList<CityResponse>) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (cityListResponse != null) {
                    cancelProgressDialog();
                    if (cityListResponse != null) {
                        //Bind Spinner Adapter
                        cityResponseArrayList.addAll(cityListResponse);
                        bindSpinnerAdapter(cityListResponse);
                        cancelProgressDialog();
                    } else {
                        cancelProgressDialog();
                    }
                } else {
                    cancelProgressDialog();
                }
            } else if (actionOperatorList.equals(NetworkConstant.CITY_LIST_FAILURE)) {
                cancelProgressDialog();
            }
        }
    };
  //Bind Spinner Adapter
    private void bindSpinnerAdapter(final ArrayList<CityResponse> cityListResponse)
    {
        buttonFrom.setThreshold(1);
        buttonTo.setThreshold(1);
        SpinerCityAdapter spinerAdapter = new SpinerCityAdapter(BusBookActivity.this, R.layout.bus_book_page,R.id.lbl_name, cityListResponse);
        buttonFrom.setAdapter(spinerAdapter);
        buttonTo.setAdapter(spinerAdapter);
        buttonFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sourceID=cityListResponse.get(position).getId();
            }
        });

        buttonTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                destinationID=cityListResponse.get(position).getId();
            }
        });

    }
    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.edit_text_date_of_journey:
                  showDatePicker();
                break;
            case R.id.search:
                if (onValidate()) {
                    if ((MobicashUtils.isNetworkAvailable(this))) {
                        callWebViewApi("client",LocalPreferenceUtility.getUserCode(this),"NA","2",mobileEditText.getText().toString(),sourceID,destinationID,departureDate.getText().toString());
                    } else {
                        showAlert(getString(R.string.alert_dialog_error_title), getString(R.string.network_not_available));
                    }
                }
                break;
            default:
                break;
        }
    }
  //call Webview Api
    private void callWebViewApi(String client, String userCode, String na, String s, String mobileNumber,
                                String sourceID, String destinationID, String departureDate) {

        Intent intent=new Intent(this,BusBookingList.class);
        intent.putExtra("date",departureDate);
        intent.putExtra("mobile",mobileNumber);
        intent.putExtra("toCity",sourceID);
        intent.putExtra("fromCity",destinationID);
        startActivity(intent);
    }
    //Set Date Picker
    private void showDatePicker() {
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
                        String date_afters = formateDateFromstring("dd-MM-yyyy", "dd-MMM-yyyy", datetime);
                        departureDate.setText(date_afters);
                        strDepartureDate=departureDate.getText().toString();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){
        Date parsed = null;
        String outputDate = "";
        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());
        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            // LOG(TAG, "ParseException - dateFormat");
        }
        return outputDate;
    }

    /**
     * validate inputs
     *
     * @return
     */
    private boolean onValidate() {

        if (mobileEditText == null || departureDate == null || buttonFrom == null || buttonTo == null) {
            showAlert(getString(R.string.Invalid_alert_title), getString(R.string.all_fields_required));
            return false;
        }
        String phoneNumber = mobileEditText.getText().toString();
        int existPlus = (phoneNumber != null && phoneNumber.startsWith("+")) ? 1 : 0;
        if (phoneNumber.isEmpty()) {
            showAlert(getString(R.string.phone_validation_alert_title), getString(R.string.error_empty_phone_number));
            return false;
        } else if (phoneNumber.length() < MINIMUM_PHONE_NUMBER_LENGHT
                + existPlus
                || phoneNumber.length() > MAXIMUM_PHONE_NUMBER_LENGHT
                + existPlus) {
            showAlert(getString(R.string.phone_validation_alert_title), getString(R.string.phone_validation_alert_message));
            return false;
        } else if (phoneNumber.startsWith("+") || phoneNumber.startsWith("0")) {
            showAlert(getString(R.string.phone_validation_alert_title), getString(R.string.phone_validation_alert_message));
            return false;
        }
        try {
            if (departureDate.getText().toString().isEmpty() || departureDate.getText().toString().equals("0")
                    || departureDate.getText().toString().equals("00") || departureDate.getText().toString().equals("000")) {
                showAlert(getString(R.string.alert_dialog_error_title), getString(R.string.tv_departure_date));
                return false;
            }

        } catch (NumberFormatException e) {
            Timber.e("NumberFormatException occured :" + e.getMessage());
            showAlert(getString(R.string.improper_input_title), getString(R.string.improper_input_msg));
            return false;
        }

        return true;
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
            alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            alertDialogBuilder = new AlertDialog.Builder(this);
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }


}