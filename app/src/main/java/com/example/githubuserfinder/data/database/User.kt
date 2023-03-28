package com.example.githubuserfinder.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
data class User(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    var id: Int,

    @ColumnInfo
    var login: String?,

    @ColumnInfo(name = "imageUrl")
    var imageUrl: String?,
)