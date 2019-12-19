package com.example.xeroxapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Upload_Page extends Fragment {
    String email;
    private CardView ratesCard,ordersCard,uploadCard;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_upload,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ratesCard = view.findViewById(R.id.rates_card);
        ordersCard = view.findViewById(R.id.orders_card);
        uploadCard = view.findViewById(R.id.upload_card);

        Intent i = getActivity().getIntent();
        //email=i.getStringExtra("EMAIL");
        email=Constants.Email;
        System.out.println("Upload page email----------=--------=------>"+email);
        ratesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),Printing_rates.class);
                startActivity(i);
            }
        });

        ordersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle  data = new Bundle();
                data.putString("email",email);
                MyOrders_Page myOrders_page = new MyOrders_Page();
                myOrders_page.setArguments(data);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,myOrders_page).addToBackStack(null).commit();

            }
        });
        uploadCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent u = new Intent(getActivity(),Documents.class);
                u.putExtra("EMAIL",email);
                startActivity(u);
            }
        });

    }


}
