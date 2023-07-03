package com.example.tab_project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //view의 초기값, Adapter세팅
        super.onViewCreated(view, savedInstanceState)

        val profileAdapter = context?.let { ProfileAdapter(it) }
        binding.rvProfile.adapter = profileAdapter
        // binding.rvProfile.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rvProfile.layoutManager = GridLayoutManager(activity, 3)

        profileAdapter?.datas?.apply {
            add(ProfileData(img = R.drawable.img_1_right))
            add(ProfileData(img = R.drawable.img_2_right))
            add(ProfileData(img = R.drawable.img_3_right))
            add(ProfileData(img = R.drawable.img_4_right))
            add(ProfileData(img = R.drawable.img_5_right))
        }

        val imageIds = intArrayOf(
            R.drawable.img_1_right,
            R.drawable.img_2_right,
            R.drawable.img_3_right,
            R.drawable.img_4_right,
            R.drawable.img_5_right,
        )

        val imageAdapter = ImageAdapter(requireContext(),imageIds)
        binding.myGridView.adapter = imageAdapter


        binding.myGridView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(requireContext(), MainActivity2::class.java)
            intent.putExtra("imageId", imageIds[position])
            startActivity(intent)
        }
    }
}
