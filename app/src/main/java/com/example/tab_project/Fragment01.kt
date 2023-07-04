package com.example.tab_project

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.contentColorFor
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class Fragment01 : Fragment(){
    private lateinit var binding: Fragment01Binding

    private val PERMISSIONS_REQUEST_CONTACTS = 100

    lateinit var contactAdapter : ContactAdapter

    //intent가 끝난 후에 showContacts를 다시 실행하도록 contract 생성
    val addContactContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Contact added successfully, run your function here
            // You can access the intent data if needed: result.data
            //contactAdapter.updateDataset()
            contactAdapter.addContactInAdapter() //contactAdapter 속의 데이터 수정
        }
    }

    //intent가 끝난 후에 showContacts를 다시 실행하도록 contract 생성
    val editContactContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Contact added successfully, run your function here
            // You can access the intent data if needed: result.data
            //contactAdapter.updateDataset()
            contactAdapter.addContactInAdapter() //contactAdapter 속의 데이터 수정
        }
    }

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

        //permission이 있어야 전화번호부 표시
        if (permissionAreGranted()) {
            showContacts() //permission 있으니 show contacts
        } else {
            requestPermissions() //ask for permission
        }
    }

    //intent에서 원래 fragment으로 돌아오면 dataset 업데이트
    override fun onResume() {
        super.onResume()

        contactAdapter.updateDataset()
    }

    private fun showContacts() {
        contactAdapter = context?.let { ContactAdapter(it) }!!

        binding.rvContactItems.adapter = contactAdapter
        binding.rvContactItems.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        //short click - 수정(세부정보),
        contactAdapter.setItemClickListener(object: ContactAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                val id = contactAdapter.contactsList[position].id
                //editContact(id, position) //해당 id를 가지는 연락처를 수정
                Toast.makeText(context, "short click", Toast.LENGTH_SHORT).show()

                //toggle viewmode
                contactAdapter.toggleViewMode(position)
            }
        })

        contactAdapter.setItemLongClickListener(object: ContactAdapter.OnItemLongClickListener{
            override fun onLongClick(v: View, position: Int) {
                val id = contactAdapter.contactsList.get(position).id

                //Toast.makeText(context, "long click", Toast.LENGTH_SHORT).show()
                removeContact(id, position) //해당 id를 가지는 연락처를 수정
                contactAdapter.updateDataset()
            }
        })

        contactAdapter.setEditClickListerer(object: ContactAdapter.OnEditClickListener{
            override fun onClick(v: View, position: Int) {
                val id = contactAdapter.contactsList[position].id
                editContact(id, position)
            }
        })

        //핸드폰의 연락처와 contacts를 연동시킴
        binding.btnSyncContacts.setOnClickListener {
            contactAdapter.updateDataset()
        }

        //핸드폰의 연락처에 새 연락처를 추가 후 창 업데이트
        binding.btnAddContact.setOnClickListener {
            addContacts()
        }
    }

    private fun addContacts() {
        // Creates a new Intent to insert a contact
        val intent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
            // Sets the MIME type to match the Contacts Provider
            type = ContactsContract.RawContacts.CONTENT_TYPE
        }

        addContactContract.launch(intent) //연락처 수정하는 intent 실행
        //contactAdapter.addContactInAdapter() //contactAdapter 속의 데이터 수정
    }


    private fun editContact(target_id: String?, position: Int) {
        //target_id를 editIntent에 사용할 수 있는 ContactURI로 변환
        val selectedContactUri = target_id?.let { getContactUriFromContactId(it) }

        //editIntent 생성
        val editIntent = Intent(Intent.ACTION_EDIT).apply {
            setDataAndType(selectedContactUri, ContactsContract.Contacts.CONTENT_ITEM_TYPE) //해당 ContactURI를 수정하도록
        }
        editIntent.putExtra("finishActivityOnSaveCompleted", true) //오류 방지를 위한 추가 플래그

        addContactContract.launch(editIntent) //연락처 수정하는 intent 실행
        contactAdapter.editContactInAdapter(position) //contactAdapter 속의 데이터 수정
    }


    private fun removeContact(target_id: String?, position: Int) {
        val selectedContactUri = target_id?.let { getContactUriFromContactId(it) }

        if (selectedContactUri != null) {
            context?.contentResolver?.delete(selectedContactUri, null)

            contactAdapter.removeContactInAdapter(position)
            Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun permissionAreGranted(): Boolean {
        val readContactsPermission = context?.let {
            ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS)
        } == PackageManager.PERMISSION_GRANTED

        val writeContactsPermission = context?.let {
            ContextCompat.checkSelfPermission(it, Manifest.permission.WRITE_CONTACTS)
        } == PackageManager.PERMISSION_GRANTED

        val callContactsPermission = context?.let {
            ContextCompat.checkSelfPermission(it, Manifest.permission.CALL_PHONE)
        } == PackageManager.PERMISSION_GRANTED

        return readContactsPermission && writeContactsPermission && callContactsPermission
    }


    //read_contacts와 write_contacts에 대한 권한을 요청
    private fun requestPermissions() {
        val permissions = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.CALL_PHONE)
        ActivityCompat.requestPermissions(requireActivity(), permissions, PERMISSIONS_REQUEST_CONTACTS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    // Permission 허락 됐으니 앱 실행
                    showContacts()
                } else {
                    // Permission 거부 됐다는 메세지
                    Toast.makeText(context, "Permission denied. Cannot read contacts.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}

fun getContactUriFromContactId(ContactId: String): Uri? {
    val baseUri: Uri = ContactsContract.Contacts.CONTENT_URI
    val contactId: Long = ContactId.toLongOrNull() ?: return null
    return ContentUris.withAppendedId(baseUri, contactId)
}

fun getContacts(context: Context): MutableList<ContactData> {
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

    datas.sortBy { it.name }

    return datas
}