package com.khanhtruong.myweather.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.khanhtruong.myweather.MainViewModel
import com.khanhtruong.myweather.data.entity.LocationSearchResultEntity
import com.khanhtruong.myweather.ui.ScreenState

@Composable
fun MyWeatherScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    var shouldShowSearchResult by remember { mutableStateOf(false) }
    val searchResultState by viewModel.searchResultFlow.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 44.dp, start = 24.dp, end = 24.dp, bottom = 44.dp)
    ) {
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
            MainContent()
        }
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
            .height(46.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color(0xFFF2F2F2))
            .padding(horizontal = 16.dp)
    ) {
        TextField(
            value = searchText,
            onValueChange = {
                searchText = it
                onSearch(it)  // Trigger search dynamically as user types
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart),
            placeholder = {
                Text(text = placeholder, fontSize = 15.sp, color = Color.Gray)
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
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

}

@Composable
fun MainContent() {

}

@Preview
@Composable
fun PreviewSearchBox() {
    SearchBox("Enter location") { Log.d("SearchBox", "search $it") }
}