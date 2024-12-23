package com.khanhtruong.myweather.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: Long,
    val name: String
)
