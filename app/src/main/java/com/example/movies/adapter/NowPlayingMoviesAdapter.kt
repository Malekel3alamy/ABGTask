package com.example.movies.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.movies.R
import com.example.movies.models.Movie
import java.time.format.DateTimeFormatter

class NowPlayingMoviesAdapter: PagingDataAdapter<Movie, NowPlayingMoviesAdapter.MyViewHolder>(
    differCallback ) {
    class MyViewHolder (val view : View) : ViewHolder(view){

        val movieImage = view.findViewById<ImageView>(R.id.movie_image)
        val title      = view.findViewById<TextView>(R.id.movie_titleBookmark)
       // val date   = view.findViewById<TextView>(R.id.date)
        val tvRating = view.findViewById<TextView>(R.id.numericRating)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.movie_item,parent,false)

        return MyViewHolder(itemView)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val movie = getItem(position)
        if (movie != null){
            holder.title.text = movie.title
            val average = movie.vote_average?.toFloat()
            holder.tvRating.text = String.format("%1.1f",average)
            Glide.with(holder.itemView).load("https://image.tmdb.org/t/p/w500/${movie.poster_path}").into(holder.movieImage)

            holder.itemView. setOnClickListener{
                onMovieClick?.invoke(movie)
            }
        }

    }

    var onMovieClick : ((Movie) -> Unit)? = null



    companion object{
        val differCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }

        }

    }
}