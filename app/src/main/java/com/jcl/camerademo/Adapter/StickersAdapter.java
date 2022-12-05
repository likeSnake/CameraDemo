package com.jcl.camerademo.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jcl.camerademo.pojo.StickersPojo;

import  com.jcl.camerademo.R;

import java.util.List;

public class StickersAdapter extends RecyclerView.Adapter<StickersAdapter.ViewHolder> {

    private List<StickersPojo> stickersPojos;
    private Context context;
    private ClickEvents clickEvents;

    public StickersAdapter(List<StickersPojo> stickersPojos, Context context, ClickEvents clickEvents){
        this.stickersPojos = stickersPojos;
        this.context = context;
        this.clickEvents = clickEvents;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView item1;
        private final ImageView item2;
        private final ImageView item3;
        private final ImageView item4;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item1 = itemView.findViewById(R.id.item_sticker_iv1);
            item2 = itemView.findViewById(R.id.item_sticker_iv2);
            item3 = itemView.findViewById(R.id.item_sticker_iv3);
            item4 = itemView.findViewById(R.id.item_sticker_iv4);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stickers, parent, false);

        return new StickersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StickersPojo info = stickersPojos.get(position);
        System.out.println(info.getList().size());
        Bitmap sticker1 = info.getList().get(0);
        Bitmap sticker2 =  info.getList().get(1);
        Bitmap sticker3 = info.getList().get(2);
        Bitmap sticker4 = info.getList().get(3);
        holder.item1.setImageBitmap(sticker1);
        holder.item2.setImageBitmap(sticker2);
        holder.item3.setImageBitmap(sticker3);
        holder.item4.setImageBitmap(sticker4);

        holder.item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickEvents != null){
                    clickEvents.getBitmap(sticker1);
                }
            }
        });
        holder.item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickEvents != null){
                    clickEvents.getBitmap(sticker2);
                }
            }
        });
        holder.item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickEvents != null){
                    clickEvents.getBitmap(sticker3);
                }
            }
        });
        holder.item4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickEvents != null){
                    clickEvents.getBitmap(sticker4);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return stickersPojos.size();
    }

    public static interface ClickEvents{
        public void getBitmap(Bitmap bitmap);
    }
}
