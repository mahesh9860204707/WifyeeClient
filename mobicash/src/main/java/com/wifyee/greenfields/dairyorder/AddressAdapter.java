package com.wifyee.greenfields.dairyorder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.database.DatabaseDB;
import com.wifyee.greenfields.database.SQLController;
import com.wifyee.greenfields.interfaces.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

    private Context mContext;
    public List<AddressModal> addressModals;
    private int lastSelectedPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name,address,mobileNo;
        public RelativeLayout rlEdit,rlDelete;
        public RadioButton radioButton;
        private ItemClickListener clickListener;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            address = view.findViewById(R.id.address);
            mobileNo = view.findViewById(R.id.mobile_no);
            rlEdit = view.findViewById(R.id.rl_edit);
            rlDelete = view.findViewById(R.id.rl_delete);
            radioButton = view.findViewById(R.id.radio_address);

            view.setTag(view);
            view.setOnClickListener(this);
            mContext = view.getContext();
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition(), false);
        }

    }

    public AddressAdapter(Context mContext, List<AddressModal> addressModals) {
        this.mContext = mContext;
        this.addressModals = addressModals;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final AddressModal addr = addressModals.get(position);

        holder.name.setText(addr.getName());
        holder.address.setText(addr.getFlatNo()+", "+addr.getLocality()+", "+addr.getCity()+", "+addr.getState()+" - "+addr.getPincode());
        holder.mobileNo.setText(addr.getMobileNo());
        holder.radioButton.setChecked(lastSelectedPosition == position);

        if(lastSelectedPosition == -1 && position ==0){
            lastSelectedPosition = position;
            holder.radioButton.setChecked(true);
        }

        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick){
                    lastSelectedPosition = position;
                    notifyDataSetChanged();
                }
            }
        });

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedPosition = position;
                notifyDataSetChanged();
            }
        });

        holder.rlEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ShippingAddress.class);
                intent.putExtra("address_id",addr.getId());
                intent.putExtra("name",addr.getName());
                intent.putExtra("mobile_no",addr.getMobileNo());
                intent.putExtra("alternate_no",addr.getAlternateNo());
                intent.putExtra("city",addr.getCity());
                intent.putExtra("locality",addr.getLocality());
                intent.putExtra("flat_no",addr.getFlatNo());
                intent.putExtra("pincode",addr.getPincode());
                intent.putExtra("state",addr.getState());
                mContext.startActivity(intent);
                ((Activity)mContext).finish();
            }
        });

        holder.rlDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog layout = new Dialog(mContext);
                layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
                layout.setContentView(R.layout.popup_alert);
                layout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button cancel = layout.findViewById(R.id.cancel_btn);
                Button confirm = layout.findViewById(R.id.confirm_btn);
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
                        addressModals.remove(position);
                        deleteCart(addr.getId());
                        notifyDataSetChanged();
                        layout.dismiss();
                        OrderSummaryDetails.addressChange=false;
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return addressModals.size();
    }

    public String getSelectedItem() {
        if (lastSelectedPosition != -1) {
            addressModals.get(lastSelectedPosition).getName();
            //Toast.makeText(mContext, "Selected Item : " + addressModals.get(lastSelectedPosition), Toast.LENGTH_SHORT).show();
            return addressModals.get(lastSelectedPosition).toString();
        }
        return "";
    }

    public List<AddressModal> getData() {
        List<AddressModal> list = new ArrayList<>();
        if (lastSelectedPosition != -1) {
            AddressModal modal = addressModals.get(lastSelectedPosition);
            String id = modal.getId();
            String name = modal.getName();
            String mobile = modal.getMobileNo();
            String alternate = modal.getAlternateNo();
            String city = modal.getCity();
            String locality = modal.getLocality();
            String flat = modal.getFlatNo();
            String pincode = modal.getPincode();
            String state = modal.getState();
            AddressModal am = new AddressModal(id,name,mobile,alternate,city,locality,flat, pincode,state);
            list.add(am);
        }
        return list;
    }

    private void deleteCart(String id){
        SQLController controller = new SQLController(mContext);
        controller.open();
        DatabaseDB db = new DatabaseDB();
        db.createTables(controller);

        String query = "DELETE from address where id ='"+id+"'";
        String result = controller.fireQuery(query);

        if(result.equals("Done")){
            Log.d("delete","Record delete");
        }else {
            Log.d("result",result);
        }
        controller.close();
    }
}