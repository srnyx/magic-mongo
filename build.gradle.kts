import xyz.srnyx.gradlegalaxy.data.pom.DeveloperData
import xyz.srnyx.gradlegalaxy.data.pom.LicenseData
import xyz.srnyx.gradlegalaxy.utility.setupJava
import xyz.srnyx.gradlegalaxy.utility.setupPublishing


plugins {
    java
    id("xyz.srnyx.gradle-galaxy") version "1.1.2"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

setupJava("xyz.srnyx", "1.2.2", "Common framework for srnyx's MongoDB management", JavaVersion.VERSION_1_8)

repositories.mavenCentral()
dependencies {
    implementation("org.mongodb", "mongodb-driver-sync", "5.0.0")
    compileOnly("org.jetbrains" , "annotations", "24.1.0")
}

setupPublishing(
    artifactId = "magic-mongo",
    url = "https://magic-mongo.srnyx.com",
    licenses = listOf(LicenseData.MIT),
    developers = listOf(DeveloperData.srnyx))
