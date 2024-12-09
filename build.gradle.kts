import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

plugins {
    kotlin("jvm") version "1.9.22"
    id("java-library")
}

group = "io.flamingock"
version = "1.0-SNAPSHOT"


var passedAsParameter = false
val flamingockVersionAsParameter: String? = project.findProperty("flamingockVersion")?.toString()
val flamingockVersion: String =  if(flamingockVersionAsParameter != null) {
    passedAsParameter = true
    flamingockVersionAsParameter
} else {
    //using "release.latest" doesn't play nice with intellij
    val flamingockReleasedVersion = getFlamingockReleasedVersion()
    flamingockReleasedVersion
}
logger.lifecycle("Building with flamingock version${if(passedAsParameter)"[from parameter]" else ""}: $flamingockVersion")


allprojects {
    group = "io.flamingock"
    version = "1.0-SNAPSHOT"

    apply(plugin = "org.jetbrains.kotlin.jvm")
    extra["flamingockVersion"] = flamingockVersion

}


subprojects {
    apply(plugin = "java-library")
    repositories {
        mavenCentral()
        mavenLocal()
    }

    val implementation by configurations
    val testImplementation by configurations
    val testRuntimeOnly by configurations

    dependencies {


        implementation(kotlin("stdlib-jdk8"))
        implementation("org.slf4j", "slf4j-api", "2.0.6")

        testImplementation("org.slf4j:slf4j-simple:2.0.6")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
        testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
        testImplementation(kotlin("test"))

        testImplementation("org.mockito:mockito-core:4.11.0")
        testImplementation("org.mockito:mockito-junit-jupiter:4.11.0")
        testImplementation("org.mockito:mockito-inline:4.11.0")
    }
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        testLogging {
            events(
                    org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT,
            )
        }
    }


    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(8))
        }
    }
}

fun getFlamingockReleasedVersion(): String {
    val metadataUrl = "https://repo.maven.apache.org/maven2/io/flamingock/flamingock-core/maven-metadata.xml"
    try {
        val metadata = URL(metadataUrl).readText()
        val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val inputStream = metadata.byteInputStream()
        val document = documentBuilder.parse(inputStream)
        return document.getElementsByTagName("latest").item(0).textContent
    } catch (e: Exception) {
        throw RuntimeException("Cannot obtain Flamingock's latest version")
    }
}
