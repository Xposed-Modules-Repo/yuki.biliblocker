plugins {
    id("com.android.application")
}

android {
    namespace = "yuki.biliblocker"
    compileSdk = 36

    defaultConfig {
        applicationId = "yuki.biliblocker"
        minSdk = 21
        targetSdk = 36
        versionCode = 3
        versionName = "1.1.1"
    }

    signingConfigs {
        create("debugSign") {
            storeFile = file("${System.getProperty("java.io.tmpdir")}/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debugSign")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

tasks.register("copyApkToRoot") {
    doLast {
        val apkDir = file("build/outputs/apk/release")
        if (apkDir.exists()) {
            apkDir.listFiles()?.filter { it.name.endsWith(".apk") }?.forEach { apk ->
                copy {
                    from(apk)
                    into(rootProject.projectDir)
                    rename { "BiliBlocker.apk" }
                }
                logger.lifecycle("BiliBlocker.apk -> ${rootProject.projectDir}")
            }
        }
    }
}

project.afterEvaluate {
    tasks.matching { it.name == "assembleRelease" }.configureEach {
        finalizedBy(":app:copyApkToRoot")
    }
}

dependencies {
    compileOnly("de.robv.android.xposed:api:82")
    compileOnly("de.robv.android.xposed:api:82:sources")
}
