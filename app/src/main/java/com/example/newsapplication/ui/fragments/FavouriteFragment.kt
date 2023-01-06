package com.example.newsapplication.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplication.models.Headlines
import com.example.newsapplication.adapters.FavouritesAdapter
import com.example.newsapplication.utils.FavouritesDataHelper
import com.example.newsapplication.R
import com.example.newsapplication.ui.activities.FavouriteDetailsActivity
import com.example.newsapplication.interfaces.SelectListener
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

// Fragment for favourites section
class FavouriteFragment:Fragment(R.layout.fragment_favourite), SelectListener {

    private lateinit var favouritesDataHelper: FavouritesDataHelper
    private lateinit var noText: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private val loggedText: String = "Must be logged in"

    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    // Check whether user is logged in then setup recycler view
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        noText = view.findViewById(R.id.no_favourites)
        noText.visibility = View.INVISIBLE
        if(checkLogin()){
            setupRecyclerView()
        }
        val progressBar = view.findViewById<ProgressBar>(R.id.idPBLoading)
        progressBar.visibility = View.INVISIBLE
        noText = view?.findViewById(R.id.no_favourites)!!
        return
    }

    // Function to check whether user is logged in
    private fun checkLogin(): Boolean {
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(requireContext(), gso)
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())

        if(currentUser == null) {
            if(account == null){
                noText.text = loggedText
                noText.visibility = View.VISIBLE
                return false
            }
            else{
                noText.visibility = View.INVISIBLE
            }
        }
        else{
            noText.visibility = View.INVISIBLE
        }
        return true
    }

    // Function to setup recycler view with favourites adapter
    private fun setupRecyclerView(){
        favouritesDataHelper =
            FavouritesDataHelper(activity)
        val cursor = favouritesDataHelper.favouriteData
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_main)
        val adapter = activity?.let {
            FavouritesAdapter(
                it,
                cursor,
                this
            )
        }
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = GridLayoutManager(activity, 1)
            recyclerView.adapter = adapter
        }
        noText = view?.findViewById(R.id.no_favourites)!!
        if(cursor.count == 0){
            noText.visibility = View.VISIBLE
        }

    }

    // Function to launch favourites detail activity when article is clicked
    override fun OnNewsClicked(headlines: Headlines?) {
        val myIntent = Intent(context, FavouriteDetailsActivity::class.java)
        myIntent.putExtra("data", headlines)
        context?.startActivity(myIntent)
    }

}