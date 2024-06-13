package com.cinemo.test.domain

import java.io.Serializable

data class Content(
    val displayStyle: String,
    val items: List<Item>
): Serializable