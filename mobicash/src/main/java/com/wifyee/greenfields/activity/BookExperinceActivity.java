package com.wifyee.greenfields.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.adapters.BookExperienceAdapter;
import com.wifyee.greenfields.adapters.FollowBrandsAdapter;
import com.wifyee.greenfields.adapters.SpinerCityAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.interfaces.OnClickTravelPackage;
import com.wifyee.greenfields.models.response.CityResponse;
import com.wifyee.greenfields.services.MobicashIntentService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import timber.log.Timber;

public class BookExperinceActivity extends BaseActivity implements OnClickTravelPackage {

    private Toolbar mToolbar;
    private ImageButton back;
    private BookExperienceAdapter bookExperienceAdapter;
    private RecyclerView recyclerView;
    private int images[]={R.drawable.thailand,R.drawable.dubai,R.drawable.asia_singapore_malaysia,R.drawable.paris};
    private String brandsName[]={"Thailand Packages","Dubai Packages","Singapore Package","Paris Packages"};
    private ArrayList<CityResponse> cityResponseArrayList=new ArrayList<>();
    private  AutoCompleteTextView buttonFrom;
    private AutoCompleteTextView buttonTo;
    private  Spinner spinnerCategory;
    private String sourceID="";
    private String destinationID="";
    private EditText departureDate;
    private String strDepartureDate="";
    private int mYear, mMonth, mDay, mHour, mMinute;
    private EditText mobileEditText;
    private EditText etNumberOfPeople;
    private final int MINIMUM_PHONE_NUMBER_LENGHT = 10;
    private final int MAXIMUM_PHONE_NUMBER_LENGHT = 10;
    private String arrayCategory[]={"Select","Economy","Deluxe","Luxury"};
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
        setContentView(R.layout.activity_book_experience);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        back= (ImageButton) findViewById(R.id.toolbar_back);
        recyclerView= (RecyclerView) findViewById(R.id.recycler_follow);
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
        bindAdapter(images,brandsName);
    }
    //Bind Adapter class of Book Experience
    private void bindAdapter(int[] images, String[] brandsName) {
        bookExperienceAdapter=new BookExperienceAdapter(this,images,brandsName,this);
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(bookExperienceAdapter);
    }

    @Override
    public void onClickTravels(Context mContext)
    {
        final Dialog layout = new Dialog(mContext);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layout.setContentView(R.layout.showpoup_fill_form_travels);
        // Set dialog title
        layout.setTitle("Travel Details");
        final Button submit = (Button) layout.findViewById(R.id.button_submit);
        etNumberOfPeople = (EditText) layout.findViewById(R.id.number_of_people);
        spinnerCategory = (Spinner) layout.findViewById(R.id.travel_category);
        buttonFrom= (AutoCompleteTextView) layout.findViewById(R.id.from_bus);
        buttonTo= (AutoCompleteTextView) layout.findViewById(R.id.to_bus);
        mobileEditText = (EditText) layout.findViewById(R.id.edit_text_mobile_number);
        departureDate = (EditText) layout.findViewById(R.id.edit_text_date_of_journey);
        showProgressDialog();
        MobicashIntentService.startActionGetCityList(this);
        //Bind array adapter of Spinner Id proof
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayCategory); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerArrayAdapter);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                layout.dismiss();
                if (onValidate()) {
                    if ((MobicashUtils.isNetworkAvailable(BookExperinceActivity.this))) {
                        //callWebViewApi("client", LocalPreferenceUtility.getUserCode(this),"NA","2",mobileEditText.getText().toString(),sourceID,destinationID,departureDate.getText().toString());
                    } else {
                        showAlert(getString(R.string.alert_dialog_error_title), getString(R.string.network_not_available));
                    }
                }
            }
        });
        departureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showDatePicker();
            }
        });
        layout.show();
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


    //Convert Date time Stamp
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


    @Override
    protected void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(operatorListReceiver, new IntentFilter(action));
        }
       // showProgressDialog();
   //     MobicashIntentService.startActionGetCityList(this);
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
            if (actionOperatorList.equals(NetworkConstant.CITY_LIST_SUCCESS)) {
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
    private void bindSpinnerAdapter(final ArrayList<CityResponse> cityListResponse) {
        buttonFrom.setThreshold(1);
        buttonTo.setThreshold(1);
        SpinerCityAdapter spinerAdapter = new SpinerCityAdapter(BookExperinceActivity.this, R.layout.bus_book_page,R.id.lbl_name, cityListResponse);
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
}
