package com.example.newsapplication.interfaces

import com.example.newsapplication.models.Headlines

// Listener to retrieve data from Api request
interface OnFetchDataListener<ApiResponse> {
    fun onFetchData(list: MutableList<Headlines>?, message: String?)
    fun onError(message: String?)
}