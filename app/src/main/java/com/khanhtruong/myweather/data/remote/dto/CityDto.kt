package com.khanhtruong.myweather.data.remote.dto

data class CityDto(
    val id: Long,
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lng: Double
)
