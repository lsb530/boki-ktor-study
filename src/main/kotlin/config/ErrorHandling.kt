package com.boki.config

import com.boki.shared.CafeException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText

fun Application.configureErrorHandling() {
    install(StatusPages) {
        exception<CafeException> { call, cause ->
            call.respondText(
                text = cause.message ?: "Bad Request",
                status = cause.errorCode.httpStatusCode
            )
        }
        exception<Throwable> { call, cause ->
            call.respondText(
                text = "500: ${cause.message}",
                status = HttpStatusCode.InternalServerError
            )
        }
    }
}