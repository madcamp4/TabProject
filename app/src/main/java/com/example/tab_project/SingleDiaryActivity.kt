package com.example.tab_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.bumptech.glide.Glide
import com.example.tab_project.DiaryAdapterSingleton.diaryAdapter
import com.example.tab_project.databinding.ActivitySinglediaryBinding

class SingleDiaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySinglediaryBinding

    val diaryAdapter = DiaryAdapterSingleton.diaryAdapter // 글로벌 변수를 불러와서 사용

    private lateinit var editDiaryActivityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var position = 0

        binding = ActivitySinglediaryBinding.inflate(layoutInflater) //activity.xml을 참조할 수 있도록 만든 binding class
        setContentView(binding.root)

        position = intent.getIntExtra("position", 0)
        val diaryData = diaryAdapter.DiaryList[position]

        // 모든 view들이 일기의 정보를 보이도록 설정
        binding.apply {
            tvDiaryDate.text = diaryData.date
            tvDiaryTitle.text = diaryData.title
            tvDiaryContent.text = diaryData.content
            Glide.with(this@SingleDiaryActivity).load(diaryData.icon).into(imageView)
        }

        binding.btnReturn.setOnClickListener {
            finish()
        }

        binding.btnEdit.setOnClickListener {
            val editDiaryIntent = Intent(this, EditDiaryActivity::class.java)
            editDiaryIntent.putExtra("position", position)

            Toast.makeText(this, "editing position $position", Toast.LENGTH_SHORT).show()

            editDiaryActivityResultLauncher.launch(editDiaryIntent)
        }

        // editDiaryActivity에서 돌아온 후 아이콘 업데이트하기
        editDiaryActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                // Perform the desired function using the data from EditDiaryActivity
                val editedIcon = data?.getIntExtra("editedIcon", 0)

                Glide.with(this@SingleDiaryActivity).load(editedIcon).into(binding.imageView)
            }
        }
    }
}