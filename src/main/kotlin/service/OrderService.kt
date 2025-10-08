package com.boki.service

import com.boki.config.AuthenticatedUser
import com.boki.domain.model.CafeOrder
import com.boki.domain.repository.CafeOrderRepository
import com.boki.shared.CafeException
import com.boki.shared.CafeOrderStatus
import com.boki.shared.CafeUserRole
import com.boki.shared.ErrorCode
import com.boki.shared.dto.OrderDto
import java.time.LocalDateTime
import java.util.*

class OrderService(
    private val menuService: MenuService,
    private val userService: UserService,
    private val cafeOrderRepository: CafeOrderRepository,
) {
    fun createOrder(
        request: OrderDto.CreateRequest,
        authenticatedUser: AuthenticatedUser,
    ): String {
        val menu = menuService.getMenu(request.menuId)
        val order = CafeOrder(
            orderCode = "OC${UUID.randomUUID()}",
            cafeMenuId = menu.id!!,
            cafeUserId = authenticatedUser.userId,
            price = menu.price,
            status = CafeOrderStatus.READY,
            orderedAt = LocalDateTime.now(),
        )
        cafeOrderRepository.create(order)
        return order.orderCode
    }

    fun getOrder(
        orderCode: String,
        authenticatedUser: AuthenticatedUser,
    ): OrderDto.DisplayResponse {
        val order = getOrderByCode(orderCode)

        checkOrderOwner(order, authenticatedUser)

        return OrderDto.DisplayResponse(
            orderCode = order.orderCode,
            menuName = menuService.getMenu(order.cafeMenuId).name,
            customerName = userService.getUser(order.cafeUserId).nickname,
            price = order.price,
            status = order.status,
            orderedAt = order.orderedAt,
            id = null // 명시적 null
        )
    }

    fun updateOrderStatus(
        orderCode: String,
        status: CafeOrderStatus,
        authenticatedUser: AuthenticatedUser,
    ) {
        val order = getOrderByCode(orderCode)
        
        checkOrderOwner(order, authenticatedUser)
        checkCustomerAction(authenticatedUser, status)
        
        order.update(status)
        cafeOrderRepository.update(order)
    }

    fun getOrders(): List<OrderDto.DisplayResponse> {
        return cafeOrderRepository.findByOrders()
    }

    fun getOrderStats(): List<OrderDto.StatsResponse> {
        // SQL
        return cafeOrderRepository.findOrderStats()
        /*
        // Kotlin Code
        val orders: List<CafeOrder> = cafeOrderRepository.findAll()
        return orders
            .groupBy { it.orderedAt.toLocalDate() }
            .map { (date, list) ->
                OrderDto.StatsResponse(
                    orderDate = date,
                    totalOrderCount = list.count().toLong(),
                    totalOrderPrice = list.sumOf { it.price }.toLong()
                )
            }.sortedByDescending { it.orderDate }
        */
    }

    private fun getOrderByCode(orderCode: String): CafeOrder {
        return cafeOrderRepository.findByCode(orderCode)
            ?: throw CafeException(ErrorCode.ORDER_NOT_FOUND)
    }

    private fun checkOrderOwner(order: CafeOrder, authenticatedUser: AuthenticatedUser) {
        if (authenticatedUser.isOnlyCustomer()) {
            if (order.cafeUserId != authenticatedUser.userId) {
                throw CafeException(ErrorCode.FORBIDDEN)
            }
        }
    }

    private fun checkCustomerAction(authenticatedUser: AuthenticatedUser, status: CafeOrderStatus) {
        // 고객은 취소만 가능(아래의 경우는 관리자까지 여기에 걸려버림)
        // if (authenticatedUser.userRoles.contains(CafeUserRole.CUSTOMER))

        // 고객은 오로지 취소만 가능
        if (authenticatedUser.userRoles == listOf(CafeUserRole.CUSTOMER)) {
            if (status != CafeOrderStatus.CANCEL) {
                throw CafeException(ErrorCode.FORBIDDEN)
            }
        }
    }
}