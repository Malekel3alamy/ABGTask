package com.example.movies.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.room.MovieEntity
import com.example.movies.utils.Constants.Companion.IMAGE_PATH

class MovieRecyclerAdapter : PagingDataAdapter<MovieEntity, com.example.movies.adapter.MovieRecyclerAdapter.MyViewHolder>(
    differCallback ) {
    class MyViewHolder (val view : View) : ViewHolder(view){

        val movieImage = view.findViewById<ImageView>(R.id.movie_image)
        val title      = view.findViewById<TextView>(R.id.movie_item_title)
       // val date   = view.findViewById<TextView>(R.id.date)
        val tvRating = view.findViewById<TextView>(R.id.movie_item_numericRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.movie_item,parent,false)

        return MyViewHolder(itemView)
    }



    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val movie = getItem(position)
if (movie != null){
    holder.title.text = movie.title
    Log.d("MovieName",movie.title.toString())
    val average = movie.vote_average?.toFloat()
    holder.tvRating.text = String.format("%1.1f",average)
    Glide.with(holder.itemView).load("$IMAGE_PATH${movie.poster_path}").into(holder.movieImage)

    holder.itemView. setOnClickListener{
        onMovieClick?.invoke(movie)
    }
}

    }

    var onMovieClick : ((MovieEntity) -> Unit)? = null



 companion object{
     val differCallback = object : DiffUtil.ItemCallback<MovieEntity>() {
         override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
             return oldItem.id == newItem.id
         }

         override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
             return oldItem == newItem
         }

     }

 }


    val differ = AsyncListDiffer(this, differCallback)



}