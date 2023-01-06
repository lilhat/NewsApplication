package com.example.newsapplication.interfaces

import com.example.newsapplication.models.Headlines

// Interface to listen for selected articles
interface SelectListener {
    fun OnNewsClicked(headlines: Headlines?)
}