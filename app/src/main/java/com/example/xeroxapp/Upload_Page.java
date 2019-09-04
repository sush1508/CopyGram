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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Upload_Page extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_upload,container,false);

        Button upload_button= view.findViewById(R.id.upload_button);
        TextView path=view.findViewById(R.id.filepath);
        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"UPLOAD CLICKED",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(),Upload_Documents.class);
                startActivity(i);
            }
        });

        path.setOnClickListener(new View.OnClickListener(){

            @Override

                public void onClick(View view){
                Intent i = new Intent(getActivity(),samplepage.class);
                startActivity(i);
                }

        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

}
