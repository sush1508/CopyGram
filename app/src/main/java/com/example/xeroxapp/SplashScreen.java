package com.example.xeroxapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        if(haveNetwork()){
            Logolauncher logolauncher = new Logolauncher();
            logolauncher.start();
        }
        else if(!haveNetwork()){

            Toast.makeText(SplashScreen.this,"No network",Toast.LENGTH_SHORT).show();
        }

    }

    private class Logolauncher extends Thread{
        public void run()
        {
            try{
                sleep(4000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            Intent intent = new Intent(SplashScreen.this,MainActivity.class);
            startActivity(intent);
            SplashScreen.this.finish();
        }
    }

    public boolean haveNetwork(){

        boolean have_wifi = false;
        boolean have_mobiledata = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo [] networkInfos = cm.getAllNetworkInfo();

        for(NetworkInfo info : networkInfos){
            if(info.getTypeName().equalsIgnoreCase("WIFI"))
                if(info.isConnected())
                have_wifi=true;
            if(info.getTypeName().equalsIgnoreCase("MOBILE"))
                if(info.isConnected())
                have_mobiledata=true;

        }
        return have_mobiledata|have_wifi;
    }
}
