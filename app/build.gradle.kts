plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.murek.appsocial"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.murek.appsocial"
        minSdk = 31
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
    buildFeatures {
        viewBinding = true
    }

}


dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.activity)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    dependencies {
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.activity:activity-ktx:1.7.2")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.cardview:cardview:1.0.0") // Usar CardView de AndroidX
        implementation("androidx.recyclerview:recyclerview:1.2.1") // Usar RecyclerView de AndroidX
        implementation("com.google.firebase:firebase-firestore:25.1.1")
        // Dependencias de testing
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
        // Glide para manejar imágenes
        implementation("com.github.bumptech.glide:glide:4.12.0")
        annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
        implementation("io.github.florent37:shapeofview:1.4.7")
    }
}