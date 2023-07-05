package com.example.tab_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tab_project.databinding.ActivityAdddiaryBinding
import com.example.tab_project.databinding.ActivityDiaryBinding
import java.time.LocalDate

class DiaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiaryBinding
    private lateinit var diaryAdapter: DiaryAdapter

    private lateinit var addDiaryContract: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDiaryBinding.inflate(layoutInflater) //activity.xml을 참조할 수 있도록 만든 binding class
        setContentView(binding.root)

        var diaryAdapter = DiaryAdapter(this)
        binding.rvDiary.adapter = diaryAdapter
        binding.rvDiary.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


        //initializing dummy data
        if (diaryAdapter != null) {
            diaryAdapter.DiaryList = generateDiaryDataList()
        }

        // AddDiary 클릭시 새 일기 작성 activity로 넘어감
        binding.btnAddDiary.setOnClickListener {
            val intent = Intent(this, AddDiaryActivity::class.java)
            addDiaryContract.launch(intent) //일기 추가하는 창으로 이동
        }

        // 클릭시 일기 메인 페이지로 이동
        if (diaryAdapter != null) {
            diaryAdapter.setItemClickListener(object: DiaryAdapter.OnItemClickListener {
                override fun onClick(v: View, position: Int) {
                    openDiary(position)
                }
            })
        }

        val itemTouchHelper = ItemTouchHelper(object: SwipeHelper(binding.rvDiary) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                val deleteButton = deleteButton(position)
                val editButton = editButton(position)
                val heartButton = heartButton(position)

                var buttons = listOf<UnderlayButton>(deleteButton, editButton, heartButton)

                return buttons
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rvDiary)


        // adddiary activity가 끝난 이후에 해당 페이지에서 얻은 데이터를 통해 데이터셋에 넣음
        addDiaryContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult() ) { result ->
            if (result.resultCode == Activity.RESULT_OK) { //activity가 제대로 종료되었으면 안에 있는 코드 실행
                val newDiaryData = DiaryData(
                    result.data?.getStringExtra("date"),
                    result.data?.getStringExtra("title"),
                    result.data?.getStringExtra("content"),
                    result.data?.getIntExtra("icon", 0))

                if (diaryAdapter != null) {
                    diaryAdapter.DiaryList.add(newDiaryData)
                    diaryAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun openDiary(position: Int) {
        val openDiaryIntent = Intent(this, SingleDiaryActivity::class.java)
        openDiaryIntent.putExtra("position", position)

        Toast.makeText(this, "opening position $position", Toast.LENGTH_SHORT).show()
        addDiaryContract.launch(openDiaryIntent) //일기 추가하는 창으로 이동
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun heartButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            this,
            "Heart",
            14.0f,
            android.R.color.holo_green_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    toast("Marked as hearted item $position")
                    diaryAdapter.DiaryList[position].isFavorite = true
                }
            })
    }

    private fun editButton(position: Int) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            this,
            "Edit",
            14.0f,
            android.R.color.holo_blue_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    toast("Marked as edited item $position")
                }
            })
    }

    private fun deleteButton(position: Int) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            this,
            "Delete",
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    toast("Deleted item $position")
                }
            })
    }

    fun generateDiaryDataList(): MutableList<DiaryData> {
        val diaryDataList = mutableListOf<DiaryData>()

        val titles = listOf(
            "Title 1",
            "Title 2",
            "Title 3",
            "Title 4",
            "Title 5",
            "Title 6",
            "Title 7",
            "Title 8"
        )

        val contents = listOf(
            "Content line 1\nContent line 2",
            "Content line 1\nContent line 2\nContent line 3",
            "Content line 1\nContent line 2\nContent line 3\nContent line 4"
        )

        for (i in 1..8) {
            val date = "00/00/00"
            val title = titles[i - 1]
            val content = contents[i % contents.size]
            val diaryData = DiaryData(date, title, content, R.drawable.img_1_right)
            diaryDataList.add(diaryData)
        }

        return diaryDataList
    }
}

