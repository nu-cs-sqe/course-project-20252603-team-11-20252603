plugins {
    id("java")
    id("application")
    id("checkstyle")
    id("jacoco")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "nu.csse.sqe"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.easymock:easymock:5.4.0")
}

application {
    mainClass.set("ui.Main")
}

javafx {
    version = "17.0.6"
    modules = listOf("javafx.controls", "javafx.graphics")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

tasks.compileJava {
    options.release = 11
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

configure<CheckstyleExtension> {
    toolVersion = "10.21.4"
    configFile = file("config/checkstyle/checkstyle.xml")
    configProperties = mapOf(
        "org.checkstyle.google.severity" to "error",
        "org.checkstyle.google.suppressionfilter.config"
            to file("config/checkstyle/suppressions.xml").absolutePath
    )
    isIgnoreFailures = false
    isShowViolations = true
    maxWarnings = 0
}

jacoco {
    toolVersion = "0.8.12"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.outputLocation = layout.buildDirectory.dir("reports/jacoco")
    }
}
