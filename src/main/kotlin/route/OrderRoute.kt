package com.boki.route

import com.boki.config.AuthenticatedUser
import com.boki.config.AuthenticatedUser.Companion.CUSTOMER_REQUIRED
import com.boki.config.AuthenticatedUser.Companion.USER_REQUIRED
import com.boki.config.authenticatedUser
import com.boki.service.OrderService
import com.boki.shared.dto.OrderDto
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.orderRoute() {
    val orderService by inject<OrderService>()

    authenticate(CUSTOMER_REQUIRED) {
        post("/orders") {
            val request = call.receive<OrderDto.CreateRequest>()
            val orderCode: String = orderService.createOrder(request, call.authenticatedUser())
            call.respond<String>(orderCode)
        }
        put("/orders/{orderCode}/status") {
            val orderCode = call.parameters["orderCode"]!!
            val status = call.receive<OrderDto.UpdateStatusRequest>().status
            orderService.updateOrderStatus(orderCode, status, call.authenticatedUser())
            call.respond(HttpStatusCode.OK)
        }
    }

    authenticate(USER_REQUIRED) {
        get("/orders/{orderCode}") {
            val orderCode = call.parameters["orderCode"]!!
            val order: OrderDto.DisplayResponse = orderService.getOrder(orderCode, call.authenticatedUser())
            call.respond<OrderDto.DisplayResponse>(order)
        }
    }

    authenticate(AuthenticatedUser.ADMINISTER_REQUIRED) {
        get("/orders") {
            val orders: List<OrderDto.DisplayResponse> = orderService.getOrders()
            call.respond(orders)
        }
    }
}