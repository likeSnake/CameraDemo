package com.jcl.camerademo.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import  com.jcl.camerademo.R;
import com.jcl.camerademo.pojo.StickersPojo;

import java.util.ArrayList;
import java.util.List;

public class ImageUtil {


    public static List<StickersPojo> initSticker(Context context){
        List<StickersPojo> list = new ArrayList<>();
        List<Bitmap> image1 = new ArrayList<>();
        List<Bitmap> image2 = new ArrayList<>();
        List<Bitmap> image3 = new ArrayList<>();
        List<Bitmap> image4 = new ArrayList<>();
        List<Bitmap> image5 = new ArrayList<>();
        List<Bitmap> image6 = new ArrayList<>();
        List<Bitmap> image7 = new ArrayList<>();

        Drawable drawable1 = context.getResources().getDrawable(R.drawable.sticker_101);
        BitmapDrawable bd1 = (BitmapDrawable) drawable1;
        Bitmap bmm1 = bd1.getBitmap();

        Drawable drawable2 = context.getResources().getDrawable(R.drawable.sticker_201);
        BitmapDrawable bd2 = (BitmapDrawable) drawable2;
        Bitmap bmm2 = bd2.getBitmap();

        Drawable drawable3 = context.getResources().getDrawable(R.drawable.sticker_301);
        BitmapDrawable bd3 = (BitmapDrawable) drawable3;
        Bitmap bmm3 = bd3.getBitmap();

        Drawable drawable4 = context.getResources().getDrawable(R.drawable.sticker_401);
        BitmapDrawable bd4 = (BitmapDrawable) drawable4;
        Bitmap bmm4 = bd4.getBitmap();

        Drawable drawable5 = context.getResources().getDrawable(R.drawable.sticker_501);
        BitmapDrawable bd5 = (BitmapDrawable) drawable5;
        Bitmap bmm5 = bd5.getBitmap();

        Drawable drawable6 = context.getResources().getDrawable(R.drawable.sticker_601);
        BitmapDrawable bd6 = (BitmapDrawable) drawable6;
        Bitmap bmm6 = bd6.getBitmap();

        Drawable drawable7 = context.getResources().getDrawable(R.drawable.sticker_701);
        BitmapDrawable bd7 = (BitmapDrawable) drawable7;
        Bitmap bmm7 = bd7.getBitmap();

        Drawable drawable8 = context.getResources().getDrawable(R.drawable.sticker_801);
        BitmapDrawable bd8 = (BitmapDrawable) drawable8;
        Bitmap bmm8 = bd8.getBitmap();

        Drawable drawable9 = context.getResources().getDrawable(R.drawable.sticker_901);
        BitmapDrawable bd9 = (BitmapDrawable) drawable9;
        Bitmap bmm9 = bd9.getBitmap();

        Drawable drawable10 = context.getResources().getDrawable(R.drawable.sticker_1001);
        BitmapDrawable bd10 = (BitmapDrawable) drawable10;
        Bitmap bmm10 = bd10.getBitmap();

        Drawable drawable11 = context.getResources().getDrawable(R.drawable.sticker_1101);
        BitmapDrawable bd11 = (BitmapDrawable) drawable11;
        Bitmap bmm11 = bd11.getBitmap();

        Drawable drawable12 = context.getResources().getDrawable(R.drawable.sticker_1201);
        BitmapDrawable bd12 = (BitmapDrawable) drawable12;
        Bitmap bmm12 = bd12.getBitmap();

        Drawable drawable13 = context.getResources().getDrawable(R.drawable.sticker_1301);
        BitmapDrawable bd13 = (BitmapDrawable) drawable13;
        Bitmap bmm13 = bd13.getBitmap();

        Drawable drawable14 = context.getResources().getDrawable(R.drawable.sticker_1401);
        BitmapDrawable bd14 = (BitmapDrawable) drawable14;
        Bitmap bmm14 = bd14.getBitmap();

        Drawable drawable15 = context.getResources().getDrawable(R.drawable.sticker_1501);
        BitmapDrawable bd15 = (BitmapDrawable) drawable15;
        Bitmap bmm15 = bd15.getBitmap();

        Drawable drawable16 = context.getResources().getDrawable(R.drawable.sticker_1601);
        BitmapDrawable bd16 = (BitmapDrawable) drawable16;
        Bitmap bmm16 = bd16.getBitmap();

        Drawable drawable17 = context.getResources().getDrawable(R.drawable.sticker_1701);
        BitmapDrawable bd17 = (BitmapDrawable) drawable17;
        Bitmap bmm17 = bd17.getBitmap();

        Drawable drawable18 = context.getResources().getDrawable(R.drawable.sticker_1801);
        BitmapDrawable bd18 = (BitmapDrawable) drawable18;
        Bitmap bmm18 = bd18.getBitmap();

        Drawable drawable19 = context.getResources().getDrawable(R.drawable.sticker_1901);
        BitmapDrawable bd19 = (BitmapDrawable) drawable19;
        Bitmap bmm19 = bd19.getBitmap();

        Drawable drawable20 = context.getResources().getDrawable(R.drawable.sticker_2001);
        BitmapDrawable bd20 = (BitmapDrawable) drawable20;
        Bitmap bmm20 = bd20.getBitmap();

        Drawable drawable21 = context.getResources().getDrawable(R.drawable.sticker_2101);
        BitmapDrawable bd21 = (BitmapDrawable) drawable21;
        Bitmap bmm21 = bd21.getBitmap();

        Drawable drawable22 = context.getResources().getDrawable(R.drawable.sticker_2201);
        BitmapDrawable bd22 = (BitmapDrawable) drawable22;
        Bitmap bmm22 = bd22.getBitmap();

        Drawable drawable23 = context.getResources().getDrawable(R.drawable.sticker_2301);
        BitmapDrawable bd23 = (BitmapDrawable) drawable23;
        Bitmap bmm23 = bd23.getBitmap();

        Drawable drawable24 = context.getResources().getDrawable(R.drawable.sticker_2401);
        BitmapDrawable bd24 = (BitmapDrawable) drawable24;
        Bitmap bmm24 = bd24.getBitmap();

        Drawable drawable25 = context.getResources().getDrawable(R.drawable.sticker_2501);
        BitmapDrawable bd25 = (BitmapDrawable) drawable25;
        Bitmap bmm25 = bd25.getBitmap();

        Drawable drawable26 = context.getResources().getDrawable(R.drawable.sticker_2601);
        BitmapDrawable bd26 = (BitmapDrawable) drawable26;
        Bitmap bmm26 = bd26.getBitmap();

        Drawable drawable27 = context.getResources().getDrawable(R.drawable.sticker_2701);
        BitmapDrawable bd27 = (BitmapDrawable) drawable27;
        Bitmap bmm27 = bd27.getBitmap();
        image1.add(bmm1);
        image1.add(bmm2);
        image1.add(bmm3);
        image1.add(bmm4);
        System.out.println("长度:"+image1.size());
        list.add(new StickersPojo(image1));

        image2.add(bmm5);
        image2.add(bmm6);
        image2.add(bmm7);
        image2.add(bmm8);
        list.add(new StickersPojo(image2));

        image3.add(bmm9);
        image3.add(bmm10);
        image3.add(bmm11);
        image3.add(bmm12);
        list.add(new StickersPojo(image3));

        image4.add(bmm13);
        image4.add(bmm14);
        image4.add(bmm15);
        image4.add(bmm16);
        list.add(new StickersPojo(image4));


        image5.add(bmm17);
        image5.add(bmm18);
        image5.add(bmm19);
        image5.add(bmm20);
        list.add(new StickersPojo(image5));

        image6.add(bmm21);
        image6.add(bmm22);
        image6.add(bmm23);
        image6.add(bmm24);
        list.add(new StickersPojo(image6));

        image7.add(bmm25);
        image7.add(bmm26);
        image7.add(bmm27);
        image7.add(bmm1);
        list.add(new StickersPojo(image7));

        return list;
    }
}
