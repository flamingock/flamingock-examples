
val mongodbVersion = "4.3.3"
val flamingockVersion = project.findProperty("flamingockVersion") as String

dependencies {
    implementation("io.flamingock:flamingock-core:$flamingockVersion")
    implementation("io.flamingock:mongodb-sync-v4-driver:$flamingockVersion")
    implementation("io.flamingock:mongodb-change-template:$flamingockVersion")
    implementation("org.mongodb:mongodb-driver-sync:$mongodbVersion")
    implementation("org.mongodb:mongodb-driver-core:$mongodbVersion")
    implementation("org.mongodb:bson:$mongodbVersion")
    implementation("org.slf4j:slf4j-simple:2.0.6")

    testImplementation("org.testcontainers:mongodb:1.18.3")
    testImplementation("org.testcontainers:junit-jupiter:1.18.3")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "io.flamingock.examples.mongodb.template.App"
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