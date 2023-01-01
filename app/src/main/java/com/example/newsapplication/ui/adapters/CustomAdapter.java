package com.example.newsapplication.ui.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapplication.Models.Headlines;
import com.example.newsapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private List<Headlines> headlines;
    private SelectListener listener;




    public CustomAdapter(Context context, List<Headlines> headlines, SelectListener listener) {
        this.context = context;
        this.headlines = removeInvalidData(headlines);
        this.listener = listener;


    }

    public void addNews(List<Headlines> newHeadlines){
        this.headlines.addAll(newHeadlines);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.headline_items, parent, false);
        return new CustomViewHolder(view);
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Picasso.get().load(headlines.get(position).getImage_url()).into(((CustomViewHolder) holder).news_img);
        ((CustomViewHolder) holder).news_title.setText(headlines.get(position).getTitle());
        ((CustomViewHolder) holder).news_source.setText(headlines.get(position).getSource_id());
        if(headlines.get(position).getDescription() != null && !headlines.get(position).getDescription().isEmpty()){
            ((CustomViewHolder) holder).news_text.setText(headlines.get(position).getDescription());
        }
        else{
            ((CustomViewHolder) holder).news_text.setText(headlines.get(position).getContent());
        }
        if(headlines.get(position).getCreator() != null){
            List creator = headlines.get(position).getCreator();
            ((CustomViewHolder) holder).news_creator.setText(creator.get(0).toString());            }
        else{
            ((CustomViewHolder) holder).news_creator.setText("Unknown Author");
        }

        ((CustomViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnNewsClicked(headlines.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return headlines == null ? 0 : headlines.size();
    }
}
