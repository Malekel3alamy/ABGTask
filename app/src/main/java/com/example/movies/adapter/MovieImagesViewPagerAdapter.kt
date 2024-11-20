package com.example.blureagency.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blureagency.models.ViewPagerServie
import com.example.movies.R
import com.example.movies.api.models.images.Logo
import com.example.movies.api.models.images.Poster
import com.example.movies.utils.Constants.Companion.IMAGE_PATH


class MovieImagesViewPagerAdapter (private val list:List<Poster>) :RecyclerView.Adapter<MovieImagesViewPagerAdapter.MyViewHolder>(){
    class MyViewHolder(val view:View):RecyclerView.ViewHolder(view) {

        val image = view.findViewById<ImageView>(R.id.viewPagerImageView)


    }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
         return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_pager_item,parent,false))
     }

     override fun getItemCount(): Int {
         return list.size
     }

     override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

         val item = list[position]
         Glide.with(holder.itemView).load("$IMAGE_PATH${item.file_path}").into(holder.image)

     }
 }