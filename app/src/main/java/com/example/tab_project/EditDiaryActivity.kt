package com.example.tab_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.tooling.data.EmptyGroup.data
import com.example.tab_project.databinding.ActivityAdddiaryBinding
import com.example.tab_project.databinding.ActivityEditdiaryBinding

class EditDiaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditdiaryBinding

    var position = 0

    val diaryAdapter = DiaryAdapterSingleton.diaryAdapter // 글로벌 변수를 불러와서 사용

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditdiaryBinding.inflate(layoutInflater) //activity.xml을 참조할 수 있도록 만든 binding class
        setContentView(binding.root)

        position = intent.getIntExtra("position", 0)

        binding.btnCancelEntry.setOnClickListener {
            finish()
        }

        binding.btnSaveEntry.setOnClickListener {
            val date = binding.etDiaryDate.text.toString()
            val title = binding.etDiaryTitle.text.toString()
            val content = binding.etDiaryContent.text.toString()

            val intent = Intent()

//            intent.apply {
//                putExtra("date", date)
//                putExtra("title", title)
//                putExtra("content", content)
//                putExtra("icon", 1)
//            }
//            setResult(Activity.RESULT_OK, intent)

            val new_diaryData = DiaryData(data, title, content, icon = null)
            diaryAdapter.DiaryList[position] = new_diaryData
            diaryAdapter.notifyItemChanged(position)

            finish()
        }

    }
}