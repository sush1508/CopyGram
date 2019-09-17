package com.example.xeroxapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Dashboard_page extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    public String user_email;
    int file_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //First page to display after launching app(when navigation drawer is not used)
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Upload_Page()).commit();
            navigationView.setCheckedItem(R.id.nav_upload);
        }

        Intent intent = getIntent();
        user_email=intent.getStringExtra("EMAIL");


        //go to payment fragment if user has clicked proceed button in Documents activity
        int amt = 0;
        if(intent.getStringExtra("flag")!= null){
            String flag1=intent.getStringExtra(("flag"));
            if(flag1.equals("1"))
            {
                intent = getIntent();
                amt = intent.getIntExtra("orderAmount",0);
                file_count = intent.getIntExtra("doc_count",0);
                System.out.println("Dashboard---------------------->>>>>>>.  "+amt);
                Bundle b = new Bundle();
                b.putInt("OrderAmt",amt);
                b.putString("email",intent.getStringExtra("email"));
                b.putInt("file_count",file_count);
                Payment_Page payment_page = new Payment_Page();
                payment_page.setArguments(b);
                goToPayment(payment_page);
                //navigationView.setCheckedItem(R.id.nav_payment);
            }
        }
       /*
*/
    }

    public void goToPayment(Payment_Page paymentPage)
    {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =    fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, paymentPage);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_upload:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Upload_Page()).commit();

                break;
            case R.id.nav_myorders :
                System.out.println("Email of user---------------- > "+user_email);
                Bundle  data = new Bundle();
                data.putString("email",user_email);
                MyOrders_Page myOrders_page = new MyOrders_Page();
                myOrders_page.setArguments(data);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,myOrders_page).commit();
                break;
           /* case R.id.nav_payment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Payment_Page()).commit();
                break;*/
            case R.id.nav_contact:
                //Toast.makeText(this,"Contact",Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Contact_Page()).commit();
                break;
            case R.id.nav_about:
                //Toast.makeText(this,"About",Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new About_Page()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        super.onBackPressed();
    }
}
