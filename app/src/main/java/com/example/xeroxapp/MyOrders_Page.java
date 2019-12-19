package com.example.xeroxapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.xeroxapp.adapter.OrdersAdapter;
import com.example.xeroxapp.model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.Inflater;

public class MyOrders_Page extends Fragment {

    private View mainView;

    String u_email,responsemsg;
    Vector v;
    ListView listView;
    SwipeRefreshLayout refresh;
    ArrayList<Model> myOrdersList;
    private OrdersAdapter ordersAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myorders,container,false);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.orders_listview);
        refresh = view.findViewById(R.id.swiperefresh);
       // u_email = getArguments().getString("email");
        u_email = Constants.Email;
        //u_email = MainActivity.Email;

        v = new Vector();
        myOrdersList = new ArrayList<>();
        System.out.println("maillllllllllllllllllllllllllllllll--------0"+u_email);
        if(!u_email.equals("")){
            fetchOrderDetails();
        }else
        {
            addToList(null,null,null,"00-00-0000","0");
            showOrderDetails();
        }





        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                v.clear();
                if(!u_email.equals("")){
                    fetchOrderDetails();
                }else
                {
                    addToList(null,null,null,null,"0");
                    showOrderDetails();
                }
                if(refresh.isRefreshing()){
                    refresh.setRefreshing(false);
                }

            }
        });
    }



    void fetchOrderDetails(){
        String jsonstring  = "[{'User_email':'"+u_email+"'}]";
        System.out.println(jsonstring);
        JSONArray request = null;
        try {
            request = new JSONArray(jsonstring);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(com.android.volley.Request.Method.POST, Constants.FETCH_ORDER_URL, request, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                System.out.println("Response length===========>"+response.length());
                System.out.println(response);
                myOrdersList.clear();
                for(int i=0;i<response.length();i++)
                {
                    try {
                        JSONObject obj  = response.getJSONObject(i);
                        System.out.println("JSON object----. >"+obj);
                        responsemsg = obj.getString("message");

                        switch(responsemsg){
                            case "No order details" : addToList("No order details",null,null,null,"0");
                                                        showOrderDetails();
                                break;
                            case "Order details fetched" : String mail = obj.getString("useremail");
                                                        String orderStatus = obj.getString("orderStatus");
                                                        String orderId = obj.getString("orderId");
                                                        String orderDate = obj.getString("orderDate");
                                                        String txnId = obj.getString("txn_Id");
                                                        String docs_count = obj.getString("noofdocs");
                                                        addToList(orderStatus,orderId,orderDate,txnId,docs_count);
                                                        String order=
                                                            "\n User email : "+mail+
                                                            "\n\n Order Id :"+orderId+
                                                            "\n\n Transaction Id :"+txnId+
                                                            "\n\n Date :"+orderDate+
                                                            "\n\n No. of documents :"+docs_count+
                                                            "\n\n Order status :"+orderStatus+"\n";
                                                        //v.add(order);
                                                        System.out.println("----> "+v);
                                                        //showOrderDetails(v);
                                                        showOrderDetails();
                                 break;
                            case "Error while fetching details" : addToList("Error while fetching details",null,null,null,"0");
                                                                    showOrderDetails();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
               // Toast.makeText(getActivity(),responsemsg,Toast.LENGTH_SHORT).show();
                   // removeDuplicates(v);




            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Errrrrrrrrrrrrrrrorrrrrrrrrrrrrrr =>"+error.getMessage());
            }
        });

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);

    }

    public static Vector removeDuplicates(Vector v)
    {
        for(int i=0;i<v.size();i++){
            for(int j=0;j<v.size();j++)
            {
                if(i!=j)
                {
                    if(v.elementAt(i).equals(v.elementAt(j)))
                    {
                        v.removeElementAt(j);
                    }
                }
            }
        }
        return v;
    }


    private void addToList(String ostatus,String order_id,String otxn_id,String odate,String dcnt){

        if(ostatus.equals("Pending")){
            myOrdersList.add(new Model(R.drawable.hourglass,ostatus,order_id,odate,otxn_id,dcnt));
        }
        else if(ostatus.equals("Complete")) {
            myOrdersList.add(new Model(R.drawable.complete,ostatus,order_id,odate,otxn_id,dcnt));
        }
        else{
            myOrdersList.add(new Model(R.drawable.hourglass,ostatus,order_id,odate,otxn_id,dcnt));
        }

    }

    void showOrderDetails()
    {
       // ArrayList arrayList = new ArrayList(v);

        ordersAdapter=new OrdersAdapter(getActivity(),myOrdersList);
        listView.setAdapter(ordersAdapter);


       // final ArrayAdapter<Vector> arrayAdapter = new ArrayAdapter<Vector>(getActivity(),R.layout.list_item,v);
       // listView.setAdapter(arrayAdapter);
    }
}
