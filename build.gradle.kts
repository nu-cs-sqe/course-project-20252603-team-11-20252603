plugins {
    id("java")
    id("checkstyle")
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
