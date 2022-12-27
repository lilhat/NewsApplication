package com.example.newsapplication.ui.fragments
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.*
import com.example.newsapplication.Models.ApiResponse
import com.example.newsapplication.Models.Headlines
import com.example.newsapplication.ui.DetailsActivity

class RecommendedFragment:Fragment(R.layout.fragment_recommended), SelectListener {

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
            Toast.makeText(activity, "Error cannot load", Toast.LENGTH_SHORT).show()
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
        recyclerView!!.post { adapter.notifyDataSetChanged() }
        if (!recyclerView.isComputingLayout && recyclerView.scrollState == SCROLL_STATE_IDLE) {
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }



    override fun OnNewsClicked(headlines: Headlines?) {
        val intent = Intent(activity, DetailsActivity::class.java)
        startActivity(intent.putExtra("data", headlines))
    }


}