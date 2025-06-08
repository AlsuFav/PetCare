plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.safe.args)
}

android {
    namespace = "ru.fav.petcare.service"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    }
}

dependencies {

    implementation(project(path = ":core:presentation"))
    implementation(project(path = ":core:domain"))
    implementation(project(path = ":core:util"))
    implementation(project(path = ":core:navigation"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.shimmer)
    implementation(libs.glide)

    implementation(libs.lifecycle.viewmodel)

    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.viewbinding.property.delegate)
}