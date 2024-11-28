package com.example.gamedb.Data

import java.util.Date

data class Platform(
    val	id: Int,
    val	name: String,
    val	released_at: Date,
    val	requirements: List<String>,

    )