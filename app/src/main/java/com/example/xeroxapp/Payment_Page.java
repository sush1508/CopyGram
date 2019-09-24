package com.example.xeroxapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class Payment_Page extends Fragment {

    Button paybtn;
    int orderamt,doc_count;
    String mail;
    TextView tvupi,tvamount,tvdocs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment,container,false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }
        orderamt = 0;
        orderamt = getArguments().getInt("OrderAmt");
        doc_count = getArguments().getInt("file_count");
        mail = getArguments().getString("email");
        System.out.println("Paymentpage.................>>>>>>>>>> "+orderamt);
        paybtn = view.findViewById(R.id.Pay_button);
        tvamount = view.findViewById(R.id.tv_rs);
        tvdocs = view.findViewById(R.id.tv_docs);
        tvupi = view.findViewById(R.id.tv_upi);

        tvamount.setText("Rs. "+Integer.toString(orderamt));
        tvupi.setText("UPI ID : "+Constants.MERCHANT_UPI);
        tvdocs.setText("No. of documents : "+Integer.toString(doc_count));

        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(getActivity(),Checksum.class);
                Intent i = new Intent(getActivity(),UPI_Payment.class);
                i.putExtra("Pay_Amount",Integer.toString(orderamt));
                i.putExtra("email",mail);
                i.putExtra("doc_count",Integer.toString(doc_count));
                System.out.println("Pay button clicked.........");
                startActivity(i);
            }
        });

    }
}
