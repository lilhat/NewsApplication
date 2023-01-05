package com.example.newsapplication.interfaces

import com.example.newsapplication.models.Headlines

interface FavouriteSelectListener {
    fun OnNewsClicked(headlines: Headlines)
}