package com.example.xeroxapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register_page extends AppCompatActivity {

    private EditText username_id, user_mobile_id, user_email_id, user_pass_id;
    private Button submit_button;
    private TextView login;
    private String userName, mobile, email, password;
    private static final String KEY_EMPTY = "";

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register_page);

        username_id = findViewById(R.id.register_username);
        user_mobile_id = findViewById(R.id.register_mobile_no);
        user_email_id = findViewById(R.id.register_email);
        user_pass_id = findViewById(R.id.register_password);
        login = findViewById(R.id.register_signin);
        submit_button = findViewById(R.id.register_submit);

        dialog = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register_page.this, MainActivity.class);
                startActivity(i);
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view == submit_button) {
                   if(haveNetwork())
                    registerUser();
                    else if (!haveNetwork()){
                        Toast.makeText(Register_page.this,"No network",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    private void registerUser() {

            userName = username_id.getText().toString().trim();
            mobile = user_mobile_id.getText().toString().trim();
            email = user_email_id.getText().toString().trim();
            password = user_pass_id.getText().toString().trim();

            if(validateInputs()){

                if(checkInputs()){

                    dialog.setMessage("Registering the user...");
                    dialog.show();

                    JSONObject request = new JSONObject();
                    try {
                        //Populate the request parameters
                        request.put("username", userName);
                        request.put("email", email);
                        request.put("password",password);
                        request.put("mobile",mobile);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest stringRequest = new JsonObjectRequest
                            (Request.Method.POST, Constants.REGISTER_URL, request, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    dialog.dismiss();
                                    try {
                                        if (response.getInt("status") == 1) {
                                            System.out.println("=========Breakpoint1-===================");
                                            Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(Register_page.this,MainActivity.class);
                                            startActivity(i);
                                            System.out.println("=========Breakpoint2-===================");
                                        } else {
                                            System.out.println("=========Breakpoint3-===================");
                                            Toast.makeText(getApplicationContext(),
                                                    response.getString("message"), Toast.LENGTH_SHORT).show();

                                        }
                                    } catch (JSONException e) {
                                        System.out.println("=========Breakpoint4-===================");
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    dialog.dismiss();
                                    System.out.println("=========Breakpoint5-===================");
                                    //Display error message whenever an error occurs
                                    Toast.makeText(getApplicationContext(),
                                            "Error connecting", Toast.LENGTH_SHORT).show();

                                }
                            });


                    RequestQueue requestQueue;
                    requestQueue = Volley.newRequestQueue(this);
                    requestQueue.add(stringRequest);
                }

            }


        }




    public void gotoLogin(View view) {
        Intent intent = new Intent(Register_page.this, MainActivity.class);
        startActivity(intent);
    }


    public boolean haveNetwork() {

        boolean have_wifi = false;
        boolean have_mobiledata = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = cm.getAllNetworkInfo();

        for (NetworkInfo info : networkInfos) {
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                    have_wifi = true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                if (info.isConnected())
                    have_mobiledata = true;

        }
        return have_mobiledata | have_wifi;
    }

    private boolean validateInputs() {
        if (KEY_EMPTY.equals(userName)) {
            username_id.setError("Username cannot be empty");
            username_id.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(password)) {
            user_pass_id.setError("Password cannot be empty");
            user_pass_id.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(email)) {
            user_email_id.setError("Email cannot be empty");
            user_email_id.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(mobile)) {
            user_mobile_id.setError("Mobile no. cannot be empty");
            user_mobile_id.requestFocus();
            return false;
        }

        return true;
    }

    boolean checkInputs()
    {
        if(mobile.length() != 10){
            user_mobile_id.setError("Enter valid mobile number");
            user_mobile_id.requestFocus();
            return false;
        }
        if(email.length()<4)
        {
            user_email_id.setError("Email invalid");
            user_email_id.requestFocus();
            return false;
        }
        if(!(email.contains("@") && email.contains(".")))
        {
            user_email_id.setError("Enter correct Email");
            user_email_id.requestFocus();
            return false;
        }
        if(password.length()<5){
            user_pass_id.setError("Password too short");
            user_pass_id.requestFocus();
            return false;
        }
    return true;
    }
}
