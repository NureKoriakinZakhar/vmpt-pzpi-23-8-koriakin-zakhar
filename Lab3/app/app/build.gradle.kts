plugins {
    alias(libs.plugins.android.application)

    // Hilt
    id("com.google.devtools.ksp") version "2.3.6"
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.koriakin.zakhar.lab"
    compileSdk {
        version = release(36)
    }

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.koriakin.zakhar.lab"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            multiDexEnabled = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Retrofit & Networking
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.google.gson)
    implementation(libs.okhttp.logging)

    // Hilt
    implementation("com.google.dagger:hilt-android:2.59.2")
    ksp("com.google.dagger:hilt-compiler:2.59.2")
}