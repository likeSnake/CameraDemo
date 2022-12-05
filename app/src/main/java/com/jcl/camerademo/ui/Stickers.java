package com.jcl.camerademo.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.jcl.camerademo.Adapter.StickersAdapter;
import  com.jcl.camerademo.R;
import com.jcl.camerademo.Util.ImageUtil;
import com.jcl.camerademo.pojo.StickersPojo;
import com.jcl.camerademo.sticker.DrawableSticker;
import com.jcl.camerademo.sticker.StickerView;

import java.util.ArrayList;
import java.util.List;

public class Stickers extends AppCompatActivity implements View.OnClickListener{
    public static final int CHOOSE_PHOTO = 2;
    private Button pestDection=null;
    private Button pictureSave=null;
    private PhotoView show_edit_iv;
    private RelativeLayout cancel;
    private RelativeLayout save;
    private TextView yes;
    private StickersAdapter stickersAdapter;
    private RecyclerView recycler;
    private StickerView stickerView;
    List<StickersPojo> list = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_albums);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        recycler = findViewById(R.id.recycler);
        stickerView = findViewById(R.id.sticker_view);

        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        show_edit_iv = findViewById(R.id.show_edit_iv);
        initStickers();
        start(false,list);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CHOOSE_PHOTO);
        } else {
            openAlbum();
        }

    }

    public void initStickers(){
        list = ImageUtil.initSticker(Stickers.this);
    }

    private void openAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://media/internal/images/media"));
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
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

    public void start(Boolean b, List<StickersPojo> list){
        LinearLayoutManager manager = new LinearLayoutManager(Stickers.this, LinearLayoutManager.VERTICAL, b);
        stickersAdapter = new StickersAdapter(list, Stickers.this, new StickersAdapter.ClickEvents() {
            @Override
            public void getBitmap(Bitmap bitmap) {
                BitmapDrawable bd=new BitmapDrawable(bitmap);
                stickerView.addSticker(new DrawableSticker(bd));

            }
        });
        recycler.setLayoutManager(manager);
        if (b){
            //倒序
            recycler.scrollToPosition(stickersAdapter.getItemCount()-1);
        }
        recycler.setAdapter(stickersAdapter);
    }

    public void showQfDialog(View view){
        TextView title = new TextView(this);
        //AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        // 创建一个 dialogView 弹窗
        android.app.AlertDialog.Builder alertBuilder = new
                android.app.AlertDialog.Builder(Stickers.this);
        final android.app.AlertDialog dialog = alertBuilder.create();
        View dialogView = null;
        //设置对话框布局
        dialogView = View.inflate(Stickers.this,
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
                Toast.makeText(Stickers.this, "The picture is saved", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PHOTO:
                //相册照片
                if (requestCode == CHOOSE_PHOTO && resultCode == RESULT_OK && null != data) {

                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitkat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }else if (requestCode == CHOOSE_PHOTO && resultCode != RESULT_OK){
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
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                        "//downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是File类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        //根据图片路径显示图片
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
            Bitmap bitmap= BitmapFactory.decodeFile(imagePath);
            show_edit_iv.setImageBitmap(bitmap);
            //albumsPicture.setImageBitmap(bitmap);//将图片放置在控件上
        }else {
            Toast.makeText(this,"得到图片失败",Toast.LENGTH_SHORT).show();
        }
    }
}