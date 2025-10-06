package com.boki.config

import com.boki.config.plugin.MyCallLogging
import io.ktor.server.application.Application
import io.ktor.server.application.install

fun Application.configureLogging() {
    install(MyCallLogging)
}