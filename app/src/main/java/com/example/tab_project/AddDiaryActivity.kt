package com.example.tab_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tab_project.databinding.ActivityAdddiaryBinding

class AddDiaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdddiaryBinding

    val diaryAdapter = DiaryAdapterSingleton.diaryAdapter // 글로벌 변수를 불러와서 사용

    val iconIds = intArrayOf(
        R.drawable.w_sunny,
        R.drawable.w_sunnycloudy,
        R.drawable.w_rainy,
        R.drawable.w_snowy,
        R.drawable.w_rainbow,
    )

    var new_iconId : Int? = R.drawable.w_sunny

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        val backgroundColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.setBackgroundColor(backgroundColor)

        binding = ActivityAdddiaryBinding.inflate(layoutInflater) //activity.xml을 참조할 수 있도록 만든 binding class
        setContentView(binding.root)

        binding.imageView.setImageResource( R.drawable.w_sunny)

        binding.btnCancelEntry.setOnClickListener {
            finish()
        }

        binding.btnSaveEntry.setOnClickListener {
            val date = binding.etDiaryDate.text.toString()
            val title = binding.etDiaryTitle.text.toString()
            val content = binding.etDiaryContent.text.toString()

            val newDiaryData = DiaryData(date, title, content, new_iconId) //디폴트 아이콘 하나 정하기

            diaryAdapter.DiaryList.add(newDiaryData)
            diaryAdapter.notifyDataSetChanged()

            //SaveDiaryAdapter.saveDiary()

            finish()
        }

        binding.imageView.setOnClickListener {
            showIconSelectionDialog()
        }

    }

    private fun showIconSelectionDialog() {
        val iconOptions = arrayOf("☀️","🌤️","🌧️","🌨️","🌈")
        val dialog = AlertDialog.Builder(this)
            .setTitle("오늘의 날씨")
            .setItems(iconOptions) { _, which ->
                handleIconSelection(which)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }

    private fun handleIconSelection(selectedOption: Int) {
        Toast.makeText(this, "Selected option $selectedOption", Toast.LENGTH_SHORT)

        new_iconId = iconIds[selectedOption]
        binding.imageView.setImageResource(new_iconId!!)
    }
}