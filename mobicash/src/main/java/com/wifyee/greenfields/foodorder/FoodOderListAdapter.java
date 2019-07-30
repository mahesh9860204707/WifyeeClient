package com.wifyee.greenfields.foodorder;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.SharedPrefence.SharedPreference;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.dairyorder.DairyProductListItem;
import com.wifyee.greenfields.dairyorder.OrderSummaryDetails;
import com.wifyee.greenfields.database.DatabaseDB;
import com.wifyee.greenfields.database.SQLController;
import com.wifyee.greenfields.foodorder.SwitchButton;
import com.wifyee.greenfields.foodorder.FoodOderList;
import com.wifyee.greenfields.interfaces.FragmentInterface;

import org.apache.http.util.ByteArrayBuffer;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.wifyee.greenfields.foodorder.AllFoodItemFragment.selectedPostion;

public class FoodOderListAdapter extends RecyclerView.Adapter<FoodOderListAdapter.ViewHolder> {
    private List<FoodOderList> mFoodOderLists;
    private String id="";
    private Context context;
    private SharedPrefenceList productItem = new SharedPrefenceList();
    private Double actual_foodAmount = 0.00;
    private FoodOderListAdapter.ItemListener mListener;

    public FoodOderListAdapter(Context ctx, List<FoodOderList> list,FoodOderListAdapter.ItemListener mListener) {
        this.mFoodOderLists = list;
        this.context = ctx;
        this.mListener = mListener;
    }

