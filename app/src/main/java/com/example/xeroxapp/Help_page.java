package com.example.xeroxapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Help_page extends AppCompatActivity {

    private TextView help_tv;
    String instructions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_page);

        help_tv=findViewById(R.id.help_tv);
        help_tv.setText("How to use?\n\n"+
                "->Click on the upload button below.\n\n" +
                "->On the following page click on the '+' button and select file to upload.\n" +
                "(supported file formats are : pdf,ppt,pptx,doc,docx,xls,txt)\n\n" +
                "->After uploading click on the my files button to see a list of your files.\n\n" +
                "->Click on each file name and fill in all the details(mandatory)\n\n" +
                "->Proceed for payment using any UPI enabled payment App.\n\n" +
                "->Do not refresh or press back button during transaction unless you get 'Transaction Successful' message\n\n" +
                "->Once done with payment check out your order details in 'My Orders' section");
    }
}
