package com.khanhtruong.myweather.data.remote

import com.khanhtruong.myweather.data.remote.dto.CityDto
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("search.json")
    fun searchCity(
        @Path("key") key: String = BuildConfig.API_KEY,
        @Path("q") query: String
    ): List<CityDto>
}