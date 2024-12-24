plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.khanhtruong.myweather"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.khanhtruong.myweather"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val apiKey = rootProject.properties.getValue("apiKey") as String
        buildConfigField("String", "BASE_URL", "\"https://api.weatherapi.com/v1/\"")
        buildConfigField("String", "API_KEY", apiKey)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.glide)

    // DI
    implementation(libs.hilt)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.compiler)

    // Network
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.converter)
    implementation(platform(libs.okhttp3.bom))
    implementation(libs.okhttp3)
    implementation(libs.okhttp3.logging.interceptor)

    // Database
    implementation(libs.androidx.datastore.preference)

    // Serializer
    implementation(libs.kotlin.serialization.json)

    testImplementation(libs.junit)
    testImplementation(libs.mockito)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.androidx.junit)
    testImplementation(libs.kotlin.coroutine.test)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.datastore.preference)
    testImplementation(libs.turbine)
    testImplementation(libs.robolectric)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}