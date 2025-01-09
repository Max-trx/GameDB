package com.example.gamedatabase.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "user_name")
    var userName: String,
    @ColumnInfo(name = "user_password")
    var userPassword: String,
    @ColumnInfo(name = "fav_games")
    val favGames: String
)