import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

plugins {
    java
    application
//    Springboot plugins
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
}

repositories {
    mavenLocal()
    mavenCentral()
}

group = "io.flamingock"
version = "1.0-SNAPSHOT"

val flamingockVersion = flamingockVersion()

dependencies {
//    Flamingock Dependencies
    implementation(platform("io.flamingock:flamingock-ce-bom:$flamingockVersion"))
    implementation("io.flamingock:flamingock-ce-mongodb-springdata")
    implementation("io.flamingock:flamingock-springboot-integration")
    annotationProcessor("io.flamingock:flamingock-processor:$flamingockVersion")

//    Springboot dependency
    implementation("org.springframework.boot:spring-boot-starter-web")

//    Springdata for MongoDB dependency
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    testImplementation("org.testcontainers:mongodb:1.21.3")
    testImplementation("org.testcontainers:junit-jupiter:1.21.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

application {
    mainClass = "io.flamingock.examples.mongodb.springboot.springdata.MongodbSpringbootSpringdata"
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

// Get Flamingock version from parameter or last
fun flamingockVersion(): String {
    var passedAsParameter = false
    val flamingockVersionAsParameter: String? = project.findProperty("flamingockVersion")?.toString()
    val flamingockVersion: String =  if(flamingockVersionAsParameter != null) {
        passedAsParameter = true
        flamingockVersionAsParameter
    } else {
        //using "release.latest" doesn't play nice with intellij
        val metadataUrl = "https://repo.maven.apache.org/maven2/io/flamingock/flamingock-core/maven-metadata.xml"
        try {
            val metadata = URL(metadataUrl).readText()
            val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            val inputStream = metadata.byteInputStream()
            val document = documentBuilder.parse(inputStream)
            document.getElementsByTagName("latest").item(0).textContent
        } catch (e: Exception) {
            throw RuntimeException("Cannot obtain Flamingock's latest version")
        }
    }
    logger.lifecycle("Building with flamingock version${if(passedAsParameter)"[from parameter]" else ""}: $flamingockVersion")
    return flamingockVersion
}
