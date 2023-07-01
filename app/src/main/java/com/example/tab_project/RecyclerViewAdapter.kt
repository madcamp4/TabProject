package com.example.tab_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(var list: MutableList<ContactData>) : RecyclerView.Adapter<ListAdapter.ListItemViewHolder>() {

    //"hold views" - itemview 안에 있는 여러 view들을 속성으로 가지고 있음
    //recyclerview.viewholder에서 상속받음
    inner class ListItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tv_name: TextView = itemView.findViewById(R.id.tvContactName)
        var tv_number: TextView = itemView.findViewById(R.id.tvContactNumber)
    }

    //어떤 view group을 받아서 viewholder를 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)  //item xml파일을 view로 inflate하여 view를 얻음
        return ListItemViewHolder(view) //해당 view를 사용해 viewholder class를 만듬
    }

    //holder에 있는 view들의 각 속성에 data를 bind함
    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        holder.tv_name.text = list[position].name
        holder.tv_number.text = list[position].number

        //해당 viewholder에 담긴 item click시 onClick() 에서 정의된 이벤트 실행
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    //리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    private lateinit var itemClickListener : OnItemClickListener
    //클래스 외부에서 클릭 이벤트 설정할 수 있도록
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int {
        return list.size
    }
}