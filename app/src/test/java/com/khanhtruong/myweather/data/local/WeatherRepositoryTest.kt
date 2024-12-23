package com.khanhtruong.myweather.data.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.khanhtruong.myweather.data.WeatherRepository
import com.khanhtruong.myweather.data.entity.LocationSearchResultEntity
import com.khanhtruong.myweather.data.remote.WeatherService
import com.khanhtruong.myweather.data.remote.dto.City
import com.khanhtruong.myweather.data.remote.dto.LocationDto
import com.khanhtruong.myweather.data.remote.dto.Weather
import com.khanhtruong.myweather.data.remote.dto.WeatherCondition
import com.khanhtruong.myweather.data.remote.dto.WeatherResponse
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class WeatherRepositoryTest {
    private lateinit var localSource: LocationLocalSource
    private lateinit var weatherService: WeatherService
    private lateinit var weatherRepository: WeatherRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        localSource = mock()
        weatherService = mock()
        weatherRepository = WeatherRepository(localSource, weatherService, testDispatcher)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testSearchForLocation() = runTest {
        val query = "Test"
        val city = City(id = 0, name = "Test City")
        val weatherResponse = WeatherResponse(
            location = LocationDto(name = city.name),
            current = Weather(
                condition = WeatherCondition(condition = "Test", icon = "icon_url"),
                tempC = 20.0f,
                humidity = 50,
                uv = 5.0f,
                feelLikeC = 25.0f
            )
        )
        whenever(weatherService.searchCity(query = query)).thenReturn(listOf(city))
        whenever(weatherService.getWeatherInfoOfLocation(name = city.name)).thenReturn(weatherResponse)

        val result = weatherRepository.searchForLocation(query)

        assertEquals(
            listOf(
                LocationSearchResultEntity(
                    name = city.name,
                    tempC = 20.0f,
                    iconUrl = "icon_url"
                )
            ), result
        )
    }

    @Test
    fun testSelectLocation() = runTest {
        val cityName = "Test City"
        weatherRepository.selectLocation(cityName)
        verify(localSource).saveSelectedCity(cityName)
    }
}