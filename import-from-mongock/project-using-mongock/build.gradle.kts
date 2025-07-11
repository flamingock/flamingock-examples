import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

plugins {
    java
    application
}

repositories {
    mavenLocal()
    mavenCentral()
}

group = "io.flamingock"
version = "1.0-SNAPSHOT"


val mongodbVersion = "5.2.0"

dependencies {

//    Mongock
    implementation(platform("io.mongock:mongock-bom:5.5.0"))
    implementation("io.mongock:mongock-standalone")
    implementation("io.mongock:mongodb-sync-v4-driver")



//    MongoDB dependencies
    implementation("org.mongodb:mongodb-driver-sync:$mongodbVersion")
    implementation("org.mongodb:mongodb-driver-core:$mongodbVersion")
    implementation("org.mongodb:bson:$mongodbVersion")

//    Others dependencies needed for this example
    implementation("org.slf4j:slf4j-simple:2.0.6")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")

    testImplementation("org.testcontainers:mongodb:1.18.3")
    testImplementation("org.testcontainers:junit-jupiter:1.18.3")

}

application {
    mainClass = "com.yourapp.YourApplication"
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    systemProperty("org.slf4j.simpleLogger.logFile", "System.out")
    testLogging {
        events(
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT,
        )
    }
}

