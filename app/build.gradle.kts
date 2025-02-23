/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.7/userguide/building_java_projects.html in the Gradle documentation.
 */
import org.gradle.api.tasks.JavaExec


plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is used by the application.
    implementation(libs.guava)

    implementation("com.formdev:flatlaf:2.6")

    implementation("io.github.jason-lang:interpreter:3.2.0")

}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    // Define the main class for the application.
    mainClass = "blackjack.Blackjack"
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

// file("app/src").listFiles()?.filter { it.extension == "mas2j" }?.forEach { mas2jFile ->
//     println("Found MAS2J file: ${mas2jFile.name}")
//     task<JavaExec>("run${mas2jFile.nameWithoutExtension}MAS") {
//         group = "run"
//         classpath = sourceSets.getByName("main").runtimeClasspath
//         mainClass.set("jason.infra.centralised.RunCentralisedMAS")
//         args(mas2jFile.path)
//         standardInput = System.`in`
//         javaLauncher.set(javaToolchains.launcherFor(java.toolchain))
//     }
// }


val mas2jFiles = file("src").walkTopDown()
    .filter { it.extension == "mas2j" }
    .toList()

if (mas2jFiles.isEmpty()) {
    println("⚠️ Nessun file MAS2J trovato in app/src/main/jason!")
} else {
    mas2jFiles.forEach { mas2jFile ->
        tasks.register<JavaExec>("run${mas2jFile.nameWithoutExtension}MAS") {
            group = "run"
            description = "Esegue il MAS ${mas2jFile.name}"

            classpath = sourceSets["main"].runtimeClasspath
            mainClass.set("jason.infra.centralised.RunCentralisedMAS")
            args(mas2jFile.absolutePath)

            standardInput = System.`in`

            javaLauncher.set(javaToolchains.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(17))
            })
        }
    }
}
