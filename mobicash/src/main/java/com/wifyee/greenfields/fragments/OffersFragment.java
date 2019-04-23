package com.wifyee.greenfields.fragments;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.MobicashUtils;
import com.wifyee.greenfields.adapters.OffersListAdapter;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.models.requests.OffersCategory;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.models.response.OffersItem;
import com.wifyee.greenfields.services.MobicashIntentService;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import timber.log.Timber;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
//import static com.facebook.FacebookSdk.getApplicationContext;


public class OffersFragment extends Fragment implements View.OnClickListener  {

    private View view;
    private RecyclerView mRecyclerView;
    public ProgressDialog progressDialog = null;
    private OffersCategory offersCategory;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private String latitude;
    private String longitude;
    private Button submit;
    /**
     * List of actions supported.
     */
    private String[] broadCastReceiverActionList = {
            NetworkConstant.OFFERS_LIST_FAILURE,
            NetworkConstant.OFFERS_LIST_SUCCESS
    };
    public OffersFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static OffersFragment getinstance() {
        return new OffersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_offers, container, false);
        final Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        mRecyclerView=(RecyclerView)view.findViewById(R.id.recyclerView_offers);
        submit=(Button) view.findViewById(R.id.addressButton);
        submit.setOnClickListener(this);
        // Open the autocomplete activity when the button is clicked.
        Button openButton = (Button) view.findViewById(R.id.open_button);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutocompleteActivity(getActivity());
            }
        });
        // Set the toolbar as action bar
        getActivity().setTitle("WiFi Offers");
        // For back button in tool bar handling.
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });
        return view;
    }
 //Open search city view
    private void openAutocompleteActivity(final Activity context) {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(context);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(context, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);

                // Format the place's details and display them in the TextView.
                double lat = place.getLatLng().latitude;
                double lng = place.getLatLng().longitude;

                latitude=String.valueOf(lat);
                longitude=String.valueOf(lng);

                latitude = latitude.substring(0,latitude.indexOf(".")+5);
                longitude = longitude.substring(0,longitude.indexOf(".")+5);

                //Toast.makeText(getApplicationContext(),"Latitude="+latitude +"Longitude" +longitude,Toast.LENGTH_SHORT).show();
                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
               // Status status = PlaceAutocomplete.getStatus(getApplicationContext(), data);

            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }

   /* //Getting Location
    private Location getLocation(Context context) {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(getActivity(), Locale.getDefault());
                            try {
                                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()   Timer timer = new Timer();
                                addressValue = address;
                                //  Toast.makeText(HomeActivity.this, "Getting Location" + addressValue, Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }
                        }

                    }

                }

                if (isGPSEnabled) {
                    if (location == null) {
                        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return location;
                        }
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(getActivity(), Locale.getDefault());
                            try {
                                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()   Timer timer = new Timer();
                                addressValue = address;
                                //   Toast.makeText(HomeActivity.this, "Getting Location" + addressValue, Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }*/

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
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(offersStatusReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        //getLocation(getActivity());
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(offersStatusReceiver, new IntentFilter(action));
        }
    }
    /**
     * Handling broadcast event for user login .
     */
    private BroadcastReceiver offersStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            cancelProgressDialog();
            try {
                if (action.equals(NetworkConstant.OFFERS_LIST_SUCCESS)) {
                    final ArrayList<OffersItem> moffersResponse = (ArrayList<OffersItem>) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    Timber.d("STATUS_PLAN_LIST_SUCCESS = > PlanListResponse  ==>" + new Gson().toJson(moffersResponse));
                    cancelProgressDialog();
                    bindRecylcerAdapter(moffersResponse);

                } else if (action.equals(NetworkConstant.OFFERS_LIST_FAILURE)) {
                    FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                    if (failureResponse != null) {
                        Timber.d("STATUS_PLAN_LIST_FAIL = > failureResponse  ==>" + new Gson().toJson(failureResponse));
                        cancelProgressDialog();
                    }
                    if (failureResponse != null && failureResponse.msg != null) {
                        cancelProgressDialog();
                        showErrorDialog(failureResponse.msg);
                    } else {
                        String errorMessage = getString(R.string.error_message);
                        cancelProgressDialog();
                        showErrorDialog(errorMessage);
                    }
                }
            } catch (Exception e) {
                cancelProgressDialog();
                Timber.e(" Exception caught in PlanListStatusReceiver " + e.getMessage());
            }
        }
    };

    private void showErrorDialog(String errorMessage) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(errorMessage);

        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
    }
    //Bind RecyclerView for plans
    private void bindRecylcerAdapter(ArrayList<OffersItem> offersDetailsArrayList) {
        OffersListAdapter adapter = new OffersListAdapter(offersDetailsArrayList,getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
    }
    private void showProgressDialog() {
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

    //Cancel Progress dialog
    private void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }


    @Override
    public void onClick(View v) {
        offersCategory=new OffersCategory();
        StringBuilder sb = new StringBuilder(latitude);
        sb.append(longitude);

        offersCategory.lat=latitude;
        offersCategory.longt=longitude;
        try {
            String hashValue="";
            hashValue = MobicashUtils.getSha1(sb.toString());
            offersCategory.hash=hashValue;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        showProgressDialog();
        MobicashIntentService.startActionWifyeeOffers(getActivity(),offersCategory);
    }

}
