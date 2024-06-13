package com.cinemo.test.domain

data class Item(
    val id: String,
    val title: String,
    val subtitle: String?,
    val thumbnail: String,
    val content: Content?
)