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
    TextView tvupload,tvfetchinfo;
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
        tvupload = view.findViewById(R.id.uploadfile);
        tvfetchinfo = view.findViewById(R.id.fetchfileinfo);

        Intent i = getActivity().getIntent();
        email=i.getStringExtra("EMAIL");

        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"UPLOAD CLICKED",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(),samplepage.class);
                i.putExtra("EMAIL",email);
                startActivity(i);
            }
        });

        tvupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(getActivity(),samplepage.class);
                intent.putExtra("EMAIL",email);
                System.out.println(email);
                startActivity(intent);
            }
        });

        tvfetchinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Fetch clickeddddddddddddddddddddd \n");
                fetchFileInfo();

            }
        });

    }

    public void fetchFileInfo(){


        String jsonstring  = "[{'User_email':'"+email+"'}]";
        System.out.println(jsonstring);
        JSONArray request = null;
        try {
            request = new JSONArray(jsonstring);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, Constants.FETCH_URL, request, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {



                    System.out.println("Response length===========>"+response.length());
                    for(int i=0;i<response.length();i++)
                    {
                        try {
                            JSONObject obj  = response.getJSONObject(i);
                            responsemsg = obj.getString("message");
                            if(responsemsg.equals("Documents fetched")){
                                f_useremail = obj.getString("useremail");
                                f_filename = obj.getString("filename");
                                f_filetype = obj.getString("filetype");

                                System.out.println("Messageeeeeeeeeee ===> "+responsemsg);
                                System.out.println("user====>"+f_useremail+"\nfilename=====>"+f_filename+"\nfiletype===>"+f_filetype);

                            }
                            else
                            {
                                System.out.println("Message : " +responsemsg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Errrrrrrrrrrrrrrrorrrrrrrrrrrrrrr =>"+error.getMessage());
            }
        });

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
/*

        final JsonObjectRequest jsonrequest = new JsonObjectRequest(Request.Method.POST, Constants.FETCH_URL, request, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{

                        response.getString("result");



                       */
/* if(response.getInt("status") == 1){
                            System.out.println("Status============== :::: "+response.getInt("status"));
                            Toast.makeText(getActivity(),response.getString("file_name"),Toast.LENGTH_SHORT).show();
                            System.out.println("Files fetcheddddd :::: "+response.getString("file_name"));
                        }
                        else{
                            System.out.println("Status============== "+response.getInt("status"));
                        }
*//*




                }catch(JSONException e){
                        e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("Erorrrrrrrrrr :  "+error.getMessage());
            }
        });

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonrequest);
*/
    }

    private void showJSON(String response) {
        String user_name = "";
        String filename = "";
        String filetype = "";
        String filelocation = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");
            for (int x = 0; x < result.length(); x++) {
                JSONObject fileData = result.getJSONObject(x);
                user_name = fileData.getString("username");
                filename = fileData.getString("filename");
                System.out.println("username====== " + user_name + "  filenameeeeeee ===== " + filename);
                //here you can store the name and phone_number in an arraylist
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
