package com.training.project.service.model

import com.training.project.service.model.Currency.DOLLAR
import com.training.project.service.model.Currency.RUPEES
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProductEventTests {

    private lateinit var product: Product

    @BeforeEach
    private fun setUp() {
        product = Product("Mobile", Price(RUPEES, 100.0))
    }

    @Test
    fun `Check Product price set event was triggered`() {
        product.setPrice(DOLLAR, 50.0)
        assertThat(product.domainEvents()).isNotEmpty()
    }

    @Test
    fun `Check Product price set event was not triggered and there is no actual change`() {
        product.setPrice(RUPEES, 100.0)
        assertThat(product.domainEvents()).isEmpty()
    }

    @Test
    fun `Check Product price set event was triggered with right attributes`() {
        val oldPrice = Price(RUPEES, 100.0)
        product.setPrice(DOLLAR, 50.0)
        val expectedEvent = ProductPriceSet(product.id, product.price, oldPrice)
        assertThat(product.domainEvents().first()).isEqualTo(expectedEvent)
    }

    @Test
    fun `Check Product renamed event was triggered`() {
        product.rename("NewMobile")
        assertThat(product.domainEvents()).isNotEmpty()
    }

    @Test
    fun `Check Product price renamed event was not triggered and there is no actual change`() {
        product.rename("Mobile")
        assertThat(product.domainEvents()).isEmpty()
    }

    @Test
    fun `Check Product renamed event was triggered with right attributes`() {
        val oldName = "Mobile"
        product.rename("NewMobile")
        val expectedEvent = ProductRenamed(product.id, product.name, oldName)
        assertThat(product.domainEvents().first()).isEqualTo(expectedEvent)
    }

    @Test
    fun `Check Product price increased event was triggered`() {
        product.increasePrice(50.0)
        assertThat(product.domainEvents()).isNotEmpty()
    }

    @Test
    fun `Check Product price increased event was triggered with right attributes`() {
        val oldPrice = Price(RUPEES, 100.0)
        product.increasePrice(50.0)
        val expectedEvent = ProductPriceIncreased(product.id, product.price, oldPrice)
        assertThat(product.domainEvents().first()).isEqualTo(expectedEvent)
    }

    @Test
    fun `Check Product price decreased event was triggered`() {
        product.decreasePrice(50.0)
        assertThat(product.domainEvents()).isNotEmpty()
    }

    @Test
    fun `Check Product price decreased event was triggered with right attributes`() {
        val oldPrice = Price(RUPEES, 100.0)
        product.decreasePrice(50.0)
        val expectedEvent = ProductPriceDecreased(product.id, product.price, oldPrice)
        assertThat(product.domainEvents().first()).isEqualTo(expectedEvent)
    }
}