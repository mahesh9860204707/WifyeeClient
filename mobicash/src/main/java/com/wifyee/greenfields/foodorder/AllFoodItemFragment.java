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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wifyee.greenfields.Intents.IntentFactory;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.SharedPrefence.SharedPreference;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.Utils.RecyclerTouchListener;

import com.wifyee.greenfields.constants.NetworkConstant;

import com.wifyee.greenfields.interfaces.FragmentInterface;
import com.wifyee.greenfields.models.response.FailureResponse;

import com.wifyee.greenfields.services.MobicashIntentService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AllFoodItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllFoodItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class AllFoodItemFragment extends Fragment implements FragmentInterface {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Context mContext = null;
    private FoodOderListAdapter foodOderListAdapter;
    public static ArrayList<SharedPrefenceList> sharedPrefenceListProduct = new ArrayList<>();
    private SharedPrefenceList productItem=new SharedPrefenceList();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<FoodOderList> foodOder_List;
    public static List<SharedPrefenceList> shred_list=new ArrayList<>();;
    public static List<FoodOderList> fix_Vale_shred_list=new ArrayList<>();;
    RelativeLayout btn_addToCart;
    @BindView(R.id.food_recyclerview)
    RecyclerView foododerRecycler;
    @BindView(R.id.empty_view_containers)
    LinearLayout emptyView;
    static SharedPreference sharedPreference;
    private OnFragmentInteractionListener mListener;
    public ProgressDialog progressDialog = null;
    public static int selectedPostion;
    FragmentInterface fInterface;
    TextView itemQuantity;

    public AllFoodItemFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AllFoodItemFragment newInstance(String param1, String param2) {
        AllFoodItemFragment fragment = new AllFoodItemFragment();
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
        fInterface = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_food_item, container, false);
        ButterKnife.bind(getActivity(), view);
        foododerRecycler = (RecyclerView) view.findViewById(R.id.food_recyclerview);
        emptyView=(LinearLayout)view.findViewById(R.id.empty_view_containers);
        btn_addToCart=(RelativeLayout) view.findViewById(R.id.add_to_cart);
        itemQuantity=(TextView) view.findViewById(R.id.item_count_price);

        sharedPreference=new SharedPreference(mContext);

        MobicashIntentService.startActionAllFoodOrderList(mContext,getMerchantID());

        /*foododerRecycler.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                foododerRecycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view,  int position) {
                //selectedPostion=position;
                //Values are passing to activity & to fragment as well
                //  Toast.makeText(getContext(), "Single Click on position :" + position,Toast.LENGTH_SHORT).show();
               // CheckBox checkBox=(CheckBox)view.findViewById(R.id.food_checkbox) ;
               // SwitchButton sb=(SwitchButton)view.findViewById(R.id.switchbutton);
                SwitchButton sb=(SwitchButton)foododerRecycler.getChildAt(position).findViewById(R.id.switchbutton);
                Toast.makeText(getActivity(),"Under Process ",Toast.LENGTH_SHORT).show();
                Boolean presnetvlaue=false;
                FoodOderList dataModel= foodOder_List.get(position);
                String price_value=dataModel.getPrice();
                dataModel.checkvalue = !dataModel.checkvalue;
                if(dataModel.checkvalue==true)
                {
                    if (sharedPreference.getFavorites(mContext)!=null) {
                        List<SharedPrefenceList> DefaultOder_lists=sharedPreference.getFavorites(mContext);
                        if (DefaultOder_lists.size()!=0) {
                            for(int y=0;y<sharedPreference.getFavorites(mContext).size();y++)
                            {
                                if(dataModel.name.equalsIgnoreCase(sharedPreference.getFavorites(mContext).get(y).name)) {
                                    presnetvlaue=true;
                                    Toast.makeText(mContext,"This Food Item already Add to Cart",Toast.LENGTH_SHORT).show();
                                    dataModel.checkvalue=false;
                                   // sb.setChecked(false);
                                   // foodOderListAdapter.notifyDataSetChanged();
                                    break;
                                }
                                else {
                                    productItem=new SharedPrefenceList();
                                    productItem.setMercantId(dataModel.itemID);
                                    productItem.setName(dataModel.name);
                                    productItem.setDescription(dataModel.description);
                                    productItem.setFoodImage(dataModel.foodImage);
                                    productItem.setPrice(dataModel.price);
                                    productItem.setQuantiy("1");
                                    sharedPrefenceListProduct.add(productItem);
                                    shred_list.add(productItem);
                                  //  sb.setChecked(true);
                                    ////foodOderListAdapter.notifyDataSetChanged();
                                }
                            }
                        }else{
                            productItem=new SharedPrefenceList();
                            productItem.setName(dataModel.name);
                            productItem.setMercantId(dataModel.itemID);
                            productItem.setDescription(dataModel.description);
                            productItem.setFoodImage(dataModel.foodImage);
                            productItem.setPrice(dataModel.price);
                            productItem.setQuantiy("1");
                            sharedPrefenceListProduct.add(productItem);
                           // sb.setChecked(true);
                            shred_list.add(productItem);
                          //  foodOderListAdapter.notifyDataSetChanged();
                        }
                    }else {
                        productItem=new SharedPrefenceList();
                        productItem.setName(dataModel.name);
                        productItem.setMercantId(dataModel.itemID);
                        productItem.setDescription(dataModel.description);
                        productItem.setFoodImage(dataModel.foodImage);
                        productItem.setPrice(dataModel.price);
                        productItem.setQuantiy("1");
                        sharedPrefenceListProduct.add(productItem);
                        shred_list.add(productItem);
                      //  sb.setChecked(true);
                       // foodOderListAdapter.notifyDataSetChanged();

                    }

                }else if(dataModel.checkvalue==false){
                    for(int i =0;i<sharedPrefenceListProduct.size();i++)
                    {
                        if(sharedPrefenceListProduct.get(i).name.toString().equalsIgnoreCase(dataModel.name))
                        {
                            sharedPrefenceListProduct.remove(i);
                            shred_list.remove(i);
                        //    sb.setChecked(false);
                           // foodOderListAdapter.notifyDataSetChanged();

                        }
                    }
                }
                foodOderListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(), "Long press on position :"+position,
                        Toast.LENGTH_LONG).show();
            }
        }));*/

        btn_addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreference.getFavorites(mContext)!=null) {
                    List<SharedPrefenceList> DefaultOder_list=sharedPreference.getFavorites(mContext);
                    boolean exitvalue=false;
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
                            if (!exitvalue) {
                                productItem.setMercantId(sharedPrefenceListProduct.get(i).mercantId);
                                //productItem.setItemId(sharedPrefenceListProduct.get(i).itemId);
                                productItem.setName(sharedPrefenceListProduct.get(i).name);
                                productItem.setDescription(sharedPrefenceListProduct.get(i).description);
                                productItem.setFoodImage(sharedPrefenceListProduct.get(i).foodImage);
                                productItem.setPrice(sharedPrefenceListProduct.get(i).price);
                                productItem.setQuantiy(sharedPrefenceListProduct.get(i).quantiy);
                                sharedPreference.addFavoriteItem(mContext, productItem);
                                Log.e("merchantId---","merchant "+productItem.getMercantId());
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

    private FoodOrderRequest getMerchantID() {
        FoodOrderRequest foodOrderRequest=new FoodOrderRequest();
        //foodOrderRequest.FoodType= LocalPreferenceUtility.getMerchantId(mContext);
        foodOrderRequest.FoodType= this.getArguments().getString("merchantid");
        return foodOrderRequest;
    }

   /* private CartFoodOderRequest getaddToCartRequest(String finalprice) {
        CartFoodOderRequest foodOrderRequest = new CartFoodOderRequest();
        foodOrderRequest.orderId=dateTime();
        foodOrderRequest.items=shred_list;
        foodOrderRequest.userId="82";
        foodOrderRequest.merchantId= MerchantActivity.selcted_merchantid;
        foodOrderRequest.orderDateTime=dateTime();
        foodOrderRequest.orderPrice=finalprice;
        foodOrderRequest.userType="client";
        return foodOrderRequest;
    }*/

    // TODO: Rename method, update argument and hook method into UI event

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(allfoodOderListReceiver);
    }
    @Override
    public void onResume() {
        super.onResume();
        // Register for all the actions
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mContext);
        for (String action : broadCastReceiverActionList) {
            broadcastManager.registerReceiver(allfoodOderListReceiver, new IntentFilter(action));
        }
        showProgressDialog();
        MobicashIntentService.startActionAllFoodOrderList(mContext,getMerchantID());


    }

    private String[] broadCastReceiverActionList = {
            NetworkConstant.STATUS_ALLFOODODER_LIST_SUCCESS,
            NetworkConstant.STATUS_ALLFOODORDER_LIST_FAIL
    };

   /* public void viewCart(){
        Double totalamount = 0.00;
        if(sharedPreference.getFavorites(mContext).size()>0) {
            *//*for (int i = 0; i < sharedPreference.getFavorites(mContext).size(); i++) {
                String pricevlaue = sharedPreference.getFavorites(mContext).get(i).price.trim();
                Double current = Double.valueOf(pricevlaue);
                totalamount = current + totalamount;
            }*//*
            btn_addToCart.setVisibility(View.VISIBLE);
        }else{
            btn_addToCart.setVisibility(View.GONE);
        }
    }*/

    public void setupUI() {
        foododerRecycler.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
        ///  RecyclerView.LayoutManager recyclerViewLayoutManager = new GridLayoutManager(mContext, 3);
        LinearLayoutManager recyclerViewLayoutManager = new LinearLayoutManager(mContext);
        foododerRecycler.setLayoutManager(recyclerViewLayoutManager);
        foodOderListAdapter = new FoodOderListAdapter(mContext, foodOder_List,fInterface);
        foododerRecycler.setAdapter(foodOderListAdapter);
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

    private BroadcastReceiver allfoodOderListReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String actionOperatorList = intent.getAction();
            if (actionOperatorList.equals(NetworkConstant.STATUS_ALLFOODODER_LIST_SUCCESS)) {
                FoodOrderResponse foodOrderResponse = (FoodOrderResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (foodOrderResponse != null && foodOrderResponse.FoodOderList != null) {
                    cancelProgressDialog();
                    foodOder_List = foodOrderResponse.FoodOderList;
                    fix_Vale_shred_list=foodOrderResponse.FoodOderList;
                    setupUI();
                } else {
                    foododerRecycler.setVisibility(View.INVISIBLE);
                    emptyView.setVisibility(View.VISIBLE);
                    cancelProgressDialog();
                }
            } else if (actionOperatorList.equals(NetworkConstant.STATUS_ALLFOODORDER_LIST_FAIL)) {
                foododerRecycler.setVisibility(View.INVISIBLE);
                emptyView.setVisibility(View.VISIBLE);
                cancelProgressDialog();
                FailureResponse failureResponse = (FailureResponse) intent.getSerializableExtra(NetworkConstant.EXTRA_DATA);
                if (failureResponse != null && failureResponse.msg != null && !failureResponse.msg.isEmpty()) {
                    cancelProgressDialog();
                    showErrorDialog(failureResponse.msg);
                }
                else {
                    showErrorDialog(getString(R.string.error_message));
                }
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
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void fragmentBecameVisible() {
       // viewCart();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

