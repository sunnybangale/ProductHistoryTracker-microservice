package com.training.project.service

import com.training.project.service.model.Currency
import com.training.project.service.model.Product
import java.util.UUID

interface ProductService {
    fun product(id: UUID): Product
    fun products(): List<Product>
    fun registerProduct(name: String, currency: Currency, amount: Double): Product
    fun deregisterProduct(id: UUID)
    fun renameProduct(id: UUID, name: String): Product
    fun setProductPrice(id: UUID, currency: Currency, amount: Double): Product
    fun increaseProductPrice(id: UUID, percentage: Double): Product
    fun decreaseProductPrice(id: UUID, percentage: Double): Product
}