import xyz.srnyx.gradlegalaxy.data.config.JavaSetupConfig
import xyz.srnyx.gradlegalaxy.data.config.publishingSimpleConfig
import xyz.srnyx.gradlegalaxy.data.pom.DeveloperData
import xyz.srnyx.gradlegalaxy.data.pom.LicenseData
import xyz.srnyx.gradlegalaxy.enums.Repository
import xyz.srnyx.gradlegalaxy.enums.repository
import xyz.srnyx.gradlegalaxy.utility.setupJava
import xyz.srnyx.gradlegalaxy.utility.setupPublishingEnv


plugins {
    java
    `java-library`
    id("xyz.srnyx.gradle-galaxy") version "2.1.0"
    id("com.gradleup.shadow") version "8.3.9"
}

setupJava(JavaSetupConfig(
    group = "xyz.srnyx",
    version = "2.0.1",
    description = "Common framework for srnyx's MongoDB management",
    javaVersion = JavaVersion.VERSION_1_8))

repository(Repository.MAVEN_CENTRAL)
dependencies {
    api("org.mongodb:mongodb-driver-sync:5.6.4")
    compileOnly("org.jetbrains:annotations:26.1.0")
}

setupPublishingEnv(publishingSimpleConfig(
    artifactId = "magic-mongo",
    url = "https://github.com/srnyx/magic-mongo",
    licenses = listOf(LicenseData.MIT),
    developers = listOf(DeveloperData.srnyx)))
