plugins {
    java
    id("org.springframework.boot") version "2.7.12"
    id("io.spring.dependency-management") version "1.1.0"
}

val flamingockVersion = project.findProperty("flamingockVersion") as String


dependencies {

    implementation("io.flamingock:flamingock-springboot-v2-runner:$flamingockVersion")
    implementation("io.flamingock:mongodb-springdata-v3-driver:$flamingockVersion")

    // Mongock dependency is only included to provide example data for demonstrating how the import works.
    // It is not required for production use.
    implementation("io.mongock:mongock-standalone:5.5.0")
    implementation("io.mongock:mongodb-sync-v4-driver:5.5.0")

    implementation("org.slf4j:slf4j-simple:2.0.6")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    testImplementation("org.testcontainers:mongodb:1.18.3")

    testImplementation("org.testcontainers:junit-jupiter:1.18.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test")


}
