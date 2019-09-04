package com.training.project.service.model

import com.google.common.base.Preconditions.checkArgument
import com.training.project.service.infrastructure.AggregateRoot
import com.training.project.service.model.Currency.DOLLAR
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Product(
        var name: String = "",
        var price: Price = Price(DOLLAR, 0.0),
        @Id @GeneratedValue
        val id: UUID = UUID.randomUUID()
) : AggregateRoot<ProductEvent>() {

    fun rename(newName: String) {
        checkArgument(newName.isNotEmpty())
        if (name != newName) {
            publish(ProductRenamed(id, newName, name))
            name = newName
        }
    }

    fun setPrice(currency: Currency, amount: Double) {
        checkArgument(amount > 0)
        setNewPrice(Price(currency, amount))?.let {
            publish(ProductPriceSet(id, it.first, it.second))
        }
    }

    fun increasePrice(percentage: Double) {
        checkArgument(percentage > 0)
        setNewPrice(price.applyPercentage(percentage))?.let{
            publish(ProductPriceIncreased(id, it.first, it.second))
        }
    }

    fun decreasePrice(percentage: Double) {
        checkArgument(percentage > 0)
        setNewPrice(price.applyPercentage(percentage * -1))?.let{
            publish(ProductPriceDecreased(id, it.first, it.second))
        }
    }

    private fun setNewPrice(price: Price): Pair<Price, Price>? =
            if (this.price != price) {
                val oldPrice = this.price
                this.price = price
                Pair(price, oldPrice)
            } else null
}
