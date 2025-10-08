package com.boki.route

import com.boki.config.AuthenticatedUser.Companion.ADMINISTER_REQUIRED
import com.boki.config.AuthenticatedUser.Companion.CUSTOMER_REQUIRED
import com.boki.domain.model.CafeMenu
import com.boki.service.MenuService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.menuRoute() {
    val menuService by inject<MenuService>()

    get("/menus") {
        val list: List<CafeMenu> = menuService.findAll()
        call.respond(list)
    }

    authenticate(ADMINISTER_REQUIRED) {
        post("/menus") {
            val menu = call.receive<CafeMenu>()
            val createdMenu = menuService.createMenu(menu)
            call.respond(createdMenu)
        }

        put("/menus") {
            val menu = call.receive<CafeMenu>()
            val updatedMenu = menuService.updateMenu(menu)
            call.respond(updatedMenu)
        }

        delete("/menus/{id}") {
            val id = call.parameters["id"]?.toLong()!!
            menuService.deleteMenu(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}