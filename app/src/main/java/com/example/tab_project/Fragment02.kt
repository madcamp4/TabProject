package com.example.tab_project

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tab_project.databinding.Fragment02Binding
import kotlinx.coroutines.NonDisposableHandle.parent

class Fragment02 : Fragment() {

    private lateinit var binding: Fragment02Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Fragment02Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileAdapter = context?.let { ProfileAdapter(it) }
        binding.rvProfile.adapter = profileAdapter
        // binding.rvProfile.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.rvProfile.layoutManager = GridLayoutManager(activity, 2)

        profileAdapter?.datas?.apply {
            add(ProfileData(img = R.drawable.img_1_right))
            add(ProfileData(img = R.drawable.img_2_right))
            add(ProfileData(img = R.drawable.img_3_right))
            add(ProfileData(img = R.drawable.img_4_right))
            add(ProfileData(img = R.drawable.img_5_right))
            add(ProfileData(img = R.drawable.img_1_right))
            add(ProfileData(img = R.drawable.img_2_right))
            add(ProfileData(img = R.drawable.img_3_right))
            add(ProfileData(img = R.drawable.img_4_right))
            add(ProfileData(img = R.drawable.img_5_right))
            add(ProfileData(img = R.drawable.img_1_right))
            add(ProfileData(img = R.drawable.img_2_right))
            add(ProfileData(img = R.drawable.img_3_right))
            add(ProfileData(img = R.drawable.img_4_right))
            add(ProfileData(img = R.drawable.img_5_right))
            add(ProfileData(img = R.drawable.img_1_right))
        }

        val img = arrayOf(
            R.drawable.img_1_right,
            R.drawable.img_2_right,
            R.drawable.img_3_right,
            R.drawable.img_4_right,
            R.drawable.img_5_right,
        )

        /* class CustomAdapter(val context: Context, val img:Array<Int>) : BaseAdapter(){

            var layoutInflater =  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


                var view = view
                if(view==null){
                   view = layoutInflater.inflate(R.layout.item_recycler_ex,parent,false);
                }
                //var imageView = view.findViewById<ImageView>(R.id.img_rv_photo);
                //imageView.setImageResource(R.drawable.img[position])

                return view!!

            }

            override fun getCount(): Int {
                return img.size
            }

            override fun getItem(position: Int): Any {
                return img[position]
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }
        }

        var customAdapter = context?.let { CustomAdapter(it,img) };
        binding.myGridView.adapter = customAdapter;
        binding.myGridView.setOnItemClickListener { parent, view, position, id ->
            //Log.e("name",img[position].toString())
            Toast.makeText(context,"image clicked!",Toast.LENGTH_LONG).show()
        }
        */
    }

}
