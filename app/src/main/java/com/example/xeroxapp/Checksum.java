package com.example.xeroxapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Checksum extends AppCompatActivity implements PaytmPaymentTransactionCallback {

    String custid="", orderId="", mid="";
    int transaction_amount;
    String Email,doc_cnt;
    TextView tv_amount,tv_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checksum);
        //initOrderId();
        tv_amount = findViewById(R.id.cs_amount);
        tv_status = findViewById(R.id.cs_status);
        transaction_amount = 0;
        System.out.println("Entered Checksum.java");
        Intent intent = getIntent();
        transaction_amount = intent.getIntExtra("Pay_Amount",0);
        Email = intent.getStringExtra("email");
        doc_cnt = intent.getStringExtra("docs_count");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
       /* Intent intent = getIntent();
        orderId = intent.getExtras().getString("orderid");
        custid = intent.getExtras().getString("custid");*/

        orderId=generateString();
        custid=generateString();

        System.out.println("Order id =>> "+orderId);
        System.out.println("Customer id =>> "+custid);
        System.out.println("Checksum-Amount((((((((((((---------------->>>>>>>>>> "+transaction_amount);

        //mid = "MZfxWW12193811341410";
         mid = "rycufK58784621542404";
        sendUserDetailTOServerdd dl = new sendUserDetailTOServerdd();
        dl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
    public class sendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(Checksum.this);
        //private String orderId , mid, custid, amt;
        String amount_to_pay;
        String url =Constants.CHECKSUM_URL;
        String verifyurl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
       // String verifyurl = " http://192.168.0.103/xeroxapp/paytm/verifyChecksum.php";
        // "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID"+orderId;
     //  String verifyurl="https://securegw.paytm.in/theia/api/v1/processTransaction?mid="+mid+"&orderId="+orderId;
        String CHECKSUMHASH ="";
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }
        protected String doInBackground(ArrayList<String>... alldata) {
            Jsonparser jsonParser = new Jsonparser(Checksum.this);
            amount_to_pay = Integer.toString(transaction_amount);
            String param=
                    "MID="+mid+
                            "&ORDER_ID=" + orderId+
                            "&CUST_ID="+custid+
                            "&CHANNEL_ID=WAP&TXN_AMOUNT="+amount_to_pay+"&WEBSITE=WEBSTAGING"+
                            "&CALLBACK_URL="+ verifyurl+"&INDUSTRY_TYPE_ID=Retail";
            JSONObject jsonObject = jsonParser.makeHttpRequest(url,"POST",param);
            // yaha per checksum ke saht order id or status receive hoga..
            // Log.e("CheckSum result >>",jsonObject.toString());
            if(jsonObject != null){
                // Log.e("CheckSum result >>",jsonObject.toString());
                try {
                    CHECKSUMHASH=jsonObject.has("CHECKSUMHASH")?jsonObject.getString("CHECKSUMHASH"):"";
                    //  Log.e("CheckSum result >>",CHECKSUMHASH);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return CHECKSUMHASH;
        }
        @Override
        protected void onPostExecute(String result) {
            // Log.e(" setup acc ","  signup result  " + result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            PaytmPGService Service = PaytmPGService.getStagingService();
            // when app is ready to publish use production service
            // PaytmPGService  Service = PaytmPGService.getProductionService();
            // now call paytm service here
            //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
            HashMap<String, String> paramMap = new HashMap<String, String>();
            //these are mandatory parameters
            paramMap.put("MID", mid); //MID provided by paytm
            paramMap.put("ORDER_ID", orderId);
            paramMap.put("CUST_ID", custid);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", amount_to_pay);
            paramMap.put("WEBSITE", "WEBSTAGING");
            paramMap.put("CALLBACK_URL" ,verifyurl);
            //paramMap.put( "EMAIL" , "abc@gmail.com");   // no need
            // paramMap.put( "MOBILE_NO" , "9144040888");  // no need
            paramMap.put("CHECKSUMHASH" ,CHECKSUMHASH);
            //paramMap.put("PAYMENT_TYPE_ID" ,"CC");    // no need
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");
            PaytmOrder Order = new PaytmOrder(paramMap);
            //  Log.e("checksum ", "param "+ paramMap.toString());
            Service.initialize(Order,null);
            // start payment service call here
            Service.startPaymentTransaction(Checksum.this, true, true,
                    Checksum.this  );
        }
    }
    @Override
    public void onTransactionResponse(Bundle bundle) {
        System.out.println("Chechsum :: response true :: "+bundle.toString());
        displayTransactionDetails(bundle);
    }
    @Override
    public void networkNotAvailable() {
        Toast.makeText(Checksum.this,"Network not available",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(Checksum.this,"Client authentication failed"+s,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void someUIErrorOccurred(String s) {
        // Log.e("checksum ", " ui fail respon  "+ s );
        Toast.makeText(Checksum.this,"Some UI error occurred"+s,Toast.LENGTH_SHORT).show();
        System.out.println("Error loading ui  ::: "+s);
    }
    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        //  Log.e("checksum ", " error loading pagerespon true "+ s + "  s1 " + s1);
        //Toast.makeText(Checksum.this,"Error loading webpage"+s+" | "+s1,Toast.LENGTH_SHORT).show();
        System.out.println("Error loading web page :: "+s+" ::: s1 :::: "+s1);
    }
    @Override
    public void onBackPressedCancelTransaction() {

        System.out.println("Cancelled transaction......................");
        Toast.makeText(this,"Transaction cancelled by back press",Toast.LENGTH_SHORT).show();
        tv_status.setText("Transaction Cancelled");
    }
    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        //  Log.e("checksum ", "  transaction cancel " );
        System.out.println("Cancelled............");
        Toast.makeText(this,"Transaction cancelled",Toast.LENGTH_SHORT).show();
    }

    private String generateString() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    void displayTransactionDetails(Bundle bundle){
        String status = bundle.getString("STATUS");
        String amount = bundle.getString("TXNAMOUNT");
        String orderid = bundle.getString("ORDERID");
        String txnid = bundle.getString("TXNID");
        System.out.println("Transaction status-------------------->>>>>>>>>  "+status+" -- "+amount+" -- "+orderid+"  -- "+txnid);
        tv_amount.setText("Rs. "+amount);
        switch(status){
            case "TXN_SUCCESS" : tv_status.setText("Payment Successful!");
                sendPaymentDetailstoDB(Email,"Done",Integer.toString(transaction_amount),txnid,orderid,doc_cnt);
                break;
            case "TXN_FAILURE" : tv_status.setText("Payment Failed");
                break;
            default:tv_status.setText("Payment status");
        }

    }

    void sendPaymentDetailstoDB(String email,String status,String amount,String txn_id,String order_id,String count)
    {
        JSONObject jobj = new JSONObject();
        try {
            jobj.put("EMAIL",email);
            jobj.put("STATUS",status);
            jobj.put("AMOUNT",amount);
            jobj.put("TXN_ID",txn_id);
            jobj.put("ORDER_ID",order_id);
            jobj.put("Docs",count);
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
                System.out.println("Some error occurred...........................");
            }
        });
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

}