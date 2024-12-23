package com.khanhtruong.myweather.ui

sealed class ScreenState<out T> {
    data class Success<T>(val data: T): ScreenState<T>()

    data object Empty : ScreenState<Nothing>()
}