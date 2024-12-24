package com.khanhtruong.myweather.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.khanhtruong.myweather.MainViewModel
import com.khanhtruong.myweather.R
import com.khanhtruong.myweather.data.entity.LocationSearchResultEntity
import com.khanhtruong.myweather.data.entity.WeatherEntity
import com.khanhtruong.myweather.ui.ScreenState
import com.khanhtruong.myweather.ui.theme.Gray20

@Composable
fun MyWeatherScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    var shouldShowSearchResult by remember { mutableStateOf(false) }
    val searchResultState by viewModel.searchResultFlow.collectAsState()
    val weatherState by viewModel.weatherInfoState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 44.dp, start = 24.dp, end = 24.dp, bottom = 44.dp)
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.systemBars))
        SearchBox(
            placeholder = "Search Location",
            onSearch = {
                viewModel.search(it)
                shouldShowSearchResult = it.isNotEmpty()
            }
        )
        if (shouldShowSearchResult) {
            SearchResultContent(
                searchResultState,
                onSelect = {
                    shouldShowSearchResult = false
                    viewModel.selectLocation(it)
                }
            )
        } else {
            MainContent(weatherState)
        }
        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.ime))
    }
}

@Composable
fun SearchBox(
    placeholder: String,
    onSearch: (String) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color(0xFFF2F2F2))
            .padding(horizontal = 14.dp)
    ) {
        TextField(
            value = searchText,
            onValueChange = {
                searchText = it
                onSearch(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart),
            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W400,
                    color = colorResource(R.color.silver_sand),
                    lineHeight = 22.sp
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = colorResource(R.color.silver_sand)
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
fun SearchResultContent(
    result: ScreenState<List<LocationSearchResultEntity>>,
    onSelect: (String) -> Unit
) {
    when (result) {
        ScreenState.Empty -> Spacer(modifier = Modifier.fillMaxSize())
        is ScreenState.Success -> SearchList(result.data, onSelect)
    }
}

@Composable
fun SearchList(
    searchResultList: List<LocationSearchResultEntity>,
    onItemClick: (String) -> Unit
) {
    LazyColumn {
        items(searchResultList) { searchResult ->
            WeatherResultItem(searchResult, onClick = onItemClick)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WeatherResultItem(
    searchResult: LocationSearchResultEntity,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF2F2F2))
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .clickable(enabled = true) {
                onClick(searchResult.name)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = searchResult.name,
                fontSize = 24.sp,
                lineHeight = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${searchResult.tempC}°",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        GlideImage(
            model = searchResult.iconUrl,
            contentDescription = "Weather Icon",
            modifier = Modifier.size(83.dp, 67.dp)
        )

    }
}

@Composable
fun MainContent(
    weatherState: ScreenState<WeatherEntity>
) {
    when (weatherState) {
        ScreenState.Empty -> EmptyWeatherState()
        is ScreenState.Success -> WeatherInfoScreen(weatherState.data)
    }
}

@Composable
fun EmptyWeatherState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No City Selected",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Please Search For A City",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun WeatherInfoScreen(weatherEntity: WeatherEntity) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Weather Icon
        GlideImage(
            model = weatherEntity.weatherIcon,
            modifier = Modifier.size(123.dp, 113.dp),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(24.dp))

        // City Name
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = weatherEntity.name,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 45.sp
            )
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(21.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Temperature
        Text(
            text = "${weatherEntity.tempC}°",
            fontSize = 70.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(35.dp))

        WeatherDetails(weatherEntity.humidity, weatherEntity.uv, weatherEntity.feelsLike)
    }
}

@Composable
fun WeatherDetails(
    humidity: Int,
    uv: Float,
    feelsLike: Float
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gray20, RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        WeatherInfo("Humidity", "$humidity%")
        WeatherInfo("UV", "$uv")
        WeatherInfo("Feels Like", "${feelsLike}°")
    }
}

@Composable
fun WeatherInfo(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}

@Preview
@Composable
fun PreviewSearchBox() {
    SearchBox("Enter location") { Log.d("SearchBox", "search $it") }
}