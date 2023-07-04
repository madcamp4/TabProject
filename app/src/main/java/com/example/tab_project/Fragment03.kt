package com.example.tab_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tab_project.databinding.Fragment01Binding
import com.example.tab_project.databinding.Fragment03Binding

class Fragment03 : Fragment() {

    private lateinit var binding: Fragment03Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Fragment03Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //view의 초기값, Adapter세팅
        super.onViewCreated(view, savedInstanceState)

        var diaryAdapter = context?.let { DiaryAdapter(it) }
        binding.rvDiary.adapter = diaryAdapter
        binding.rvDiary.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        if (diaryAdapter != null) {
            diaryAdapter.DiaryList = mutableListOf<DiaryData>((DiaryData("7/4", "제목", "ㅗㅠㅇ류어ㅗㄹ", R.drawable.img_1_right) ))
            println("ddf")
        }

    }
}