package com.example.xeroxapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText email_id, pass_id;
    private Button sign_button_id, reg_button_id;
    private TextView forgot_pass;
    private String Email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViews();

        sign_button_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViews();
                //Intent i =new Intent(MainActivity.this,Dashboard_page.class);
                //startActivity(i);
                //validateUser(Email,password);

            }
        });

        reg_button_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Register_page.class);
                startActivity(intent);
            }
        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Dashboard_page.class);
                startActivity(i);
            }
        });

    }



    public void setViews()
    {
        email_id = findViewById(R.id.main_user_name);
        Email = email_id.getText().toString();

        pass_id = findViewById(R.id.main_password);
        password = pass_id.getText().toString();

        sign_button_id = findViewById(R.id.main_sign_in_button);

        reg_button_id = findViewById(R.id.main_register_button);

        forgot_pass = findViewById(R.id.main_forgot_pass);
    }


    public void validateUser(String email,String pass)
    {





    }


}

