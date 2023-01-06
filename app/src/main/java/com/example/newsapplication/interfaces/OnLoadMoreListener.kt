package com.example.newsapplication.interfaces

import com.example.newsapplication.models.Headlines

// Listener to retrieve additional articles from Api request
interface OnLoadMoreListener<ApiResponse> {
    fun onFetchData(list: MutableList<Headlines>?, message: String?)
    fun onError(message: String?)
}