package com.example.tab_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tab_project.databinding.Fragment01Binding
import com.example.tab_project.databinding.Fragment02Binding

class Fragment02 : Fragment() {

    private lateinit var binding: Fragment02Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Fragment02Binding.inflate(inflater, container, false)
        return binding.root
    }

}