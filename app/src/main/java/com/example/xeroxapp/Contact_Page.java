package com.example.xeroxapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Contact_Page extends Fragment {
    TextView contacttv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact,container,false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contacttv = view.findViewById(R.id.textview_contact);
        contacttv.setText("\n\nEmail : pccoerxerox@gmail.com\n\n" +
                "Contact number : 9130470745\n\n" +
                "For order details contact Tanishka Xerox Center\n\n" +
                "PCCOE&R , Ravet");

    }
}
