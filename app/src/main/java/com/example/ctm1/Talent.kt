package com.example.ctm1

import java.io.Serializable

data class Talent(
    val category: String,
    val title: String,
    val description: String,
    val price: String,
    val writer: String,
    val date: String,

    val deadline: String = "",

    val currentCount: Int = 0,
    val maxCount: Int = 0,
    var likeCount: Int = 0,
    var isLiked: Boolean = false
) : Serializable