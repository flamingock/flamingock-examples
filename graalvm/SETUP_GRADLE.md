# Steeps for GraalVM support with Gradle

1. Add flamingock dependencies 
```kotlin
implementation("io.flamingock:mongodb-sync-v4-driver:$flamingockVersion")
implementation("io.flamingock:flamingock-core:$flamingockVersion")
implementation("io.flamingock:flamingock-graalvm:$flamingockVersion")
```

2. Add flamingock annotation processor
```kotlin
annotationProcessor("io.flamingock:flamingock-core:$flamingockVersion")
```

3. Add plugin manager to `settings.gradle.kts`
```kotlin
pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
    }
}
```

4. Add the configuration file `resource-config.json` to the following path:
   ```
   src/main/resources/META-INF/native-image/${GROUP_ID}/${ARTIFACT_ID}/
   ```
   With the following content:
```json
{
  "resources": {
    "includes": [
      {
        "pattern": "META-INF/flamingock-metadata.json",
      }
    ],
  },
}
```
   GraalVM will automatically detect and use this configuration during native image generation.


5. Build application
```shell
./gradlew clean build
```

6. Create Native Image

### Native Image Build Parameters
- `--no-fallback`: Ensures the build fails if native image generation isn't possible, rather than creating a fallback JAR. This is important for catching configuration issues early.

- `--features=io.flamingock.graalvm.RegistrationFeature`: Registers Flamingock's custom feature for native image generation. This feature handles the registration of necessary classes and resources.

- `-H:+ReportExceptionStackTraces`: Enables detailed stack traces in the native image. This is crucial for debugging issues in the native binary.

- `--initialize-at-build-time`: Specifies classes to be initialized during build time rather than at runtime. This improves startup time and reduces runtime initialization complexity.

Here's a minimal setup to build the native image:


```shell
 native-image \
  --no-fallback \
  --features=io.flamingock.graalvm.RegistrationFeature \
  -H:+ReportExceptionStackTraces \
  --initialize-at-build-time=org.slf4j,org.slf4j.impl,org.slf4j.simple \
  -jar build/libs/graalvm-0.0.1-SNAPSHOT.jar
```

7. Run native image
```shell
./flamingock-graalvm--1.0-SNAPSHOT
```

