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
import com.example.newsapplication.Models.ApiResponse
import com.example.newsapplication.Models.Headlines
import com.example.newsapplication.R
import com.example.newsapplication.ui.activities.DetailsActivity
import com.example.newsapplication.ui.activities.MainActivity
import com.example.newsapplication.ui.adapters.CustomAdapter
import com.example.newsapplication.ui.adapters.OnFetchDataListener
import com.example.newsapplication.ui.adapters.RequestManager
import com.example.newsapplication.ui.adapters.SelectListener

class SearchFragment:Fragment(R.layout.fragment_search),
    SelectListener {

    private var progressBar : ProgressBar? = null


    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        container?.removeAllViews()
        val bundle = this.arguments
        val query = bundle!!.getString("message")
        val manager =
            RequestManager(activity)
        manager.getNewsHeadlines(listener, null, query, null)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private val listener = object :
        OnFetchDataListener<ApiResponse> {
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
        val adapter = CustomAdapter(
            activity,
            list,
            this
        )
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(activity, 1)
            recyclerView.adapter = adapter
        }
        recyclerView?.adapter?.notifyDataSetChanged()
        // TODO - Return back to settings fragment from details activity
    }



    override fun OnNewsClicked(headlines: Headlines?) {
        val myIntent = Intent(context, DetailsActivity::class.java)
        myIntent.putExtra("data", headlines)
        context?.startActivity(myIntent)
    }


}