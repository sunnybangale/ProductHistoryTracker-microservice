package com.training.project.graphql

import com.training.project.service.model.Price as PriceModel
import com.training.project.service.model.Product as ProductModel

data class Product(private val product: ProductModel) {
    val id = product.id
    val name = product.name
    val price = Price(product.price)
}

data class Price(private val price: PriceModel) {
    val currency = Currency.valueOf(price.currency.name)
    val amount: Float = price.amount.toFloat()
}

enum class Currency { DOLLAR, PESOS, RUPEES }
