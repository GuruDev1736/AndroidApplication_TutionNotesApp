plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.guruprasad.tutionnotesaplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.guruprasad.tutionnotesaplication"
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

    viewBinding{
        enable = true
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.navigation:navigation-fragment:2.7.2")
    implementation("androidx.navigation:navigation-ui:2.7.2")
    implementation("com.google.firebase:firebase-database-ktx:20.2.2")
    implementation("com.google.firebase:firebase-storage-ktx:20.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7") {
        version { require("1.8.20") }
    }
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8") {
        version { require("1.8.20") }
    }

    implementation("com.airbnb.android:lottie:6.1.0")
    implementation ("com.karumi:dexter:6.2.3")
    implementation ("com.github.GrenderG:Toasty:1.5.2")
    implementation ("com.firebaseui:firebase-ui-database:8.0.2")

    // pdf viewer
    implementation ("com.github.afreakyelf:Pdf-Viewer:v1.0.7")



}