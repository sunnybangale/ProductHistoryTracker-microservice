package com.training.project.graphql

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.training.project.service.ProductService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import com.training.project.service.model.Currency as CurrencyModel
import com.training.project.service.model.Price as PriceModel
import com.training.project.service.model.Product as ProductModel

class MutationResolverTests {

    private lateinit var service: ProductService
    private lateinit var resolver: ProductMutationResolver

    private val priceModel = PriceModel(CurrencyModel.DOLLAR, 10.0)
    private val productModel = ProductModel("Coke", priceModel)

    @BeforeEach
    fun setUp() {
        service = mock()
        resolver = ProductMutationResolver(service)
    }

    @Test
    fun `Check registerProduct result wrapping`() {
        whenever(service.registerProduct(any(), any(), any())).thenReturn(productModel)
        resolver.registerProduct(RegisterProductInput("Coke", Currency.DOLLAR, 10.0)).apply {
            assertThat(this).isEqualTo(RegisterProductPayload(Product(productModel)))
        }
    }

    @Test
    fun `Check deregisterProduct result wrapping`() {
        whenever(service.product(any())).thenReturn(productModel)
        resolver.deregisterProduct(DeregisterProductInput(productModel.id)).apply {
            assertThat(this).isEqualTo(DeregisterProductPayload(Product(productModel)))
        }
    }

    @Test
    fun `Check deregisterProduct exception propagation`() {
        whenever(service.product(any())).thenThrow(NoSuchElementException())
        assertThrows<NoSuchElementException> { resolver.deregisterProduct(DeregisterProductInput(UUID.randomUUID()))}
    }

    @Test
    fun `Check Product increasePrice result wrapping`() {
        whenever(service.increaseProductPrice(any(), any())).thenReturn(productModel)
        resolver.increaseProductPrice(IncreaseProductPriceInput(productModel.id, 50.0)).apply {
            assertThat(this).isEqualTo(IncreaseProductPricePayload(Product(productModel)))
        }
    }

    @Test
    fun `Check Product decreasePrice result wrapping`() {
        whenever(service.decreaseProductPrice(any(), any())).thenReturn(productModel)
        resolver.decreaseProductPrice(DecreaseProductPriceInput(productModel.id, 50.0)).apply {
            assertThat(this).isEqualTo(DecreaseProductPricePayload(Product(productModel)))
        }
    }

    @Test
    fun `Check Product setPrice result wrapping`() {
        whenever(service.setProductPrice(any(), any(), any())).thenReturn(productModel)
        resolver.setProductPrice(SetProductPriceInput(productModel.id, Currency.RUPEES, 50.0)).apply {
            assertThat(this).isEqualTo(SetProductPricePayload(Product(productModel)))
        }
    }

    @Test
    fun `Check Product renameProduct result wrapping`() {
        whenever(service.renameProduct(any(), any())).thenReturn(productModel)
        resolver.renameProduct(RenameProductInput(productModel.id, "Fanta")).apply {
            assertThat(this).isEqualTo(RenameProductPayload(Product(productModel)))
        }
    }
}