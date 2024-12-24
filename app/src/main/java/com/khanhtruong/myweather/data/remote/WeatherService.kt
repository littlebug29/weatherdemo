package com.khanhtruong.myweather.data.remote

import com.khanhtruong.myweather.BuildConfig
import com.khanhtruong.myweather.data.remote.dto.City
import com.khanhtruong.myweather.data.remote.dto.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("search.json")
    suspend fun searchCity(
        @Query("key") key: String = BuildConfig.API_KEY,
        @Query("q") query: String
    ): List<City>

    @GET("current.json")
    suspend fun getWeatherInfoOfLocation(
        @Query("key") key: String = BuildConfig.API_KEY,
        @Query("q") name: String
    ): WeatherResponse
}