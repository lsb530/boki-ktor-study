package com.boki.config

import com.boki.config.plugin.ResponseDelayPlugin
import io.ktor.server.application.Application
import io.ktor.server.application.install

import io.ktor.server.plugins.doublereceive.*

fun Application.configureHttp() {
    install(DoubleReceive)
    install(ResponseDelayPlugin)
}