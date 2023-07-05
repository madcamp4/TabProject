package com.example.tab_project

import android.app.Activity
import android.content.ClipData.Item
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tab_project.databinding.ActivityAdddiaryBinding
import com.example.tab_project.databinding.ActivityDiaryBinding
import java.time.LocalDate


class DiaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiaryBinding
    lateinit var diaryAdapter: DiaryAdapter
    lateinit var swipeHelper: ItemTouchHelper

    //private lateinit var addDiaryContract: ActivityResultLauncher<Intent>
    //private lateinit var editDiaryContract: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        val backgroundColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.setBackgroundColor(backgroundColor)
        binding = ActivityDiaryBinding.inflate(layoutInflater) //activity.xml을 참조할 수 있도록 만든 binding class
        setContentView(binding.root)

        diaryAdapter = DiaryAdapter(this)
        DiaryAdapterSingleton.diaryAdapter = diaryAdapter // set global singleton object

        binding.rvDiary.adapter = diaryAdapter
        binding.rvDiary.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        SaveDiaryAdapter.init(applicationContext)

        //initializing dummy data
        if (diaryAdapter != null) {
            diaryAdapter.DiaryList = generateDiaryDataList()
        }

        // AddDiary 클릭시 새 일기 작성 activity로 넘어감
        binding.btnAddDiary.setOnClickListener {
            val intent = Intent(this, AddDiaryActivity::class.java)
            //addDiaryContract.launch(intent) //일기 추가하는 창으로 이동

            startActivity(intent)
        }

        binding.switchFavorites.setOnCheckedChangeListener { _ , isChecked ->
            diaryAdapter.isdisplayFavorites = isChecked
            diaryAdapter.notifyDataSetChanged()
        }

        // 클릭시 일기 메인 페이지로 이동
        if (diaryAdapter != null) {
            diaryAdapter.setItemClickListener(object: DiaryAdapter.OnItemClickListener {
                override fun onClick(v: View, position: Int) {
                    openDiary(position)
                }
            })
        }

        swipeHelper = ItemTouchHelper(object: SwipeHelper(binding.rvDiary) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                val deleteButton = deleteButton(position)
                val editButton = editButton(position)
                val heartButton = heartButton(position)

                var buttons = listOf<UnderlayButton>(deleteButton, editButton, heartButton)

                return buttons
            }
        })
        swipeHelper.attachToRecyclerView(binding.rvDiary)

    }

    override fun onResume() {
        super.onResume()
        SaveDiaryAdapter.loadDiary()
    }

    private fun openDiary(position: Int) {
        val openDiaryIntent = Intent(this, SingleDiaryActivity::class.java)
        openDiaryIntent.putExtra("position", position)

        println(position)
        toast("opening position $position")

        startActivity(openDiaryIntent)
        //addDiaryContract.launch(openDiaryIntent) //일기 하나들 보는 창으로 이동
    }

    private fun editDiary(position: Int) {
        val editDiaryIntent = Intent(this, EditDiaryActivity::class.java)
        editDiaryIntent.putExtra("position", position)

        Toast.makeText(this, "editing position $position", Toast.LENGTH_SHORT).show()

        startActivity(editDiaryIntent)
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
                    toast("Marked $position th item as favorite")

                    diaryAdapter.DiaryList[position].isFavorite = diaryAdapter.DiaryList[position].isFavorite == false
                    diaryAdapter.notifyItemChanged(position)
                    //SaveDiaryAdapter.saveDiary()
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
                    editDiary(position)
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
                    diaryAdapter.DiaryList.removeAt(position)
                    diaryAdapter.notifyItemRemoved(position)
                    //SaveDiaryAdapter.saveDiary()
                }
            })
    }

    fun generateDiaryDataList(): MutableList<DiaryData> {
        val diaryDataList = mutableListOf<DiaryData>()

        val titles = listOf(
            "새로운 도전!!",
            "친구들과 논날~",
            "운동의 즐거움",
            "주륵주륵 비 오는날..",
            "가족과 함께한 저녁",
        )

        val contents = listOf(
            "오늘은 새로운 프로젝트에 도전했다. 처음부터 끝까지 열심히 해낼 수 있을 것 같아서 기대가 크다. 힘들지만 흥미로운 도전이 될 것 같아 기분이 좋다.",
            "오늘은 오랜만에 친구들과의 모임이 있었다. 함께 맛있는 음식을 먹으며 이야기를 나누고 웃을 수 있어서 정말 행복했다. 소중한 사람들과 함께 시간을 보낼 수 있어서 감사하다.",
            "오늘은 운동을 하러 체육관에 갔다. 오랜만에 몸을 움직이니 기분이 좋아졌고, 운동 후의 상쾌한 느낌은 참 좋다. 건강한 습관을 유지하며 자신을 가꾸는 것이 중요하다는 생각이 들었다.",
            "오늘은 비가 와서 집에서 조용히 시간을 보냈다. 창밖으로 비를 보며 커피 한 잔 마시는 것이 너무 편안하다. 그래도 내일은 맑은 날씨가 되기를 바라며 기대하고 있다.",
            "오늘은 가족과 함께 저녁을 먹으며 소중한 대화를 나눴다. 가족들과의 시간은 언제나 특별하고 소중하다. 서로의 이야기를 공유하고 웃을 수 있어서 행복한 하루였다."
        )

        val dates = listOf(
            "2023년 7월 1일",
            "2023년 7월 2일",
            "2023년 7월 3일",
            "2023년 7월 4일",
            "2023년 7월 5일"
        )

        val imageIcon = listOf(
            R.drawable.w_sunny,
            R.drawable.w_sunnycloudy,
            R.drawable.w_rainy,
            R.drawable.w_snowy,
            R.drawable.w_rainbow,
        )

        for (i in 0..4) {

            val title = titles[i]
            val content = contents[i % contents.size]
            val diaryData = DiaryData(dates[i], titles[i], contents[i], imageIcon[i])
            diaryDataList.add(diaryData)

        }

        return diaryDataList
    }
}

