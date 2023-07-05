package com.example.tab_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.example.tab_project.DiaryAdapterSingleton.diaryAdapter
import com.example.tab_project.databinding.ActivitySinglediaryBinding

class SingleDiaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySinglediaryBinding

    private lateinit var editDiaryContract: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var position = 0

        binding = ActivitySinglediaryBinding.inflate(layoutInflater) //activity.xml을 참조할 수 있도록 만든 binding class
        setContentView(binding.root)

        position = intent.getIntExtra("position", 0)

        binding.btnReturn.setOnClickListener {
            finish()
        }

        editDiaryContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult() ) { result ->
            if (result.resultCode == Activity.RESULT_OK) { //activity가 제대로 종료되었으면 안에 있는 코드 실행
                val newDiaryData = DiaryData(
                    result.data?.getStringExtra("date"),
                    result.data?.getStringExtra("title"),
                    result.data?.getStringExtra("content"),
                    result.data?.getIntExtra("icon", 0))

                val position = result.data?.getIntExtra("position", 0)

                if (diaryAdapter != null) {
                    DiaryAdapterSingleton.diaryAdapter.DiaryList[position!!] = newDiaryData
                    DiaryAdapterSingleton.diaryAdapter.notifyItemChanged(position)
                }
            }
        }

        binding.btnEdit.setOnClickListener {
            Toast.makeText(this, "Editing $position", Toast.LENGTH_SHORT).show()

            val editDiaryIntent = Intent(this, EditDiaryActivity::class.java)
            editDiaryIntent.putExtra("position", position)

            Toast.makeText(this, "editing position $position", Toast.LENGTH_SHORT).show()
            editDiaryContract.launch(editDiaryIntent) //일기 추가하는 창으로 이동
        }
    }
}