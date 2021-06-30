package com.ayush.hungreed.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ayush.hungreed.R

class ProfileFragment : Fragment() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var name:TextView
    lateinit var number:TextView
    lateinit var email:TextView
    lateinit var address:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPreferences=
            this.activity?.getSharedPreferences(getString(R.string.preference_file_name),Context.MODE_PRIVATE)!!

        name=view.findViewById(R.id.profile_name)
        number=view.findViewById(R.id.profile_number)
        email=view.findViewById(R.id.profile_email)
        address=view.findViewById(R.id.profile_address)

        name.text=sharedPreferences.getString("name","Your Name")
        number.text=sharedPreferences.getString("mobile_number","Your Number")
        email.text=sharedPreferences.getString("email","Email Address")
        address.text=sharedPreferences.getString("address","Address")

        return view
    }

}