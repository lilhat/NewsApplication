package com.example.newsapplication.ui.adapters

import com.example.newsapplication.Models.Headlines

interface SelectListener {
    fun OnNewsClicked(headlines: Headlines?)
}