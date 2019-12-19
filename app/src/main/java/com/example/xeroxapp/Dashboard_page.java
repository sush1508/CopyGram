package com.example.xeroxapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Dashboard_page extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    public String user_email;
    int file_count;
    TextView nav_email_id;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_page);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Copygram");

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
        //user_email=intent.getStringExtra("EMAIL");
        user_email=Constants.Email;
        MainActivity.Email=user_email;
        nav_email_id = navigationView.getHeaderView(0).findViewById(R.id.nav_email);
        nav_email_id.setText(Constants.Email);


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
                toolbar.setSubtitle("Upload");
                break;
            case R.id.nav_myorders :
                System.out.println("Email of user---------------- > "+user_email);
                Bundle  data = new Bundle();
                data.putString("email",user_email);
                MyOrders_Page myOrders_page = new MyOrders_Page();
                myOrders_page.setArguments(data);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,myOrders_page).commit();
                toolbar.setSubtitle("Orders");
                break;
           /* case R.id.nav_payment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Payment_Page()).commit();
                break;*/
            case R.id.nav_contact:
                //Toast.makeText(this,"Contact",Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Contact_Page()).commit();
                toolbar.setSubtitle("Contact");
                break;
            case R.id.nav_about:
                //Toast.makeText(this,"About",Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new About_Page()).commit();
                toolbar.setSubtitle("About");
                break;
            case R.id.nav_logout:
                logoutUser();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        System.out.println(".............Help icon clicked..................."+item);
        if(item.getItemId()==R.id.help_icon){
            Intent i = new Intent(Dashboard_page.this,Help_page.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Upload_Page()).commit();
            //super.onBackPressed();
        }
        toolbar.setSubtitle("");
        //super.onBackPressed();
    }
    public void logoutUser(){
        new User(Dashboard_page.this).removeUser();
        Intent u=new Intent(Dashboard_page.this,MainActivity.class);
        startActivity(u);
        finish();
    }

}
