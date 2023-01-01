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
    private int size;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    RecyclerView mRecyclerView;

    public void setLoaded() {
        isLoading = false;
    }


    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;

    }


    public CustomAdapter(Context context, List<Headlines> headlines, SelectListener listener) {
        this.context = context;
        this.headlines = removeInvalidData(headlines);
        this.listener = listener;
        size = headlines.size();


    }

    public void addNews(List<Headlines> newHeadlines){
        this.headlines.addAll(newHeadlines);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.headline_items, parent, false);
            return new CustomViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }

    }



    @Override
    public int getItemViewType(int position) {
        return headlines.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {


    }

    private void populateItemRows(CustomViewHolder viewHolder, int position) {
        onBindViewHolder(viewHolder, position);

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
        if(holder instanceof CustomViewHolder){
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
        else if (holder instanceof LoadingViewHolder){
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }

    }





    @Override
    public int getItemCount() {
        return headlines == null ? 0 : headlines.size();
    }
}
