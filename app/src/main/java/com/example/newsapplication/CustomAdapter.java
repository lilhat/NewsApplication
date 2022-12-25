package com.example.newsapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapplication.Models.Headlines;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private final Context context;
    private List<Headlines> headlines;
    private SelectListener listener;
    private int size;
    public CustomAdapter(Context context, List<Headlines> headlines, SelectListener listener) {
        this.context = context;
        this.headlines = headlines;
        this.listener = listener;
        size = headlines.size();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.headline_items, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        if (headlines.get(position).getImage_url()!=null && !headlines.get(position).getImage_url().isEmpty()){
            Picasso.get().load(headlines.get(position).getImage_url()).into(holder.news_img);
            holder.news_title.setText(headlines.get(position).getTitle());
            holder.news_source.setText(headlines.get(position).getSource_id());
            holder.news_text.setText(headlines.get(position).getDescription());
            if(headlines.get(position).getCreator() != null){
                holder.news_creator.setText(headlines.get(position).getCreator().toString());            }
            else{
                holder.news_creator.setText("Unknown Author");
            }


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnNewsClicked(headlines.get(holder.getAdapterPosition()));
                }
            });
        }
        else {
            headlines.remove(position);
            // TODO notify recycler update, size - 1
            onBindViewHolder(holder,position);

        }






    }

    @Override
    public int getItemCount() {
        return headlines.size();
    }
}
