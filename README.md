# WeatherDemo

**WeatherDemo** is a demo application built with **Kotlin** and **Jetpack Compose**.

## Prerequisites

Before you begin, ensure you have the following installed:

- **Android Studio Flamingo** or later
- **Android SDK 33**
- **Kotlin 2.0.0** or later
- **Gradle 8.3** or later

## Getting Started

1. **Clone the repository:**

   ```bash
   git clone https://github.com/littlebug29/weatherdemo.git
   ```
2. **Open the project in Android Studio:**

  - Launch Android Studio.
  - Select Open an existing project.
  - Navigate to the cloned weatherdemo directory and click OK.
3. **Build the project:**

  - Click on Build in the top menu.
  - Select Make Project.


4. **Run the application:**

  - Click on Run in the top menu.
  - Select Run 'app'.
  - Choose an emulator or connected device to deploy the app.

## Dependencies
The project uses the following dependencies:

  - Jetpack Compose – For building the UI
  - Kotlin Coroutines – For asynchronous programming
  - Retrofit – For network requests
  - Hilt – For dependency injection
  - Data Store - For persist local storage
  - OkHttp3 - A Http Client to config network client
  - Glide - For loading image from network
These dependencies are managed via Gradle and will be downloaded automatically during the build process.

## Configuration
The app fetches weather data from a weather API. To configure the API:

1. Obtain an API key:

  - Register on the weather API provider's website to get an API key. (WeatherAPI.com).
2. Add the API key to the project:

  - Open the `gradle.properties` file in the root directory.

  - Replace `apiKey` with the API key obtained in step 1.
