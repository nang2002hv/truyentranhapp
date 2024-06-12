package com.example.btl1.Model

import androidx.room.Entity
import androidx.room.PrimaryKey



import java.io.Serializable

data class ChapterModel(
    var id: Int,
    val truyenId: Int,
    val tenchap: String,
    val linkchap: String,
    val timechap: String
) : Serializable