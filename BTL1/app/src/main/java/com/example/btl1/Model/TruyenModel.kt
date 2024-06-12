package com.example.btl1.Model


import java.io.Serializable
data class TruyenModel(
    var id: Int = 0,
    var tentruyen: String,
    var linktruyen: String,
    var chaptruyen: ArrayList<ChapterModel>,
    var linkhinhtruyen: String
) : Serializable