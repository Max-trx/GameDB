package com.example.gamedatabase.data

import android.util.Log
import kotlinx.coroutines.flow.Flow

class OfflineUserRepository(private val userDao: UserDao) : UserRepository {
    override fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers()
    }
    override suspend fun getUserById(userId: Int): User? {
        return userDao.getUserById(userId)
    }
    override suspend fun updateFavGames(userId: Int, favGames: String) {
        userDao.updateFavGames(userId, favGames)
        Log.d("UserDao", "Update completed")
    }
    override suspend fun setNewUser(user: User) {
        userDao.setNewUser(user)
    }
}