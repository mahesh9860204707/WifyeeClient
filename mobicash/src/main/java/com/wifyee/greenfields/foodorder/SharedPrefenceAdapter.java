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

import com.squareup.picasso.Picasso;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.SharedPrefence.SharedPreference;

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
        public TextView tv_foodName, tv_foodprice, tv_fooddescrp, tv_foodquanity, tv_quantityNumber, tv_view, tv_remove;
        ImageView imag_foodimage;
        ImageView  img_minus, img_plus;
        public ViewHolder(View v) {
            super(v);
            tv_view = (TextView) v.findViewById(R.id.tv_view);
            tv_remove = (TextView) v.findViewById(R.id.tv_remove);
            tv_foodName = (TextView) v.findViewById(R.id.tv_productname);
            tv_foodprice = (TextView) v.findViewById(R.id.tv_price);
            tv_fooddescrp = (TextView) v.findViewById(R.id.tv_descprition);
            tv_foodquanity = (TextView) v.findViewById(R.id.tv_quantity);
            imag_foodimage = (ImageView) v.findViewById(R.id.imag_food);
            tv_quantityNumber = (TextView) v.findViewById(R.id.tv_quantityNumber);
            img_minus = (ImageView) v.findViewById(R.id.minus);
            img_plus = (ImageView) v.findViewById(R.id.plus);
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
        //  holder.mDateValue.setText(logItem.date);
        Vholder.tv_foodName.setText(CartFoodOderItem.name);
        Vholder.tv_fooddescrp.setText(CartFoodOderItem.description);
        Vholder.tv_foodprice.setText("₹"+CartFoodOderItem.getCalculatedAmt());
        Vholder.tv_foodquanity.setText(CartFoodOderItem.quantiy);
        Vholder.tv_quantityNumber.setText(CartFoodOderItem.quantiy);

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

        Vholder.tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Double curent_total = 0.00;
                //add the item to remove
                SharedPrefenceList curent_productItem;
                curent_productItem = new SharedPrefenceList();
                curent_productItem.setName(mFoodOderItemCollection.get(position).name);
                curent_productItem.setDescription(mFoodOderItemCollection.get(position).description);
                curent_productItem.setFoodImage(mFoodOderItemCollection.get(position).foodImage);
                curent_productItem.setPrice(mFoodOderItemCollection.get(position).price);
                curent_productItem.setQuantiy(mFoodOderItemCollection.get(position).quantiy);
                sharedPreference.removeFavoriteItem(mContext, curent_productItem);
                mFoodOderItemCollection.remove(position);
                for (int u = 0; u < mFoodOderItemCollection.size(); u++) {
                    String pricevlaue = mFoodOderItemCollection.get(u).price.trim();
                    Double current = Double.valueOf(pricevlaue);
                    curent_total = current + curent_total;
                }
                AddToCartActivity.tv_totalamount.setText(String.valueOf(curent_total));
                notifyDataSetChanged();*/

                mFoodOderItemCollection.remove(position);
                deleteCart(CartFoodOderItem.getItemId());
                notifyDataSetChanged();
                fInterface.fragmentBecameVisible();
            }
        });

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
        // imageLoader.DisplayImage(NetworkConstant.MOBICASH_BASE_URL_TESTING+"/uploads/food/"+FoodOderItem.foodImage, Vholder.imag_foodimage);
          Picasso.with(mContext).load(NetworkConstant.MOBICASH_BASE_URL_TESTING+"/uploads/food/"+CartFoodOderItem.foodImage).into(Vholder.imag_foodimage);
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
