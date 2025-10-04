package com.boki.config

import com.boki.domain.CafeMenuTable
import com.boki.domain.CafeOrderTable
import com.boki.domain.CafeUserTable
import com.boki.domain.repository.CafeMenuRepository
import com.boki.domain.repository.CafeOrderRepository
import com.boki.domain.repository.CafeUserRepository
import com.boki.service.MenuService
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val appModule = module {
    single { CafeMenuRepository(CafeMenuTable) }
    single { CafeUserRepository(CafeUserTable) }
    single { CafeOrderRepository(CafeOrderTable) }

    single { MenuService(get()) }
}

fun Application.configureDependencyInjection() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
}