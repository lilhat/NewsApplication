package com.example.newsapplication;

import com.example.newsapplication.Models.Headlines;

import java.util.List;

public interface OnFetchDataListener<ApiResponse> {
    void onFetchData(List<Headlines> list, String message);
    void onError(String message);
}
