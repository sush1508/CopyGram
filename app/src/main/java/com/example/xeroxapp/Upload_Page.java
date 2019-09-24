package com.example.xeroxapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
    Button upload_button;
    TextView tvupload,instruction;
    String f_useremail,f_filename,f_filetype;
    String email,responsemsg;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_upload,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        upload_button= view.findViewById(R.id.upload_button);

        Intent i = getActivity().getIntent();
        email=i.getStringExtra("EMAIL");

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),Documents.class);
                i.putExtra("EMAIL",email);
                startActivity(i);
               // getActivity().overridePendingTransition(R.anim.anim_fadein,R.anim.anim_fadeout);

            }
        });

        instruction = view.findViewById(R.id.tv_instructions);
        instruction.setText("Instructions:\n" +
                "Follow the below steps:\n\n" +
                "(*)Click on the upload button below.\n\n" +
                "(*)On the following page click on the '+' button and select file to upload.\n\n" +
                "(supported file formats are : pdf,ppt,pptx,doc,docx,xls,txt)\n" +
                "(*)After uploading click on the my files button to see a list of your files.\n\n" +
                "(*)Click on each file name and fill in all the details(mandatory)\n\n" +
                "(*)Proceed for payment using paytm.");


    }


}
