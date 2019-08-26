package com.wifyee.greenfields.dairyorder;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.database.DatabaseDB;
import com.wifyee.greenfields.database.SQLController;
import com.wifyee.greenfields.interfaces.FragmentInterface;
import com.wifyee.greenfields.models.ItemQtyModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlaceOrderAdapter extends RecyclerView.Adapter{

    //List mValues;
    Context mContext;
    ArrayAdapter<String> dataAdapter;
    public ArrayList<PlaceOrderData> placeOrderData;
    FragmentInterface fInterface;
    public ProgressDialog progressDialog;

    public PlaceOrderAdapter(Context context,ArrayList<PlaceOrderData> placeOrderData,FragmentInterface fInterface) {
        this.placeOrderData = placeOrderData;
        this.fInterface = fInterface;
        mContext = context;
    }

    @Override
    public PlaceOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_summary_adapter_layout, parent, false);
        return new PlaceOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder vHolder, final int position) {
        final PlaceOrderAdapter.ViewHolder myViewHolder = (PlaceOrderAdapter.ViewHolder) vHolder;
        final PlaceOrderData place = placeOrderData.get(position);

        myViewHolder.setData(placeOrderData.get(position));

        myViewHolder.textViewType.setTypeface(Fonts.getSemiBold(mContext));
        myViewHolder.tvQuality.setTypeface(Fonts.getRegular(mContext));
        myViewHolder.tvPrice.setTypeface(Fonts.getRegular(mContext));
        myViewHolder.increase.setTypeface(Fonts.getSemiBold(mContext));
        myViewHolder.decrease.setTypeface(Fonts.getSemiBold(mContext));
        myViewHolder.integerNumber.setTypeface(Fonts.getSemiBold(mContext));
        myViewHolder.discountAmt.setTypeface(Fonts.getRegular(mContext));


        myViewHolder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.parseInt(myViewHolder.integerNumber.getText().toString()) + 1;
                if (value >= 1) {
                   checkItemQuantity(myViewHolder,place,place.getItemId(),value);
                }
            }
        });

        myViewHolder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.parseInt(myViewHolder.integerNumber.getText().toString()) - 1;
                if (value >= 1) {
                    //checkItemQuantity(myViewHolder,place,place.getItemId(),value);
                    myViewHolder.integerNumber.setText("" + value);
                    place.setQuantity(myViewHolder.integerNumber.getText().toString());
                    double sum = Double.parseDouble(place.getOrderPrice()) * value;
                    double discount = Double.parseDouble(place.getItemDiscount()) * value;
                    double roundOff = Math.round(discount * 100.0) / 100.0;
                    String s = String.format("%.2f", roundOff);
                    myViewHolder.discountAmt.setText("₹"+s);
                    myViewHolder.tvPrice.setText("₹"+sum);
                    updateCart(place.getItemId(),String.valueOf(value));
                    fInterface.fragmentBecameVisible();
                }else {
                    placeOrderData.remove(position);
                    deleteCart(place.getItemId());
                    notifyDataSetChanged();
                    fInterface.fragmentBecameVisible();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return placeOrderData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewType,decrease,increase,integerNumber,tvQuality,discountAmt,tvPrice,tvItemUnit;
        public ImageView imageView;
        public View exceed;

        public ViewHolder(View v) {
            super(v);
            textViewType =  v.findViewById(R.id.textViewType);
            imageView =  v.findViewById(R.id.imageView);
            tvQuality =  v.findViewById(R.id.textViewQuality);
            tvPrice =  v.findViewById(R.id.textViewPrice);
            tvItemUnit =  v.findViewById(R.id.quantity_unit);
            increase =  v.findViewById(R.id.increase);
            decrease =  v.findViewById(R.id.decrease);
            integerNumber =  v.findViewById(R.id.integer_number);
            //exceed =  v.findViewById(R.id.exceed);
            discountAmt =  v.findViewById(R.id.distcount_amt);
        }

        public void setData(PlaceOrderData item) {
            //this.item = item;
            String upperString = item.getItemName().substring(0,1).toUpperCase() + item.getItemName().substring(1).toLowerCase();
            textViewType.setText(upperString);
            tvPrice.setText("₹" + item.getCalculatedAmt());
            //tvQuantity.setText("Total item:"+item.getQuantity());
            integerNumber.setText(item.getQuantity());
            tvQuality.setText(item.getItemType());
            //tvItemUnit.setText("Unit:"+item.getQuantityUnit());
            tvItemUnit.setText(item.getQuantityUnit());
            double discount = Double.parseDouble(item.getItemDiscount()) * Integer.parseInt(integerNumber.getText().toString());
            discountAmt.setText("₹"+discount);

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.food_bg4)
                    .error(R.drawable.food_bg4)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(mContext).load(item.getItemImagePath())
                    .apply(options)
                    .into(imageView);

            /*Picasso.with(mContext)
                    .load(item.getItemImagePath())
                    .noFade().into(imageView);*/
        }

    }

    private void deleteCart(String id){
        SQLController controller = new SQLController(mContext);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);

        String query = "DELETE from "+db.TblOtherOrder+" where item_id ='"+id+"'";
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

        String query = "UPDATE "+db.TblOtherOrder+" set quantity='"+quantity+"' where item_id ='"+id+"'";
        String result = controller.fireQuery(query);

        if(result.equals("Done")){
            Log.d("update","Record update");
        }else {
            Log.d("result",result);
        }
        controller.close();
    }

    private void checkItemQuantity(final ViewHolder viewHolder, final PlaceOrderData place, final String itemId, final int quantity){
        showProgressDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest request = new StringRequest(Request.Method.POST, DairyNetworkConstant.BASE_URL_DAIRY
                + DairyNetworkConstant.CHECK_ITEM_INDIDUALY+itemId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                cancelProgressDialog();
                Log.w("checkItem",response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt(ResponseAttributeConstants.STATUS) != 0) {
                        int qty = Integer.parseInt(object.getString("qty"));
                        if(quantity<=qty){
                            viewHolder.integerNumber.setText("" + quantity);
                            double sum = Double.parseDouble(place.getOrderPrice()) * quantity;
                            double discount = Double.parseDouble(place.getItemDiscount()) * quantity;
                            viewHolder.tvPrice.setText("₹"+sum);
                            double roundOff = Math.round(discount * 100.0) / 100.0;
                            String s = String.format("%.2f", roundOff);
                            viewHolder.discountAmt.setText("₹"+s);
                            place.setQuantity(viewHolder.integerNumber.getText().toString());
                            updateCart(itemId,String.valueOf(quantity));
                            fInterface.fragmentBecameVisible();
                        }else {
                            Toast.makeText(mContext,"We have only "+qty+" quantity",Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    cancelProgressDialog();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("checkItem_error", String.valueOf(error));
                cancelProgressDialog();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        requestQueue.add(request);
    }

    protected void showProgressDialog() {
        progressDialog = new ProgressDialog(mContext, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIcon(0);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    protected void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }
}
