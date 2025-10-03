package com.boki.domain.model

import com.boki.shared.CafeOrderStatus
import com.boki.domain.BaseModel
import com.boki.shared.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class CafeOrder(
    val orderCode: String,
    val cafeMenuId: Long,
    val cafeUserId: Long,
    val price: Int,
    var status: CafeOrderStatus,
    @Serializable(with = LocalDateTimeSerializer::class)
    val orderedAt: LocalDateTime,
    override var id: Long? = null,
) : BaseModel
