package com.example.tab_project

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.RequiresApi

class ImgViewActivity : AppCompatActivity() {
    private lateinit var bigImage: ImageView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imageview)

        bigImage = findViewById(R.id.bigImage)

        val imageId = intent.getIntExtra("imageId",0)
        bigImage.setImageResource(imageId)

    }
}