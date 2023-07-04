package com.example.tab_project

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.example.tab_project.databinding.ActivitySinglediaryBinding

class SingleDiaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySinglediaryBinding

    val position = intent.getIntExtra("position", 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySinglediaryBinding.inflate(layoutInflater) //activity.xml을 참조할 수 있도록 만든 binding class
        setContentView(binding.root)

        binding.btnReturn.setOnClickListener {
            finish()
        }

        binding.btnEdit.setOnClickListener {
            Toast.makeText(this, "Editing $position", Toast.LENGTH_SHORT).show()
        }
    }
}