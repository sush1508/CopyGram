package com.example.xeroxapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class MyCustomListAdapter extends ArrayAdapter {

    Context context;
    int resource;
    Vector<String> fileList;
    String documentname;
    private myOnClickListener mylistener;


    public MyCustomListAdapter(Context context, int resource, Vector<String> fileList){
        super(context,resource,fileList);
        this.context = context;
        this.resource = resource;
        this.fileList = fileList;

    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(resource,null);
        final TextView tvfilename = view.findViewById(R.id.docname);
        ImageView deletebutton = view.findViewById(R.id.deletefile_button);
        final ImageView filldetails = view.findViewById(R.id.fill_details_icon);
        tvfilename.setText(fileList.get(position));

        tvfilename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("List item clicked..........................=>>>>>>>>>>"+tvfilename.getText().toString());
                documentname = tvfilename.getText().toString();
                mylistener.myOnClick(position,documentname);
                filldetails.setImageResource(R.drawable.ic_done_black_24dp);

            }
        });
        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete the file
                System.out.println("..............Delete clicked.............");
                removeItem(position);
            }
        });

        return view;
    }


    private void removeItem(final int position){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure you want to delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                deleteFromDB(position);
                fileList.remove(position);
                notifyDataSetChanged();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public interface myOnClickListener{
        void myOnClick(int position,String itemname);
    }

    public void setmyOnClickListener(myOnClickListener listener) {

        this.mylistener = listener;
    }

    void deleteFromDB(int pos)
    {
        String selectedFile;
        selectedFile = fileList.elementAt(pos);
        System.out.println("Selected file------> "+selectedFile);

        JSONObject object = new JSONObject();
        try {
            object.put("deleteFile",selectedFile);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.DELETE_FILE_URL, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if(response.getInt("status") == 1)
                    {
                        Toast.makeText(getContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

    }
}
