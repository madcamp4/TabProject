package com.example.tab_project

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import kotlinx.coroutines.NonDisposableHandle.parent

class DiaryAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var DiaryList : MutableList<DiaryData>


    override fun getItemViewType(position: Int): Int {
        return DiaryList[position].viewMode
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_diary_short, parent, false)

        return when(viewType) {
            SHORT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_diary_short, parent, false)  //item xml파일을 view로 inflate하여 view를 얻음
                ShortViewHolder(view)
            }
            FULL -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_diary_full, parent, false)  //item xml파일을 view로 inflate하여 view를 얻음
                FullViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_diary_full, parent, false)  //item xml파일을 view로 inflate하여 view를 얻음
                SwipedViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            SHORT -> {
                (holder as ShortViewHolder).bind(DiaryList[position])
            }
            FULL -> {
                (holder as FullViewHolder).bind(DiaryList[position])
            }
            else -> {
                (holder as SwipedViewHolder).bind(DiaryList[position])
            }
        }

        //해당 viewholder에 담긴 itemView click시 onClick() 에서 정의된 이벤트 실행
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

    }

    //Click에 대한 리스너 인터페이스 - 연락처 수정
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    private lateinit var itemClickListener : OnItemClickListener
    //클래스 외부에서 클릭 이벤트 설정할 수 있도록
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int {
        return DiaryList.size
    }

    open inner class ShortViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var iv_icon = itemView.findViewById<ImageView>(R.id.ivProfileImage)
        var tv_date = itemView.findViewById<TextView>(R.id.tvDate)
        var tv_title = itemView.findViewById<TextView>(R.id.tvTitle)
        var tv_content = itemView.findViewById<TextView>(R.id.tvContent)


        open fun bind(data: DiaryData) {
            tv_date.text = data.date
            tv_title.text = data.title

            // 첫번째 줄만 content에 포함시킴 - 요약 버전
            val long_content = data.content
            var short_content = ""
            val lines = long_content?.lines()
            if (!lines.isNullOrEmpty()) {
                short_content = lines[0]
            }
            tv_content.text = short_content

            Glide.with(itemView).load(data.icon).into(iv_icon)
        }
    }

    inner class FullViewHolder(view: View) : ShortViewHolder(view) {
        override fun bind(data: DiaryData) {

            tv_date.text = data.date
            tv_title.text = data.title
            tv_content.text = data.content
            Glide.with(itemView).load(data.icon).into(iv_icon)
        }
    }


    inner class SwipedViewHolder(view: View) : ShortViewHolder(view) {

    }
}