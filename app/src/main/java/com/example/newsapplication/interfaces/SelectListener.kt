package com.example.newsapplication.interfaces

import com.example.newsapplication.models.Headlines

interface SelectListener {
    fun OnNewsClicked(headlines: Headlines?)
}