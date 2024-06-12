plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.example.btl1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.btl1"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

}

dependencies {

    implementation ("androidx.room:room-runtime:2.6.1+")
    annotationProcessor ("androidx.room:room-compiler:2.6.1+")
    implementation ("androidx.recyclerview:recyclerview:1.3.2+")
    implementation ("androidx.recyclerview:recyclerview-selection:1.1.0+")
    implementation ("org.jsoup:jsoup:1.17.2+")
    implementation ("com.google.android.material:material:1.2.1+")
    implementation ("androidx.cardview:cardview:1.0.0+")
    implementation ("com.squareup.picasso:picasso:2.8+")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}