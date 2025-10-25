package com.laioffer.spotify.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.laioffer.spotify.R
import dagger.hilt.android.AndroidEntryPoint

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container:ViewGroup?,
        savedInstanceState: Bundle?
    ):View?{
        return inflater.inflate(R.layout.fragment_home,container,false);
    }
}