plugins {
    java
}

val flamingockVersion = project.findProperty("flamingockVersion") as String
val jacksonVersion = "2.16.0"

dependencies {
    implementation("io.flamingock:flamingock-core:$flamingockVersion")
    implementation("io.flamingock:sql-cloud-transactioner:$flamingockVersion")
    implementation("io.flamingock:sql-template:$flamingockVersion")

    implementation("com.mysql:mysql-connector-j:8.2.0")
    implementation("org.slf4j:slf4j-simple:2.0.6")
    implementation("org.apache.httpcomponents:httpclient:4.5.14")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("commons-logging:commons-logging:1.2")

}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "io.flamingock.examples.mysql.standalone.MysqlStandaloneApplication"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(sourceSets.main.get().output)
    dependsOn(configurations.compileClasspath)
    from({
        configurations.compileClasspath.get().filter {
            it.name.endsWith("jar")
        }.map { zipTree(it) }
    })
}

