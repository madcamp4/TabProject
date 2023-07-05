package com.example.tab_project

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide

class ImgViewActivity : AppCompatActivity() {
    private lateinit var bigImage: ImageView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imageview)

        bigImage = findViewById(R.id.bigImage)

        val imageUri = intent.getParcelableExtra<Uri>("imageUri")
        println(imageUri)
        if (imageUri != null) {
            Glide.with(this)
                .load(imageUri)
                .into(bigImage)
        } else {
            val imageId = intent.getIntExtra("imageId", 0)
            if (imageId != 0) {
                bigImage.setImageResource(imageId)
            }
        }
    }
}