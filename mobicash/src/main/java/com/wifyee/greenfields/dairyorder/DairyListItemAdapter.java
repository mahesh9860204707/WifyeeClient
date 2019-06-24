package com.wifyee.greenfields.dairyorder;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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

import com.squareup.picasso.Picasso;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.activity.AddAddress;
import com.wifyee.greenfields.database.DatabaseDB;
import com.wifyee.greenfields.database.SQLController;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DairyListItemAdapter extends RecyclerView.Adapter  {

    List mValues;
    Context mContext;
    protected DairyListItemAdapter.ItemListener mListener;
    ArrayAdapter<String> dataAdapter;

    public DairyListItemAdapter(Context context, List values, DairyListItemAdapter.ItemListener itemListener) {

        mValues = values;
        mContext = context;
        mListener = itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,AdapterView.OnItemSelectedListener {

        public TextView textView,out_of_stock,discountAmt;
        public TextView tvPrice;
        public TextView tvItemType;
        public TextView tvQuality,integerNumber,unit;
        public CircleImageView imageView;
        public RelativeLayout relativeLayout;
        public ImageButton increase;
        public ImageView decrease;
        public CheckBox chbbuy;
        public Spinner spinner;
        public LinearLayout ll;
        DairyProductListItem item;

        public ViewHolder(View v) {

            super(v);
            v.setOnClickListener(this);

            textView = (TextView) v.findViewById(R.id.textView);
            spinner = (Spinner) v.findViewById(R.id.spinner);
            tvPrice = (TextView) v.findViewById(R.id.textViewPrice);
            tvItemType = (TextView) v.findViewById(R.id.textViewType);
            tvQuality = (TextView) v.findViewById(R.id.textViewQuality);
            discountAmt = (TextView) v.findViewById(R.id.distcount_amt);
            imageView = (CircleImageView) v.findViewById(R.id.imageView);
            relativeLayout = (RelativeLayout) v.findViewById(R.id.relativeLayout);
            increase = (ImageButton) v.findViewById(R.id.increase);
            decrease = (ImageView) v.findViewById(R.id.decrease);
            integerNumber = (TextView) v.findViewById(R.id.integer_number);
            unit = (TextView) v.findViewById(R.id.unit);
            out_of_stock = (TextView) v.findViewById(R.id.out_of_stock);
            chbbuy = (CheckBox)v.findViewById(R.id.chb_buy);
            ll = (LinearLayout)v.findViewById(R.id.ll);

            increase.setOnClickListener(this);
            decrease.setOnClickListener(this);
            spinner.setOnItemSelectedListener(this);
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
            spinner.setAdapter(dataAdapter);

        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // On selecting a spinner item
            String item = parent.getItemAtPosition(position).toString();
            ((TextView) view).setTextColor(Color.BLACK);
            dataAdapter.notifyDataSetChanged();
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }

        public void setData(DairyProductListItem item) {
            this.item = item;
            textView.setText(item.getItemType());
            tvPrice.setText("₹ " + item.getItemPrice());
            tvItemType.setText(item.getItemName());
            tvQuality.setText(item.getItemQuality());
            unit.setText(item.getItemUnit());
            discountAmt.setText("Discount: ₹ "+item.getItemDiscount());
            Picasso.with(mContext)
                    .load(item.getItemImagePath())
                    .noFade().into(imageView);
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

        int qty = Integer.parseInt(((DairyProductListItem)mValues.get(position)).getItemQuantity());
        if(qty<=0 || ((DairyProductListItem) mValues.get(position)).getCurrentStatus().equals("0")){
            if (qty<=0) {
                myViewHolder.out_of_stock.setVisibility(View.VISIBLE);
                myViewHolder.imageView.setAlpha(0.4f);
            }
            if (((DairyProductListItem) mValues.get(position)).getCurrentStatus().equals("0")){
                myViewHolder.ll.setAlpha(0.4f);
            }
            myViewHolder.chbbuy.setEnabled(false);
            //myViewHolder.increase.setEnabled(false);
            //myViewHolder.decrease.setEnabled(false);
            myViewHolder.chbbuy.setAlpha(0.5f);
            //myViewHolder.itemView.setEnabled(false);
        }else{
            myViewHolder.out_of_stock.setVisibility(View.GONE);
            myViewHolder.imageView.setAlpha(1f);
            myViewHolder.ll.setAlpha(1f);
            myViewHolder.chbbuy.setEnabled(true);
            //myViewHolder.increase.setEnabled(true);
            //myViewHolder.decrease.setEnabled(true);
            myViewHolder.chbbuy.setAlpha(1f);
        }

        if(checkInCart(((DairyProductListItem) mValues.get(position)).getItemId())!=0){
            myViewHolder.chbbuy.setChecked(true);
            myViewHolder.chbbuy.setText(mContext.getString(R.string.cart));
        }else {
            myViewHolder.chbbuy.setChecked(false);
            myViewHolder.chbbuy.setText(mContext.getString(R.string.buy));
        }

            myViewHolder.chbbuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if((myViewHolder.chbbuy.isEnabled())) {
                        if (myViewHolder.chbbuy.isChecked() && myViewHolder.chbbuy.getText().equals(mContext.getString(R.string.buy))) {
                            mListener.onBuyCallBack(((DairyProductListItem) mValues.get(position)),
                                    ((DairyProductListItem) mValues.get(position)).getItemId(),
                                    myViewHolder.integerNumber.getText().toString(),
                                    myViewHolder.unit.getText().toString());
                            myViewHolder.chbbuy.setText(mContext.getString(R.string.cart));
                            LocalPreferenceUtility.putLatitudeOther(mContext, LocalPreferenceUtility.getLatitude(mContext));
                            LocalPreferenceUtility.putLongitudeOther(mContext, LocalPreferenceUtility.getLongitude(mContext));
                            //Log.e("Lat","Lat set in other cart");


                        } else if (!myViewHolder.chbbuy.isChecked() && myViewHolder.chbbuy.getText().equals(mContext.getString(R.string.cart))) {
                            mListener.onRemoveCartCallBack(((DairyProductListItem) mValues.get(position)).getItemId());
                            myViewHolder.chbbuy.setText(mContext.getString(R.string.buy));
                        }
                    }
                }
            });

        myViewHolder.increase.setOnClickListener(new View.OnClickListener() {
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
        });

        /*if(((DairyProductListItem) mValues.get(position)).getCurrentStatus().equals("0")){
            myViewHolder.ll.setAlpha(0.4f);
                *//*ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                imageView.setColorFilter(filter);*//*
            myViewHolder.chbbuy.setEnabled(false);
        }else {
            myViewHolder.ll.setAlpha(1f);
            myViewHolder.chbbuy.setEnabled(true);
        }*/
    }




    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(DairyProductListItem item);
        void onBuyCallBack(DairyProductListItem item,String itemId,String quantity,String qtyInUnit);
        void onRemoveCartCallBack(String topicId);
    }

    public int checkInCart(String itemId) {
        int total=0;
        SQLController controller=new SQLController(mContext);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);
        String query = "SELECT item_id from cart_item where item_id='"+itemId+"'";

        Cursor data = controller.retrieve(query);

        total = data.getCount();
        Log.e("itemCount",String.valueOf(total));

        data.close();
        controller.close();
        return total;
    }
}