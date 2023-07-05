package com.example.tab_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tab_project.databinding.ActivityAdddiaryBinding
import com.example.tab_project.databinding.ActivityEditdiaryBinding

class EditDiaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditdiaryBinding

    var position = 0

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

        binding = ActivityEditdiaryBinding.inflate(layoutInflater) //activity.xml을 참조할 수 있도록 만든 binding class
        setContentView(binding.root)

        position = intent.getIntExtra("position", 0)

        val diaryData = diaryAdapter.DiaryList[position]

        new_iconId = diaryData.icon // 만일 아이콘이 수정되지 않으면 그대로

        // 모든 view들이 일기의 정보를 보이도록 설정
        binding.apply {
            etDiaryDate.setText(diaryData.date)
            etDiaryTitle.setText(diaryData.title)
            etDiaryContent.setText(diaryData.content)
            Glide.with(this@EditDiaryActivity).load(diaryData.icon).into(imageView)
        }

        binding.btnCancelEntry.setOnClickListener {
            finish()
        }

        binding.btnSaveEntry.setOnClickListener {
            val date = binding.etDiaryDate.text.toString()
            val title = binding.etDiaryTitle.text.toString()
            val content = binding.etDiaryContent.text.toString()

            val new_diaryData = DiaryData(date, title, content, new_iconId)
            diaryAdapter.DiaryList[position] = new_diaryData
            diaryAdapter.notifyItemChanged(position)

            val data = Intent()
            data.putExtra("editedIcon", new_iconId)
            setResult(RESULT_OK, data)

            finish()
        }

        binding.imageView.setOnClickListener {
            showIconSelectionDialog()
        }
    }

    private fun showIconSelectionDialog() {
        val iconOptions = arrayOf("Option 1", "Option 2", "Option 3", "Option 3", "Option 3") // Replace with your actual options
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