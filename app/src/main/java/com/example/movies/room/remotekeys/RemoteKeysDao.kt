package com.example.movies.room.remotekeys

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("Select * From remote_key Where id = :id")
    suspend fun getRemoteKeyByMovieID(id:Int): RemoteKeys?

    @Query("Delete From remote_key")
    suspend fun clearRemoteKeys()


    @Query("Select created_at From remote_key Order By created_at DESC LIMIT 1")
    suspend fun getCreationTime(): Long?
}