package com.training.project.service

import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.training.project.service.implementation.ProductServiceImpl
import com.training.project.service.model.Currency.DOLLAR
import com.training.project.service.model.Price
import com.training.project.service.model.Product
import com.training.project.service.model.ProductDeregistered
import com.training.project.service.model.ProductRegistered
import com.training.project.service.model.ProductRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationEventPublisher
import java.util.Optional

class ProductServiceEventsTests {

    private lateinit var service: ProductService

    private val repository: ProductRepository = mock()
    private val publisher: ApplicationEventPublisher = mock()

    private val product = Product("Camera", Price(DOLLAR, 100.0))

    @BeforeEach
    fun setUp() {
        service = ProductServiceImpl(repository, publisher)
    }

    @Test
    fun `Check Product registration event`() {

        whenever(repository.save(any<Product>())).thenReturn(product)
        whenever(repository.findById(any())).thenReturn(Optional.of(product))

        service.registerProduct("Camera", DOLLAR, 100.0)

        val expectedEvent = ProductRegistered(product.id, product.name, product.price)
        assertThat(argumentCaptor<ProductRegistered>().getIt()).isEqualTo(expectedEvent)
    }

    @Test
    fun `Check Product deregistration event`() {
        whenever(repository.findById(any())).thenReturn(Optional.of(product))

        service.deregisterProduct(product.id)

        val expectedEvent = ProductDeregistered(product.id)
        assertThat(argumentCaptor<ProductDeregistered>().getIt()).isEqualTo(expectedEvent)
    }

    private inline fun <reified T : Any> KArgumentCaptor<T>.getIt() = verify(publisher).publishEvent(capture()).let { lastValue }
}