# Steeps for GraalVM support

## 1. Add graalVM dependency and annotation processor
```xml
<dependencyManagement>
   <dependencies>
      <dependency>
         <groupId>io.flamingock</groupId>
         <artifactId>flamingock-community-bom</artifactId>
         <version>${flamingockVersion}</version>
         <type>pom</type>
         <scope>import</scope>
      </dependency>
   </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>io.flamingock</groupId>
        <artifactId>flamingock-community</artifactId>
    </dependency>

    <dependency>
        <groupId>io.flamingock</groupId>
        <artifactId>flamingock-graalvm</artifactId>
    </dependency>
</dependencies>

```

## 2. Add annotation processor
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.11.0</version>
            <configuration>
                <annotationProcessorPaths>
                    <path>
                        <groupId>io.flamingock</groupId>
                        <artifactId>flamingock-processor</artifactId>
                        <version>${flamingock.version}</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## 3. Add the configuration file `resource-config.json` in your project root:
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

## 4. Build application
```shell
./mvnw clean package
```

## 5. Create Native Image

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

## 6. Run native image
```shell
./graalvm-1.0-SNAPSHOT
```

