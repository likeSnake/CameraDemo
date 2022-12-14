package com.jcl.camerademo.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.jcl.camerademo.R;


public class Slimming extends AppCompatActivity implements View.OnClickListener{

    private PhotoView slimming_pv;
    public static final int CHOOSE_PHOTO = 2;
    private RelativeLayout cancel;
    private RelativeLayout save;
    private TextView yes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slimming2);
        slimming_pv = findViewById(R.id.slimming_pv);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CHOOSE_PHOTO);
        } else {
            openAlbum();
        }
    }


    private void openAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://media/internal/images/media"));
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PHOTO:
                //????????????
                if (resultCode == RESULT_OK && null != data) {
                    // handleImageBeforeKitKat(data);
                    handleImageOnKitkat(data);
                }else if (resultCode != RESULT_OK){
                    finish();
                }
            default:
                break;
        }
    }
    @TargetApi(19)
    private void handleImageOnKitkat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //?????????document?????????uri????????????document id??????
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                        "//downloads/public_downloads"), Long.parseLong(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //?????????content?????????uri??????????????????????????????
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //?????????File?????????uri?????????????????????????????????
            imagePath = uri.getPath();
        }
        //??????????????????????????????
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }
    private String getImagePath(Uri uri,String selection){
        String path=null;
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath){
        if(imagePath!=null){
            System.out.println(imagePath);
            Bitmap bitmap= BitmapFactory.decodeFile(imagePath);
            slimming_pv.setImageBitmap(bitmap);
            //albumsPicture.setImageBitmap(bitmap);//???????????????????????????
        }else {
            Toast.makeText(this,"??????????????????",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                finish();
                break;
            case R.id.save:
                showQfDialog(v);
                break;
        }
    }

    public void showQfDialog(View view){
        TextView title = new TextView(this);
        //AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        // ???????????? dialogView ??????
        android.app.AlertDialog.Builder alertBuilder = new
                android.app.AlertDialog.Builder(Slimming.this);
        final android.app.AlertDialog dialog = alertBuilder.create();
        View dialogView = null;
        //?????????????????????
        dialogView = View.inflate(Slimming.this,
                R.layout.dialog_exit, null);
        dialog.setView(dialogView);
        dialog.getWindow().setBackgroundDrawableResource(com.google.android.material.R.color.mtrl_btn_transparent_bg_color);

        dialog.setView(dialogView);
        dialog.setCustomTitle(title);
        dialog.show();
        yes = dialogView.findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(Slimming.this, "The picture is saved", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


    }
}