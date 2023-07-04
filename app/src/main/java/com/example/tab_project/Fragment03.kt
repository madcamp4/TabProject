package com.example.tab_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.tab_project.databinding.Fragment03Binding

class Fragment03 : Fragment() {

    private lateinit var binding: Fragment03Binding

    private lateinit var startDiaryContract: ActivityResultLauncher<Intent>

    private val REQUEST_CODE = 1

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

        //startDiary 클릭시 Diary activity로 이동
        binding.btnStartDiary.setOnClickListener {
            println(context)
            println(activity)
            val intent = Intent(context, DiaryActivity::class.java)
            startDiaryContract.launch(intent) //일기 추가하는 창으로 이동
        }

        startDiaryContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult() ) { result ->
            if (result.resultCode == Activity.RESULT_OK) { //activity가 제대로 종료되었으면 안에 있는 코드 실행

            }
        }

    }
}