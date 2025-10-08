package com.boki.shared.dto

import com.boki.shared.CafeOrderStatus
import com.boki.shared.LocalDateSerializer
import com.boki.shared.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

class OrderDto {

    @Serializable
    data class CreateRequest(val menuId: Long)

    @Serializable
    data class DisplayResponse(
        val orderCode: String,
        val menuName: String,
        val customerName: String,
        val price: Int,
        var status: CafeOrderStatus,

        @Serializable(with = LocalDateTimeSerializer::class)
        val orderedAt: LocalDateTime,
        var id: Long? = null,
    )

    @Serializable
    data class UpdateStatusRequest(
        val status: CafeOrderStatus
    )

    @Serializable
    data class StatsResponse(
        @Serializable(with = LocalDateSerializer::class)
        val orderDate: LocalDate,
        val totalOrderCount: Long,
        val totalOrderPrice: Long,
    )
}