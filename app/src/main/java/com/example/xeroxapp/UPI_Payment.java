package com.example.xeroxapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UPI_Payment extends AppCompatActivity {

String usermail,amount,doc_count;
JSONObject object;
TextView pay_status,pay_amount,paid_to,tv_txnid;
final int UPI_PAYMENT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upi__payment);

        Intent i = getIntent();
        //usermail = i.getStringExtra("email");
        usermail = Constants.Email;
        System.out.println("UPI PAYMENT PAGE email----====---->>>"+usermail);
        amount = i.getStringExtra("Pay_Amount");
        doc_count = i.getStringExtra("doc_count");
        object=new JSONObject();
        pay_amount = findViewById(R.id.upi_amount);
        pay_status = findViewById(R.id.upi_status);
        paid_to = findViewById(R.id.upi_paidto);
        tv_txnid = findViewById(R.id.txnid);
        //System.out.println("UPI===>"+Constants.MERCHANT_UPI+"\nEMAIL====>"+usermail+"\nAMOUNT====>"+amount);
        payUsingUPI(Constants.MERCHANT_UPI,amount,usermail);



    }

    void payUsingUPI(String upi,String amt,String email)
    {
        Uri uri = Uri.parse("upi://pay").buildUpon().appendQueryParameter("pa",upi)
                .appendQueryParameter("pn","sushant")
                .appendQueryParameter("tn","Paid by:"+email)
                .appendQueryParameter("am",amt)
                .appendQueryParameter("cu","INR").build();



        Intent upi_payment = new Intent(Intent.ACTION_VIEW);
        upi_payment.setData(uri);
        Intent chooser = Intent.createChooser(upi_payment,"Pay with");
        if(null!=chooser.resolveActivity(getPackageManager()))
        {

            startActivityForResult(chooser,UPI_PAYMENT);

        }else{
            Toast.makeText(UPI_Payment.this,"No upi app found",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //System.out.println("Resultcode=====>"+resultCode+"requestcode=======>"+requestCode);
        //System.out.println("Data--------->"+data+"--------------");

        switch (requestCode)
        {
            case UPI_PAYMENT :
                //System.out.println("----Resultcode=====>"+resultCode+"requestcode=======>"+requestCode);
                if(RESULT_OK == resultCode || requestCode==1)
                {
                    if(data!=null)
                    {
                       // System.out.println("data------------------------>"+data);
                        String text = data.getStringExtra("response");
                       // System.out.println("-----------text-------> "+text);
                        if(text.contains("Status=SUCCESS")){
                            try {
                                object.put("code","1");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            ArrayList<String> datalist = new ArrayList<>();
                            datalist.add(text);
                            upipaymentDataoperation(datalist);

                        }else
                        {
                            try {
                                object.put("code","-1");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(this,"Some error occurred",Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        try {
                            object.put("code","-1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //System.out.println("<----------------Return data is null---------->");
                        ArrayList<String> datalist = new ArrayList<>();
                        datalist.add("Nothing");
                        upipaymentDataoperation(datalist);
                    }
                }else{
                    try {
                        object.put("code","-1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ArrayList<String> datalist = new ArrayList<>();
                    datalist.add("Nothing");
                    upipaymentDataoperation(datalist);
                }
                break;

        }

        try {
            if(object.getString("code").equals("1"))
            {
                Toast.makeText(this,object.getString("0")+object.getString("1")+object.getString("2")+object.getString("3"),Toast.LENGTH_LONG).show();
               //System.out.println(object.getString("0")+object.getString("1")+object.getString("2")+object.getString("3"));
                sendDetailsToDB();
                updatePaymentStatus();

            }
            else
            {
                pay_amount.setText("Paid : Rs. 0");
                pay_status.setText("Payment\nunsuccessful");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updatePaymentStatus() {

        pay_status.setText("Payment\nSuccessful");
        pay_amount.setText("Paid : Rs. "+amount);
        paid_to.setText("Paid to : Tanishka Xerox Center\nPCCOER");
        try {
            tv_txnid.setText("Transaction id:"+object.getString("0"));
        } catch (JSONException e) {
                e.printStackTrace();
        }

    }

    private void upipaymentDataoperation(ArrayList<String> data) {
        String status = "";
        String approvalref="";
        if(isConnectionAvailable(UPI_Payment.this))
            {
                String str = data.get(0);
                //System.out.println("-------->Str->---.->-_.->>>> "+str);
                String paymentcancel="";
                if(str==null || str.equals("Nothing"))
                {
                    str="discard";
                    //System.out.println("Discarddddddddddddddddddddddddddddddd"+str);
                    Toast.makeText(this,"Error paying",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    String response[] = str.split("&");
                    //System.out.println("response///////////-------->"+response[1]);

                    for(int i=0;i<response.length;i++)
                    {
                        String equalStr[] = response[i].split("=");
                        //System.out.println("------------equalStr------------>"+equalStr[1]);
                        try {
                            object.put(Integer.toString(i),equalStr[1]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(equalStr.length>=2)
                        {
                            if(equalStr[0].toLowerCase().equals("Status".toLowerCase()))
                            {
                                status = equalStr[1].toLowerCase();
                                //System.out.println("Status1========== "+status);
                            }
                            else if(equalStr[0].toLowerCase().equals("Approval Ref.".toLowerCase()) || equalStr[0].toLowerCase().equals("TxnRef.".toLowerCase()))
                            {
                                approvalref = equalStr[1];
                                //System.out.println("approvalref------------->"+approvalref);
                            }
                        }
                        else
                        {
                            paymentcancel = "Payment cancel by user";
                            if(status.equals("success"))
                            {
                                Toast.makeText(UPI_Payment.this,"Transaction successful",Toast.LENGTH_SHORT).show();
                                //System.out.println("Status2========== "+status);
                            }
                            else if("Payment cancel by user".equals(paymentcancel))
                            {
                                Toast.makeText(UPI_Payment.this,"Transaction cancelled by user",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(UPI_Payment.this,"Transaction failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    //System.out.println("JSON===>>"+object.toString());

                }
                 }
            else
            {
                Toast.makeText(this,"No internet connection",Toast.LENGTH_SHORT).show();
            }

    }

    private boolean isConnectionAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm!=null)
        {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if(networkInfo!=null && networkInfo.isConnected()&&networkInfo.isConnectedOrConnecting()&&networkInfo.isAvailable())
            {
                return  true;
            }
        }
        return false;
    }

    void sendDetailsToDB()
    {
        JSONObject jobj = new JSONObject();
        try {

            jobj.put("EMAIL",usermail);
            jobj.put("STATUS","Done");
            jobj.put("AMOUNT",amount);
            jobj.put("TXN_ID",object.getString("0"));
            jobj.put("REFERENCE_ID",object.getString("3"));
            jobj.put("Docs",doc_count);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.PAYMENT_URL, jobj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String result = response.getString("message");
                    System.out.println("Result-------------------- >> "+result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //System.out.println("Some error occurred...........................");
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

}
