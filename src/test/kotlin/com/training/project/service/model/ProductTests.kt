package com.training.project.service.model

import com.training.project.service.model.Currency.DOLLAR
import com.training.project.service.model.Currency.RUPEES
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ProductTests {

    private lateinit var product: Product

    @BeforeEach
    fun setUp() {
        product = Product("Laptop", Price(DOLLAR, 100.0))
    }

    @Test
    fun `Check Product instantiation`() {
        Product().apply {
            assertThat(name).isEmpty()
            assertThat(price).isEqualTo(Price(DOLLAR, 0.0))
        }
    }

    @Test
    fun `Check rename a Product`() {
        Product().apply {
            rename("NEW_NAME")
            assertThat(name).isEqualTo("NEW_NAME")
        }
    }

    @Test
    fun `Check Product cannot be renamed to empty string`() {
        Product().apply {
            assertThrows<IllegalArgumentException> { rename("") }
        }
    }

    @Test
    fun `Check Product price increase percentage is greater than zero`() {
        product.apply {
            assertThrows<IllegalArgumentException> { increasePrice(0.0) }
        }
    }

    @Test
    fun `Check Product price increased`() {
        product.apply {
            increasePrice(50.0)
            assertThat(price).isEqualTo(Price(DOLLAR, 150.0))
        }
    }

    @Test
    fun `Check Product price decrease percentage is greater than zero`() {
        product.apply {
            assertThrows<IllegalArgumentException> { decreasePrice(0.0) }
        }
    }

    @Test
    fun `Check Product price decreased`() {
        product.apply {
            decreasePrice(50.0)
            assertThat(price).isEqualTo(Price(DOLLAR, 50.0))
        }
    }

    @Test
    fun `Check Product set price is greater than zero`() {
        product.apply {
            assertThrows<IllegalArgumentException> { setPrice(RUPEES, 0.0) }
        }
    }

    @Test
    fun `Check Product set price`() {
        product.apply {
            setPrice(RUPEES, 50.0)
            assertThat(price).isEqualTo(Price(RUPEES, 50.0))
        }
    }
}
