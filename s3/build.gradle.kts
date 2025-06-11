plugins {
    java
    application
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

repositories {
    mavenLocal()
    mavenCentral() // Fallback for other dependencies
}

group = "io.flamingock"
version = "1.0-SNAPSHOT"

val flamingockVersion = "0.0.34-beta"
val awsSdkVersion = "2.25.28"

dependencies {
    // Import the flamingock BOM
    implementation(platform("io.flamingock:flamingock-ce-bom:$flamingockVersion"))

    // AWS SDK dependencies with explicit versions
    implementation("software.amazon.awssdk:dynamodb-enhanced:$awsSdkVersion")
    implementation("software.amazon.awssdk:url-connection-client:$awsSdkVersion")

    // Other dependencies
    implementation("org.slf4j:slf4j-api:2.0.6")
    implementation("org.slf4j:slf4j-simple:2.0.6")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.20")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

application {
    mainClass = "io.flamingock.examples.dynamodb.standalone.CommunityStandaloneDynamoDBApp"
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

// Optional task to verify dependency versions
tasks.register("printDependencyVersions") {
    doLast {
        configurations.compileClasspath.get().resolvedConfiguration.lenientConfiguration.allModuleDependencies.forEach {
            if (it.moduleGroup == "io.flamingock") {
                println("${it.moduleGroup}:${it.moduleName}:${it.moduleVersion}")
            }
        }
    }
}