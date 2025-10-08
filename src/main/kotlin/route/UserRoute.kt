package com.boki.route

import com.boki.config.AuthenticatedUser
import com.boki.service.LoginService
import com.boki.shared.dto.UserDto
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject

fun Route.userRoute() {
    val loginService by inject<LoginService>()

    get("/me") {
        val user = call.sessions.get<AuthenticatedUser>()
            ?: AuthenticatedUser.none()
        call.respond(user)
    }

    post("/login") {
        val user = call.receive<UserDto.LoginRequest>()
        loginService.login(user, call.sessions)
        call.respond(HttpStatusCode.OK)
    }

    post("/signup") {
        val user = call.receive<UserDto.LoginRequest>()
        loginService.signup(user, call.sessions)
        call.respond(HttpStatusCode.OK)
    }

    post("/logout") {
        loginService.logout(call.sessions)
        call.respond(HttpStatusCode.OK)
    }
}