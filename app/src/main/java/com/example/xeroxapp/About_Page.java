package com.example.xeroxapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class About_Page extends Fragment {

    TextView tvabout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        tvabout = getView().findViewById(R.id.tv_about);
        tvabout.setText("Copygram app is developed by a group of students of TE COMP (PCCOER,Ravet)" +
                "\n\nThis app intends to ease the customers who wish to get their documents printed with ease" +
                "\n\nNo more waiting queues" +
                "\n\nGet your order placed at your finger tips");
    }
}
