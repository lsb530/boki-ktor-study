package com.boki.domain.model

import com.boki.domain.BaseModel
import com.boki.shared.CafeUserRole
import kotlinx.serialization.Serializable

@Serializable
data class CafeUser(
    val nickname: String,
    val password: String,
    val roles: List<CafeUserRole>,
    override var id: Long? = null,
) : BaseModel