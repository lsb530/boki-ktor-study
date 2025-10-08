package com.boki.route

import com.boki.domain.model.CafeMenu
import com.boki.service.MenuService
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.menuRoute() {
    val menuService by inject<MenuService>()

    get("/menus") {
        val list: List<CafeMenu> = menuService.findAll()
        call.respond(list)
    }
}