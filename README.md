# Magic Mongo [![Release](https://repo.srnyx.com/api/badge/latest/releases/xyz/srnyx/magic-mongo?color=006d82&name=Release)](https://repo.srnyx.com/#/releases/xyz/srnyx/magic-mongo) [![Snapshot](https://repo.srnyx.com/api/badge/latest/snapshots/xyz/srnyx/magic-mongo?color=006d82&name=Snapshot)](https://repo.srnyx.com/#/snapshots/xyz/srnyx/magic-mongo)

Common framework for srnyx's MongoDB management

### Wiki / Javadocs

- **Wiki:** [github.com/srnyx/magic-mongo/wiki](https://github.com/srnyx/magic-mongo/wiki)
- **Javadocs:** [repo.srnyx.com/javadoc/releases/xyz/srnyx/magic-mongo/latest](https://repo.srnyx.com/javadoc/releases/xyz/srnyx/magic-mongo/latest)

## Importing

You can import the library using [Reposilite](https://repo.srnyx.com/#/releases/xyz/srnyx/magic-mongo). Make sure to replace `VERSION` with the version you want.

- **Gradle Kotlin** (`build.gradle.kts`)**:**
```kotlin
// Required plugins
plugins { 
  java
  id("com.gradleup.shadow") version "9.4.2" // https://github.com/GradleUp/shadow/releases/latest
}
// Reposilite repository
repositories { 
  maven("https://repo.srnyx.com/releases/")
}
// Magic Mongo dependency declaration
dependencies {
  implementation("xyz.srnyx:magic-mongo:VERSION")
}
```
- **Gradle Groovy** (`build.gradle`)**:**
```groovy
// Required plugins
plugins {
  id 'java'
  id 'com.gradleup.shadow' version '9.4.2' // https://github.com/GradleUp/shadow/releases/latest
}
// Reposilite repository
repositories {
  maven { url = 'https://repo.srnyx.com/releases/' }
}
// Magic Mongo dependency declaration
dependencies {
  implementation 'xyz.srnyx:magic-mongo:VERSION'
}
```
* **Maven** (`pom.xml`)**:**
    * Shade plugin
  ```xml
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>3.4.1</version>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>shade</goal>
            </goals>
          </execution>
        </executions>
        <!-- Exclude META-INF to avoid conflicts (not sure if this is needed) -->
        <configuration>
          <filters>
            <filter>
              <artifact>xyz.srnyx:*</artifact>
              <excludes>
                <exclude>META-INF/*.MF</exclude>
              </excludes>
            </filter>
          </filters>
        </configuration>
      </plugin>
    </plugins>
  </build>
  ```
    * Reposilite repository
  ```xml
   <repositories>
        <repository>
            <id>srnyx</id>
            <url>https://repo.srnyx.com/releases/</url>
        </repository>
    </repositories>
  ```
    * Magic Mongo dependency declaration
  ```xml
    <dependencies>
        <dependency>
            <groupId>xyz.srnyx</groupId>
            <artifactId>magic-mongo</artifactId>
            <version>VERSION</version>
        </dependency>
    </dependencies>
  ```
