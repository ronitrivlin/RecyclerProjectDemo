package com.example.recyclerprojectdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Item> mData;

    public MyAdapter(ArrayList<Item> data)
    {
        mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final ImageView ivImage;
        public final TextView tvText1;
        public final TextView tvText2;
        public  ViewHolder(View itemView){
            super(itemView);
            ivImage = itemView.findViewById(R.id.imageView);
            tvText1 = itemView.findViewById(R.id.text1);
            tvText2 = itemView.findViewById(R.id.text2);
        }
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_layout , parent, false);
        //  עם התצוגה שלנו ViewHolder ניצור  ונחזיר את ה
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.tvText1.setOnClickListener(new View.OnClickListener() {    @Override
        public void onClick(View v) {
            int position = viewHolder.getAdapterPosition();
            Item data = mData.get(position);
            Toast.makeText(parent.getContext(), data.getDescription(),  Toast.LENGTH_LONG).show();
        }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        Item item = mData.get(position);
        holder.tvText1.setText(item.getName());
        holder.tvText2.setText(item.getDescription());
        holder.ivImage.setImageResource(R.drawable.dog);

    }

    @Override
    public int getItemCount() {

        return mData.size();
    }
}
