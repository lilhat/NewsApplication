package com.example.newsapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
        this.headlines = removeInvalidData(headlines);
        this.listener = listener;
        size = headlines.size();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.headline_items, parent, false));

    }

    private List removeInvalidData(List<Headlines> headlines) {
        for (int position = 0; position < headlines.size(); position++) {
            if (headlines.get(position).getImage_url() == null || headlines.get(position).getImage_url().isEmpty()) {
                headlines.remove(position);
            }
            else if (headlines.get(position).getDescription() == null || headlines.get(position).getDescription().isEmpty()){
                if(headlines.get(position).getContent() == null || headlines.get(position).getContent().isEmpty()){
                    headlines.remove(position);
                }
            }
        }
        return headlines;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Picasso.get().load(headlines.get(position).getImage_url()).into(holder.news_img);
        holder.news_title.setText(headlines.get(position).getTitle());
        holder.news_source.setText(headlines.get(position).getSource_id());
        if(headlines.get(position).getDescription() != null && !headlines.get(position).getDescription().isEmpty()){
            holder.news_text.setText(headlines.get(position).getDescription());
        }
        else{
            holder.news_text.setText(headlines.get(position).getContent());
        }
        if(headlines.get(position).getCreator() != null){
            List creator = headlines.get(position).getCreator();
            holder.news_creator.setText(creator.get(0).toString());            }
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





    @Override
    public int getItemCount() {
        return headlines.size();
    }
}
