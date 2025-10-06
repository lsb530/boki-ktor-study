package com.boki

import com.boki.config.configureDatabase
import com.boki.config.configureDependencyInjection
import com.boki.config.configureHttp
import com.boki.config.configureLogging
import com.boki.config.configureRouting
import com.boki.config.configureSerialization
import com.boki.config.configureSession
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDatabase()
    configureDependencyInjection()
    configureHttp()
    configureSession()
    configureSerialization()
    configureRouting()
    configureLogging()
}