    @Override
    public FoodOderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       /* View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_foodorder_view_layout, parent, false);
     */
       View view1 = LayoutInflater.from(context).inflate(R.layout.new_foodorder_view_layout,parent,false);
        return new ViewHolder(view1);

    }

    @Override
    public void onBindViewHolder(final FoodOderListAdapter.ViewHolder holder, final int position) {
        final FoodOderList object = mFoodOderLists.get(position);
        if (object != null) {
            holder.tv_foodprice.setText("₹"+object.getPrice());
            String upperString = object.getName().substring(0,1).toUpperCase() + object.getName().substring(1);

            if(!object.getQuantiy().isEmpty()){
                String quanityString = object.getQuantiy().substring(0,1).toUpperCase() + object.getQuantiy().substring(1).toLowerCase();
                holder.tv_foodName.setText(upperString+" ("+quanityString+")");
            }else {
                holder.tv_foodName.setText(upperString);
            }

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.food_bg4)
                    .error(R.drawable.food_bg4)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(context).load(NetworkConstant.MOBICASH_BASE_URL_TESTING+"/uploads/food/"+object.foodImage)
                    .apply(options)
                    .into(holder.imag_foodimage);

            holder.tv_foodName.setTypeface(Fonts.getSemiBold(context));
            holder.tv_fooddescrp.setTypeface(Fonts.getRegular(context));
            holder.tv_foodprice.setTypeface(Fonts.getRegular(context));
            holder.increment.setTypeface(Fonts.getSemiBold(context));
            holder.decrement.setTypeface(Fonts.getSemiBold(context));
            holder.integerNumber.setTypeface(Fonts.getSemiBold(context));
            holder.txtPlus.setTypeface(Fonts.getSemiBold(context));
            holder.txtAdd.setTypeface(Fonts.getSemiBold(context));
            holder.tvDiscount.setTypeface(Fonts.getRegular(context));

            if (object.getDescription().isEmpty()){
                holder.tv_fooddescrp.setVisibility(View.GONE);
            }else {
                String DescriptionString = object.getDescription().substring(0,1).toUpperCase() + object.getDescription().substring(1);
                holder.tv_fooddescrp.setText(DescriptionString);
                holder.tv_fooddescrp.setVisibility(View.VISIBLE);
            }

            if (object.getDiscountPrice().equals("0.00")){
                holder.icDiscount.setVisibility(View.GONE);
                holder.tvDiscount.setVisibility(View.GONE);
            }else {
                holder.tvDiscount.setText("₹"+object.getDiscountPrice());
                holder.icDiscount.setVisibility(View.VISIBLE);
                holder.tvDiscount.setVisibility(View.VISIBLE);
                holder.icDiscount.animate().rotation(360).setDuration(1000).start();
            }

            if(object.getCategory().equals("1")){
                holder.icCategory.setImageResource(R.drawable.ic_veg1);
            }else {
                holder.icCategory.setImageResource(R.drawable.ic_non_veg1);
            }

            //Picasso.with(context).load(NetworkConstant.MOBICASH_BASE_URL_TESTING+"/uploads/food/"+object.foodImage).into( holder.imag_foodimage);

            if(checkInCart(object.itemID,holder)!=0){
                holder.llAdd.setVisibility(View.INVISIBLE);
                holder.llIncrDecr.setVisibility(View.VISIBLE);
            }else {
                holder.llAdd.setVisibility(View.VISIBLE);
                holder.llIncrDecr.setVisibility(View.INVISIBLE);
            }

            if(FoodOrderListActivity.currentStatus.equals("0")){
                holder.llAdd.setEnabled(false);
                holder.ll.setAlpha(0.3f);
            }else {
                holder.llAdd.setEnabled(true);
                holder.ll.setAlpha(1f);
            }

            holder.increment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int value = Integer.parseInt(holder.integerNumber.getText().toString()) + 1;
                    if (value >= 1) {
                        //checkItemQuantity(myViewHolder,place,place.getItemId(),value);
                        holder.integerNumber.setText("" + value);
                        updateCart(object.itemID, String.valueOf(value));
                        mListener.onBuyCallBack(object, "1",1);
                    }
                }
            });


            holder.decrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int value = Integer.parseInt(holder.integerNumber.getText().toString()) - 1;
                    if (value >= 1) {
                        //checkItemQuantity(myViewHolder,place,place.getItemId(),value);
                        holder.integerNumber.setText("" + value);
                        updateCart(object.itemID, String.valueOf(value));
                    }else {
                        deleteCartItem(object.itemID,holder);
                        holder.llIncrDecr.setVisibility(View.INVISIBLE);
                        holder.llAdd.setVisibility(View.VISIBLE);
                        mListener.onRemoveCartCallBack(object.itemID,0);
                    }
                    mListener.onRemoveCartCallBack("",1);
                }
            });


            /*List<SharedPrefenceList> alreadyAddItems= null;
            try {
                alreadyAddItems = AllFoodItemFragment.sharedPreference.getFavorites(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                assert alreadyAddItems != null;
                if(alreadyAddItems.size() > 0)
                {
                    for (int h=0;h<alreadyAddItems.size();h++)
                    {
                        if((object.getMerchantId().equalsIgnoreCase(alreadyAddItems.get(h).mercantId))||(object.getName().equalsIgnoreCase(alreadyAddItems.get(h).name)))
                        {
                            //holder.changeLayout.setVisibility(View.VISIBLE);
                            holder.tv_add.setText("ADD");
                        }
                    }
                }else{
                    holder.tv_add.setText("IN CART");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            /*holder.img_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        boolean exist=false;
                        List<SharedPrefenceList> alreadyAddItem=AllFoodItemFragment.sharedPreference.getFavorites(context);
                        FoodOderList selectObject  =  mFoodOderLists.get(position);
                        int curent_quty = Integer.parseInt(holder.tv_qty.getText().toString()) + 1;

                        for (int k = 0; k < AllFoodItemFragment.fix_Vale_shred_list.size(); k++) {
                            if (AllFoodItemFragment.fix_Vale_shred_list.get(k).itemID.equalsIgnoreCase(object.itemID)) {
                                actual_foodAmount = 0.00;
                                actual_foodAmount = Double.valueOf(AllFoodItemFragment.fix_Vale_shred_list.get(k).price.toString());
                                break;
                            }
                        }

                        Double curent_amount = actual_foodAmount * curent_quty;
                        try {
                            for (int z=0;z<alreadyAddItem.size();z++)
                            {
                                if((selectObject.getMerchantId().equalsIgnoreCase(alreadyAddItem.get(z).mercantId))||(selectObject.getName().equalsIgnoreCase(alreadyAddItem.get(z).name)))
                                {   holder.tv_qty.setText(String.valueOf(curent_quty));
                                    alreadyAddItem.get(z).quantiy= String.valueOf(curent_quty);
                                    alreadyAddItem.get(z).price=String.valueOf(curent_amount);

                                    SharedPrefenceList curent_productItem;
                                    curent_productItem = new SharedPrefenceList();
                                    curent_productItem.setName(alreadyAddItem.get(z).name);
                                    curent_productItem.setDescription(alreadyAddItem.get(z).description);
                                    curent_productItem.setFoodImage(alreadyAddItem.get(z).foodImage);
                                    curent_productItem.setPrice(alreadyAddItem.get(z).price);
                                    curent_productItem.setQuantiy(alreadyAddItem.get(z).quantiy);
                                    curent_productItem.setMercantId(alreadyAddItem.get(z).mercantId);
                                    AllFoodItemFragment.sharedPreference.updateFavoriteItem(context, curent_productItem);
                                    holder.tv_qty.setText(String.valueOf(curent_quty));
                                    exist=true;
                                    break;
                                    //notifyDataSetChanged();

                                }else {
                                    exist=false;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(!exist)
                        {
                            if(AllFoodItemFragment.sharedPrefenceListProduct.size()>0)
                            {
                                for (int g=0;g<AllFoodItemFragment.sharedPrefenceListProduct.size();g++)
                                {
                                    if(((object.name.equalsIgnoreCase(AllFoodItemFragment.sharedPrefenceListProduct.get(g).name))||(object.getMerchantId().equalsIgnoreCase(AllFoodItemFragment.sharedPrefenceListProduct.get(g).mercantId))))
                                    {
                                        AllFoodItemFragment.sharedPrefenceListProduct.get(g).name=object.name;
                                        AllFoodItemFragment.sharedPrefenceListProduct.get(g).price=String.valueOf(curent_amount);
                                        AllFoodItemFragment.sharedPrefenceListProduct.get(g).foodImage=object.foodImage;
                                        AllFoodItemFragment.sharedPrefenceListProduct.get(g).quantiy=String.valueOf(curent_quty);
                                        AllFoodItemFragment.sharedPrefenceListProduct.get(g).description=object.description;
                                        AllFoodItemFragment.sharedPrefenceListProduct.get(g).itemId=object.itemID;

                                        break;
                                    }
                                 }
                            }
                            holder.tv_qty.setText(String.valueOf(curent_quty));

                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            });
*/
        holder.llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("merIdOfFood",FoodOrderListActivity.merchantId);
                //Log.e("LocalPreferenceUtility",LocalPreferenceUtility.getMerchantId(context));
                //Log.e("MerchantName","merName"+LocalPreferenceUtility.getMerchantName(context));
                if(FoodOrderListActivity.merchantId.equals(LocalPreferenceUtility.getMerchantId(context)) || LocalPreferenceUtility.getMerchantId(context).equals("")) {
                    //if (holder.tv_add.getText().equals("ADD")) {
                    holder.llAdd.setVisibility(View.INVISIBLE);
                    holder.llIncrDecr.setVisibility(View.VISIBLE);
                    mListener.onBuyCallBack(object, "1",0);
                    //holder.tv_add.setText("IN CART");
                    LocalPreferenceUtility.putMerchantId(context, FoodOrderListActivity.merchantId);
                    LocalPreferenceUtility.putMerchantName(context, FoodOrderListActivity.merchantName);
                    LocalPreferenceUtility.putLatitudeFood(context,LocalPreferenceUtility.getLatitude(context));
                    LocalPreferenceUtility.putLongitudeFood(context,LocalPreferenceUtility.getLongitude(context));

                    /*}else if (holder.tv_add.getText().equals("IN CART")){
                        mListener.onRemoveCartCallBack(object.itemID);
                        holder.tv_add.setText("ADD");
                    }*/

                    /*try {
                        LocalPreferenceUtility.putMerchantId(context, FoodOrderListActivity.merchantId);
                        LocalPreferenceUtility.putMerchantName(context, FoodOrderListActivity.merchantName);
                        if (AllFoodItemFragment.sharedPreference.getFavorites(context) != null) {
                            List<SharedPrefenceList> DefaultOder_lists = AllFoodItemFragment.sharedPreference.getFavorites(context);
                            if (DefaultOder_lists.size() != 0) {
                                try {
                                    for (int y = 0; y < AllFoodItemFragment.sharedPreference.getFavorites(context).size(); y++) {
                                        if (object.name.equalsIgnoreCase(AllFoodItemFragment.sharedPreference.getFavorites(context).get(y).name)) {
                                            Toast.makeText(context, "This Food Item already Add to Cart", Toast.LENGTH_SHORT).show();
                                            break;
                                        } else {
                                            productItem = new SharedPrefenceList();
                                            productItem.setMercantId(object.itemID);
                                            productItem.setName(object.name);
                                            productItem.setDescription(object.description);
                                            productItem.setFoodImage(object.foodImage);
                                            productItem.setPrice(object.price);
                                            productItem.setQuantiy("1");

                                            Log.w("ITEM--", "ITEM ID " + productItem.getMercantId());

                                            AllFoodItemFragment.sharedPrefenceListProduct.add(productItem);
                                            holder.tv_add.setText("IN CART");
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
                                productItem.setName(object.name);
                                productItem.setMercantId(object.itemID);
                                productItem.setDescription(object.description);
                                productItem.setFoodImage(object.foodImage);
                                productItem.setPrice(object.price);
                                productItem.setQuantiy("1");
                                AllFoodItemFragment.sharedPrefenceListProduct.add(productItem);
                                Log.w("ITEM--", "ITEM ID " + productItem.getMercantId());
                                holder.tv_add.setText("IN CART");
                                //notifyDataSetChanged();
                                // sb.setChecked(true);
                                //  AllFoodItemFragment.shred_list.add(productItem);
                                //  foodOderListAdapter.notifyDataSetChanged();
                            }
                        } else {
                            productItem = new SharedPrefenceList();
                            productItem.setName(object.name);
                            productItem.setMercantId(object.itemID);
                            productItem.setDescription(object.description);
                            productItem.setFoodImage(object.foodImage);
                            productItem.setPrice(object.price);
                            productItem.setQuantiy("1");
                            AllFoodItemFragment.sharedPrefenceListProduct.add(productItem);
                            Log.w("ITEM--", "ITEM ID " + productItem.getMercantId());
                            holder.tv_add.setText("IN CART");
                            //AllFoodItemFragment.shred_list.add(productItem);
                            //  sb.setChecked(true);
                            // foodOderListAdapter.notifyDataSetChanged();
                            // notifyDataSetChanged();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                }else{
                    final Dialog layout = new Dialog(context);
                    layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    layout.setContentView(R.layout.popup_item_remove_cart);
                    layout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Button cancel = layout.findViewById(R.id.cancel_btn);
                    Button confirm = layout.findViewById(R.id.confirm_btn);
                    TextView message = layout.findViewById(R.id.message);
                    TextView txt = layout.findViewById(R.id.tv);
                    String first = "<font color='#000000'>"+LocalPreferenceUtility.getMerchantName(context)+"</font>";
                    String second = "<font color='#000000'>"+FoodOrderListActivity.merchantName+"</font>";
                    message.setText(Html.fromHtml("Your cart contains dishes from "+first+
                            ". Do you want to discard the selection and add dishes from "+second+"?"));
                    layout.show();

                    txt.setTypeface(Fonts.getSemiBold(context));
                    message.setTypeface(Fonts.getRegular(context));
                    confirm.setTypeface(Fonts.getSemiBold(context));
                    cancel.setTypeface(Fonts.getSemiBold(context));

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout.dismiss();
                        }
                    });

                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //clear cart table
                            deleteCart();
                            LocalPreferenceUtility.putMerchantId(context, FoodOrderListActivity.merchantId);
                            LocalPreferenceUtility.putMerchantName(context, FoodOrderListActivity.merchantName);
                            holder.llIncrDecr.setVisibility(View.VISIBLE);
                            holder.llAdd.setVisibility(View.INVISIBLE);
                            mListener.onBuyCallBack(object,"1",0);
                            /*productItem = new SharedPrefenceList();
                            productItem.setMercantId(object.itemID);
                            productItem.setName(object.name);
                            productItem.setDescription(object.description);
                            productItem.setFoodImage(object.foodImage);
                            productItem.setPrice(object.price);
                            productItem.setQuantiy("1");
                            Log.w("ITEM--", "ITEM ID " + productItem.getMercantId());
                            AllFoodItemFragment.sharedPrefenceListProduct.add(productItem);*/
                            //holder.tv_add.setText("IN CART");
                            layout.dismiss();
                        }
                    });
                }
                //AllFoodItemFragment.sharedPreference.addFavorite(context,AllFoodItemFragment.sharedPrefenceListProduct);
                //fragmentInterface.fragmentBecameVisible();
            }
        });

        /*holder.img_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Boolean exist_remove=false;
                        if (Integer.parseInt(holder.tv_qty.getText().toString())==1) {
                            holder.changeLayout.setVisibility(View.GONE);
                            holder.tv_add.setVisibility(View.VISIBLE);
                            SharedPrefenceList curent_productItem;
                            curent_productItem = new SharedPrefenceList();
                            curent_productItem.setName(object.name);
                            curent_productItem.setDescription(object.description);
                            curent_productItem.setFoodImage(object.foodImage);
                            curent_productItem.setPrice(object.price);
                            curent_productItem.setQuantiy(object.quantiy);
                            AllFoodItemFragment.sharedPreference.removeFavoriteItem(context, curent_productItem);
                            if(AllFoodItemFragment.sharedPrefenceListProduct.size()>0)
                            {
                                for (int g=0;g<AllFoodItemFragment.sharedPrefenceListProduct.size();g++)
                                {
                                    if(((object.name.equalsIgnoreCase(AllFoodItemFragment.sharedPrefenceListProduct.get(g).name))||(object.getMerchantId().equalsIgnoreCase(AllFoodItemFragment.sharedPrefenceListProduct.get(g).mercantId))))
                                    {
                                        AllFoodItemFragment.sharedPrefenceListProduct.remove(g);
                                        break;
                                    }
                                }
                            }

                        } else {
                            holder.changeLayout.setVisibility(View.VISIBLE);
                            holder.tv_add.setVisibility(View.GONE);
                            List<SharedPrefenceList> alreadyAddItem_remove=AllFoodItemFragment.sharedPreference.getFavorites(context);
                            FoodOderList selectObject  =  mFoodOderLists.get(position);
                            int curent_quty = Integer.parseInt(holder.tv_qty.getText().toString()) - 1;
                            for (int k = 0; k < AllFoodItemFragment.fix_Vale_shred_list.size(); k++) {
                                if (AllFoodItemFragment.fix_Vale_shred_list.get(k).itemID.equalsIgnoreCase(object.itemID)) {
                                    actual_foodAmount = 0.00;
                                    actual_foodAmount = Double.valueOf(AllFoodItemFragment.fix_Vale_shred_list.get(k).price.toString());
                                    break;
                                }
                            }
                            Double curent_amount = actual_foodAmount * curent_quty;
                            try {
                                for (int z=0;z<alreadyAddItem_remove.size();z++)
                                {

                                    if((selectObject.getMerchantId().equalsIgnoreCase(alreadyAddItem_remove.get(z).mercantId))||(selectObject.getName().equalsIgnoreCase(alreadyAddItem_remove.get(z).name)))
                                    {   holder.tv_qty.setText(String.valueOf(curent_quty));
                                        alreadyAddItem_remove.get(z).quantiy= String.valueOf(curent_quty);
                                        alreadyAddItem_remove.get(z).price=String.valueOf(curent_amount);

                                        SharedPrefenceList curent_productItem;
                                        curent_productItem = new SharedPrefenceList();
                                        curent_productItem.setName(alreadyAddItem_remove.get(z).name);
                                        curent_productItem.setDescription(alreadyAddItem_remove.get(z).description);
                                        curent_productItem.setFoodImage(alreadyAddItem_remove.get(z).foodImage);
                                        curent_productItem.setPrice(alreadyAddItem_remove.get(z).price);
                                        curent_productItem.setQuantiy(alreadyAddItem_remove.get(z).quantiy);
                                        AllFoodItemFragment.sharedPreference.updateFavoriteItem(context, curent_productItem);
                                        holder.tv_qty.setText(String.valueOf(curent_quty));
                                        exist_remove=true;
                                        break;
                                        //notifyDataSetChanged();

                                    }else {
                                        exist_remove=false;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(exist_remove==false)
                            {
                                if(AllFoodItemFragment.sharedPrefenceListProduct.size()>0)
                                {
                                    for (int g=0;g<AllFoodItemFragment.sharedPrefenceListProduct.size();g++)
                                    {
                                        if(((object.name.equalsIgnoreCase(AllFoodItemFragment.sharedPrefenceListProduct.get(g).name))||(object.getMerchantId().equalsIgnoreCase(AllFoodItemFragment.sharedPrefenceListProduct.get(g).mercantId))))
                                        {
                                            AllFoodItemFragment.sharedPrefenceListProduct.get(g).name=object.name;
                                            AllFoodItemFragment.sharedPrefenceListProduct.get(g).price=String.valueOf(curent_amount);
                                            AllFoodItemFragment.sharedPrefenceListProduct.get(g).foodImage=object.foodImage;
                                            AllFoodItemFragment.sharedPrefenceListProduct.get(g).quantiy=String.valueOf(curent_quty);
                                            AllFoodItemFragment.sharedPrefenceListProduct.get(g).description=object.description;
                                            break;
                                        }
                                    }
                                }
                                holder.tv_qty.setText(String.valueOf(curent_quty));

                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            });*/

        }
    }



    @Override
    public int getItemCount() {

        return mFoodOderLists.size();
    }

    /*@Override
    public int getItemViewType(int position) {
        return mFoodOderLists.get(position);
    }*/


    public  class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_foodName,tv_foodprice,tv_fooddescrp,tv_add,tv_qty,txtAdd,txtPlus,increment,decrement,integerNumber,tvDiscount;
        private ImageView imag_foodimage,icDiscount,icCategory;
        private LinearLayout ll,llAdd,llIncrDecr;

        public ViewHolder(View v) {
            super(v);
            tv_add =  v.findViewById(R.id.add);
            tv_foodName =  v.findViewById(R.id.tv_foodName);
            tv_foodprice =  v.findViewById(R.id.tv_price);
            tv_fooddescrp =  v.findViewById(R.id.tv_descprition);
            imag_foodimage = v.findViewById(R.id.imag_food);
            icDiscount = v.findViewById(R.id.ic_discount);
            icCategory = v.findViewById(R.id.category_image);
            ll = v.findViewById(R.id.ll);
            llAdd = v.findViewById(R.id.ll_add);
            llIncrDecr = v.findViewById(R.id.ll_incr_decr);
            txtAdd = v.findViewById(R.id.txt_add);
            txtPlus = v.findViewById(R.id.txt_plus);
            increment = v.findViewById(R.id.increase);
            decrement = v.findViewById(R.id.decrease);
            integerNumber = v.findViewById(R.id.integer_number);
            tvDiscount = v.findViewById(R.id.tv_discount);
        }
    }

    public interface ItemListener {
        void onBuyCallBack(FoodOderList item,String quantity,int flag);
        void onRemoveCartCallBack(String id,int flag);
    }

    public int checkInCart(String itemId,ViewHolder holder) {
        int total;
        SQLController controller=new SQLController(context);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);
        String query = "SELECT * from food_cart where item_id='"+itemId+"'";

        Cursor data = controller.retrieve(query);
        total = data.getCount();
        //Log.e("itemCount",String.valueOf(total));
        if(data.getCount()>0){
            data.moveToFirst();
            do{
                String quantity = data.getString(data.getColumnIndex("quantity"));
                holder.integerNumber.setText(quantity);

            }while (data.moveToNext());
        }
        data.close();
        controller.close();
        return total;
    }

    private void deleteCart(){
        SQLController controller = new SQLController(context);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);

        String query = "DELETE from food_cart";
        String result = controller.fireQuery(query);

        if(result.equals("Done")){
            Log.d("delete","Record All delete");
        }else {
            Log.d("result",result);
        }
        controller.close();
    }

    private void deleteCartItem(String id,ViewHolder holder){
        SQLController controller = new SQLController(context);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);

        String query = "DELETE from food_cart where item_id ='"+id+"'";
        String result = controller.fireQuery(query);

        if(result.equals("Done")){
            Log.d("delete","Record delete");
            holder.llIncrDecr.setVisibility(View.INVISIBLE);
            holder.llAdd.setVisibility(View.VISIBLE);
        }else {
            Log.d("result",result);
        }
        controller.close();
    }

    private void updateCart(String id,String quantity){
        SQLController controller = new SQLController(context);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);

        String query = "UPDATE food_cart set quantity='"+quantity+"' where item_id ='"+id+"'";
        String result = controller.fireQuery(query);

        if(result.equals("Done")){
            Log.d("update","Record update");
        }else {
            Log.d("result",result);
        }
        controller.close();
    }
}