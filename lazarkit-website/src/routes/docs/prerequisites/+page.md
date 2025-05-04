<script>
  async function getData() {
    try {
      const response = await fetch("https://mymaven.bylazar.com/api/maven/latest/version/releases/com/bylazar/ftcontrol");
      if (!response.ok) throw new Error("Failed to fetch");
      const text = await response.json();
      return text;
    } catch (err) {
      throw err;
    }
  }
</script>

# FTControl Setup Guide
This guide helps you integrate `ftcontrol` into your FTC Robot Controller.

## Clone FTC Robot Controller
Start by cloning the official FTC repository:

```bash
git clone https://github.com/FIRST-Tech-Challenge/FtcRobotController
```

## [Optional] Enable Kotlin Support

> You only need this if you want to use Kotlin instead of Java.

Edit TeamCode/build.gradle:

```groovy title="build.gradle"
//ADD THIS // [svp! ~~:3]
apply plugin: 'org.jetbrains.kotlin.android'
//

android {
    namespace = 'org.firstinspires.ftc.teamcode'

    //ADD THIS // [svp! ~~:11]
    kotlinOptions {
        jvmTarget = '1.8'
    }
    
    compileSdk 35

    defaultConfig {
        minSdk 24
    }
    //

    packagingOptions {
        jniLibs.useLegacyPackaging true
    }
}
```

And in the root build.gradle:

```groovy title="build.gradle"
buildscript {
    ...
    dependencies {
        //ADD THIS // [svp! ~~:3]
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0"
        //
    }
}
```

## Add FTControl to Your Project

> [if you aren't using Kotlin] Edit TeamCode/build.gradle:

```groovy title="build.gradle"
android {
    namespace = 'org.firstinspires.ftc.teamcode'

    //ADD THIS // [svp! ~~:7]
    compileSdk 35

    defaultConfig {
        minSdk 24
    }
    //

    packagingOptions {
        jniLibs.useLegacyPackaging true
    }
}
```

In your root build.gradle or settings.gradle, add this Maven repository:

```groovy title="build.gradle"
allprojects {
    repositories {
        mavenCentral()
        google()
        maven {// [svp! ~~:3]
            url = "https://mymaven.bylazar.com/releases"
        }
    }
}
```

Then in your module’s build.gradle:

```groovy title="build.gradle"
dependencies {
    implementation "com.bylazar:ftcontrol:VERSION"// [svp! ~~:1]
}
```

{#await getData()}
    Replace VERSION with the latest version: 0.0.0.
{:then res}
    Replace VERSION with the latest version: {res.version}.
{:catch err}
    Latest version fetch failed: {err.message}.
{/await}

Make sure you have the sdk-35 isntalled. Go to Settings > SDK Manager and install the appropriate version.

<video width="100%" controls>
  <source src="/docs/sdk.mp4" type="video/mp4">
  Your browser does not support the video tag.
</video>

Possible error: `Failed to find Platform SDK with path: platforms;android-35`

## Before Updating: Check the Changelog

Before updating to the latest version of ftcontrol, it's essential to check the changelog for any breaking changes, bug fixes, or new features.

[Visit Changelog](/docs/changelog)