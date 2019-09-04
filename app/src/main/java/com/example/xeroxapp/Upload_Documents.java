package com.example.xeroxapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.UUID;


public class Upload_Documents extends AppCompatActivity {

    private  static final int READ_REQUEST_CODE =1;
    private static final String TAG="Upload_Documents";
   // RecyclerView recyclerview;
    //DirectoryEntryAdapter adapter;
    RecyclerView.LayoutManager layoutmanager;
    String displayname= null;
    String filesize = null;
    Uri uri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__documents);
        performFileSearch();
/*
        recyclerview=findViewById(R.id.recyclerview_directory_entries);
        layoutmanager=recyclerview.getLayoutManager();
        recyclerview.scrollToPosition(0);
        recyclerview.setAdapter(adapter);*/
    }

    public void performFileSearch(){
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        i.setType("*/*");
        String mimeTypes[]={"image/*","pdf/*","text/*"};
        if(mimeTypes !=null){
            i.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        }

        startActivityForResult(i,READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{
            if(requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK)
            {
                //Uri uris[] = null;

                if(data != null)
                {
                    uri = data.getData();

                    //****************
                    Log.i(TAG,"Uri : "+uri.toString());
                    //******************
                    System.out.println("Urirrrrrrrrrrrrrrrrrrr :::::: "+uri);
                    getMetadata(uri);
                    Toast.makeText(getApplicationContext(),displayname,Toast.LENGTH_LONG).show();
                   System.out.println("Pppppppppppppppppppppppppppppp  "+data.getData().getPath());
                        if(uploadMultipart(uri)){
                            System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSS");
                        }


                    //updateEntries(uri);
                    /*
                ClipData clipData = data.getClipData();
                if(clipData != null)
                {
                    uris = new Uri[clipData.getItemCount()];
                    for(int k=0;k<clipData.getItemCount();k++)
                    {
                        uris[k]=clipData.getItemAt(k).getUri();
                        System.out.println("Uriiiiiiiiiiiiiiiii  ::: "+uris[k]);
                    }
                }*/

                    ImageView image=findViewById(R.id.doc_image);
                    image.setImageBitmap(getBitmapFromUri(uri));


                }
            }

        }catch(Exception e){
            e.getStackTrace();
        }

    }

    public void getMetadata(Uri uri){
        Cursor cursor =getApplicationContext().getContentResolver().query(uri,null,null,null,null,null);

        if(cursor != null && cursor.moveToFirst()){
            displayname = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            int sizeindex  = cursor.getColumnIndex(OpenableColumns.SIZE);


            if(!cursor.isNull(sizeindex))
            {
                filesize = cursor.getString(sizeindex);
            }else
            {
                filesize="unknown";
            }
            System.out.println("Sizeeeeeeeeeeeeeeeeeeeeeee :  :  "+filesize);
            System.out.println("Display nameeeeeeeeeeeeee  ::  "+displayname);

        }

        cursor.close();
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

   /* void updateEntries(Uri uri){

    }
*/

   public boolean uploadMultipart(Uri uri){

       String path = uri.getPath();
       //String path = FilePath.getPath(this,uri);
       System.out.println("Pathhhhhhhhhhhhhhhhhh : : "+path);

       if(path==null){
           //display toast
           System.out.println("No pathhhhhhhhhhhhhhhhhhhhhhhh");
       }else{
           try{
               String uploadId = UUID.randomUUID().toString();//"http://192.168.137.69/xeroxapp/uploadfile.php"
               System.out.println("Iddddddddddddddddddddddddd:: "+uploadId);
               new MultipartUploadRequest(this,uploadId,Constants.UPLOAD_URL)
                       .addFileToUpload(path,"pdf")
                       .addParameter("name",displayname)
                       //.addParameter("type","pdf")
                       //.addParameter("size",filesize)
                       .setNotificationConfig(new UploadNotificationConfig())
                       .setMaxRetries(2)
                       .startUpload();

               System.out.println("Upload completeeeeeeeeeeeeeeeeeeeeeee");
               return true;
           }catch (Exception e){
               Toast.makeText(Upload_Documents.this,e.getMessage(),Toast.LENGTH_LONG).show();
               System.out.println("Errrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr"+e.getMessage());
           }

       }
return false;
   }
}
