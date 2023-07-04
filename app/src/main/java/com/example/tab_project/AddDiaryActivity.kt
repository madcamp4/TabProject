package com.example.tab_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tab_project.databinding.ActivityAdddiaryBinding

class AddDiaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdddiaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdddiaryBinding.inflate(layoutInflater) //activity.xml을 참조할 수 있도록 만든 binding class
        setContentView(binding.root)

        binding.btnCancelEntry.setOnClickListener {
            finish()
        }

        binding.btnSaveEntry.setOnClickListener {

            val date = binding.etDiaryDate.text.toString()
            val title = binding.etDiaryTitle.text.toString()
            val content = binding.etDiaryContent.text.toString()

            val intent = Intent()

            intent.apply {
                putExtra("date", date)
                putExtra("title", title)
                putExtra("content", content)
                putExtra("icon", 1)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()

        }

    }
}