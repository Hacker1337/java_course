import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}


allprojects {
    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }
        withType<Test> {
            useJUnitPlatform()
            testLogging {
                events = setOf(
                        TestLogEvent.PASSED,
                        TestLogEvent.FAILED,
                        TestLogEvent.SKIPPED,
                        TestLogEvent.STANDARD_OUT,
                        TestLogEvent.STANDARD_ERROR,
                )
            }
        }
    }
}
