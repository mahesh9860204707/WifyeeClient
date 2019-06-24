package com.wifyee.greenfields.foodorder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.SharedPrefence.SharedPreference;
import com.wifyee.greenfields.Utils.RecyclerTouchListener;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.interfaces.FragmentInterface;
import com.wifyee.greenfields.models.response.FailureResponse;
import com.wifyee.greenfields.services.MobicashIntentService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllNonVegFoodItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllNonVegFoodItemFragment extends Fragment implements FragmentInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btn_addToCart,btn_buynow;
    private OnFragmentInteractionListener mListener;
    static SharedPreference sharedPreference;
    public AllNonVegFoodItemFragment() {
        // Required empty public constructor
    }
    public ProgressDialog progressDialog = null;
    private Context mContext = null;
    private VegOrNonVegAdapter VegOrNonVegAdapter;
    List<VegORNonVegList> foodOder_List;
    List<SharedPrefenceList> shred_list=new ArrayList<>();;
    public static ArrayList<SharedPrefenceList> sharedPrefenceListProduct = new ArrayList<>();
    SharedPrefenceList productItem=new SharedPrefenceList();

    @BindView(R.id.non_veg_recyclerview)
    RecyclerView foododerRecycler;

    @BindView(R.id.empty_view_containers)
    LinearLayout emptyView;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllNonVegFoodItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllNonVegFoodItemFragment newInstance(String param1, String param2) {
        AllNonVegFoodItemFragment fragment = new AllNonVegFoodItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mContext = this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_non_veg_food_item, container, false);

        foododerRecycler = (RecyclerView) view.findViewById(R.id.non_veg_recyclerview);
        emptyView=(LinearLayout)view.findViewById(R.id.empty_view_containers);
        btn_addToCart=(Button)view.findViewById(R.id.add_to_cart);
        btn_buynow=(Button)view.findViewById(R.id.buy_now);
        sharedPreference=new SharedPreference(mContext);
        MobicashIntentService.startActionFoodOrderList(mContext, getFoodCategoryRequest());

        /*foododerRecycler.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                foododerRecycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                 VegORNonVegList dataModel= foodOder_List.get(position);
                 dataModel.checkvalue = !dataModel.checkvalue;
                CheckBox checkBox=(CheckBox)view.findViewById(R.id.food_checkbox) ;
                SwitchButton sb=(SwitchButton)view.findViewById(R.id.switchbutton);
                boolean presnetvlaue=false;
                String price_value=dataModel.getPrice();
                if(dataModel.checkvalue==true)
                {
                    if (sharedPreference.getFavorites(mContext)!=null) {
                        List<SharedPrefenceList> DefaultOder_lists=sharedPreference.getFavorites(mContext);
                        if (DefaultOder_lists.size()!=0) {
                            for(int y=0;y<sharedPreference.getFavorites(mContext).size();y++)
                            {
                                if(dataModel.name.equalsIgnoreCase(sharedPreference.getFavorites(mContext).get(y).name))
                                {
                                    presnetvlaue=true;
                                    Toast.makeText(mContext,"This Food Item already Add to Cart",Toast.LENGTH_SHORT).show();
                                    dataModel.checkvalue=false;
                                    sb.setChecked(false);
                                    VegOrNonVegAdapter.notifyDataSetChanged();
                                    break;
                                }
                                else {
                                    productItem=new SharedPrefenceList();
                                    productItem.setName(dataModel.name);
                                    productItem.setDescription(dataModel.description);
                                    productItem.setFoodImage(dataModel.foodImage);
                                    productItem.setPrice(dataModel.price);
                                    productItem.setQuantiy("1");
                                    sharedPrefenceListProduct.add(productItem);
                                    shred_list.add(productItem);
                                    sb.setChecked(true);
                                    VegOrNonVegAdapter.notifyDataSetChanged();
                                }


                            }
                        }else{
                            productItem=new SharedPrefenceList();
                            productItem.setName(dataModel.name);
                            productItem.setDescription(dataModel.description);
                            productItem.setFoodImage(dataModel.foodImage);
                            productItem.setPrice(dataModel.price);
                            productItem.setQuantiy("1");
                            sharedPrefenceListProduct.add(productItem);
                            shred_list.add(productItem);
                            sb.setChecked(true);
                            VegOrNonVegAdapter.notifyDataSetChanged();

                        }
                    }
                }else if(dataModel.checkvalue==false){
                    for(int i =0;i<sharedPrefenceListProduct.size();i++)
                    {
                        if(sharedPrefenceListProduct.get(i).name.toString().equalsIgnoreCase(dataModel.name))
                        {
                            sharedPrefenceListProduct.remove(i);
                            shred_list.remove(i);
                            sb.setChecked(false);
                            VegOrNonVegAdapter.notifyDataSetChanged();
                        }
                    }
                }
               *//* VegOrNonVegAdapter.notifyDataSetChanged();*//*
            }

            @Override
            public void onLongClick(View view, int position) {
            
            }
        }));*/

        btn_addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // MobicashIntentService.startActionADDTOCARTLIST(mContext,getaddToCartRequest());
                if (sharedPreference.getFavorites(mContext)!=null) {
                    List<SharedPrefenceList> DefaultOder_list=sharedPreference.getFavorites(mContext);
                    Boolean exitvalue=false;
                    if (sharedPrefenceListProduct != null && DefaultOder_list.size() != 0) {
                        for (int i = 0; i < sharedPrefenceListProduct.size(); i++) {
                            for(int t=0;t<sharedPreference.getFavorites(mContext).size();t++)
                            {
                                if(sharedPrefenceListProduct.get(i).name.equalsIgnoreCase(sharedPreference.getFavorites(mContext).get(t).name))
                                {
                                    exitvalue=true;
                                    break;
                                }else{
                                    exitvalue=false;
                                }
                            }
                            if (exitvalue==false) {
                                productItem.setMercantId(sharedPrefenceListProduct.get(i).mercantId);
                                productItem.setName(sharedPrefenceListProduct.get(i).name);
                                productItem.setDescription(sharedPrefenceListProduct.get(i).description);
                                productItem.setFoodImage(sharedPrefenceListProduct.get(i).foodImage);
                                productItem.setPrice(sharedPrefenceListProduct.get(i).price);
                                productItem.setQuantiy(sharedPrefenceListProduct.get(i).quantiy);
                                sharedPreference.addFavoriteItem(mContext, productItem);
                                Log.e("-ItemId-","merchant "+productItem.getMercantId());

                            }
                        }
                        sharedPrefenceListProduct.clear();
                    }else{
                        sharedPreference.addFavorite(mContext,sharedPrefenceListProduct);
                        sharedPrefenceListProduct.clear();
                    }
                }
                else if(sharedPreference.getFavorites(mContext)==null){
                    sharedPreference.addFavorite(mContext,sharedPrefenceListProduct);
                    sharedPrefenceListProduct.clear();
                }
                //sharedPreference.addFavorite(mContext,sharedPrefenceListProduct);
                startActivity(IntentFactory.createAddTOCartActivity(getContext()));
            }
        });

            return view;
    }

    private FoodOrderRequest getFoodCategoryRequest() {
        FoodOrderRequest foodOrderRequest = new FoodOrderRequest();
        String foodType= this.getArguments().getString("message");
        foodOrderRequest.MerchantId= this.getArguments().getString("merchantid");
        if (foodType.equalsIgnoreCase("veg")) {
            foodOrderRequest.FoodType = NetworkConstant.TYPE_FOODVEG;
        } else  if (foodType.equalsIgnoreCase("non-veg")) {
            foodOrderRequest.FoodType = NetworkConstant.TYPE_FOODNONVEG;
        }else {
            foodOrderRequest.FoodType = foodType;
        }
        return foodOrderRequest;
    }
    private void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        cancelProgressDialog();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(foodOderListReceiver);
    }
    @Override
    public void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mContext);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(foodOderListReceiver, new IntentFilter(action));
        }
        showProgressDialog();
        MobicashIntentService.startActionFoodOrderList(mContext, getFoodCategoryRequest());
    }
    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_FOODODER_LIST_SUCCESS,
            NetworkConstant.STATUS_FOODORDER_LIST_FAIL
    };
    public void setupUI() {
       //RecyclerView.LayoutManager linearLayoutManager = new GridLayoutManager(mContext, 3);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        foododerRecycler.setLayoutManager(linearLayoutManager);
        VegOrNonVegAdapter = new VegOrNonVegAdapter(mContext, foododerRecycler, foodOder_List);
        foododerRecycler.setAdapter(VegOrNonVegAdapter);
        VegOrNonVegAdapter.notifyDataSetChanged();
    }
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
    private BroadcastReceiver foodOderListReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String actionOperatorList = intent.getAction();
            if (actionOperatorList.equals(NetworkConstant.STATUS_FOODODER_LIST_SUCCESS)) {
                FoodOrderResponse foodOrderResponse = (FoodOrderResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (foodOrderResponse != null && foodOrderResponse.VegOrNonVegFoodOderList != null) {
                    cancelProgressDialog();
                    foodOder_List = foodOrderResponse.VegOrNonVegFoodOderList;
                    setupUI();
                } else {
                    cancelProgressDialog();
                    foododerRecycler.setVisibility(View.INVISIBLE);
                    emptyView.setVisibility(View.VISIBLE);
                 }
            } else if (actionOperatorList.equals(NetworkConstant.STATUS_FOODORDER_LIST_FAIL)) {
                foododerRecycler.setVisibility(View.INVISIBLE);
                emptyView.setVisibility(View.VISIBLE);
                cancelProgressDialog();
                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (failureResponse != null && failureResponse.msg != null && !failureResponse.msg.isEmpty()) {
                    cancelProgressDialog();
                   // showErrorDialog(failureResponse.msg);
                    showErrorDialog(String.valueOf(R.string.error_message));
                }
                showErrorDialog(String.valueOf(R.string.error_message));
            }
        }
    };
    public void showErrorDialog(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
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
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void fragmentBecameVisible() {


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
