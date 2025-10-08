package com.boki.service

import com.boki.domain.model.CafeMenu
import com.boki.domain.repository.CafeMenuRepository

class MenuService(
    private val cafeMenuRepository: CafeMenuRepository,
) {
    fun findAll(): List<CafeMenu> {
        return cafeMenuRepository.findAll()
    }

    fun getMenu(id: Long): CafeMenu {
        return cafeMenuRepository.read(id)
            ?: throw IllegalArgumentException("Menu not found")
    }

    fun createMenu(menu: CafeMenu): CafeMenu {
        return cafeMenuRepository.create(menu)
    }

    fun updateMenu(menu: CafeMenu): CafeMenu {
        return cafeMenuRepository.update(menu)
    }

    fun deleteMenu(id: Long) {
        cafeMenuRepository.delete(id)
    }
}