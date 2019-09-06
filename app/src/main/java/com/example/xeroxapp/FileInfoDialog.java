package com.example.xeroxapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class FileInfoDialog extends AppCompatDialogFragment {

    EditText etcopies,etpages;
    TextView textViewtitle;
    RadioButton radio_color,radio_sides;
    RadioGroup rg1,rg2;

    String print_sides,print_color,print_pages,print_copies;
    String u_email="",docname="";
    private FileInfoDialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater  = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.file_info_layout,null);

        etcopies = view.findViewById(R.id.et_copies);
        etpages = view.findViewById(R.id.et_pages);
        textViewtitle = view.findViewById(R.id.tvtitle);

        rg1 = view.findViewById(R.id.radiogrp1);
        rg2 = view.findViewById(R.id.radiogrp2);


        /*Bundle bundle = savedInstanceState;
        bundle.putString("Filename",textViewtitle.getText().toString());*/
        //final AlertDialog dialog=builder.create();

        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int value1 = rg1.getCheckedRadioButtonId();
                        radio_sides = view.findViewById(value1);
                        int value2 = rg2.getCheckedRadioButtonId();
                        radio_color = view.findViewById(value2);

                        print_pages = etpages.getText().toString();
                        print_copies = etcopies.getText().toString();
                        print_sides = radio_sides.getText().toString();
                        print_color = radio_color.getText().toString();

                        u_email = getArguments().getString("email");
                        docname = getArguments().getString("filename");
                        System.out.println(u_email+" **************** "+docname);

                        //dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                        listener.getTexts(print_copies,print_pages,print_sides,print_color);
                        if(validateDetails()) {

                            System.out.println("(  " + print_color + "  ||  " + print_copies + "  ||  " + print_pages + "  ||  " + print_sides + "  )");
                            System.out.println("Closing dialog");
                                if(SendFileDetailstoDB()){
                                    Toast.makeText(getContext(),"Details updated",Toast.LENGTH_LONG).show();
                                }
                           // dialog.dismiss();
                        }
                        else{
                            System.out.println("Incomplete..............");
                            Toast.makeText(getContext(),"Details couldn't be updated",Toast.LENGTH_LONG).show();
                        }


                    }
                });


        //dialog.show();



              /*  b_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int value1 = rg1.getCheckedRadioButtonId();
                        radio_sides = view.findViewById(value1);
                        int value2 = rg2.getCheckedRadioButtonId();
                        radio_color = view.findViewById(value2);
                        System.out.println("value1=> "+value1+" value2=> "+value2);
                        print_pages = etpages.getText().toString();
                        print_copies = etcopies.getText().toString();
                        print_sides = radio_sides.getText().toString();
                        print_color = radio_color.getText().toString();

                       // listener.getTexts(print_copies,print_pages,print_sides,print_color);

                        u_email = getArguments().getString("email");
                        docname = getArguments().getString("filename");

                        System.out.println(u_email+" **************** "+docname);
                      //  dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        if(validateDetails()) {

                            System.out.println("(  " + print_color + "  ||  " + print_copies + "  ||  " + print_pages + "  ||  " + print_sides + "  )");
                            SendFiletoDB();
                      //      dialog.dismiss();

                        }
                        else
                        {
                            Toast.makeText(view.getContext(),"Fill all the details",Toast.LENGTH_SHORT).show();
                            System.out.println("...............Fill in the details.....................");
                        }

                    }
                });
*/

//        return dialog;
        return builder.create();
    }






    public boolean validateDetails(){

        if(print_copies.equals(""))
        {
            etcopies.setError("Please enter no. of copies");
            etcopies.requestFocus();
            return false;
        }
        if(print_pages.equals(""))
        {
            etpages.setError("Please enter no. of pages");
            etpages.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (FileInfoDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"Must implement FileInfoDialogListener");
        }
    }

    public interface FileInfoDialogListener{
        void getTexts(String copies,String pages,String sides,String color);
    }

    public boolean SendFileDetailstoDB(){
        JSONObject request = new JSONObject();
        try {

            request.put("usermail",u_email);
            request.put("filename",docname);
            request.put("copies",print_copies);
            request.put("pages",print_pages);
            request.put("sides",print_sides);
            request.put("color",print_color);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest objrequest = new JsonObjectRequest(com.android.volley.Request.Method.POST,Constants.UPDATE_FILE_DETAILS_URL,request,new com.android.volley.Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {

                try {
                    System.out.println(response.getString("message"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Errorrrrrrrrrrrrrr : "+error.getMessage());

            }

        });

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(objrequest);
        return true;
    }

}
