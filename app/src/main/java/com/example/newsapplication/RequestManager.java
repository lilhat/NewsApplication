package com.example.newsapplication;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.newsapplication.Models.ApiResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {
    Context context;
    String text;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://newsdata.io/api/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public void getNewsHeadlines(OnFetchDataListener<ApiResponse> listener, String category, String query, Integer page)
    {
        CallNewsApi callNewsApi = retrofit.create(CallNewsApi.class);
        Call<ApiResponse> call = callNewsApi.callHeadlines(context.getString(R.string.api_key2) , "gb", category, "en", query, page);

        try {
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                   if (!response.isSuccessful()){
                       Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                   }
                   Log.d(TAG,"response.raw().request().url();"+response.raw().request().url());
                   listener.onFetchData(response.body().getResults(), response.message());
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    listener.onError("Request Failed");
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getUrl(){
        return text;
    }
    
    public RequestManager(Context context) {
        this.context = context;
    }

    // TODO - Create another function to search everything
    public interface CallNewsApi {
        @GET("news")
        Call<ApiResponse> callHeadlines(
                @Query("apiKey") String api_key,
                @Query("country") String country,
                @Query("category") String category,
                @Query("language") String language,
                @Query("q") String query,
                @Query("page") Integer page

        );
    }
}
