package com.example.tab_project

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class DiaryAdapter(private val context: Context) : RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {

    lateinit var DiaryList : MutableList<DiaryData>


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryAdapter.ViewHolder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.card_view, parent, false)

        println("dfdfgd")

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(DiaryList[position])
        println(DiaryList[position])
    }


    override fun getItemCount(): Int {
        return DiaryList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv_icon = itemView.findViewById<ImageView>(R.id.ivProfileImage)
        var tv_date = itemView.findViewById<TextView>(R.id.tvDate)
        var tv_title = itemView.findViewById<TextView>(R.id.tvTitle)
        var tv_content = itemView.findViewById<TextView>(R.id.tvContent)

        fun bind(data: DiaryData) {
            tv_date.text = data.date
            tv_title.text = data.title
            tv_content.text = data.content
            Glide.with(itemView).load(data.icon).into(iv_icon)
        }
    }
}

