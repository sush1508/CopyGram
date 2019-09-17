package com.example.xeroxapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import android.support.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AppCompatActivity;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Documents extends AppCompatActivity implements FileInfoDialog.FileInfoDialogListener {

    Button file_btn,payment_btn;
    FloatingActionButton choose_btn;
    static String u_email;
    String f_useremail,f_filename,f_filetype;
    String responsemsg;
    String selectedFilename;
    Vector v;
    ListView lst;
    String pcolor,psides,ppages,pcopies;
    public static final int COLOR_PRINT_1=5,BLACK_PRINT_1=1;
    int totalAmount,cnt;
    int Itemcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);
        totalAmount = 0;
        Itemcount = 0;
        cnt = 0;
        choose_btn=findViewById(R.id.pickbutton);
        file_btn = findViewById(R.id.myfiles_button);
        lst = findViewById(R.id.listView);
        v = new Vector();
        Intent e = getIntent();
        u_email = e.getStringExtra("EMAIL");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return;
            }
        }

        payment_btn = findViewById(R.id.doc_payment_button);
        payment_btn.setEnabled(false);
        payment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Documents.this,Dashboard_page.class);
                intent1.putExtra("flag","1");
                intent1.putExtra("orderAmount",totalAmount);
                intent1.putExtra("doc_count",Itemcount);
                intent1.putExtra("email",u_email);
                System.out.println("Costtttttttttttttttttttttttttttttttt ======= >      "+totalAmount);
                startActivity(intent1);
            }
        });

        choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialFilePicker()
                        .withActivity(Documents.this)
                        .withRequestCode(10)
                        .start();

            }
        });

        file_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchFileInfo();
                //v.clear();
            }
        });

    }

    private void enable_button() {
        payment_btn.setBackground(getDrawable(R.drawable.buttonshape));
        payment_btn.setEnabled(true);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
            enable_button();
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
            }
        }
    }

    ProgressDialog progress;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if(requestCode == 10 && resultCode == RESULT_OK){

            progress = new ProgressDialog(Documents.this);
            progress.setTitle("Uploading");
            progress.setMessage("Please wait...");
            progress.show();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                        System.out.println("gggggggggggggg  : "+data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                        String filesize = Long.toString(f.length());
                        System.out.println("Sizeeeeee :"+filesize);
                        String file_path = f.getAbsolutePath()
                                .replaceAll("[\\s+-]","")
                                .replaceAll("&","")
                                .replaceAll("\\*","")
                                .replaceAll("#","")
                                .replaceAll("@","")
                                .replaceAll("!","").toLowerCase();
                        //file_path=file_path.substring(file_path);

                        System.out.println("file path++++++++++:"+file_path);
                        String content_type  = getMimeType(file_path);
                        if(content_type==null){System.out.println("Sorrrryyyyyyy");}

                        System.out.println("Content typpppppeeeeeeeeeeee :"+content_type);

                        String filename = file_path.substring(file_path.lastIndexOf("/")+1);
                        filename = u_email.substring(0,4)+"_"+filename;
                        System.out.println("File name ::::::::::"+filename);
                        OkHttpClient client = new OkHttpClient();
                        RequestBody file_body;
                        Request request;

                        System.out.println("ddddddddddddddddddddddd: "+MediaType.parse(content_type)+":dddddddddddddddd");
                        file_body = RequestBody.create(MediaType.parse(content_type),f);
                        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+file_body+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                        RequestBody request_body = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("type",content_type)
                                .addFormDataPart("uploaded_file",filename, file_body)
                                .addFormDataPart("fsize",filesize)
                                .addFormDataPart("useremail",u_email)
                                .build();
                        request = new Request.Builder()
                                .url(Constants.UPLOAD_URL)
                                .post(request_body)
                                .build();


                        Response response = client.newCall(request).execute();

                        if(!response.isSuccessful()){
                            throw new Exception("Error : "+response);
                        }
                        else
                        {
                            System.out.println("Doneeeeeeeeeeeeeeeeeeeeeeeee");

                        }

                        progress.dismiss();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            t.start();
        }
    }

    private String getMimeType(String path) {

        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        System.out.println("MIMETYPE ::::"+MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension));
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }


    public void fetchFileInfo(){

        String jsonstring  = "[{'User_email':'"+u_email+"'}]";
        System.out.println(jsonstring);
        JSONArray request = null;
        try {
            request = new JSONArray(jsonstring);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(com.android.volley.Request.Method.POST, Constants.FETCH_URL, request, new com.android.volley.Response.Listener<JSONArray>() {
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
                            v.add(f_filename);

                            System.out.println("Messageeeeeeeeeee ===> "+responsemsg);
                            System.out.println("user====>"+f_useremail+"\nfilename=====>"+f_filename+"\nfiletype===>"+f_filetype);
                            //Toast.makeText(Documents.this,responsemsg,Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            System.out.println("Message : " +responsemsg);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    System.out.println(v);
                }
                Toast.makeText(Documents.this,responsemsg,Toast.LENGTH_SHORT).show();
                removeDuplicates(v);



            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Errrrrrrrrrrrrrrrorrrrrrrrrrrrrrr =>"+error.getMessage());
            }
        });

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(Documents.this);
        requestQueue.add(jsonArrayRequest);

        final MyCustomListAdapter adapter = new MyCustomListAdapter(this,R.layout.my_list_item,v);
        lst.setAdapter(adapter);
        cnt=lst.getAdapter().getCount();

        adapter.setmyOnClickListener(new MyCustomListAdapter.myOnClickListener() {
            @Override
            public void myOnClick(int position, String itemname) {
                fillDocumentDetails(itemname);

            }
        });
    }

    public void fillDocumentDetails(String selectedFilename){
        FileInfoDialog fileInfoDialog = new FileInfoDialog();
        Bundle data  =new Bundle();
        data.putString("email",u_email);
        data.putString("filename",selectedFilename);
        fileInfoDialog.setArguments(data);
        fileInfoDialog.show(getSupportFragmentManager(),"File Details");
    }

    public static void removeDuplicates(Vector v)
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
    }

    @Override
    public void getTexts(String copies, String pages, String sides, String color) {
        System.out.println("((  " + copies + "  ||  " + pages + "  ||  " + sides + "  ||  " + color + "  ))");
        pcopies=copies;
        ppages=pages;
        psides=sides;
        pcolor=color;
        if(updateAmount(pcolor,pcopies,ppages,psides)){
            Itemcount++;
        }
        System.out.println("Count of list items============================> "+cnt+"  "+Itemcount);
        if(Itemcount == cnt)
        {
            enable_button();
        }
    }

    boolean updateAmount(String color,String copiesCount,String pageCount,String sides){
        int copyC,pageC;
        copyC = Integer.parseInt(copiesCount);
        pageC = Integer.parseInt(pageCount);
        switch(color)
        {
            case "Color":
                totalAmount +=(copyC*pageC*COLOR_PRINT_1);
                /*System.out.println("...........CopyC=> "+copyC+" ..........pageC=> "+pageC+"................" );
                System.out.println("Color amount ::::::::::::::::::: >>>>>>>>>>>>> "+totalAmount);*/
                break;
            case "Black and white":
                totalAmount +=(copyC*pageC*BLACK_PRINT_1);
                break;
            default:
        }
        System.out.println("Total amount=====================>"+totalAmount);
        payment_btn.setText("Pay Rs."+Integer.toString(totalAmount));
        return true;
    }

}
