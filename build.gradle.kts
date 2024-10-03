import xyz.srnyx.gradlegalaxy.data.pom.DeveloperData
import xyz.srnyx.gradlegalaxy.data.pom.LicenseData
import xyz.srnyx.gradlegalaxy.enums.Repository
import xyz.srnyx.gradlegalaxy.enums.repository
import xyz.srnyx.gradlegalaxy.utility.setupJava
import xyz.srnyx.gradlegalaxy.utility.setupPublishing


plugins {
    java
    id("xyz.srnyx.gradle-galaxy") version "1.3.2"
    id("com.gradleup.shadow") version "8.3.3"
}

setupJava("xyz.srnyx", "2.0.0", "Common framework for srnyx's MongoDB management", JavaVersion.VERSION_1_8)

repository(Repository.MAVEN_CENTRAL)
dependencies {
    implementation("org.mongodb", "mongodb-driver-sync", "5.2.0")
    compileOnly("org.jetbrains" , "annotations", "25.0.0")
}

setupPublishing(
    artifactId = "magic-mongo",
    url = "https://magic-mongo.srnyx.com",
    licenses = listOf(LicenseData.MIT),
    developers = listOf(DeveloperData.srnyx))
