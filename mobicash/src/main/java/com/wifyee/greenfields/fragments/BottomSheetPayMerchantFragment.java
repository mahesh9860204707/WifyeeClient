package com.wifyee.greenfields.fragments;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.Fonts;

public class BottomSheetPayMerchantFragment extends BottomSheetDialogFragment {
    public BottomSheetPayMerchantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pay_merchant_fragment, container, false);

        TextView txt = view.findViewById(R.id.txt);
        Button btn = view.findViewById(R.id.btn);


        txt.setTypeface(Fonts.getRegular(getContext()));
        btn.setTypeface(Fonts.getSemiBold(getContext()));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;

    }
}
