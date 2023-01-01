package com.example.newsapplication.Models;

import java.io.Serializable;
import java.util.List;

public class ApiResponse implements Serializable {
    String status;
    int totalResults;
    List<Headlines> results;
    int nextPage;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Headlines> getResults() {
        return results;
    }

    public void setResults(List<Headlines> results) {
        this.results = results;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }
}
