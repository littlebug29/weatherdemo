package com.khanhtruong.myweather.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.mutablePreferencesOf
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.quality.Strictness


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class LocationLocalSourceTest {

    @get:Rule
    val strictRule: MockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var locationLocalSource: LocationLocalSource
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    @Before
    fun setUp() {
        val testContext = ApplicationProvider.getApplicationContext<Context>()
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope.backgroundScope,
            produceFile = { testContext.preferencesDataStoreFile("locations") }
        )
        locationLocalSource = LocationLocalSource(dataStore)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.runTest {
            dataStore.edit { it.clear() }
        }
        testScope.cancel()
    }

    @Test
    fun testSaveSelectedLocation() = runTest {
        val testLocation = "Tokyo"
        locationLocalSource.saveSelectedCity(testLocation)
        assertEquals(testLocation, locationLocalSource.selectedLocationsFlow.first())
    }

    @Test
    fun testSaveSelectedLocationUpdated() = runTest {
        val testLocation = "Tokyo"
        val newLocation = "Ha Noi"

        locationLocalSource.saveSelectedCity(testLocation)
        locationLocalSource.saveSelectedCity(newLocation)
        val result = locationLocalSource.selectedLocationsFlow.first()
        assertEquals(newLocation, result)
    }
}