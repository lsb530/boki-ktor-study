package com.boki.config

import com.boki.domain.model.CafeMenu
import com.boki.service.MenuService
import com.boki.shared.CafeOrderStatus
import com.boki.shared.dto.OrderDto
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject
import java.time.LocalDateTime

fun Application.configureRouting() {
    val menuService: MenuService by inject<MenuService>()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("/api") {
            get("/menus") {
                val list: List<CafeMenu> = menuService.findAll()
                call.respond(list)
            }

            post("/orders") {
                val request = call.receive<OrderDto.CreateRequest>()
                val selectedMenu = menuService.getMenu(request.menuId)
                val newOrder = OrderDto.DisplayResponse(
                    orderCode = "order-code1",
                    menuName = selectedMenu.name,
                    customerName = "홍길동",
                    price = selectedMenu.price,
                    status = CafeOrderStatus.READY,
                    orderedAt = LocalDateTime.now(),
                    id = 1,
                )
                call.respond(newOrder)
            }

            get("/orders/{orderCode}") {
                val orderCode = call.parameters["orderCode"]!!
                val findOrder = OrderDto.DisplayResponse(
                    orderCode = orderCode,
                    menuName = "아이스라떼",
                    customerName = "홍길동",
                    price = 1000,
                    status = CafeOrderStatus.READY,
                    orderedAt = LocalDateTime.now(),
                    id = 1,
                )
                call.respond(findOrder)
            }

            get("/me") {
                val user = call.sessions.get<AuthenticatedUser>()
                    ?: AuthenticatedUser.none()
                call.respond(user)
            }

            post("/login") {

            }

            post("/signup") {

            }

            post("/logout") {

            }
        }
    }
}
