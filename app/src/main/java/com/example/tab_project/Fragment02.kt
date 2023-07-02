package com.example.tab_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
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
        super.onViewCreated(view, savedInstanceState)

        val profileAdapter = context?.let { ProfileAdapter(it) }
        binding.rvProfile.adapter = profileAdapter
        // binding.rvProfile.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rvProfile.layoutManager = GridLayoutManager(activity, 2)

        profileAdapter?.datas?.apply {
            add(ProfileData(img = R.drawable.img_1_right))
            add(ProfileData(img = R.drawable.img_2_right))
            add(ProfileData(img = R.drawable.img_3_right))
            add(ProfileData(img = R.drawable.img_4_right))
            add(ProfileData(img = R.drawable.img_5_right))
            add(ProfileData(img = R.drawable.img_1_right))
            add(ProfileData(img = R.drawable.img_2_right))
            add(ProfileData(img = R.drawable.img_3_right))
            add(ProfileData(img = R.drawable.img_4_right))
            add(ProfileData(img = R.drawable.img_5_right))
            add(ProfileData(img = R.drawable.img_1_right))
            add(ProfileData(img = R.drawable.img_2_right))
            add(ProfileData(img = R.drawable.img_3_right))
            add(ProfileData(img = R.drawable.img_4_right))
            add(ProfileData(img = R.drawable.img_5_right))
            add(ProfileData(img = R.drawable.img_1_right))
        }
    }

}
