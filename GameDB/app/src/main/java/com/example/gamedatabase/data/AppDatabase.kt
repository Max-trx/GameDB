package com.example.gamedatabase.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val databaseFile = context.getDatabasePath("user_database")
                val builder = if (databaseFile.exists()) {
                    // Base de données existante, ne pas recréer
                    Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "user_database"
                    )
                } else {
                    // Première initialisation à partir d'un fichier dans assets
                    Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "user_database"
                    )
                        .createFromAsset("databases/users.db")
                }

                builder.fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}