package com.example.xeroxapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;

public class Printing_rates extends AppCompatActivity {

    TableRow tr;
    TableLayout tableLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printing_rates);

        tableLayout = findViewById(R.id.table);

    }
}
