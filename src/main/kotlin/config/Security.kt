package com.boki.config

import com.boki.config.AuthenticatedUser.Companion.CUSTOMER_REQUIRED
import com.boki.shared.CafeUserRole
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.session
import io.ktor.server.response.respondText

fun Application.configureSecurity() {
    install(Authentication) {
        session<AuthenticatedUser>(CUSTOMER_REQUIRED) {
            validate { session: AuthenticatedUser ->
                session.takeIf { it.userRoles.contains(CafeUserRole.CUSTOMER) }
            }
            challenge {
                call.respondText(
                    text = "only for customer",
                    status = HttpStatusCode.Forbidden
                )
            }
        }
    }
}