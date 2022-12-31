package com.example.newsapplication.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.newsapplication.R
import com.example.newsapplication.ui.activities.MainActivity
import com.google.firebase.auth.FirebaseAuth


class LogoutFragment: Fragment(R.layout.fragment_logout) {
    private lateinit var auth: FirebaseAuth

    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if(currentUser != null){
            auth.signOut()
        }
        else {
            Toast.makeText(activity, "Already signed out", Toast.LENGTH_SHORT).show()
            val logoutText = view?.findViewById<TextView>(R.id.success_text)
            logoutText?.text = context?.getString(R.string.already_logout)
        }
        val homeButton = view?.findViewById<Button>(R.id.home_btn)
        homeButton?.setOnClickListener{
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

    }
}