package com.training.project.service.model

import javax.persistence.Embeddable

@Embeddable
data class Price(val currency: Currency, val amount: Double) {
    fun applyPercentage(percentage: Double) = Price(this.currency, applyPercentage(this.amount, percentage))

    private fun applyPercentage(amount: Double, percentage: Double) = amount * (1.0 + percentage / 100.0)
}

enum class Currency { DOLLAR, PESOS, RUPEES }
