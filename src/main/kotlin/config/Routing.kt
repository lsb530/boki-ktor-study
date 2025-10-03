package com.boki.config

import com.boki.domain.model.CafeMenu
import com.boki.domain.repository.CafeMenuRepository
import com.boki.shared.CafeOrderStatus
import com.boki.shared.dto.OrderDto
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDateTime

fun Application.configureRouting(cafeMenuRepository: CafeMenuRepository) {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        route("/api") {
            get("/menus") {
                val list: List<CafeMenu> = cafeMenuRepository.findAll()
                call.respond(list)
            }

            post("/orders") {
                val request = call.receive<OrderDto.CreateRequest>()
                val selectedMenu = cafeMenuRepository.read(request.menuId)!!
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
        }
    }
}
