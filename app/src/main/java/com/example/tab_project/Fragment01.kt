package com.example.tab_project

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tab_project.databinding.Fragment01Binding

import android.provider.ContactsContract
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity

class Fragment01 : Fragment(){
    private lateinit var binding: Fragment01Binding

    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Fragment01Binding.inflate(inflater, container, false) //fragment_01을 참조할 수 있도록 만든 binding class
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (hasReadContactsPermission()) {
            showContacts() //permission 있으니 show contacts
        } else {
            requestReadContactsPermission() //ask for permission
        }

        /*핸드폰의 연락처와 contacts를 연동시킴*/
        binding.btnSyncContacts.setOnClickListener {
            showContacts()
        }

        /*핸드폰의 연락처에 새 연락처를 추가 후 창 업데이트*/
        binding.btnAddContact.setOnClickListener {
            addContacts()
            showContacts()
        }
    }

    private fun showContacts() {
        var dataList = context?.let { getContacts(it) }

        var listAdapter : ListAdapter? = dataList?.let { ListAdapter(it) }
        binding.rvContactItems.adapter = listAdapter
        binding.rvContactItems.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        if (listAdapter != null) {
            listAdapter.setItemClickListener(object: ListAdapter.OnItemClickListener{
                override fun onClick(v: View, position: Int) {
                    val id = dataList?.get(position)?.id
                    editContacts(id)
                }
            })
        }
    }

    private fun addContacts() {
        // Creates a new Intent to insert a contact
        val intent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
            // Sets the MIME type to match the Contacts Provider
            type = ContactsContract.RawContacts.CONTENT_TYPE
        }
        context?.let { startActivity(it, intent, null) }
    }

    fun getContactUriFromContactId(ContactId: String): Uri? {
        val baseUri: Uri = ContactsContract.Contacts.CONTENT_URI
        val contactId: Long = ContactId.toLongOrNull() ?: return null
        return ContentUris.withAppendedId(baseUri, contactId)
    }

    fun editContacts(target_id: String?) {
        //target_id를 editIntent에 사용할 수 있는 ContactURI로 변환
        val selectedContactUri = target_id?.let { getContactUriFromContactId(it) }

        //editIntent 생성
        val editIntent = Intent(Intent.ACTION_EDIT).apply {
            setDataAndType(selectedContactUri, ContactsContract.Contacts.CONTENT_ITEM_TYPE) //해당 ContactURI를 수정하도록
        }
        editIntent.putExtra("finishActivityOnSaveCompleted", true) //오류 방지를 위한 추가 플래그
        context?.let { startActivity(it, editIntent, null) }
    }

    private fun getContacts(context: Context): MutableList<ContactData> {
        //데이터가 저장될 datas
        val datas: MutableList<ContactData> = mutableListOf()

        val resolver: ContentResolver = context.contentResolver

        //전화번호들이 저장된 Uri를 가져옴
        val phoneUri: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

        //ContactsContract.CommonDataKinds.Phone의 칼럼들을 가져옴
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        //ContentResolver에 쿼리를 날림
        val cursor: Cursor? = resolver.query(phoneUri, projection, null, null, null)

        //커서를 이동시키며 커서에 담긴 데이터를 하나씩 추출 후 datas에 담음
        cursor?.let {
            val idIndex: Int = it.getColumnIndex(projection[0]) //각 column의 index
            val nameIndex: Int = it.getColumnIndex(projection[1])
            val numberIndex: Int = it.getColumnIndex(projection[2])

            while (it.moveToNext()) {
                val id: String = it.getString(idIndex) //cursor가 있는 row에서 idIndex에 해당하는 String
                val name: String = it.getString(nameIndex)
                val number: String = it.getString(numberIndex)

                val phoneBook =  ContactData(id, name, number)
                datas.add(phoneBook)
            }
            it.close()
        }

        return datas
    }

    private fun hasReadContactsPermission(): Boolean {
        val permission = Manifest.permission.READ_CONTACTS
        val result = context?.let { ContextCompat.checkSelfPermission(it, permission) }
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestReadContactsPermission() {
        val permission = Manifest.permission.READ_CONTACTS
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), PERMISSIONS_REQUEST_READ_CONTACTS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    showContacts()
                } else {
                    // Permission denied
                    Toast.makeText(context, "Permission denied. Cannot read contacts.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}