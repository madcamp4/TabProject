package com.example.tab_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tab_project.databinding.ActivityAdddiaryBinding

class AddDiaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdddiaryBinding

    val diaryAdapter = DiaryAdapterSingleton.diaryAdapter // 글로벌 변수를 불러와서 사용

    val iconIds = intArrayOf(
        R.drawable.img_1_right,
        R.drawable.img_2_right,
        R.drawable.img_3_right,
        R.drawable.img_4_right,
        R.drawable.img_5_right,
    )

    var new_iconId : Int? = null

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
        val iconOptions = arrayOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5") // Replace with your actual options
        val dialog = AlertDialog.Builder(this)
            .setTitle("Select an icon")
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