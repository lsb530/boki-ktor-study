import org.gradle.kotlin.dsl.implementation

val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val h2_version: String by project
val hikaricp_version: String by project
val koin_version: String by project
val jbcrypt_version: String by project

plugins {
    kotlin("jvm") version "2.2.20"
    id("io.ktor.plugin") version "3.3.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.2.20"
}

group = "com.boki"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"

//    val isDevelopment: Boolean = project.ext.has("development")
//    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-content-negotiation")   // Content Negotiation
    implementation("io.ktor:ktor-serialization-kotlinx-json")   // Serialization
    implementation("io.ktor:ktor-server-netty")                 // Netty Server
    implementation("io.ktor:ktor-server-sessions")              // Session
    implementation("io.ktor:ktor-server-double-receive-jvm")    // Parse Body multiple times
    implementation("io.ktor:ktor-server-auth-jvm")              // Authentication
    implementation("io.ktor:ktor-server-status-pages")          // Global Exception Handler

    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

    // Exposed for Java Time
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")

    // DB: H2
    implementation("com.h2database:h2:$h2_version")

    // DB Config(Connection Pool)
    implementation("com.zaxxer:HikariCP:${hikaricp_version}")

    // Koin
    implementation("io.insert-koin:koin-ktor:${koin_version}")
    implementation("io.insert-koin:koin-logger-slf4j:${koin_version}")

    // Bcrypt
    implementation("org.mindrot:jbcrypt:${jbcrypt_version}")

    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
