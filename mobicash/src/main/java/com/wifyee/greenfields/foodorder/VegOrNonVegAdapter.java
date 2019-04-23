package com.wifyee.greenfields.foodorder;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.SharedPrefence.SharedPreference;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.constants.NetworkConstant;


import java.util.List;

/**
 * Created by user on 12/10/2017.
 */

public class VegOrNonVegAdapter extends RecyclerView.Adapter<VegOrNonVegAdapter.ViewHolder>{

    private List<VegORNonVegList> mFoodOderItemCollection;
            Context mContext;
    private RecyclerView mRecyclerView;
    private final LayoutInflater mLayoutInflater;
    SwitchButton switchButton;
    private SharedPrefenceList productItem = new SharedPrefenceList();

public VegOrNonVegAdapter(Context context, RecyclerView recyclerView, List<VegORNonVegList> foodItemCollection) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFoodOderItemCollection = foodItemCollection;
        mRecyclerView = recyclerView;

}




public static class ViewHolder extends RecyclerView.ViewHolder{

    public TextView tv_foodName,tv_foodprice,tv_fooddescrp,tv_category,sb,add;
    ImageView imag_foodimage;
    CheckBox checkBox;

    public ViewHolder(View v){
        super(v);
        tv_foodName = (TextView) v.findViewById(R.id.tv_foodName);
        tv_foodprice = (TextView) v.findViewById(R.id.tv_price);
        tv_fooddescrp = (TextView) v.findViewById(R.id.tv_descprition);
        imag_foodimage=(ImageView)v.findViewById(R.id.imag_food);
        checkBox=(CheckBox)v.findViewById(R.id.food_checkbox);
        tv_category=(TextView) v.findViewById(R.id.tvcategory);
        add=(TextView) v.findViewById(R.id.add);
        sb=(SwitchButton)v.findViewById(R.id.switchbutton);


    }
}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view1 = LayoutInflater.from(mContext).inflate(R.layout.foododer_view_items_layout,parent,false);

        return new ViewHolder(view1);
    }


    @Override
    public void onBindViewHolder(final ViewHolder Vholder, int position){
        final VegORNonVegList FoodOderItem = mFoodOderItemCollection.get(position);

        //  holder.mDateValue.setText(logItem.date);
        Vholder.tv_category.setText(FoodOderItem.category);
        Vholder.tv_foodName.setText(FoodOderItem.name);
        Vholder.tv_fooddescrp.setText(FoodOderItem.description );
        Vholder.tv_foodprice.setText(FoodOderItem.price);
        Vholder.checkBox.setChecked(FoodOderItem.checkvalue);
      //  Vholder.sb.setText(FoodOderItem.checkvalue);
     /*   Vholder.sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Vholder.checkBox.isChecked()) {
                    Toast.makeText(mContext,Vholder.sb.getText(),Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });*/


        // imageLoader.DisplayImage(NetworkConstant.MOBICASH_BASE_URL_TESTING+"/uploads/food/"+FoodOderItem.foodImage, Vholder.imag_foodimage);
        Picasso.with(mContext)
                .load(NetworkConstant.MOBICASH_BASE_URL_TESTING+"/uploads/food/"+FoodOderItem.foodImage)
                .into(Vholder.imag_foodimage);
         //Vholder.imag_foodimage.setBackgroundResource(R.drawable.vada_pao);

        Vholder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("merIdOfFood",FoodOrderListActivity.merchantId);
                Log.e("LocalPreferenceUtility", LocalPreferenceUtility.getMerchantId(mContext));
                Log.e("MerchantName","merName"+LocalPreferenceUtility.getMerchantName(mContext));
                if(FoodOrderListActivity.merchantId.equals(LocalPreferenceUtility.getMerchantId(mContext)) || LocalPreferenceUtility.getMerchantId(mContext).equals("")) {
                    try {
                        LocalPreferenceUtility.putMerchantId(mContext, FoodOrderListActivity.merchantId);
                        LocalPreferenceUtility.putMerchantName(mContext, FoodOrderListActivity.merchantName);
                        if (AllFoodItemFragment.sharedPreference.getFavorites(mContext) != null) {
                            List<SharedPrefenceList> DefaultOder_lists = AllFoodItemFragment.sharedPreference.getFavorites(mContext);
                            if (DefaultOder_lists.size() != 0) {
                                try {
                                    for (int y = 0; y < AllFoodItemFragment.sharedPreference.getFavorites(mContext).size(); y++) {
                                        if (FoodOderItem.name.equalsIgnoreCase(AllFoodItemFragment.sharedPreference.getFavorites(mContext).get(y).name)) {
                                            Toast.makeText(mContext, "This Food Item already Add to Cart", Toast.LENGTH_SHORT).show();
                                            break;
                                        } else {
                                            productItem = new SharedPrefenceList();
                                            productItem.setMercantId(FoodOderItem.itemId);
                                            productItem.setName(FoodOderItem.name);
                                            productItem.setDescription(FoodOderItem.description);
                                            productItem.setFoodImage(FoodOderItem.foodImage);
                                            productItem.setPrice(FoodOderItem.price);
                                            productItem.setQuantiy("1");

                                            Log.w("ITEM--", "ITEM ID " + productItem.getMercantId());

                                            AllNonVegFoodItemFragment.sharedPrefenceListProduct.add(productItem);
                                            Vholder.add.setText("IN CART");
                                            //notifyDataSetChanged();
                                            // AllFoodItemFragment.shred_list.add(productItem);
                                            //  sb.setChecked(true);
                                            ////foodOderListAdapter.notifyDataSetChanged();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                productItem = new SharedPrefenceList();
                                productItem.setName(FoodOderItem.name);
                                productItem.setMercantId(FoodOderItem.itemId);
                                productItem.setDescription(FoodOderItem.description);
                                productItem.setFoodImage(FoodOderItem.foodImage);
                                productItem.setPrice(FoodOderItem.price);
                                productItem.setQuantiy("1");
                                Log.w("ITEM--", "ITEM ID " + productItem.getMercantId());
                                AllNonVegFoodItemFragment.sharedPrefenceListProduct.add(productItem);
                                Vholder.add.setText("IN CART");
                                //notifyDataSetChanged();
                                // sb.setChecked(true);
                                //  AllFoodItemFragment.shred_list.add(productItem);
                                //  foodOderListAdapter.notifyDataSetChanged();
                            }
                        } else {
                            productItem = new SharedPrefenceList();
                            productItem.setName(FoodOderItem.name);
                            productItem.setMercantId(FoodOderItem.itemId);
                            productItem.setDescription(FoodOderItem.description);
                            productItem.setFoodImage(FoodOderItem.foodImage);
                            productItem.setPrice(FoodOderItem.price);
                            productItem.setQuantiy("1");
                            AllNonVegFoodItemFragment.sharedPrefenceListProduct.add(productItem);
                            Vholder.add.setText("IN CART");
                            //AllFoodItemFragment.shred_list.add(productItem);
                            //  sb.setChecked(true);
                            // foodOderListAdapter.notifyDataSetChanged();
                            // notifyDataSetChanged();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    LocalPreferenceUtility.putLatitudeFood(mContext,LocalPreferenceUtility.getLatitude(mContext));
                    LocalPreferenceUtility.putLongitudeFood(mContext,LocalPreferenceUtility.getLongitude(mContext));
                    Log.e("lat","lat set in food VegNonVeg");
                }else {
                    final Dialog layout = new Dialog(mContext);
                    layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    layout.setContentView(R.layout.popup_item_remove_cart);
                    layout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Button cancel = layout.findViewById(R.id.cancel_btn);
                    Button confirm = layout.findViewById(R.id.confirm_btn);
                    TextView message = layout.findViewById(R.id.message);
                    message.setText("Your cart contains dishes from " + LocalPreferenceUtility.getMerchantName(mContext)
                            + ". Do you want to discard the selection and add dishes from " + FoodOrderListActivity.merchantName + "?");
                    layout.show();

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout.dismiss();
                        }
                    });

                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreference sharedPreference = new SharedPreference();
                            sharedPreference.clearCache(mContext);
                            LocalPreferenceUtility.putMerchantId(mContext, FoodOrderListActivity.merchantId);
                            LocalPreferenceUtility.putMerchantName(mContext, FoodOrderListActivity.merchantName);
                            productItem = new SharedPrefenceList();
                            productItem.setName(FoodOderItem.name);
                            productItem.setMercantId(FoodOderItem.itemId);
                            productItem.setDescription(FoodOderItem.description);
                            productItem.setFoodImage(FoodOderItem.foodImage);
                            productItem.setPrice(FoodOderItem.price);
                            productItem.setQuantiy("1");
                            AllNonVegFoodItemFragment.sharedPrefenceListProduct.add(productItem);
                            Log.w("ITEM--", "ITEM ID " + productItem.getMercantId());
                            Vholder.add.setText("IN CART");
                            layout.dismiss();
                        }
                    });
                }
                AllNonVegFoodItemFragment.sharedPreference.addFavorite(mContext,AllNonVegFoodItemFragment.sharedPrefenceListProduct);

            }
        });

    }


    @Override
    public int getItemCount() {
        return (mFoodOderItemCollection != null) ? mFoodOderItemCollection.size() : 0;
    }
}
