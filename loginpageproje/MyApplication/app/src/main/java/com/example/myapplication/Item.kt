

package com.example.myapplication

data class Item(
    val id: Int,
    val title: String,
    val category: String,
    val description: String,
    val image: String? // imageResId değil, image olarak yazılmalı
)
