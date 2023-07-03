package com.example.tab_project

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import kotlinx.coroutines.NonDisposableHandle.parent

class ImageAdapter(private val context: Context, private val imageIds: IntArray) : BaseAdapter() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

//    var layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int {
        return imageIds.size
    }

    override fun getItem(position: Int): Any {
        return imageIds[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_recycler_ex, parent, false);
        }
        var imageView = view?.findViewById<ImageView>(R.id.img_rv_photo);
        imageView?.setImageResource(imageIds[position])

        return view!!
    }
}
