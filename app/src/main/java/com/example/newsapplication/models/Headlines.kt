package com.example.newsapplication.models

import java.io.Serializable

// Model for individual articles
open class Headlines : Serializable {
    var source_id: String? = null
    var creator: List<*>? = null
    var title = ""
    var description: String? = null
    var link = ""
    var image_url: String? = null
    var country: List<*>? = null
    var category: List<*>? = null
    var pubDate = ""
    var content: String? = null
}