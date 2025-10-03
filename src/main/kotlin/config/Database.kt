package com.boki.config

import com.boki.domain.CafeMenuTable
import com.boki.domain.CafeOrderTable
import com.boki.domain.CafeUserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.h2.tools.Server
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    configureH2()
    connectDatabase()
    initData()
}

private fun initData() {
    transaction {
        addLogger(StdOutSqlLogger)

        SchemaUtils.create(
            CafeMenuTable,
            CafeUserTable,
            CafeOrderTable,
        )
    }
}

private fun connectDatabase() {
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:h2:mem:cafedb"
        driverClassName = "org.h2.Driver"
        validate()
    }

    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)
}


fun Application.configureH2() {
    val h2Server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092")

    // ** deprecated after Ktor 3.x ** //
    // https://youtrack.jetbrains.com/issue/KTOR-7264?utm_source=chatgpt.com
    /*
    environment.monitor.subscribe(ApplicationStarted) { application ->
        h2Server.start()
        application.environment.log.info("H2 server started. ${h2Server.url}")
    }

    environment.monitor.subscribe(ApplicationStopped) { application ->
        h2Server.stop()
        application.environment.log.info("H2 server stopped. ${h2Server.url}")
    }
     */

    monitor.subscribe(ApplicationStarted) { app ->
        h2Server.start()
        app.log.info("H2 server started. ${h2Server.url}")
    }

    monitor.subscribe(ApplicationStopped) { app ->
        h2Server.stop()
        app.log.info("H2 server stopped. ${h2Server.url}")
    }
}