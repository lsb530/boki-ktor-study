package com.boki.config

import com.boki.shared.CafeUserRole
import io.ktor.server.application.*
import io.ktor.server.sessions.*
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
) {
    // null object pattern
    companion object {
        fun none(): AuthenticatedUser = AuthenticatedUser(0, listOf())
    }
}