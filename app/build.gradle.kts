plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.safe.args)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.gradle.secrets)
}

android {
    namespace = "ru.fav.petcare.app"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.fav.petcare"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = rootProject.extra.get("versionCode") as Int
        versionName = rootProject.extra.get("versionName") as String

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(path = ":core:presentation"))
    implementation(project(path = ":core:data"))
    implementation(project(path = ":core:domain"))
    implementation(project(path = ":core:navigation"))
    implementation(project(path = ":core:notification"))
    implementation(project(path = ":core:network"))
    implementation(project(path = ":core:util"))

    implementation(project(path = ":feature:splash"))
    implementation(project(path = ":feature:authorization"))
    implementation(project(path = ":feature:registration"))
    implementation(project(path = ":feature:home"))
    implementation(project(path = ":feature:service"))
    implementation(project(path = ":feature:profile"))
    implementation(project(path = ":feature:safety"))
    implementation(project(path = ":feature:pet:all"))
    implementation(project(path = ":feature:pet:details"))
    implementation(project(path = ":feature:pet:add"))
    implementation(project(path = ":feature:appointment:all"))
    implementation(project(path = ":feature:appointment:add:pet"))
    implementation(project(path = ":feature:appointment:add:service"))
    implementation(project(path = ":feature:appointment:add:timeslot"))
    implementation(project(path = ":feature:appointment:add:confirm"))
    implementation(project(path = ":feature:appointment:details"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.fragment)
    implementation(libs.lifecycle.viewmodel)

    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.viewbinding.property.delegate)
    implementation(libs.yandex.maps.mobile)

    // Firebase
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.remote.config)
}