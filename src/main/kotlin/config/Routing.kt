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

        // 1번 방식 - 여기에 더 적당함
        singlePageApplication {
            useResources = true
            react("frontend")
        }

        // 2번 방식
        /*
        staticResources("/", "frontend") {
            default("index.html")
            preCompressed(CompressedFileType.GZIP)
        }
        */
    }
}
