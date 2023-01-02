package com.example.newsapplication.ui.fragments
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.*
import com.example.newsapplication.Models.ApiResponse
import com.example.newsapplication.Models.Headlines
import com.example.newsapplication.ui.activities.DetailsActivity
import com.example.newsapplication.ui.activities.FavouriteDetailsActivity
import com.example.newsapplication.ui.adapters.*


class PreferredFragment:Fragment(R.layout.fragment_preferred),
    SelectListener, View.OnClickListener{
    private var progressBar : ProgressBar? = null
    private lateinit var manager: RequestManager
    private var i = 1
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sharedPreferences: SharedPreferences
    private val SHARED_PREF_NAME = "MyPref"
    private val KEY_BUSBOX = "Bus_Box"
    private val KEY_ENTBOX = "Ent_Box"
    private val KEY_ENVBOX = "Env_Box"
    private val KEY_FOOBOX = "Foo_Box"
    private val KEY_HEABOX = "Hea_Box"
    private val KEY_WORBOX = "Wor_Box"
    private val KEY_TOPBOX = "Top_Box"
    private val KEY_POLBOX = "Pol_Box"
    private val KEY_SPOBOX = "Spo_Box"
    private val KEY_SCIBOX = "Sci_Box"
    private val KEY_TECBOX = "Tec_Box"
    private lateinit var adapter: CustomAdapter
    private lateinit var recyclerView: RecyclerView
    private var isScrolling = false
    private lateinit var favouritesDataHelper: FavouritesDataHelper

    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        manager = activity?.let { RequestManager(it) }!!
        manager.getNewsHeadlines(listener, listener2, null, null)
        sharedPreferences = context?.getSharedPreferences(SHARED_PREF_NAME,
            AppCompatActivity.MODE_PRIVATE)!!
        editor = sharedPreferences.edit()
        buttonSetup(view)
    }



    private fun buttonSetup(view: View){
        val busButton = view?.findViewById<Button>(R.id.bus_btn)
        if(!isChecked(KEY_BUSBOX)){
            busButton?.visibility = View.GONE
        }
        busButton?.setOnClickListener(this)
        val entButton = view?.findViewById<Button>(R.id.ent_btn)
        if(!isChecked(KEY_ENTBOX)){
            entButton?.visibility = View.GONE
        }
        entButton?.setOnClickListener(this)
        val envButton = view?.findViewById<Button>(R.id.env_btn)
        if(!isChecked(KEY_ENVBOX)){
            envButton?.visibility = View.GONE
        }
        envButton?.setOnClickListener(this)
        val fooButton = view?.findViewById<Button>(R.id.foo_btn)
        if(!isChecked(KEY_FOOBOX)){
            fooButton?.visibility = View.GONE
        }
        fooButton?.setOnClickListener(this)
        val heaButton = view?.findViewById<Button>(R.id.hea_btn)
        if(!isChecked(KEY_HEABOX)){
            heaButton?.visibility = View.GONE
        }
        heaButton?.setOnClickListener(this)
        val polButton = view?.findViewById<Button>(R.id.pol_btn)
        if(!isChecked(KEY_POLBOX)){
            polButton?.visibility = View.GONE
        }
        polButton?.setOnClickListener(this)
        val sciButton = view?.findViewById<Button>(R.id.sci_btn)
        if(!isChecked(KEY_SCIBOX)){
            sciButton?.visibility = View.GONE
        }
        sciButton?.setOnClickListener(this)
        val spoButton = view?.findViewById<Button>(R.id.spo_btn)
        if(!isChecked(KEY_SPOBOX)){
            spoButton?.visibility = View.GONE
        }
        spoButton?.setOnClickListener(this)
        val tecButton = view?.findViewById<Button>(R.id.tec_btn)
        if(!isChecked(KEY_TECBOX)){
            tecButton?.visibility = View.GONE
        }
        tecButton?.setOnClickListener(this)
        val topButton = view?.findViewById<Button>(R.id.top_btn)
        if(!isChecked(KEY_TOPBOX)){
            topButton?.visibility = View.GONE
        }
        topButton?.setOnClickListener(this)
        val worButton = view?.findViewById<Button>(R.id.wor_btn)
        if(!isChecked(KEY_WORBOX)){
            worButton?.visibility = View.GONE
        }
        worButton?.setOnClickListener(this)
    }

    private fun isChecked(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
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
        recyclerView = view?.findViewById(R.id.recycler_main)!!
        adapter = activity?.let {
            CustomAdapter(
                it,
                list!!,
                this
            )
        }!!
        if (recyclerView != null) {
            recyclerView!!.setHasFixedSize(true)
            recyclerView!!.layoutManager = GridLayoutManager(activity, 1)
            recyclerView!!.adapter = adapter
            recyclerView.addOnScrollListener(this@PreferredFragment.scrollListener)
        }
    }

    private fun addNews(list: MutableList<Headlines>?){
        adapter.addNews(list)
    }

    override fun OnNewsClicked(headlines: Headlines?) {
        favouritesDataHelper = FavouritesDataHelper(activity)
        if(favouritesDataHelper.checkData(headlines?.title)){
            val myIntent = Intent(context, FavouriteDetailsActivity::class.java)
            myIntent.putExtra("data", headlines)
            context?.startActivity(myIntent)
        }
        else{
            val myIntent = Intent(context, DetailsActivity::class.java)
            myIntent.putExtra("data", headlines)
            context?.startActivity(myIntent)
        }
    }

    override fun onClick(p0: View?) {
        progressBar = view?.findViewById(R.id.idPBLoading)
        progressBar?.visibility = View.VISIBLE
        val b = p0 as Button
        val buttonText = b.text.toString()
        activity?.let { RequestManager(it) }
            ?.getNewsHeadlines(listener, listener2, buttonText, null)
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

                manager.getNewsHeadlines(listener, listener2, null, null)
                isScrolling = false

            }

        }
    }


}