package com.training.project.graphql


import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import com.training.project.service.model.Currency as CurrencyModel
import com.training.project.service.model.Price as PriceModel
import com.training.project.service.model.Product as ProductModel

class ModelTests {

    private val priceModel = PriceModel(CurrencyModel.DOLLAR, 10.0)
    private val productModel = ProductModel("Docker", priceModel)

    @Test
    fun `Check Price`() {
        Price(priceModel).apply {
            assertThat(currency).isEqualTo(Currency.DOLLAR)
            assertThat(amount).isEqualTo(10.0.toFloat())
        }
    }

    @Test
    fun `Check Product`() {
        Product(productModel).apply {
            assertThat(id).isEqualTo(productModel.id)
            assertThat(name).isEqualTo("Docker")
            assertThat(price).isEqualTo(Price(priceModel))
        }
    }

    @Test
    fun `Checking constants as per code coverage`() {
        assertThat(Currency.RUPEES).isNotNull()
        assertThat(Currency.DOLLAR).isNotNull()
        assertThat(Currency.PESOS).isNotNull()
    }
}