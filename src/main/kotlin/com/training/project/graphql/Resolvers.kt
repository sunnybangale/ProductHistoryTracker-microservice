package com.training.project.graphql

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.training.project.service.ProductService
import org.springframework.stereotype.Component
import java.util.UUID
import com.training.project.service.model.Currency as CurrencyModel

@Component
class ProductQueryResolver(private val service: ProductService) : GraphQLQueryResolver {
    fun product(id: UUID) = Product(service.product(id))
    fun products() = service.products().map { Product(it) }.toList()
}

@Component
class ProductMutationResolver(private val service: ProductService) : GraphQLMutationResolver {
    fun registerProduct(input: RegisterProductInput) = RegisterProductPayload(Product(service.registerProduct(input.name, currency(input.currency), input.amount)))
    fun deregisterProduct(input: DeregisterProductInput) = DeregisterProductPayload(Product(service.product(input.id).apply { service.deregisterProduct(id) }))
    fun renameProduct(input: RenameProductInput) = RenameProductPayload(Product(service.renameProduct(input.id, input.name)))
    fun setProductPrice(input: SetProductPriceInput) = SetProductPricePayload(Product(service.setProductPrice(input.id, currency(input.currency), input.amount)))
    fun increaseProductPrice(input: IncreaseProductPriceInput) = IncreaseProductPricePayload(Product(service.increaseProductPrice(input.id, input.percentage)))
    fun decreaseProductPrice(input: DecreaseProductPriceInput) = DecreaseProductPricePayload(Product(service.decreaseProductPrice(input.id, input.percentage)))
}

data class RegisterProductInput(val name: String, val currency: Currency, val amount: Double)
data class RegisterProductPayload(val product: Product)

data class DeregisterProductInput(val id: UUID)
data class DeregisterProductPayload(val product: Product)

data class IncreaseProductPriceInput(val id: UUID, val percentage: Double)
data class IncreaseProductPricePayload(val product: Product)

data class DecreaseProductPriceInput(val id: UUID, val percentage: Double)
data class DecreaseProductPricePayload(val product: Product)

data class SetProductPriceInput(val id: UUID, val currency: Currency, val amount: Double)
data class SetProductPricePayload(val product: Product)

data class RenameProductInput(val id: UUID, val name: String)
data class RenameProductPayload(val product: Product)

private fun currency(currency: Currency) = CurrencyModel.valueOf(currency.name)