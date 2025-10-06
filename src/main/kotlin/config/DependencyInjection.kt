package com.boki.config

import com.boki.domain.CafeMenuTable
import com.boki.domain.CafeOrderTable
import com.boki.domain.CafeUserTable
import com.boki.domain.repository.CafeMenuRepository
import com.boki.domain.repository.CafeOrderRepository
import com.boki.domain.repository.CafeUserRepository
import com.boki.service.LoginService
import com.boki.service.MenuService
import com.boki.service.UserService
import com.boki.shared.BCryptPasswordEncoder
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val appModule = module {
    single { BCryptPasswordEncoder() }
    single { CafeMenuRepository(CafeMenuTable) }
    single { CafeUserRepository(CafeUserTable) }
    single { CafeOrderRepository(CafeOrderTable) }

    single { MenuService(get()) }
    single { UserService(get(), get()) }
    single { LoginService(get()) }
}

fun Application.configureDependencyInjection() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
}