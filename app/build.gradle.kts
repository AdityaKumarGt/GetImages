plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.getimages"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.getimages"
        minSdk = 21
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    //To blur the background
    implementation("jp.wasabeef:glide-transformations:4.3.0")


    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //To zoom the image
    implementation("com.jsibbold:zoomage:1.3.1")

    //for using Room Database
    implementation("androidx.room:room-runtime:2.6.1") // Replace with the latest version
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")

    //LiveData
    implementation ("androidx.lifecycle:lifecycle-livedata:2.7.0")





}