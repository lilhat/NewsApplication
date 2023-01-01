package com.example.newsapplication.ui.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.newsapplication.Models.ApiResponse;
import com.example.newsapplication.Models.Headlines;
import com.example.newsapplication.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public class RequestManager {
    Response<ApiResponse> apiResponse = null;
    List<Headlines> oldHeadlines;
    List<Headlines>  newHeadlines;
    Integer page = 0;
    Context context;
    String text;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://newsdata.io/api/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public void getNewsHeadlines(OnFetchDataListener<ApiResponse> listener, OnLoadMoreListener listener2, String category, String query)
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
                   else{
                       page += 1;
                       if(apiResponse == null){
                           apiResponse = response;
                           Log.d(TAG,"response.raw().request().url();"+response.raw().request().url());
                           listener.onFetchData(apiResponse.body().getResults(), apiResponse.message());
                       }
                       else{
                            newHeadlines = response.body().getResults();
                           Log.d(TAG,"response.raw().request().url();"+response.raw().request().url());
                           listener2.onFetchData(newHeadlines, response.message());
                       }

                   }

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
