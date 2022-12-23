package com.example.newsapplication
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.Models.ApiResponse
import com.example.newsapplication.Models.Headlines
import com.example.newsapplication.CustomAdapter
import com.example.newsapplication.OnFetchDataListener
import com.example.newsapplication.R
import com.example.newsapplication.RequestManager

class PreferredFragment:Fragment(R.layout.fragment_preferred),SelectListener{

    private var progressBar : ProgressBar? = null
    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val manager = RequestManager(activity)
        manager.getNewsHeadlines(listener, null, null)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private val listener = object : OnFetchDataListener<ApiResponse> {
        override fun onFetchData(list: MutableList<Headlines>?, message: String?) {
            showNews(list)
            progressBar = view?.findViewById<ProgressBar>(R.id.idPBLoading)
            progressBar?.visibility = View.INVISIBLE
        }

        override fun onError(message: String?) {
            TODO("Not yet implemented")
        }

    }

    private fun showNews(list: MutableList<Headlines>?){
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_main)
        val adapter = CustomAdapter(activity, list, this)
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(activity, 1)
            recyclerView.adapter = adapter
        }
    }

    override fun OnNewsClicked(headlines: Headlines?) {
        val intent = Intent(activity, DetailsActivity::class.java)
        startActivity(intent.putExtra("data", headlines))
    }
}