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
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.contentColorFor
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView


class ContactAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var contactsList : MutableList<ContactData> =  getContacts(context)

    lateinit var storedContactsList : MutableList<ContactData>

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

        var btn_call: ImageButton = itemView.findViewById(R.id.btnCall)
        var btn_message: ImageButton = itemView.findViewById(R.id.btnMessage)
        var btn_edit: ImageButton = itemView.findViewById(R.id.btnEdit)
        var btn_share: ImageButton = itemView.findViewById(R.id.btnShare)

        fun bind(item: ContactData) {
            tv_name.text = item.name
            tv_number.text = item.number

            btn_call.setOnClickListener {
                Toast.makeText(context, "calling the number ${item.number}", Toast.LENGTH_SHORT).show()
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:${item.number}")
                startActivity(context, intent, null)
            }

            btn_message.setOnClickListener {
                Toast.makeText(context, "messaging the number ${item.number}", Toast.LENGTH_SHORT).show()
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("smsto:${item.number}")
                startActivity(context, intent, null)
            }

            btn_share.setOnClickListener {
                Toast.makeText(context, "sharing the number ${item.number}", Toast.LENGTH_SHORT).show()
                val contactUri = item.id?.let { it1 -> getContactUriFromContactId(it1) }

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = ContactsContract.Contacts.CONTENT_VCARD_TYPE
                intent.putExtra(Intent.EXTRA_STREAM, item.number)
                startActivity(context, Intent.createChooser(intent, "Share Contact"), null)
            }

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

                //expanded view에서 edit button 연결
                holder.btn_edit.setOnClickListener {
                    editClickListener.onClick(it, position)
                }
            }
        }

        //해당 viewholder에 담긴 itemView click시 onClick() 에서 정의된 이벤트 실행
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

        //해당 viewholder에 담긴 itemView longclick시 onLongClick()에서 정의된 이벤트 실행
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

    interface OnEditClickListener {
        fun onClick(v: View, position: Int)
    }

    private lateinit var editClickListener: OnEditClickListener
    fun setEditClickListerer(onEditClickListener: OnEditClickListener) {
        this.editClickListener = onEditClickListener
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

    //creates backup of the contactslist
    private fun setViewModesList() {
        storedContactsList = contactsList
    }

    //restore the viewmode attributes of the contactslist using the stored viewmodeslist
    private fun getViewModesList() {
        for (contact in contactsList) {
            for (storedContact in storedContactsList) {
                if (contact.id == storedContact.id) {
                    contact.viewMode = storedContact.viewMode
                }
            }
        }
    }

    fun addContactInAdapter() {
        setViewModesList()
        contactsList = getContacts(context)
        getViewModesList()
        notifyDataSetChanged()
    }

    fun editContactInAdapter(position: Int) {
        setViewModesList()
        contactsList = getContacts(context)
        getViewModesList()
        notifyItemChanged(position)
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