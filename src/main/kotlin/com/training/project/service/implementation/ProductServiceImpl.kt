package com.training.project.service.implementation

import com.training.project.service.ProductService
import com.training.project.service.model.Currency
import com.training.project.service.model.Price
import com.training.project.service.model.Product
import com.training.project.service.model.ProductDeregistered
import com.training.project.service.model.ProductEvent
import com.training.project.service.model.ProductRegistered
import com.training.project.service.model.ProductRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.util.UUID
import javax.transaction.Transactional

@Service
@Transactional
class ProductServiceImpl(private val repository: ProductRepository, private val publish: ApplicationEventPublisher) : ProductService {

    override fun product(id: UUID): Product = repository.findById(id).get()
    override fun products(): List<Product> = repository.findAll().toList()

    override fun registerProduct(name: String, currency: Currency, amount: Double): Product = repository.save(Product(name, Price(currency, amount))).also { publish(ProductRegistered(it.id, it.name, it.price)) }
    override fun deregisterProduct(id: UUID) = repository.deleteById(id).also { publish(ProductDeregistered(id)) }
    override fun renameProduct(id: UUID, name: String): Product = mutate(id) { it.rename(name) }
    override fun setProductPrice(id: UUID, currency: Currency, amount: Double): Product = mutate(id) { it.setPrice(currency, amount) }
    override fun increaseProductPrice(id: UUID, percentage: Double): Product = mutate(id) { it.increasePrice(percentage) }
    override fun decreaseProductPrice(id: UUID, percentage: Double): Product = mutate(id) { it.decreasePrice(percentage) }

    private fun mutate(id: UUID, mutation: (Product) -> Unit): Product {
        val product = product(id)
        mutation(product)
        return repository.save(product)
    }

    private fun publish(event: ProductEvent) = publish.publishEvent(event)
}