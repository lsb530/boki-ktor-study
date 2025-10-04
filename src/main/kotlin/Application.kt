package com.boki

import com.boki.config.configureDatabase
import com.boki.config.configureDependencyInjection
import com.boki.config.configureRouting
import com.boki.config.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDatabase()
    configureSerialization()
    configureDependencyInjection()
    configureRouting()
}
