package com.example.newsapplication.models

import java.io.Serializable

class ApiResponse : Serializable {
    var status: String? = null
    var totalResults = 0
    var results: List<Headlines>? = null
    var nextPage = 0
}