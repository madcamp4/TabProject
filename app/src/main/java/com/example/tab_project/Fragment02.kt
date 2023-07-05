package com.example.tab_project

import android.Manifest
import android.Manifest.permission_group.STORAGE
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tab_project.databinding.Fragment02Binding
import java.io.File
import java.text.SimpleDateFormat


class Fragment02 : Fragment() {

    private lateinit var binding: Fragment02Binding
    private lateinit var imageView: ImageView

    // storage 권한 처리에 필요한 변수
    private val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
//        Manifest.permission.READ_EXTERNAL_STORAGE,
//        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private val PERMISSIONS_CODE = 98
    private val STORAGE_CODE = 99


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Fragment02Binding.inflate(inflater, container, false)
        imageView = binding.avatars
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //view의 초기값, Adapter세팅
        super.onViewCreated(view, savedInstanceState)

        val profileAdapter = context?.let { ProfileAdapter(it) }
        binding.rvProfile.adapter = profileAdapter
        // binding.rvProfile.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rvProfile.layoutManager = GridLayoutManager(activity, 3)

        if (!checkPermission(PERMISSIONS, PERMISSIONS_CODE)) {
            ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, PERMISSIONS_CODE)
        }

        profileAdapter?.datas?.apply {
            add(ProfileData(img = R.drawable.img_1_right))
            add(ProfileData(img = R.drawable.img_2_right))
            add(ProfileData(img = R.drawable.img_3_right))
            add(ProfileData(img = R.drawable.img_4_right))
            add(ProfileData(img = R.drawable.img_5_right))
        }

        // 카메라
        val camera = binding.btnCamera
        cameraActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    println("아직 이프 안들어감")
                    val imageBitmap = data?.extras?.get("data") as Bitmap

                    val croppedBitmap = cropToSquare(requireContext(), imageBitmap)

                    //if (data?.getStringExtra("data") != null) {
                    if (croppedBitmap != null) {
                        println("saving file")
                        val uri = saveFile(RandomFileName(), "image/jpeg", croppedBitmap)

                        val profileAdapter = binding.rvProfile.adapter as? ProfileAdapter
                        uri?.let { ProfileData(img = it) }?.let { profileAdapter?.datas?.add(it) }
                        profileAdapter?.notifyDataSetChanged()

                        imageView.setImageURI(uri as Uri?)
                    }
                }
            }
        camera.setOnClickListener {
            CallCamera()
        }

        // 사진 저장
        val picture = binding.btnPicture
        pictureActivityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val uri = data?.data
                    imageView.setImageURI(uri)
                }
            }
        picture.setOnClickListener {
            GetAlbum()
        }
    }

    private lateinit var cameraActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var pictureActivityResultLauncher: ActivityResultLauncher<Intent>

    fun cropToSquare(context: Context, bitmap: Bitmap): Bitmap? {
        try {
            // 이미지를 정사각형으로 자르기
            val size = bitmap.width.coerceAtMost(bitmap.height)
            val x = (bitmap.width - size) / 2
            val y = (bitmap.height - size) / 2
            val squareBitmap = Bitmap.createBitmap(bitmap, x, y, size, size)

            // 이미지 크기 조정 (예: 100x100 픽셀)
            val resizedBitmap = Bitmap.createScaledBitmap(squareBitmap, 100, 100, false)

            return resizedBitmap
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    // 카메라 촬영 - 권한 처리
    private fun CallCamera() {
        println("call camera executed")
        if (checkPermission(PERMISSIONS, PERMISSIONS_CODE)) {
        //if(true) {
            println("permssion granted")
            val callIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraActivityResultLauncher.launch(callIntent)
        }
        else {
            ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, PERMISSIONS_CODE)
        }
    }

    // 사진 저장
    private fun saveFile(fileName: String, mimeType: String, bitmap: Bitmap): Uri? {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        }

        val resolver = context?.contentResolver
        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val item = resolver?.insert(collection, values)

        item?.let { uri ->
            if (resolver != null) {
                resolver.openOutputStream(uri)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.flush()
                }
            }
            return uri
        }
        return null
    }

    // 결과
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val imageView = binding.avatars

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PERMISSIONS_CODE -> {
                    if (data?.extras?.get("data") != null) {
                        val img = data.extras?.get("data") as Bitmap
                        val uri = saveFile(RandomFileName(), "image/jpeg", img)
                        imageView.setImageURI(Uri.parse(uri.toString()))
                    }
                }
            }
        }
    }

    // 파일명을 날짜 저장
    private fun RandomFileName(): String {
        return SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
    }

    // 갤러리 취득
    private fun GetAlbum() {
        if (checkPermission(PERMISSIONS, PERMISSIONS_CODE)) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            pictureActivityResultLauncher.launch(intent)
        }
    }

    // 권한 확인 및 요청
    private fun checkPermission(permissions: Array<String>, requestCode: Int): Boolean {
        var success = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (context?.let { ContextCompat.checkSelfPermission(it, permission) }
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    success = false
                }
                println(permission)
                println(success)
            }
        }
//        if (success != true) {
//            ActivityCompat.requestPermissions(requireActivity(), permissions, requestCode)
//            println("asked permission")
//        }

        return success
    }

    // 권한 요청 결과 처리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
                PERMISSIONS_CODE-> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(context, "권한들을 승인해 주세요", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }

    }
}
