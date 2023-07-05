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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.tab_project.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Camera
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    private val tabTitleArray = arrayOf("CONTACTS", "ALBUM", "DIARY")

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        val backgroundColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.setBackgroundColor(backgroundColor)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        val tabSelectedTextColor = ContextCompat.getColor(this, R.color.white)
        val tabUnselectedTextColor = ContextCompat.getColor(this, R.color.black)

        binding.apply {
            viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabTitleArray[position]
            }.attach()

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        viewPager.currentItem = tab.position
                        setTabTextColor(tabLayout, tabSelectedTextColor, tab.position)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        setTabTextColor(tabLayout, tabUnselectedTextColor, tab.position)
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {}

                private fun setTabTextColor(tabLayout: TabLayout, color: Int, position: Int) {
                    val tabView = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(position)
                    if (tabView is TextView) {
                        tabView.setTextColor(color)
                    }
                }
            })

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tabLayout.selectTab(tabLayout.getTabAt(position))
                }
            })

        }
    }
}