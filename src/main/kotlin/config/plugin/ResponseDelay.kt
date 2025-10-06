package com.boki.config.plugin

import io.ktor.server.application.createRouteScopedPlugin

val ResponseDelayPlugin = createRouteScopedPlugin(name = "ResponseDelayPlugin") {
    onCall {
        Thread.sleep(500)
    }
}