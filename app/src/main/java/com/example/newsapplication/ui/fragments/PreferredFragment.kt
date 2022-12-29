package com.example.newsapplication.ui.fragments
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.*
import com.example.newsapplication.Models.ApiResponse
import com.example.newsapplication.Models.Headlines
import com.example.newsapplication.ui.DetailsActivity

class PreferredFragment:Fragment(R.layout.fragment_preferred), SelectListener, View.OnClickListener{
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        buttonSetup(view)

    }

    fun buttonSetup(view: View){
        val b1 = view?.findViewById<Button>(R.id.btn_1)
        b1?.setOnClickListener(this)
        val b2 = view?.findViewById<Button>(R.id.btn_2)
        b2?.setOnClickListener(this)
        val b3 = view?.findViewById<Button>(R.id.btn_3)
        b3?.setOnClickListener(this)
        val b4 = view?.findViewById<Button>(R.id.btn_4)
        b4?.setOnClickListener(this)
        val b5 = view?.findViewById<Button>(R.id.btn_5)
        b5?.setOnClickListener(this)
        val b6 = view?.findViewById<Button>(R.id.btn_6)
        b6?.setOnClickListener(this)
        val b7 = view?.findViewById<Button>(R.id.btn_7)
        b7?.setOnClickListener(this)
        val b8 = view?.findViewById<Button>(R.id.btn_8)
        b8?.setOnClickListener(this)
        val b9 = view?.findViewById<Button>(R.id.btn_9)
        b9?.setOnClickListener(this)
        val b10 = view?.findViewById<Button>(R.id.btn_10)
        b10?.setOnClickListener(this)
        val b11 = view?.findViewById<Button>(R.id.btn_11)
        b11?.setOnClickListener(this)
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
        val myIntent = Intent(context, DetailsActivity::class.java)
        myIntent.putExtra("data", headlines)
        context?.startActivity(myIntent)

    }

    override fun onClick(p0: View?) {
        progressBar = view?.findViewById<ProgressBar>(R.id.idPBLoading)
        progressBar?.visibility = View.VISIBLE
        val b = p0 as Button
        val buttonText = b.text.toString()
        val manager = RequestManager(activity)
        manager.getNewsHeadlines(listener, buttonText, null)
    }


}