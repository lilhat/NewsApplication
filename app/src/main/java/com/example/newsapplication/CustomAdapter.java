package com.example.newsapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapplication.Models.Headlines;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private final Context context;
    private final List<Headlines> headlines;

    public CustomAdapter(Context context, List<Headlines> headlines) {
        this.context = context;
        this.headlines = headlines;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.headline_items, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        holder.news_title.setText(headlines.get(position).getTitle());
        holder.news_source.setText(headlines.get(position).getSource().getName());

        if (headlines.get(position).getUrlToImage()!=null && !headlines.get(position).getUrlToImage().isEmpty()){
            Picasso.get().load(headlines.get(position).getUrlToImage()).into(holder.news_img);
        }

    }

    @Override
    public int getItemCount() {
        return headlines.size();
    }
}
