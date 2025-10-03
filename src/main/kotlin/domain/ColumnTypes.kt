package com.boki.domain

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table

class EnumListColumnType<T : Enum<T>>(
    private val enumClass: Class<T>,
    private val varcharLength: Int,
) : ColumnType<List<T>>() {

    override fun sqlType(): String = "VARCHAR($varcharLength)"

    override fun valueFromDB(value: Any): List<T> = when (value) {
        is String -> {
            if (value.isBlank()) {
                emptyList()
            } else {
                value.split(',')
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .map { java.lang.Enum.valueOf(enumClass, it) }
            }
        }
        is List<*> -> {
            // DB 드라이버/버전에 따라 드물게 List<Enum<*>>로 들어올 수 있으므로 방어적 처리
            value.filter { it is Enum<*> && enumClass.isInstance(it) }
                .map { @Suppress("UNCHECKED_CAST") (it as T) }
        }
        else -> error("Unexpected value for EnumListColumnType: ${value::class}")
    }

    override fun notNullValueToDB(value: List<T>): Any =
        if (value.isEmpty()) "" else value.joinToString(",") { it.name }

    override fun nonNullValueToString(value: List<T>): String =
        "'${value.joinToString(",") { it.name }}'"
}

/** 일반 버전: Java/일반 함수에서도 사용 가능 (Class<T>를 직접 전달) */
fun <T : Enum<T>> Table.enumListColumn(
    name: String,
    enumClass: Class<T>,
    varcharLength: Int = 255
): Column<List<T>> =
    registerColumn(name, EnumListColumnType(enumClass, varcharLength))

/** reified 버전: 호출부 간결. Java에선 호출 불가. (재귀 방지를 위해 직접 registerColumn 호출) */
inline fun <reified T : Enum<T>> Table.enumList(
    name: String,
    varcharLength: Int = 255
): Column<List<T>> =
    registerColumn(name, EnumListColumnType(T::class.java, varcharLength))