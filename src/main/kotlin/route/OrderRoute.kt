package com.boki.route

import com.boki.config.AuthenticatedUser.Companion.CUSTOMER_REQUIRED
import com.boki.service.MenuService
import com.boki.shared.CafeOrderStatus
import com.boki.shared.dto.OrderDto
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.time.LocalDateTime

fun Route.orderRoute() {
    val menuService by inject<MenuService>()

    authenticate(CUSTOMER_REQUIRED) {
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
            call.respond(newOrder.orderCode)
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
    }
}