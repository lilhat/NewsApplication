package com.example.newsapplication.ui.adapters

import com.example.newsapplication.Models.Headlines

interface OnLoadMoreListener<ApiResponse> {
    fun onFetchData(list: MutableList<Headlines>?, message: String?)
    fun onError(message: String?)
}