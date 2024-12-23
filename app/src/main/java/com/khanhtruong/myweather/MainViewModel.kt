package com.khanhtruong.myweather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khanhtruong.myweather.data.WeatherRepository
import com.khanhtruong.myweather.data.entity.LocationSearchResultEntity
import com.khanhtruong.myweather.data.entity.WeatherEntity
import com.khanhtruong.myweather.ui.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val mutableWeatherInfoState =
        MutableStateFlow<ScreenState<WeatherEntity>>(ScreenState.Empty)
    val weatherInfoState = mutableWeatherInfoState.asStateFlow()

    private val mutableLocationSearchQueryFlow = MutableStateFlow("")
    private val mutableSearchResultFlow =
        MutableStateFlow<ScreenState<List<LocationSearchResultEntity>>>(ScreenState.Empty)
    val searchResultFlow = mutableSearchResultFlow.asStateFlow()

    init {
        viewModelScope.launch {
            weatherRepository.currentWeatherFlow
                .collect {
                    mutableWeatherInfoState.value = ScreenState.Success(it)
                }
            mutableLocationSearchQueryFlow
                .debounce(200)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .flatMapConcat {
                    flowOf(weatherRepository.searchForLocation(it))
                }
                .onEach { mutableSearchResultFlow.value = ScreenState.Success(it) }
        }
    }

    fun search(query: String) {
        mutableLocationSearchQueryFlow.value = query
    }

    fun selectLocation(name: String) = viewModelScope.launch {
        weatherRepository.selectLocation(name)
    }
}