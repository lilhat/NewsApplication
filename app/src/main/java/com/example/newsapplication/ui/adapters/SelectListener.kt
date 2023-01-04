package com.example.newsapplication.ui.adapters

import com.example.newsapplication.models.Headlines

interface SelectListener {
    fun OnNewsClicked(headlines: Headlines?)
}