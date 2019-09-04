package com.training.project.graphql

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.training.project.service.ProductService
import com.training.project.service.model.Currency
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.NoSuchElementException
import java.util.UUID.randomUUID
import com.training.project.service.model.Price as PriceModel
import com.training.project.service.model.Product as ProductModel

class QueryResolverTests {

    private lateinit var service: ProductService
    private lateinit var resolver: ProductQueryResolver

    private val priceModel = PriceModel(Currency.DOLLAR, 10.0)
    private val productModel = ProductModel("Coke", priceModel)

    @BeforeEach
    fun setUp() {
        service = mock()
        resolver = ProductQueryResolver(service)
    }

    @Test
    fun `Check Product retrieval`() {
        whenever(service.product(any())).thenReturn(productModel)
        resolver.product(randomUUID()).apply {
            assertThat(id).isEqualTo(productModel.id)
        }
    }

    @Test
    fun `Check Product retrieval throws exception when its empty`() {
        whenever(service.product(any())).thenThrow(NoSuchElementException())
        assertThrows<NoSuchElementException> { resolver.product(randomUUID()) }
    }

    @Test
    fun `Check Products retrieval`() {
        whenever(service.products()).thenReturn(listOf(productModel))
        resolver.products().apply {
            assertThat(this).isNotEmpty
        }
    }
}