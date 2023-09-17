plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("de.mannodermaus.android-junit5") version "1.9.3.0"
}

android {
    namespace = "com.mohsen.nobka"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.mohsen.nobka"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "BASE_URL", properties.getValue("remote.url").toString())
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // Allow references to generated code
    kapt {
        correctErrorTypes = true
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.dagger:hilt-android:2.44")
    implementation("androidx.test:runner:1.5.2")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("com.squareup.okhttp3:logging-interceptor:3.9.0")

    val room_version = "2.5.2"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    val fragment_version = "1.5.7"
    implementation("androidx.fragment:fragment-ktx:$fragment_version")

    val junit5_version = "5.9.3"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit5_version")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit5_version")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junit5_version")

    val mock_version = "1.13.7"
    testImplementation("io.mockk:mockk:${mock_version}")
    testImplementation("io.mockk:mockk-android:${mock_version}")
    testImplementation("io.mockk:mockk-agent:${mock_version}")

    val turbine_version = "1.0.0"
    testImplementation ("app.cash.turbine:turbine:$turbine_version")

    val coroutines_version = "1.7.3"
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version")
}