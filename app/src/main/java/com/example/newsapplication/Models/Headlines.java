package com.example.newsapplication.Models;

import java.io.Serializable;
import java.util.List;

public class Headlines implements Serializable {
    String source_id = null;
    List creator;
    String title = "";
    String description = "";
    String link = "";
    String image_url = "";
    List country;
    List category;
    String pubDate = "";
    String content = "";


    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public List getCreator() {
        return creator;
    }

    public void setCreator(List creator) {
        this.creator = creator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public List getCountry() {
        return country;
    }

    public void setCountry(List country) {
        this.country = country;
    }

    public List getCategory() {
        return category;
    }

    public void setCategory(List category) {
        this.category = category;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
