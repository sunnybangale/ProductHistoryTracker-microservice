package com.training.project.service.model

import com.training.project.service.model.Currency.DOLLAR
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PriceTests {

    @Test
    fun `Check Price instantiation`() {
        Price(DOLLAR, 10.0).apply {
            assertThat(currency).isEqualTo(DOLLAR)
            assertThat(amount).isEqualTo(10.0)
        }
    }

    @Test
    fun `Check Price increment`() {
        val price = Price(DOLLAR, 10.0)
        val incrementedPercentage = price.applyPercentage(10.0)
        assertThat(price).isNotEqualTo(incrementedPercentage)
        assertThat(incrementedPercentage.amount).isEqualTo(11.0)
    }

    @Test
    fun `Check Price decrement`() {
        val price = Price(DOLLAR, 10.0)
        val decrementedPercentage = price.applyPercentage(-10.0)
        assertThat(price).isNotEqualTo(decrementedPercentage)
        assertThat(decrementedPercentage.amount).isEqualTo(9.0)
    }
}
