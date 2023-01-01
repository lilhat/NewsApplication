package com.example.newsapplication.Models

import java.io.Serializable

open class Headlines : Serializable {
    var source_id: String? = null
    var creator: List<*>? = null
    var title = ""
    var description = ""
    var link = ""
    var image_url = ""
    var country: List<*>? = null
    var category: List<*>? = null
    var pubDate = ""
    var content = ""
}