package com.prototype.exam.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "login")
data class LoginUser(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val username: String,
    val password: String
)