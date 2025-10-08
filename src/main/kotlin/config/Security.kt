package com.boki.config

import com.boki.config.AuthenticatedUser.Companion.ADMINISTER_REQUIRED
import com.boki.config.AuthenticatedUser.Companion.CUSTOMER_REQUIRED
import com.boki.config.AuthenticatedUser.Companion.USER_REQUIRED
import com.boki.shared.CafeUserRole
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*

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
        session<AuthenticatedUser>(ADMINISTER_REQUIRED) {
            validate { session: AuthenticatedUser ->
                session.takeIf { it.userRoles.contains(CafeUserRole.ADMINISTER) }
            }
            challenge {
                call.respondText(
                    text = "only for administer",
                    status = HttpStatusCode.Forbidden
                )
            }
        }
        session<AuthenticatedUser>(USER_REQUIRED) {
            validate { session: AuthenticatedUser ->
                session.takeIf { it.userRoles.isNotEmpty() }
            }
            challenge {
                call.respondText(
                    text = "only for user",
                    status = HttpStatusCode.Forbidden
                )
            }
        }
    }
}

fun ApplicationCall.authenticatedUser(): AuthenticatedUser = authentication.principal<AuthenticatedUser>()!!