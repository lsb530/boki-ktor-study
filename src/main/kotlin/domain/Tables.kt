package com.boki.domain

import com.boki.shared.CafeMenuCategory
import com.boki.shared.CafeOrderStatus
import com.boki.shared.CafeUserRole
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object CafeMenuTable : LongIdTable(name = "cafe_menu") {
    val name = varchar("menu_name", length = 50)
    val price = integer("price")

    /// enumeration은 enum을 문자가 아닌 숫자로 저장하는 형태
//    val category = enumeration("category", CafeMenuCategory::class)

    /// 아래 2개는 같은 방식
//    val category = enumerationByName("category", 10, CafeMenuCategory::class)
    val category = enumerationByName<CafeMenuCategory>("category", length = 10)
    val image = text("image")
}

object CafeUserTable : LongIdTable(name = "cafe_user") {
    val nickname = varchar("nickname", length = 50)
    val password = varchar("password", length = 100)

    //    val roles = enumListColumn("roles", CafeUserRole::class.java, varcharLength = 20)
    val roles = enumList<CafeUserRole>("roles", varcharLength = 20)
}

object CafeOrderTable : LongIdTable(name = "cafe_order") {
    val orderCode = varchar("order_code", length = 50)
    val cafeUserId = reference("cafe_user_id", CafeUserTable)
    val cafeMenuId = reference("cafe_menu_id", CafeMenuTable)
    val price = integer("price")
    val status = enumerationByName<CafeOrderStatus>("status", length = 10)
    val orderedAt = datetime("ordered_at")
}