# Steeps for GraalVM support with Gradle

## 1. Add flamingock dependencies
```kotlin
implementation(platform("io.flamingock:flamingock-community-bom:$flamingockVersion"))
implementation("io.flamingock:flamingock-community")
implementation("io.flamingock:flamingock-graalvm")
```

## 2. Add flamingock annotation processor
```kotlin
annotationProcessor("io.flamingock:flamingock-processor:$flamingockVersion")
```

## 3. Add plugin manager to `settings.gradle.kts`
```kotlin
pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
    }
}
```

## 4. Add the configuration file `resource-config.json` in your project root:
   With the following content:
```json
{
  "resources": {
    "includes": [
      {
        "pattern": "META-INF/flamingock/metadata.json"
      }
    ],
  },
}
```
   GraalVM will automatically detect and use this configuration during native image generation.


## 5. Build application
```shell
./gradlew clean build
```


## 6. Create Native Image

### Native image build parameters
- `--no-fallback`: Ensures the build fails if native image generation isn't possible, rather than creating a fallback JAR. This is important for catching configuration issues early.

- `--features=io.flamingock.graalvm.RegistrationFeature` – Enables Flamingock’s Native Image integration (performs required class & resource registrations). Append additional Graal Feature classes, comma‑separated, to extend Native Image configuration for your own components.

- `-H:+ReportExceptionStackTraces`: Enables detailed stack traces in the native image. This is crucial for debugging issues in the native binary.

- `--initialize-at-build-time`: – **Optional**. Build‑time init for listed classes/packages (freeze static state; faster start; avoids early reflection/I/O). Flamingock does not require specific entries. Use only if a library benefits (e.g., logging). Example: --initialize-at-build-time=org.slf4j.impl,org.slf4j.simple. Omit if unsure.

Here's a minimal setup to build the native image:


```shell
 native-image \
  --no-fallback \
  --features=io.flamingock.graalvm.RegistrationFeature \
  -H:ResourceConfigurationFiles=resource-config.json \
  -H:+ReportExceptionStackTraces \
  --initialize-at-build-time=org.slf4j.simple \
  -jar build/libs/graalvm-0.0.1-SNAPSHOT.jar
```

## 7. Run native image
```shell
./graalvm-0.0.1-SNAPSHOT
```

