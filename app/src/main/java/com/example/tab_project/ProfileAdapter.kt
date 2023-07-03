package com.example.tab_project

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContextCompat
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
            Log.i("ssss",position.toString())
            val imageId = imageIds[position]
            val intent = Intent(context, MainActivity2::class.java)
            intent.putExtra("imageId", imageId)
            context.startActivity(intent)
        }

//        binding.myGridView.onItemClickListener =
//            AdapterView.OnItemClickListener { parent, view, position, id ->
//                val intent = Intent(ContentProviderCompat.requireContext(), MainActivity2::class.java)
//                intent.putExtra("imageId", imageIds[position])
//                ContextCompat.startActivity(intent)
//            }

    }

    //    class GridAdapter(val layout: View): RecyclerView.ViewHolder(layout)

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imgProfile: ImageView = itemView.findViewById(R.id.img_rv_photo)

        fun bind(item: ProfileData) {

            Glide.with(itemView).load(item.img).into(imgProfile)

        }
    }
}