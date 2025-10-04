package com.boki

import com.boki.config.configureDatabase
import com.boki.config.configureRouting
import com.boki.config.configureSerialization
import com.boki.domain.CafeMenuTable
import com.boki.domain.repository.CafeMenuRepository
import com.boki.service.MenuService
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureDatabase()

    val cafeMenuRepository = CafeMenuRepository(CafeMenuTable)
    val menuService = MenuService(cafeMenuRepository)
    configureRouting(menuService)
}
