package com.example.tab_project

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var contactsList : MutableList<ContactData> =  getContacts(context)

    //"hold views" - itemview 안에 있는 여러 view들을 속성으로 가지고 있음
    //recyclerview.viewholder에서 상속받음
    inner class RegularItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tv_name: TextView = itemView.findViewById(R.id.tvContactName)
        var tv_number: TextView = itemView.findViewById(R.id.tvContactNumber)

        fun bind(item: ContactData) {
            tv_name.text = item.name
            tv_number.text = item.number
        }
    }

    inner class ExpandedItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tv_name: TextView = itemView.findViewById(R.id.tvContactName)
        var tv_number: TextView = itemView.findViewById(R.id.tvContactNumber)
        var tv_ID: TextView = itemView.findViewById(R.id.tvID)

        fun bind(item: ContactData) {
            tv_name.text = item.name
            tv_number.text = item.number
            tv_ID.text = item.id
        }
    }

    override fun getItemViewType(position: Int): Int {
        return contactsList[position].viewMode
    }

    //어떤 view group을 받아서 viewholder를 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            REGULAR -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)  //item xml파일을 view로 inflate하여 view를 얻음
                RegularItemViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact_expanded, parent, false)  //item xml파일을 view로 inflate하여 view를 얻음
                ExpandedItemViewHolder(view)
            }
        }
    }


    //holder에 있는 view들의 각 속성에 data를 bind함
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(contactsList[position].viewMode) {
            REGULAR -> {
                (holder as RegularItemViewHolder).bind(contactsList[position])
            }
            else -> {
                (holder as ExpandedItemViewHolder).bind(contactsList[position])
            }
        }

        //해당 viewholder에 담긴 item click시 onClick() 에서 정의된 이벤트 실행
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

        //해당 viewholder에 담긴 item longclick시 onLongClick()에서 정의된 이벤트 실행
        holder.itemView.setOnLongClickListener {
            itemLongClickListener.onLongClick(it, position)
            true
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

    //longClick에 대한 리스너 인터페이스 - 연락처 제거
    interface OnItemLongClickListener {
        fun onLongClick(v: View, position: Int)
    }

    private lateinit var itemLongClickListener : OnItemLongClickListener
    fun setItemLongClickListener(onItemLongClickListener: OnItemLongClickListener) {
        this.itemLongClickListener = onItemLongClickListener
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    fun updateDataset() {
        contactsList = getContacts(context)
        notifyDataSetChanged()
    }


    fun addContactInAdapter() {
        contactsList.add()
    }

    fun editContactInAdapter() {

    }

    fun removeContactInAdapter(position: Int) {
        contactsList.removeAt(position)

        notifyItemRemoved(position)
    }

    fun toggleViewMode(position: Int) {
        contactsList[position].viewMode = when(contactsList[position].viewMode) {
            EXPANDED -> REGULAR
            else -> EXPANDED
        }
        notifyItemChanged(position)
    }

}