package com.example.gamedatabase.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Item] from a given data source.
 */
interface UserRepository {
    fun getAllUsers(): Flow<List<User>>
    suspend fun getUserById(userId: Int): User?
    suspend fun updateFavGames(userId: Int, favGames: String)
}