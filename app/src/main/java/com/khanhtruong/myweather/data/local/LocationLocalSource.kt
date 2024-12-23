package com.khanhtruong.myweather.data.local

import androidx.annotation.VisibleForTesting
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationLocalSource @Inject constructor(private val dataStore: DataStore<Preferences>) {
    val selectedLocationsFlow: Flow<String> = dataStore.data
        .mapNotNull { preferences ->
            preferences[KEY_SELECTED_LOCATIONS]
        }

    suspend fun saveSelectedCity(newSelectedLocation: String) {
        dataStore.edit { preferences ->
            preferences[KEY_SELECTED_LOCATIONS] = newSelectedLocation
        }
    }

    companion object {
        @VisibleForTesting
        val KEY_SELECTED_LOCATIONS = stringPreferencesKey("key_locations")
    }
}