package com.prototype.exam.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.prototype.exam.data.model.LoginUser
import com.prototype.exam.data.model.User


@Database(entities = [User::class, LoginUser::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun loginDao(): LoginDao

    companion object {
        @Volatile
        private var database: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return database ?: synchronized(this) {
                database ?: buildDatabase(context).also { database = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            val dbname = "users"

            val builder = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "${dbname}.db"
            )

//            val passphrase: ByteArray = SQLiteDatabase.getBytes("P@s5P4ras3VeryL0n9".toCharArray())
//            val factory = SupportFactory(passphrase, object : SQLiteDatabaseHook {
//                override fun preKey(database: SQLiteDatabase?) = Unit
//                override fun postKey(database: SQLiteDatabase?) {
//                    database?.rawExecSQL("PRAGMA cipher_memory_security = ON")
//                }
//            })
//            builder.openHelperFactory(factory)
            return builder.build()
        }
    }
}
