package com.khanhtruong.myweather.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.khanhtruong.myweather.data.entity.LocationSearchResultEntity
import com.khanhtruong.myweather.ui.ScreenState

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
