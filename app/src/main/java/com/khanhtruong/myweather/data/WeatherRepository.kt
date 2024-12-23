package com.khanhtruong.myweather.data

import com.khanhtruong.myweather.data.entity.LocationSearchResultEntity
import com.khanhtruong.myweather.data.entity.WeatherEntity
import com.khanhtruong.myweather.data.local.LocationLocalSource
import com.khanhtruong.myweather.data.remote.WeatherService
import com.khanhtruong.myweather.data.remote.dto.City
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class WeatherRepository @Inject constructor(
    private val localSource: LocationLocalSource,
    private val weatherService: WeatherService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    val currentWeatherFlow: Flow<WeatherEntity> = localSource.selectedLocationsFlow
        .map { weatherService.getWeatherInfoOfLocation(name = it) }
        .flowOn(ioDispatcher)
        .catch { _ -> null }
        .mapNotNull {
            WeatherEntity(
                name = it.location.name,
                weatherIcon = it.current.condition.icon,
                humidity = it.current.humidity,
                uv = it.current.uv,
                feelsLike = it.current.feelLikeC,
                tempC = it.current.tempC
            )
        }

    suspend fun searchForLocation(query: String): List<LocationSearchResultEntity> =
        withContext(ioDispatcher) {
            val locationSearchResult = try {
                weatherService.searchCity(query = query)
            } catch (cancelException: CancellationException) {
                throw cancelException
            } catch (_: Exception) {
                emptyList<City>()
            }
            locationSearchResult
                .map { city -> weatherService.getWeatherInfoOfLocation(name = city.name) }
                .map { weatherResp ->
                    LocationSearchResultEntity(
                        name = weatherResp.location.name,
                        tempC = weatherResp.current.tempC,
                        iconUrl = weatherResp.current.condition.icon
                    )
                }
        }

    suspend fun selectLocation(name: String) = localSource.saveSelectedCity(name)

}