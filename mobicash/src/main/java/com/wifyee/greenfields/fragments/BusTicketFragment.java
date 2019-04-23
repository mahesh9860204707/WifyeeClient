package com.wifyee.greenfields.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.activity.BusBookActivity;
import com.wifyee.greenfields.activity.BusBookingList;
import com.wifyee.greenfields.adapters.SpinerCityAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.models.response.CityResponse;
import com.wifyee.greenfields.services.MobicashIntentService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by sumanta on 1/5/17.
 */

public class BusTicketFragment extends Fragment  {


    private final int MINIMUM_PHONE_NUMBER_LENGHT = 4;
    private final int MAXIMUM_PHONE_NUMBER_LENGHT = 19;
    public ProgressDialog progressDialog = null;

     private AutoCompleteTextView buttonFrom;
     private AutoCompleteTextView buttonTo;
     private EditText mobileEditText;
     private EditText departureDateTicketing;

    private String sourceID="";
    private String destinationID="";
    private String strDepartureDate="";
    private int mYear, mMonth, mDay, mHour, mMinute;
    private ArrayList<CityResponse> cityResponseArrayList=new ArrayList<>();
    private View view=null;
    public BusTicketFragment() {
        // Required empty public constructor
    }
    /**
     * List of actions supported.
     */
    private String[] broadCastReceiverActionList = {
            NetworkConstant.CITY_LIST_SUCCESS,
            NetworkConstant.CITY_LIST_FAILURE,
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(operatorListReceiver, new IntentFilter(action));
        }
        showProgressDialog();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MobicashIntentService.startActionGetCityList(getActivity());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_book_bus_ticket, container, false);
         buttonFrom=(AutoCompleteTextView)view.findViewById(R.id.from_bus);
         buttonTo=(AutoCompleteTextView)view.findViewById(R.id.to_bus);
         Button buttonSearchTicketing= (Button) view.findViewById(R.id.search_ticketing);
         departureDateTicketing= (EditText) view.findViewById(R.id.edit_text_date_of_journey_ticketing);
         mobileEditText= (EditText) view.findViewById(R.id.edit_text_mobile_number);
         buttonSearchTicketing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onValidate()) {
                    if ((MobicashUtils.isNetworkAvailable(getActivity()))) {
                        callWebViewApi("client", LocalPreferenceUtility.getUserCode(getActivity()),"NA","2",mobileEditText.getText().toString(),sourceID,destinationID,departureDateTicketing.getText().toString());
                    } else {
                        showAlert(getString(R.string.alert_dialog_error_title), getString(R.string.network_not_available));
                    }
                }
            }
        });
        departureDateTicketing.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelProgressDialog();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(operatorListReceiver);
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
        SpinerCityAdapter spinerAdapter = new SpinerCityAdapter(getActivity(), R.layout.bus_book_page,R.id.lbl_name, cityListResponse);
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

    //call Webview Api
    private void callWebViewApi(String client, String userCode, String na, String s, String mobileNumber,
                                String sourceID, String destinationID, String departureDate) {
        Intent intent=new Intent(getActivity(),BusBookingList.class);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
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
                        departureDateTicketing.setText(date_afters);
                        strDepartureDate=departureDateTicketing.getText().toString();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
//Formatted Date from String
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
        if (mobileEditText == null || departureDateTicketing == null || buttonFrom == null || buttonTo == null) {
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
            if (departureDateTicketing.getText().toString().isEmpty() || departureDateTicketing.getText().toString().equals("0")
                    || departureDateTicketing.getText().toString().equals("00") || departureDateTicketing.getText().toString().equals("000")) {
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
            alertDialogBuilder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            alertDialogBuilder = new AlertDialog.Builder(getActivity());
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

    /**
     * show profile upload progress dialog
     */
    protected void showProgressDialog() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        } else {
            progressDialog = new ProgressDialog(getActivity());
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