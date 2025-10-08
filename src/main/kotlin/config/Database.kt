package com.boki.config

import com.boki.domain.CafeMenuTable
import com.boki.domain.CafeOrderTable
import com.boki.domain.CafeUserTable
import com.boki.shared.dummyMenuQueryList
import com.boki.shared.dummyUserQueryList
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

private fun Application.configureH2() {
    val tcpPort = environment.config.property("ktor.database.tcpPort").getString()

    val h2Server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", tcpPort)

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

private fun Application.connectDatabase() {
    val jdbcUrl = environment.config.property("ktor.database.jdbcUrl").getString()
    val dbDriver = environment.config.property("ktor.database.driver").getString()

    val config = HikariConfig().apply {
        this.jdbcUrl = jdbcUrl
        this.driverClassName = dbDriver
        validate()
    }

    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)
}


private fun initData() {
    transaction {
        addLogger(StdOutSqlLogger)

        SchemaUtils.create(
            CafeMenuTable,
            CafeUserTable,
            CafeOrderTable,
        )

        execInBatch(dummyUserQueryList)
        execInBatch(dummyMenuQueryList)
    }
}
