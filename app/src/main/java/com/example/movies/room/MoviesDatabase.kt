package com.example.movies.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movies.api.models.Movie
import com.example.movies.room.remotekeys.RemoteKeys
import com.example.movies.room.remotekeys.RemoteKeysDao

@Database(
    entities = [Movie::class,RemoteKeys::class],
    version = 1
)
@TypeConverters(Converters::class)
 abstract class MoviesDatabase : RoomDatabase() {

     abstract fun getMoviesDao() : MoviesDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao


     companion object{



         @Volatile
         private var instance : MoviesDatabase? = null

         fun getInstance(context : Context): MoviesDatabase {
             if (instance == null) {
                 synchronized(this) {
                     if (instance == null) {
                         instance = createDatabase(context.applicationContext)
                     }
                 }
             }
             return instance!!
         }

         private   fun createDatabase(context: Context)  =

             Room.databaseBuilder(context.applicationContext,
                 MoviesDatabase::class.java,
                 "movies_db.db").build()
     }



}