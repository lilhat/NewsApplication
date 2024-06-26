package com.example.newsapplication.utils

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.newsapplication.models.ApiResponse
import com.example.newsapplication.models.Headlines
import com.example.newsapplication.R
import com.example.newsapplication.interfaces.OnFetchDataListener
import com.example.newsapplication.interfaces.OnLoadMoreListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Request manager that uses retrofit to send requests to the news api
class RequestManager(var context: Context) {
    var apiResponse: Response<ApiResponse>? = null
    var newHeadlines: MutableList<Headlines>? = null
    var page = 0
    private var retrofit = Retrofit.Builder()
        .baseUrl("https://newsdata.io/api/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Creating the retrofit object and enqueueing calls
    fun getNewsHeadlines(
        listener: OnFetchDataListener<ApiResponse>,
        listener2: OnLoadMoreListener<*>?,
        category: String?,
        query: String?
    ) {
        val callNewsApi = retrofit.create(
            CallNewsApi::class.java
        )
        // Maximum 200 calls a day, if exceeded increment the below api_key variable by 1 (e.g. api_key2)
        val call = callNewsApi.callHeadlines(
            context.getString(R.string.api_key4),
            "gb",
            category,
            "en",
            query,
            page
        )
        try {
            call.enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    if (!response.isSuccessful) {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    } else {
                        page += 1
                        // If the call is being made for the first time, the page should be 0
                        // so use first listener
                        if (apiResponse == null) {
                            apiResponse = response
                            Log.d(
                                ContentValues.TAG,
                                "response.raw().request().url();" + response.raw().request().url()
                            )
                            listener.onFetchData(
                                apiResponse!!.body()?.results as MutableList<Headlines>?,
                                apiResponse!!.message()
                            )
                        }
                        // If the call is being made for the second time, page is incremented
                        // and second listener is used so that the previous response can be combined
                        else {
                            newHeadlines = response.body()!!.results as MutableList<Headlines>?
                            Log.d(
                                ContentValues.TAG,
                                "response.raw().request().url();" + response.raw().request().url()
                            )
                            listener2?.onFetchData(newHeadlines, response.message())
                        }
                    }
                }

                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    listener.onError("Request Failed")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Interface defining the parameters of the call
    interface CallNewsApi {
        @GET("news")
        fun callHeadlines(
            @Query("apiKey") api_key: String?,
            @Query("country") country: String?,
            @Query("category") category: String?,
            @Query("language") language: String?,
            @Query("q") query: String?,
            @Query("page") page: Int?
        ): Call<ApiResponse>
    }
}