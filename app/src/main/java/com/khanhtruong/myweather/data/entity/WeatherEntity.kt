package com.khanhtruong.myweather.data.entity

data class WeatherEntity(
    val name: String,
    val weatherIcon: String,
    val tempC: Float,
    val humidity: Int,
    val uv: Float,
    val feelsLike: Float
)
