package com.boki.domain.model

import com.boki.domain.BaseModel
import com.boki.shared.CafeMenuCategory
import kotlinx.serialization.Serializable

@Serializable
data class CafeMenu(
    val name: String,
    val price: Int,
    val category: CafeMenuCategory,
    val image: String,
    override var id: Long? = null,
): BaseModel
