package com.example.xeroxapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register_page extends AppCompatActivity {

    private EditText username_id,user_mobile_id,user_email_id,user_pass_id;
    private Button submit_button;
    private TextView login;
    private String userName,mobile,email,password;

    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Intent i = new Intent(Register_page.this,MainActivity.class);
                startActivity(i);
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(view == submit_button){
                    registerUser();
                }

            }
        });

    }

    private void registerUser() {

        userName = username_id.getText().toString();
        mobile = user_mobile_id.getText().toString();
        email = user_email_id.getText().toString();
        password = user_pass_id.getText().toString();


        dialog.setMessage("Registering the user...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                    System.out.println(jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        dialog.hide();
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parameters = new HashMap<>();
                parameters.put("username",userName);
                parameters.put("password",password);
                parameters.put("email",email);

                parameters.put("mobile",mobile);
                return parameters;
            }
        };

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }




    public void gotoLogin(View view)
    {
        Intent intent = new Intent(Register_page.this,MainActivity.class);
        startActivity(intent);
    }


}
