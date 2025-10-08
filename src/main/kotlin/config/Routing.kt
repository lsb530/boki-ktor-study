package com.boki.config

import com.boki.route.menuRoute
import com.boki.route.orderRoute
import com.boki.route.userRoute
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/api") {
            menuRoute()
            userRoute()
            orderRoute()
        }

        singlePageApplication {
            react("frontend")
        }
    }
}
