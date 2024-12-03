plugins {
    java
    id("org.springframework.boot") version "2.7.12"
    id("io.spring.dependency-management") version "1.1.0"
}
val flamingockVersion = project.findProperty("flamingockVersion") as String

dependencies {

    implementation("com.mysql:mysql-connector-j:8.2.0")
    implementation("io.flamingock:sql-springboot-template:$flamingockVersion")
    implementation("io.flamingock:flamingock-springboot-v2-runner:$flamingockVersion")

    implementation("org.slf4j:slf4j-simple:2.0.6")

    //TODO remove once cloud is available
    implementation(project(":local-drivers:mongodb:mongodb-springdata-v3-driver"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
}

