package com.example.tab_project

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProfileAdapter(private val context: Context) : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_ex, parent, false)

        return ViewHolder(view)
    }

    var datas = mutableListOf<ProfileData>()

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ProfileAdapter.ViewHolder, position: Int) {
        val imageIds = intArrayOf(
            R.drawable.img_1_right,
            R.drawable.img_2_right,
            R.drawable.img_3_right,
            R.drawable.img_4_right,
            R.drawable.img_5_right,
        )

        holder.bind(datas[position])

        holder.itemView.setOnClickListener {

            var item = datas[position]
            if (item.img is Int) {
                // 정수형 리소스 ID인 경우
                val imageId = item.img as Int
                val intent = Intent(context, ImgViewActivity::class.java)
                intent.putExtra("imageId", imageId)
                context.startActivity(intent)
            } else if (item.img is Uri) {
                // URI인 경우
                val uri = item.img as Uri
                val intent = Intent(context, ImgViewActivity::class.java)
                intent.putExtra("imageUri", uri)
                context.startActivity(intent)
                // 원하는 처리를 수행
            }

        }

        holder.overlayImage1.setOnClickListener {
            holder.overlayImage1.visibility = View.GONE
            holder.overlayImage2.visibility = View.VISIBLE
        }
        holder.overlayImage2.setOnClickListener {
            holder.overlayImage1.visibility = View.VISIBLE
            holder.overlayImage2.visibility = View.GONE
        }

    }

    //    class GridAdapter(val layout: View): RecyclerView.ViewHolder(layout)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val overlayImage1 = itemView.findViewById<ImageView>(R.id.overlayImage1)
        val overlayImage2 = itemView.findViewById<ImageView>(R.id.overlayImage2)


        private val imgProfile: ImageView = itemView.findViewById(R.id.img_rv_photo)

        fun bind(item: ProfileData) {

            when (item.img) {
                is Int -> {
                    // 정수형 리소스 ID인 경우
                    Glide.with(itemView).load(item.img as Int).into(imgProfile)
                }
                is Uri -> {
                    // URI인 경우
                    Glide.with(itemView).load(item.img as Uri).into(imgProfile)
                }
                // 다른 유형의 이미지 처리에 대한 로직을 추가할 수 있습니다.
            }

        }
    }
}