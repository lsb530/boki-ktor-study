package com.boki

import com.boki.config.configureDatabase
import com.boki.config.configureDependencyInjection
import com.boki.config.configureErrorHandling
import com.boki.config.configureHttp
import com.boki.config.configureLogging
import com.boki.config.configureRouting
import com.boki.config.configureSecurity
import com.boki.config.configureSerialization
import com.boki.config.configureSession
import com.boki.shared.applicationEnv
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    this.log.info("current env: ${applicationEnv()}")

    configureDatabase()
    configureDependencyInjection()
    configureHttp()
    configureSession()
    configureSecurity()
    configureSerialization()
    configureRouting()
    configureErrorHandling()
    configureLogging()
}
