package com.khanhtruong.myweather

import app.cash.turbine.test
import com.khanhtruong.myweather.data.WeatherRepository
import com.khanhtruong.myweather.data.entity.LocationSearchResultEntity
import com.khanhtruong.myweather.data.entity.WeatherEntity
import com.khanhtruong.myweather.ui.ScreenState
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val weatherRepository: WeatherRepository = mock()
    private lateinit var viewModel: MainViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val weatherFlow = MutableSharedFlow<WeatherEntity>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        whenever(weatherRepository.currentWeatherFlow).thenReturn(weatherFlow)
        viewModel = MainViewModel(weatherRepository)
    }

    @Test
    fun testInitialWeatherDataCollection() = runTest {
        val weatherEntity = WeatherEntity(
            name = "New York",
            weatherIcon = "sunny.png",
            tempC = 25.0f,
            humidity = 60,
            uv = 5.0f,
            feelsLike = 27.0f
        )
        weatherFlow.emit(weatherEntity)

        viewModel.weatherInfoState.test {
            assertEquals(ScreenState.Success(weatherEntity), awaitItem())
        }
    }

    @Test
    fun testLocationSearchEmitsResults() = runTest {
        val searchResults = listOf(
            LocationSearchResultEntity(
                name = "New York",
                tempC = 23.0f,
                iconUrl = "cloudy.png"
            )
        )
        whenever(weatherRepository.searchForLocation("New")).thenReturn(searchResults)

        viewModel.search("New")
        advanceTimeBy(300) // Simulate debounce time

        viewModel.searchResultFlow.test {
            assertEquals(ScreenState.Success(searchResults), awaitItem())
        }
    }

    @Test
    fun testEmptySearchDoesNotTriggerFlow() = runTest {
        viewModel.search("")
        advanceTimeBy(300)

        viewModel.searchResultFlow.test {
            assertEquals(ScreenState.Empty, awaitItem())
            expectNoEvents()
        }
    }

    @Test
    fun testLocationSelection() = runTest {
        viewModel.selectLocation("Paris")
        advanceUntilIdle()
        verify(weatherRepository).selectLocation("Paris")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
