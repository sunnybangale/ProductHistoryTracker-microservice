package com.training.project.service

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import com.training.project.service.model.Currency.DOLLAR
import com.training.project.service.model.Currency.RUPEES
import com.training.project.service.model.Price
import com.training.project.service.model.Product
import com.training.project.service.model.ProductPriceSet
import com.training.project.service.model.ProductRegistered
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.event.EventListener

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ProductServiceEventsIntegrationTests(@Autowired val service: ProductService) {

    @MockBean
    lateinit var eventHandler: TestEventHandler

    private lateinit var product: Product

    @BeforeEach
    fun setUp() {
        product = service.registerProduct("Mobile", RUPEES, 100.0)
    }

    @Test
    fun `Check Product registration event was triggered`() {
        val argumentCaptor = argumentCaptor<ProductRegistered>()
        verify(eventHandler).handleProductRegistered(argumentCaptor.capture())

        val expected = ProductRegistered(product.id, "Mobile", Price(RUPEES, 100.0))
        assertThat(argumentCaptor.lastValue).isEqualTo(expected)
    }

    @Test
    fun `Check Product price set event was triggered with right attributes`() {
        service.setProductPrice(product.id, DOLLAR, 50.0)

        val argumentCaptor = argumentCaptor<ProductPriceSet>()
        verify(eventHandler).handleProductPriceSet(argumentCaptor.capture())

        val expected = ProductPriceSet(product.id, Price(DOLLAR, 50.0), Price(RUPEES, 100.0))
        assertThat(argumentCaptor.lastValue).isEqualTo(expected)
    }
}

interface TestEventHandler {
    @EventListener
    fun handleProductRegistered(event: ProductRegistered)

    @EventListener
    fun handleProductPriceSet(event: ProductPriceSet)
}