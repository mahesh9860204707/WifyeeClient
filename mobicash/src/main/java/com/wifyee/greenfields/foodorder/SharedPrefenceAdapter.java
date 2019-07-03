package com.wifyee.greenfields.foodorder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.SharedPrefence.SharedPreference;

import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.database.DatabaseDB;
import com.wifyee.greenfields.database.SQLController;
import com.wifyee.greenfields.interfaces.FragmentInterface;

import java.util.List;
/**
 * Created by user on 12/10/2017.
 */
public class SharedPrefenceAdapter  extends RecyclerView.Adapter<SharedPrefenceAdapter.ViewHolder> {
    private List<SharedPrefenceList> mFoodOderItemCollection;
    Context mContext;
    private RecyclerView mRecyclerView;
    private final LayoutInflater mLayoutInflater;
    Integer totalquanty = 0;
    Double actual_foodAmount = 0.00;
    Double Chnge_totalamount = 0.00;
    SharedPreference sharedPreference;
    FragmentInterface fInterface;

    public SharedPrefenceAdapter(Context context, RecyclerView recyclerView, List<SharedPrefenceList> foodItemCollection,
                                 FragmentInterface fInterface) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFoodOderItemCollection = foodItemCollection;
        mRecyclerView = recyclerView;
        this.fInterface = fInterface;
    //sharedPreference = new SharedPreference(mContext);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_foodName, tv_foodprice, tv_fooddescrp, tv_foodquanity, tv_quantityNumber,tvDiscount;
        ImageView imag_foodimage,icDiscount;
        TextView  img_minus, img_plus;
        public ViewHolder(View v) {
            super(v);
            tv_foodName =  v.findViewById(R.id.tv_productname);
            tv_foodprice =  v.findViewById(R.id.tv_price);
            tv_fooddescrp =  v.findViewById(R.id.tv_descprition);
            //tv_foodquanity =  v.findViewById(R.id.tv_quantity);
            imag_foodimage =  v.findViewById(R.id.imag_food);
            icDiscount = v.findViewById(R.id.ic_discount);
            tv_quantityNumber =  v.findViewById(R.id.tv_quantityNumber);
            img_minus =  v.findViewById(R.id.minus);
            img_plus =  v.findViewById(R.id.plus);
            tvDiscount = v.findViewById(R.id.tv_discount);
        }
    }

   @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.addtocartlistlayout, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder Vholder, final int position) {
        final SharedPrefenceList CartFoodOderItem = mFoodOderItemCollection.get(position);
        String upperString = CartFoodOderItem.name.substring(0,1).toUpperCase() + CartFoodOderItem.name.substring(1);
        Vholder.tv_foodName.setText(upperString);
        String DescriptionString = CartFoodOderItem.getDescription().substring(0,1).toUpperCase() + CartFoodOderItem.getDescription().substring(1);
        Vholder.tv_fooddescrp.setText(DescriptionString);
        Vholder.tv_foodprice.setText("₹"+CartFoodOderItem.getCalculatedAmt());
        //Vholder.tv_foodquanity.setText(CartFoodOderItem.quantiy);
        Vholder.tv_quantityNumber.setText(CartFoodOderItem.quantiy);

        Vholder.tv_foodName.setTypeface(Fonts.getSemiBold(mContext));
        Vholder.tv_fooddescrp.setTypeface(Fonts.getRegular(mContext));
        Vholder.tv_foodprice.setTypeface(Fonts.getRegular(mContext));
        Vholder.img_plus.setTypeface(Fonts.getSemiBold(mContext));
        Vholder.img_minus.setTypeface(Fonts.getSemiBold(mContext));
        Vholder.tv_quantityNumber.setTypeface(Fonts.getSemiBold(mContext));
        Vholder.tvDiscount.setTypeface(Fonts.getRegular(mContext));

        if (CartFoodOderItem.getDescription().isEmpty()){
            Vholder.tv_fooddescrp.setVisibility(View.GONE);
        }else {
            Vholder.tv_fooddescrp.setVisibility(View.VISIBLE);
        }

        /*if (CartFoodOderItem.getDiscountPrice().equals("0.00")){
            Vholder.icDiscount.setVisibility(View.GONE);
            Vholder.tvDiscount.setVisibility(View.GONE);
        }else {
            Vholder.tvDiscount.setText("₹"+CartFoodOderItem.getDiscountPrice());
            Vholder.icDiscount.setVisibility(View.VISIBLE);
            Vholder.tvDiscount.setVisibility(View.VISIBLE);
        }*/


        /*Vholder.tv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SingleFoodItemActivity.class);
                intent.putExtra("food_name", mFoodOderItemCollection.get(position).name);
                intent.putExtra("food_desc", mFoodOderItemCollection.get(position).description);
                intent.putExtra("food_price", mFoodOderItemCollection.get(position).price);
                intent.putExtra("food_qty", mFoodOderItemCollection.get(position).quantiy);
                intent.putExtra("food_image", mFoodOderItemCollection.get(position).foodImage);
                intent.putExtra("food_id", mFoodOderItemCollection.get(position).mercantId);
                mContext.startActivity(intent);
            }
        });*/

        /*Vholder.tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFoodOderItemCollection.remove(position);
                deleteCart(CartFoodOderItem.getItemId());
                notifyDataSetChanged();
                fInterface.fragmentBecameVisible();
            }
        });*/

        Vholder.img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = Integer.parseInt(Vholder.tv_quantityNumber.getText().toString()) + 1;
                if (value >= 1) {
                    //checkItemQuantity(myViewHolder,place,place.getItemId(),value);
                    Vholder.tv_quantityNumber.setText("" + value);
                    CartFoodOderItem.setQuantiy(Vholder.tv_quantityNumber.getText().toString());
                    double sum = Double.parseDouble(CartFoodOderItem.getPrice()) * value;
                    Vholder.tv_foodprice.setText("₹"+sum);
                    updateCart(CartFoodOderItem.getItemId(),String.valueOf(value));
                    fInterface.fragmentBecameVisible();
                }

                /*Chnge_totalamount = 0.00;
                SharedPrefenceList sharedPrefenceList = mFoodOderItemCollection.get(position);
                int curent_quty = Integer.parseInt(sharedPrefenceList.getQuantiy()) + 1;
                sharedPrefenceList.setQuantiy(String.valueOf(curent_quty));
                Vholder.tv_quantityNumber.setText(sharedPrefenceList.getQuantiy());
                for (int k = 0; k < AllFoodItemFragment.fix_Vale_shred_list.size(); k++) {
                    if (AllFoodItemFragment.fix_Vale_shred_list.get(k).name.toString().equalsIgnoreCase(sharedPrefenceList.name)) {
                        actual_foodAmount = 0.00;
                        actual_foodAmount = Double.valueOf(AllFoodItemFragment.fix_Vale_shred_list.get(k).price.toString());
                        break;
                    }
                }
                Double curent_amount = actual_foodAmount * curent_quty;
                //   Double curent_amount=Double.valueOf(sharedPrefenceList.getPrice())*curent_quty;
                sharedPrefenceList.setPrice(String.valueOf(curent_amount));
                for (int j = 0; j < mFoodOderItemCollection.size(); j++) {
                    String currentTotalpricevlaue = mFoodOderItemCollection.get(j).getPrice();
                    Double current = Double.valueOf(currentTotalpricevlaue);
                    Chnge_totalamount = current + Chnge_totalamount;
                }
                AddToCartActivity.tv_totalamount.setText(String.valueOf(Chnge_totalamount));
                SharedPrefenceList curent_productItem;
                curent_productItem = new SharedPrefenceList();
                curent_productItem.setName(mFoodOderItemCollection.get(position).name);
                curent_productItem.setDescription(mFoodOderItemCollection.get(position).description);
                curent_productItem.setFoodImage(mFoodOderItemCollection.get(position).foodImage);
                curent_productItem.setPrice(mFoodOderItemCollection.get(position).price);
                curent_productItem.setQuantiy(mFoodOderItemCollection.get(position).quantiy);
                sharedPreference.updateFavoriteItem(mContext, curent_productItem);
                Vholder.tv_quantityNumber.setText(sharedPrefenceList.getQuantiy());
                notifyDataSetChanged();*/

            }
        });

        Vholder.img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = Integer.parseInt(Vholder.tv_quantityNumber.getText().toString()) - 1;
                if (value >= 1) {
                    //checkItemQuantity(myViewHolder,place,place.getItemId(),value);
                    Vholder.tv_quantityNumber.setText("" + value);
                    CartFoodOderItem.setQuantiy(Vholder.tv_quantityNumber.getText().toString());
                    double sum = Double.parseDouble(CartFoodOderItem.getPrice()) * value;
                    Vholder.tv_foodprice.setText("₹"+sum);
                    updateCart(CartFoodOderItem.getItemId(),String.valueOf(value));
                    fInterface.fragmentBecameVisible();
                }else {
                    mFoodOderItemCollection.remove(position);
                    deleteCart(CartFoodOderItem.getItemId());
                    notifyDataSetChanged();
                    fInterface.fragmentBecameVisible();
                }

                /*Chnge_totalamount = 0.00;
                SharedPrefenceList sharedPrefenceList = mFoodOderItemCollection.get(position);
                if (Integer.parseInt(sharedPrefenceList.getQuantiy()) > 1) {
                    int curent_quty = Integer.parseInt(sharedPrefenceList.getQuantiy()) - 1;
                    sharedPrefenceList.setQuantiy(String.valueOf(curent_quty));
                    Vholder.tv_quantityNumber.setText(sharedPrefenceList.getQuantiy());
                    for (int k = 0; k < AllFoodItemFragment.fix_Vale_shred_list.size(); k++) {
                        if (AllFoodItemFragment.fix_Vale_shred_list.get(k).name.toString().equalsIgnoreCase(sharedPrefenceList.name)) {
                            actual_foodAmount = 0.00;
                            actual_foodAmount = Double.valueOf(AllFoodItemFragment.fix_Vale_shred_list.get(k).price.toString());
                            break;
                        }
                    }
                    Double curent_amount = actual_foodAmount * curent_quty;
                    //   Double curent_amount=Double.valueOf(sharedPrefenceList.getPrice())*curent_quty;
                    sharedPrefenceList.setPrice(String.valueOf(curent_amount));
                    for (int j = 0; j < mFoodOderItemCollection.size(); j++) {
                        String currentTotalpricevlaue = mFoodOderItemCollection.get(j).getPrice();
                        Double current = Double.valueOf(currentTotalpricevlaue);
                        Chnge_totalamount = current + Chnge_totalamount;
                    }
                    AddToCartActivity.tv_totalamount.setText(String.valueOf(Chnge_totalamount));
                } else {
                    Toast.makeText(mContext, "Minimum quantity is one.", Toast.LENGTH_SHORT).show();
                }
                SharedPrefenceList curent_productItem;
                curent_productItem = new SharedPrefenceList();
                curent_productItem.setName(mFoodOderItemCollection.get(position).name);
                curent_productItem.setDescription(mFoodOderItemCollection.get(position).description);
                curent_productItem.setFoodImage(mFoodOderItemCollection.get(position).foodImage);
                curent_productItem.setPrice(mFoodOderItemCollection.get(position).price);
                curent_productItem.setQuantiy(mFoodOderItemCollection.get(position).quantiy);
                sharedPreference.updateFavoriteItem(mContext, curent_productItem);
                Vholder.tv_quantityNumber.setText(sharedPrefenceList.getQuantiy());
                notifyDataSetChanged();*/
            }
        });

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.food_bg4)
                .error(R.drawable.food_bg4)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(mContext).load(NetworkConstant.MOBICASH_BASE_URL_TESTING+"/uploads/food/"+CartFoodOderItem.foodImage)
                .apply(options)
                .into(Vholder.imag_foodimage);

        // imageLoader.DisplayImage(NetworkConstant.MOBICASH_BASE_URL_TESTING+"/uploads/food/"+FoodOderItem.foodImage, Vholder.imag_foodimage);
        //Picasso.with(mContext).load(NetworkConstant.MOBICASH_BASE_URL_TESTING+"/uploads/food/"+CartFoodOderItem.foodImage)
        // .into(Vholder.imag_foodimage);
      //  Vholder.imag_foodimage.setBackgroundResource(R.drawable.vada_pao);

    }
    @Override
    public int getItemCount() {
        return (mFoodOderItemCollection != null) ? mFoodOderItemCollection.size() : 0;
        //return  mFoodOderItemCollection.size() ;
    }

    private void deleteCart(String id){
        SQLController controller = new SQLController(mContext);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);

        String query = "DELETE from food_cart_item where item_id ='"+id+"'";
        String result = controller.fireQuery(query);

        if(result.equals("Done")){
            Log.d("delete","Record delete");
        }else {
            Log.d("result",result);
        }
        controller.close();
    }

    private void updateCart(String id,String quantity){
        SQLController controller = new SQLController(mContext);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);

        String query = "UPDATE food_cart_item set quantity='"+quantity+"' where item_id ='"+id+"'";
        String result = controller.fireQuery(query);

        if(result.equals("Done")){
            Log.d("update","Record update");
        }else {
            Log.d("result",result);
        }
        controller.close();
    }
}
