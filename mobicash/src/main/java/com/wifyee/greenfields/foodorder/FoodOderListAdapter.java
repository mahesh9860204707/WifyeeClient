package com.wifyee.greenfields.foodorder;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.SharedPrefence.SharedPreference;
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
            holder.tv_foodprice.setText(object.getPrice());
            holder.tv_foodName.setText(object.getName());
            holder.tv_fooddescrp.setText(object.getDescription());
            holder.sb.setTag(position);
            holder.sb.setChecked(object.isCheckvalue());
            Picasso.with(context).load(NetworkConstant.MOBICASH_BASE_URL_TESTING+"/uploads/food/"+object.foodImage).into( holder.imag_foodimage);

            if(checkInCart(object.itemID)!=0){
                holder.tv_add.setText("IN CART");
            }else {
                holder.tv_add.setText("ADD");
            }

            if(FoodOrderListActivity.currentStatus.equals("0")){
                holder.tv_add.setEnabled(false);
                holder.ll.setAlpha(0.3f);
            }else {
                holder.tv_add.setEnabled(true);
                holder.ll.setAlpha(1f);
            }
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
        holder.tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("merIdOfFood",FoodOrderListActivity.merchantId);
                Log.e("LocalPreferenceUtility",LocalPreferenceUtility.getMerchantId(context));
                Log.e("MerchantName","merName"+LocalPreferenceUtility.getMerchantName(context));
                if(FoodOrderListActivity.merchantId.equals(LocalPreferenceUtility.getMerchantId(context)) || LocalPreferenceUtility.getMerchantId(context).equals("")) {
                    if (holder.tv_add.getText().equals("ADD")) {
                        mListener.onBuyCallBack(object, "1");
                        holder.tv_add.setText("IN CART");
                        LocalPreferenceUtility.putMerchantId(context, FoodOrderListActivity.merchantId);
                        LocalPreferenceUtility.putMerchantName(context, FoodOrderListActivity.merchantName);
                        LocalPreferenceUtility.putLatitudeFood(context,LocalPreferenceUtility.getLatitude(context));
                        LocalPreferenceUtility.putLongitudeFood(context,LocalPreferenceUtility.getLongitude(context));

                    }else if (holder.tv_add.getText().equals("IN CART")){
                        mListener.onRemoveCartCallBack(object.itemID);
                        holder.tv_add.setText("ADD");
                    }

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
                    message.setText("Your cart contains dishes from "+LocalPreferenceUtility.getMerchantName(context)
                            +". Do you want to discard the selection and add dishes from "+FoodOrderListActivity.merchantName+"?");
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
                            /*SharedPreference sharedPreference = new SharedPreference();
                            sharedPreference.clearCache(context);*/

                            //clear cart table
                            deleteCart();
                            LocalPreferenceUtility.putMerchantId(context, FoodOrderListActivity.merchantId);
                            LocalPreferenceUtility.putMerchantName(context, FoodOrderListActivity.merchantName);
                            mListener.onBuyCallBack(object,"1");
                            /*productItem = new SharedPrefenceList();
                            productItem.setMercantId(object.itemID);
                            productItem.setName(object.name);
                            productItem.setDescription(object.description);
                            productItem.setFoodImage(object.foodImage);
                            productItem.setPrice(object.price);
                            productItem.setQuantiy("1");
                            Log.w("ITEM--", "ITEM ID " + productItem.getMercantId());
                            AllFoodItemFragment.sharedPrefenceListProduct.add(productItem);*/
                            holder.tv_add.setText("IN CART");

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

        private TextView tv_foodName,tv_foodprice,tv_fooddescrp,tv_add,tv_qty;
        private SwitchButton sb;
        private ImageView imag_foodimage;
        private CheckBox checkBox;
        private LinearLayout changeLayout,ll;
        ImageView img_add,img_sub;

        public ViewHolder(View v) {
            super(v);
            img_add=(ImageView)v.findViewById(R.id.countadd);
            img_sub=(ImageView) v.findViewById(R.id.remove);
            tv_qty=(TextView)v.findViewById(R.id.qty) ;
            changeLayout=(LinearLayout) v.findViewById(R.id.changelayout);
            tv_add=(TextView)v.findViewById(R.id.add);
            tv_foodName = (TextView) v.findViewById(R.id.tv_foodName);
            tv_foodprice = (TextView) v.findViewById(R.id.tv_price);
            tv_fooddescrp = (TextView) v.findViewById(R.id.tv_descprition);
            imag_foodimage=(ImageView)v.findViewById(R.id.imag_food);
            checkBox=(CheckBox)v.findViewById(R.id.food_checkbox);
            sb=(SwitchButton)v.findViewById(R.id.switchbutton);
            ll =(LinearLayout) v.findViewById(R.id.ll);
        }
    }

    public interface ItemListener {
        void onBuyCallBack(FoodOderList item,String quantity);
        void onRemoveCartCallBack(String id);
    }

    public int checkInCart(String itemId) {
        int total;
        SQLController controller=new SQLController(context);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);
        String query = "SELECT item_id from food_cart_item where item_id='"+itemId+"'";

        Cursor data = controller.retrieve(query);

        total = data.getCount();
        Log.e("itemCount",String.valueOf(total));

        data.close();
        controller.close();
        return total;
    }

    private void deleteCart(){
        SQLController controller = new SQLController(context);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);

        String query = "DELETE from food_cart_item";
        String result = controller.fireQuery(query);

        if(result.equals("Done")){
            Log.d("delete","Record All delete");
        }else {
            Log.d("result",result);
        }
        controller.close();
    }
}