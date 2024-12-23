package com.khanhtruong.myweather.data

import com.khanhtruong.myweather.BuildConfig
import com.khanhtruong.myweather.data.remote.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Provides
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(httpClient)
        .build()

    @Provides
    fun provideWeatherService(retrofit: Retrofit): WeatherService =
        retrofit.create(WeatherService::class.java)

    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}