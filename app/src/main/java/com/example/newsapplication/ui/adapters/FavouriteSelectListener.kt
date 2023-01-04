package com.example.newsapplication.ui.adapters

import com.example.newsapplication.models.Headlines

interface FavouriteSelectListener {
    fun OnNewsClicked(headlines: Headlines)
}