package com.khanhtruong.myweather.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val location: LocationDto,
    val current: Weather
)

@Serializable
data class LocationDto(val name: String)

@Serializable
data class Weather(
    @SerialName("temp_c")
    val tempC: Float,
    val humidity: Int,
    val uv: Float,
    @SerialName("feelslike_c")
    val feelLikeC: Float,
    val condition: WeatherCondition
)

@Serializable
data class WeatherCondition(
    @SerialName("text")
    val condition: String,
    val icon: String
)
