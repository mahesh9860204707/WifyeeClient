package com.wifyee.greenfields.dairyorder;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.activity.AddAddress;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;
import com.wifyee.greenfields.database.DatabaseDB;
import com.wifyee.greenfields.database.SQLController;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DairyListItemAdapter extends RecyclerView.Adapter  {

    List mValues;
    Context mContext;
    private DairyListItemAdapter.ItemListener mListener;

    public DairyListItemAdapter(Context context, List values, DairyListItemAdapter.ItemListener itemListener) {

        mValues = values;
        mContext = context;
        mListener = itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView out_of_stock,discountAmt,txtAdd,txtPlus,increment,decrement,integerNumber;
        public TextView tvPrice;
        public TextView tvItemType;
        public TextView tvQuality,unit;
        public ImageView imageView,icDiscount;
        public CheckBox chbbuy;
        public LinearLayout ll,llAdd,llIncrDecr;
        DairyProductListItem item;

        public ViewHolder(View v) {

            super(v);
            v.setOnClickListener(this);

            //textView = (TextView) v.findViewById(R.id.textView);
            //spinner = (Spinner) v.findViewById(R.id.spinner);
            tvPrice =  v.findViewById(R.id.textViewPrice);
            tvItemType =  v.findViewById(R.id.textViewType);
            tvQuality =  v.findViewById(R.id.textViewQuality);
            discountAmt =  v.findViewById(R.id.distcount_amt);
            imageView =  v.findViewById(R.id.imageView);
            icDiscount = v.findViewById(R.id.ic_discount);
            llAdd = v.findViewById(R.id.ll_add);
            llIncrDecr = v.findViewById(R.id.ll_incr_decr);
            txtAdd = v.findViewById(R.id.txt_add);
            txtPlus = v.findViewById(R.id.txt_plus);
            increment = v.findViewById(R.id.increase);
            decrement = v.findViewById(R.id.decrease);
            integerNumber = v.findViewById(R.id.integer_number);
            unit = (TextView) v.findViewById(R.id.unit);
            out_of_stock =  v.findViewById(R.id.out_of_stock);
            //chbbuy = (CheckBox)v.findViewById(R.id.chb_buy);
            ll = v.findViewById(R.id.ll);

           /* spinner.setOnItemSelectedListener(this);
            // Spinner Drop down elements
            List<String> categories = new ArrayList<String>();
            categories.add("1 ltr");
            categories.add("1/2 ltr");
            categories.add("1 Kg");
            categories.add("500 gms");
            categories.add("200 gms");

            // Creating adapter for spinner
            dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, categories);
            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);*/

        }

        /*@Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // On selecting a spinner item
            String item = parent.getItemAtPosition(position).toString();
            ((TextView) view).setTextColor(Color.BLACK);
            dataAdapter.notifyDataSetChanged();
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }*/

        public void setData(DairyProductListItem item) {
            this.item = item;
            //textView.setText(item.getItemType());
            tvPrice.setText("₹" + item.getItemPrice());
            tvItemType.setText(item.getItemName());
            tvQuality.setText(item.getItemQuality());
            unit.setText(item.getItemUnit());
            discountAmt.setText("₹"+item.getItemDiscount());

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

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }


    @Override
    public DairyListItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dairy_list_item_view, parent, false);
        return new DairyListItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder vHolder, final int position) {
        final DairyListItemAdapter.ViewHolder myViewHolder = (DairyListItemAdapter.ViewHolder) vHolder;
        myViewHolder.setData((DairyProductListItem) mValues.get(position));

        myViewHolder.tvItemType.setTypeface(Fonts.getSemiBold(mContext));
        myViewHolder.tvQuality.setTypeface(Fonts.getRegular(mContext));
        myViewHolder.tvPrice.setTypeface(Fonts.getRegular(mContext));
        myViewHolder.increment.setTypeface(Fonts.getSemiBold(mContext));
        myViewHolder.decrement.setTypeface(Fonts.getSemiBold(mContext));
        myViewHolder.integerNumber.setTypeface(Fonts.getSemiBold(mContext));
        myViewHolder.txtPlus.setTypeface(Fonts.getSemiBold(mContext));
        myViewHolder.txtAdd.setTypeface(Fonts.getSemiBold(mContext));
        myViewHolder.discountAmt.setTypeface(Fonts.getRegular(mContext));

        int qty = Integer.parseInt(((DairyProductListItem)mValues.get(position)).getItemQuantity());
        if(qty<=0 || ((DairyProductListItem) mValues.get(position)).getCurrentStatus().equals("0")){
            if (qty<=0) {
                myViewHolder.out_of_stock.setVisibility(View.VISIBLE);
                myViewHolder.imageView.setAlpha(0.4f);
            }
            if (((DairyProductListItem) mValues.get(position)).getCurrentStatus().equals("0")){
                myViewHolder.ll.setAlpha(0.4f);
            }
            myViewHolder.llAdd.setEnabled(false);
            myViewHolder.llAdd.setAlpha(0.5f);

        }else{
            myViewHolder.out_of_stock.setVisibility(View.GONE);
            myViewHolder.imageView.setAlpha(1f);
            myViewHolder.ll.setAlpha(1f);
            myViewHolder.llAdd.setEnabled(true);
            myViewHolder.llAdd.setAlpha(1f);
        }

        if(checkInCart(((DairyProductListItem) mValues.get(position)).getItemId(),myViewHolder)!=0){
            myViewHolder.llAdd.setVisibility(View.INVISIBLE);
            myViewHolder.llIncrDecr.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.llAdd.setVisibility(View.VISIBLE);
            myViewHolder.llIncrDecr.setVisibility(View.INVISIBLE);
        }

        if (((DairyProductListItem) mValues.get(position)).getItemDiscount().equals("0.00")){
            myViewHolder.icDiscount.setVisibility(View.GONE);
            myViewHolder.discountAmt.setVisibility(View.GONE);
        }else {
            myViewHolder.icDiscount.setVisibility(View.VISIBLE);
            myViewHolder.discountAmt.setVisibility(View.VISIBLE);
            //myViewHolder.icDiscount.animate().rotation(360).setDuration(1000).start();
        }

            myViewHolder.llAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    /*if((myViewHolder.chbbuy.isEnabled())) {
                        if (myViewHolder.chbbuy.isChecked() && myViewHolder.chbbuy.getText().equals(mContext.getString(R.string.buy))) {
                            mListener.onBuyCallBack(((DairyProductListItem) mValues.get(position)),
                                    ((DairyProductListItem) mValues.get(position)).getItemId(),
                                    myViewHolder.integerNumber.getText().toString(),
                                    myViewHolder.unit.getText().toString());*/
                            myViewHolder.llAdd.setVisibility(View.INVISIBLE);
                            myViewHolder.llIncrDecr.setVisibility(View.VISIBLE);

                            mListener.onBuyCallBack(((DairyProductListItem) mValues.get(position)),
                                    ((DairyProductListItem) mValues.get(position)).getItemId(),
                                    myViewHolder.integerNumber.getText().toString(),
                                    myViewHolder.unit.getText().toString(),0);

                            LocalPreferenceUtility.putLatitudeOther(mContext, LocalPreferenceUtility.getLatitude(mContext));
                            LocalPreferenceUtility.putLongitudeOther(mContext, LocalPreferenceUtility.getLongitude(mContext));

                        /*} else if (!myViewHolder.chbbuy.isChecked() && myViewHolder.chbbuy.getText().equals(mContext.getString(R.string.cart))) {
                            mListener.onRemoveCartCallBack(((DairyProductListItem) mValues.get(position)).getItemId());
                            myViewHolder.chbbuy.setText(mContext.getString(R.string.buy));
                        }
                    }*/
                }
            });

        myViewHolder.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = Integer.parseInt(myViewHolder.integerNumber.getText().toString()) + 1;
                if (value >= 1) {
                    //checkItemQuantity(myViewHolder,place,place.getItemId(),value);
                    checkItemQuantity(myViewHolder,((DairyProductListItem) mValues.get(position)).getItemId(),value,view);

                }
            }
        });


        myViewHolder.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = Integer.parseInt(myViewHolder.integerNumber.getText().toString()) - 1;
                if (value >= 1) {
                    //checkItemQuantity(myViewHolder,place,place.getItemId(),value);
                    myViewHolder.integerNumber.setText("" + value);
                    updateCart(((DairyProductListItem) mValues.get(position)).getItemId(), String.valueOf(value),myViewHolder);
                }else {
                    deleteCartItem(((DairyProductListItem) mValues.get(position)).getItemId(),myViewHolder);
                    myViewHolder.llIncrDecr.setVisibility(View.INVISIBLE);
                    myViewHolder.llAdd.setVisibility(View.VISIBLE);
                    mListener.onRemoveCartCallBack(((DairyProductListItem) mValues.get(position)).getItemId(),0);
                }
            }
        });

        /*myViewHolder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.parseInt(myViewHolder.integerNumber.getText().toString()) + 1;
                if (value >= 1)
                    myViewHolder.integerNumber.setText("" + value);
            }
        });

        myViewHolder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.parseInt(myViewHolder.integerNumber.getText().toString()) - 1;
                if (value >= 1)
                    myViewHolder.integerNumber.setText("" + value);
            }
        });*/

    }




    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(DairyProductListItem item);
        void onBuyCallBack(DairyProductListItem item,String itemId,String quantity,String qtyInUnit,int flag);
        void onRemoveCartCallBack(String topicId,int flag);
    }

    private void checkItemQuantity(final ViewHolder viewHolder, final String itemId, final int quantity,final View view){
        DairyItemListActivity.progressBar.setVisibility(View.VISIBLE);
        DairyItemListActivity.recyclerView.setAlpha(0.5f);
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest request = new StringRequest(Request.Method.POST, DairyNetworkConstant.BASE_URL_DAIRY
                + DairyNetworkConstant.CHECK_ITEM_INDIDUALY+itemId, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DairyItemListActivity.progressBar.setVisibility(View.GONE);
                DairyItemListActivity.recyclerView.setAlpha(1f);
                Log.w("checkItem",response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt(ResponseAttributeConstants.STATUS) != 0) {
                        int qty = Integer.parseInt(object.getString("qty"));
                        if(quantity<=qty){
                            viewHolder.integerNumber.setText("" + quantity);
                            updateCart(itemId,String.valueOf(quantity),viewHolder);
                        }else {
                            Snackbar.make(view,"We have only "+qty+" quantity",Snackbar.LENGTH_LONG).show();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Snackbar.make(view,e.toString(),Snackbar.LENGTH_LONG).show();
                    DairyItemListActivity.progressBar.setVisibility(View.GONE);
                    DairyItemListActivity.recyclerView.setAlpha(1f);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("checkItem_error", String.valueOf(error));
                Snackbar.make(view,"Something went wrong. Plz try again",Snackbar.LENGTH_LONG).show();
                DairyItemListActivity.progressBar.setVisibility(View.GONE);
                DairyItemListActivity.recyclerView.setAlpha(1f);
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        requestQueue.add(request);
    }

    public int checkInCart(String itemId,ViewHolder holder) {
        int total=0;
        SQLController controller=new SQLController(mContext);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);
        String query = "SELECT * from cart_item where item_id='"+itemId+"'";

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

    private void deleteCartItem(String id,ViewHolder holder){
        SQLController controller = new SQLController(mContext);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);

        String query = "DELETE from cart_item where item_id ='"+id+"'";
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

    private void updateCart(String id,String quantity,ViewHolder myViewHolder){
        SQLController controller = new SQLController(mContext);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);

        String query = "UPDATE cart_item set quantity ='"+quantity+"' where item_id ='"+id+"'";
        String result = controller.fireQuery(query);

        if(result.equals("Done")){
            Log.d("update","Record update");
            mListener.onBuyCallBack(((DairyProductListItem) mValues.get(0)), "", "", "",1);
        }else {
            Log.d("result",result);
        }
        controller.close();
    }
}