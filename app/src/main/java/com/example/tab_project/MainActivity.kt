package com.example.tab_project

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.tab_project.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val tabTitleArray = arrayOf("First Tab", "ALBUM", "Third Tab")

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabTitleArray[position]
                /**  [TabLayoutMediator]: [TabLayout]과 [ViewPager2]를 연동을 도와주는 객체이다. */
            }.attach()
        }

    }
}