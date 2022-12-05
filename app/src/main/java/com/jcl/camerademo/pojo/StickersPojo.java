package com.jcl.camerademo.pojo;

import android.graphics.Bitmap;

import java.util.List;

public class StickersPojo {
    private List<Bitmap> bitmapList;

    public StickersPojo(List<Bitmap> bitmapList)     {
        this.bitmapList = bitmapList;
    }

    public List<Bitmap> getList() {
        return bitmapList;
    }

    public void setList(List<Bitmap> list) {
        this.bitmapList = list;
    }
}
