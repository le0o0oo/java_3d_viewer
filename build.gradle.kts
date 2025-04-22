plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
}

group = "org.leo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "5.10.2"
val jmeVersion = "3.7.0-stable"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(22)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    //mainModule.set("org.leo.tridimensional_viewer")
    mainClass.set("org.leo.tridimensional_viewer.Main")

}

javafx {
    version = "22.0.1"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation("org.controlsfx:controlsfx:11.2.1")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")

    implementation("org.jmonkeyengine:jme3-core:${jmeVersion}")
    implementation("org.jmonkeyengine:jme3-desktop:${jmeVersion}")
    implementation("org.jmonkeyengine:jme3-lwjgl3:${jmeVersion}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    forceMerge("jme3-core", "jme3-desktop", "jme3-lwjgl3") // Treat them as part of unnamed module
    addOptions("--bind-services")
    launcher {
        name = "app"
    }
}
