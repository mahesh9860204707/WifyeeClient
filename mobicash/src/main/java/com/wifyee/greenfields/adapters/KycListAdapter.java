package com.wifyee.greenfields.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.interfaces.OnClickListenerAddBeneficiary;
import com.wifyee.greenfields.interfaces.OnClickListenerUpdateKYC;
import com.wifyee.greenfields.models.requests.BeneficiaryBean;
import com.wifyee.greenfields.models.requests.KYCBean;
import com.wifyee.greenfields.models.requests.KYCDocumentsBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Amit Kumar on 27-12-2016.
 */
public class KycListAdapter extends RecyclerView.Adapter<KycListAdapter.LogItemViewHolder> {

    private List<KYCDocumentsBean> mKycBeanList;
    private final LayoutInflater mLayoutInflater;
    private Context mContext;
    public OnClickListenerUpdateKYC ClickListener;

    public KycListAdapter(Context context, List<KYCDocumentsBean> mKYCbeanList, OnClickListenerUpdateKYC listener) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mKycBeanList = mKYCbeanList;
        this.ClickListener = listener;
    }

    @Override
    public LogItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.kyc_list_item, parent, false);
        return new LogItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LogItemViewHolder holder, final int position)
    {
        final KYCDocumentsBean kycItem = mKycBeanList.get(position);
        holder.mIdProof.setText(kycItem.getProofNumber());
        holder.mAddressProof.setText(kycItem.getProofNumber());
        holder.mSerialNumber.setText(String.valueOf(position));
/*
      holder.mUpdateKyc.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(ClickListener!=null) {
                ClickListener.seClickListener(mKycBeanList.get(position));
              }
          }
      });*/
        holder.mDeleteKyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showPopUpDeleteKyc(mContext);
            }
        });
    }
    //Delete Beneficiary
    private void showPopUpDeleteKyc(final Context context)
    {
        final Dialog layout = new Dialog(context);
        layout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Include dialog.xml file
        layout.setContentView(R.layout.showpopup_delete_kyc);
        // Set dialog title
        layout.setTitle("Delete KYC");
        Button yes = (Button) layout.findViewById(R.id.yes);
        Button no = (Button) layout.findViewById(R.id.no);
        layout.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    layout.dismiss();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    layout.dismiss();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return (mKycBeanList != null) ? mKycBeanList.size() : 0;
    }

    public void cleartLogItemCollection() {
        mKycBeanList.clear();
    }

    public void setLogItemCollection(List<KYCDocumentsBean> kycItemCollection) {
        for (KYCDocumentsBean kycItem : kycItemCollection) {
            mKycBeanList.add(0, kycItem);
        }
        notifyDataSetChanged();
    }

    public class LogItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.serial_number)
        public TextView mSerialNumber;

        @BindView(R.id.id_proof_doc)
        public TextView mIdProof;

        @BindView(R.id.address_proof_number)
        public TextView mAddressProof;


        @BindView(R.id.edit_kyc)
        public ImageView mUpdateKyc;

        @BindView(R.id.delete_kyc)
        public ImageView mDeleteKyc;



        public LogItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


