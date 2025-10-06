package com.boki.config

import com.boki.shared.CafeUserRole
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.sessions.SessionStorageMemory
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import kotlinx.serialization.Serializable

fun Application.configureSession() {
    install(Sessions) {
        cookie<AuthenticatedUser>("CU_SESSION_ID", SessionStorageMemory()) {
            cookie.path = "/"
        }
    }
}

@Serializable
data class AuthenticatedUser(
    val userId: Long,
    val userRoles: List<CafeUserRole>
)