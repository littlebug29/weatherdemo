package com.khanhtruong.myweather.data.remote

import com.khanhtruong.myweather.BuildConfig
import com.khanhtruong.myweather.data.remote.dto.City
import com.khanhtruong.myweather.data.remote.dto.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("search.json")
    suspend fun searchCity(
        @Path("key") key: String = BuildConfig.API_KEY,
        @Path("q") query: String
    ): List<City>

    @GET("current.json")
    suspend fun getWeatherInfoOfLocation(
        @Path("key") key: String = BuildConfig.API_KEY,
        @Path("q") name: String
    ): WeatherResponse
}