package com.boki.service

import com.boki.config.AuthenticatedUser
import com.boki.config.AuthenticatedUser.Companion.SESSION_NAME
import com.boki.shared.dto.UserDto
import io.ktor.server.sessions.*

class LoginService(
    private val userService: UserService,
) {
    fun login(
        cafeUserLoginRequest: UserDto.LoginRequest,
        currentSession: CurrentSession,
    ) {
        checkNoSession(currentSession)

        val user = userService.getCafeUser(
            nickname = cafeUserLoginRequest.nickname,
            plainPassword = cafeUserLoginRequest.plainPassword
        )
        currentSession.set(
            AuthenticatedUser(user.id!!, user.roles)
        )
    }

    fun signup(
        cafeUserSignupRequest: UserDto.LoginRequest,
        currentSession: CurrentSession
    ) {
        checkNoSession(currentSession)

        val user = userService.createCustomer(
            nickname = cafeUserSignupRequest.nickname,
            plainPassword = cafeUserSignupRequest.plainPassword,
        )
        currentSession.set(
            AuthenticatedUser(user.id!!, user.roles)
        )
    }

    fun logout(currentSession: CurrentSession) {
        currentSession.clear(name = SESSION_NAME)
    }

    private fun checkNoSession(currentSession: CurrentSession) {
        val authenticatedUser: AuthenticatedUser? = currentSession.get<AuthenticatedUser>()
        if (authenticatedUser != null) {
            throw RuntimeException()
        }
    }
}