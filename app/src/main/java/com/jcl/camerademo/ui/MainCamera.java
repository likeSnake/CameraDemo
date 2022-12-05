package com.jcl.camerademo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.jcl.camerademo.R;

public class MainCamera extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout camera;
    private LinearLayout stickers;
    private LinearLayout slimming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main2);
        /*MainCamera.java*/
        camera = findViewById(R.id.camera);
        stickers = findViewById(R.id.stickers);
        slimming = findViewById(R.id.slimming);
        camera.setOnClickListener(this);
        stickers.setOnClickListener(this);
        slimming.setOnClickListener(this);

    }

    private void startCamera() {
        Intent intent = new Intent(MainCamera.this, Photograph.class);
        startActivity(intent);
    }
    public void startAlbum(){
        Intent intent = new Intent(MainCamera.this,Stickers.class);
        startActivity(intent);
    }
    public void startSlimming(){
        Intent intent = new Intent(MainCamera.this,Slimming.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.camera:
                startCamera();
                break;
            case R.id.stickers:
                System.out.println("打开系统相册");
                startAlbum();
                break;
            case R.id.slimming:
                startSlimming();
        }
    }
}