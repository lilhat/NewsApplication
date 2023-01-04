package com.example.newsapplication.ui.fragments
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.models.ApiResponse
import com.example.newsapplication.models.Headlines
import com.example.newsapplication.R
import com.example.newsapplication.ui.activities.DetailsActivity
import com.example.newsapplication.ui.activities.FavouriteDetailsActivity
import com.example.newsapplication.ui.adapters.*

class RecommendedFragment:Fragment(R.layout.fragment_recommended),
    SelectListener {
    private lateinit var manager: RequestManager
    private lateinit var adapter: CustomAdapter
    private var progressBar : ProgressBar? = null
    private var isScrolling = false
    private lateinit var favouritesDataHelper: FavouritesDataHelper


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        manager = activity?.let { RequestManager(it) }!!
        manager.getNewsHeadlines(listener, listener2, null, null)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private val listener = object :
        OnFetchDataListener<ApiResponse> {
        override fun onFetchData(list: MutableList<Headlines>?, message: String?) {
            showNews(list)
            progressBar = view?.findViewById(R.id.idPBLoading)
            progressBar?.visibility = View.INVISIBLE
        }

        override fun onError(message: String?) {
            Toast.makeText(activity, "Error cannot load", Toast.LENGTH_SHORT).show()
        }

    }

    private val listener2 = object :
        OnLoadMoreListener<ApiResponse> {
        override fun onFetchData(list: MutableList<Headlines>?, message: String?) {
            addNews(list)
            progressBar = view?.findViewById(R.id.idPBLoading)
            progressBar?.visibility = View.INVISIBLE
        }

        override fun onError(message: String?) {
            Toast.makeText(activity, "Error cannot load", Toast.LENGTH_SHORT).show()
        }

    }

    private fun showNews(list: MutableList<Headlines>?){
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_main)
        adapter = activity?.let {
            CustomAdapter(
                it,
                list!!,
                this
            )
        }!!
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(activity, 1)
            recyclerView.adapter = adapter
            recyclerView.addOnScrollListener(this@RecommendedFragment.scrollListener)
        }

    }

    private fun addNews(list: MutableList<Headlines>?){
        adapter.addNews(list)
    }



    private val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (!recyclerView.canScrollVertically(1)) {
                progressBar?.visibility = View.VISIBLE
                manager.getNewsHeadlines(listener, listener2, null, null)
                isScrolling = false

            }

        }
    }


    override fun OnNewsClicked(headlines: Headlines?) {
        favouritesDataHelper = FavouritesDataHelper(activity)
        if(favouritesDataHelper.checkData(headlines?.title)){
            val myIntent = Intent(context, FavouriteDetailsActivity::class.java)
            myIntent.putExtra("data", headlines)
            myIntent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
            )
            context?.startActivity(myIntent)
        }
        else{
            val myIntent = Intent(context, DetailsActivity::class.java)
            myIntent.putExtra("data", headlines)
            context?.startActivity(myIntent)
        }

    }


}