package com.example.gamedatabase.data

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the Inventory database
 */
@Dao
interface UserDao {

    @Query("SELECT * from users ")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?

    @Query("UPDATE users SET fav_games = :favGames WHERE id = :userId")
    suspend fun updateFavGames(userId: Int, favGames: String)

    @Insert
    suspend fun setNewUser(user: User)

}