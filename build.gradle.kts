plugins {
    id("java")
    id("checkstyle")
    id("jacoco")
    id("info.solidsoft.pitest") version "1.15.0"
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

pitest {
    junit5PluginVersion.set("1.2.1")
    targetClasses.set(setOf("domain.*"))
    targetTests.set(setOf("domain.*"))
    threads.set(4)
    outputFormats.set(setOf("HTML"))
    timestampedReports.set(false)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.outputLocation = layout.buildDirectory.dir("reports/jacoco")
    }
}
