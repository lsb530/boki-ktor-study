package com.boki.config

import com.boki.domain.CafeMenuTable
import com.boki.domain.CafeOrderTable
import com.boki.domain.CafeUserTable
import com.boki.domain.model.CafeOrder
import com.boki.shared.CafeOrderStatus
import com.boki.shared.dummyMenuQueryList
import com.boki.shared.dummyUserQueryList
import com.boki.shared.getPropertyBoolean
import com.boki.shared.getPropertyString
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.h2.tools.Server
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random

fun Application.configureDatabase() {
    configureH2()
    connectDatabase()

    if (getPropertyBoolean("db.initData", false)) {
        initData()
    }
}

private fun Application.configureH2() {
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

private fun Application.connectDatabase() {
    val config = HikariConfig().apply {
        this.jdbcUrl = getPropertyString("db.jdbcUrl")
        this.driverClassName = getPropertyString("db.driverClassName")
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
        batchInsertOrder()
    }
}

private fun batchInsertOrder(): List<ResultRow> {
    val menuPairs = CafeMenuTable.selectAll()
        .toList()
        .map { it[CafeMenuTable.id].value to it[CafeMenuTable.price] }

    // batch insert for dsl
    val iterator =
        (1..300).map { id ->
            val (menuId, price) = menuPairs.random()
            CafeOrder(
                orderCode = "OC${UUID.randomUUID()}",
                cafeUserId = 1L,
                cafeMenuId = menuId,
                price = price,
                status = CafeOrderStatus.READY,
                orderedAt = LocalDateTime.now().minusDays(Random.nextLong(10))
            )
        }

    return CafeOrderTable.batchInsert(
        iterator,
        shouldReturnGeneratedValues = false,
        body = {
            this[CafeOrderTable.orderCode] = it.orderCode
            this[CafeOrderTable.cafeMenuId] = it.cafeMenuId
            this[CafeOrderTable.cafeUserId] = it.cafeUserId
            this[CafeOrderTable.price] = it.price
            this[CafeOrderTable.status] = it.status
            this[CafeOrderTable.orderedAt] = it.orderedAt
        }
    )
}