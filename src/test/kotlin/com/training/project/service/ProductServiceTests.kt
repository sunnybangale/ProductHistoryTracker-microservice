package com.training.project.service

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.training.project.service.implementation.ProductServiceImpl
import com.training.project.service.model.Currency.PESOS
import com.training.project.service.model.Price
import com.training.project.service.model.Product
import com.training.project.service.model.ProductRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.AdditionalAnswers.returnsFirstArg
import org.springframework.context.ApplicationEventPublisher
import java.util.Optional
import java.util.UUID

class ProductServiceTests {

    private lateinit var service: ProductService

    private var repository: ProductRepository = mock()
    private var publisher: ApplicationEventPublisher = mock()
    private val expectedProduct = Product("Robot", Price(PESOS, 100.0))

    @BeforeEach
    fun setUp() {
        service = ProductServiceImpl(repository, publisher)
    }

    @Test
    fun `Check Product is retrieved`() {
        whenever(repository.findById(any())).thenReturn(Optional.of(expectedProduct))
        val product = service.product(expectedProduct.id)

        assertThat(product).isNotNull
        assertThat(product).isEqualTo(expectedProduct)
    }

    @Test
    fun `Check Product is retrieved failure throws exception`() {
        whenever(repository.findById(any())).thenThrow(NoSuchElementException())
        assertThrows<NoSuchElementException> { service.product(UUID.randomUUID()) }
    }

    @Test
    fun `Check Products are retrieved in a list`() {
        whenever(repository.findAll()).thenReturn(listOf(expectedProduct))
        val products = service.products()

        assertThat(products).isNotEmpty
        assertThat(products).contains(expectedProduct)
    }

    @Test
    fun `Check Product registration`() {
        whenever(repository.save(any<Product>())).then(returnsFirstArg<Product>())
        val product = service.registerProduct("Robot", PESOS, 100.0)

        product.apply {
            assertThat(name).isEqualTo(expectedProduct.name)
            assertThat(price).isEqualTo(expectedProduct.price)
        }
    }

    @Test
    fun `Check Product deregistration`() {
        whenever(repository.deleteById(any())).thenThrow(IllegalArgumentException())
        assertThrows<IllegalArgumentException> { service.deregisterProduct(UUID.randomUUID()) }
    }

    @Test
    fun `Check Product set price`() {
        whenever(repository.findById(any())).thenReturn(Optional.of(expectedProduct))
        whenever(repository.save(any<Product>())).then(returnsFirstArg<Product>())

        service.setProductPrice(expectedProduct.id, PESOS, 100.0)
        assertThat(service.product(expectedProduct.id).price).isEqualTo(expectedProduct.price)
    }

    @Test
    fun `Check Product set price cannot be assigned negative value`() {
        whenever(repository.findById(any())).thenReturn(Optional.of(expectedProduct))
        whenever(repository.save(any<Product>())).then(returnsFirstArg<Product>())

        service.setProductPrice(expectedProduct.id, PESOS, 50.0)
        assertThrows<IllegalArgumentException> { service.setProductPrice(expectedProduct.id, PESOS, -100.0) }
    }

    @Test
    fun `Check Product increase price`() {
        whenever(repository.findById(any())).thenReturn(Optional.of(expectedProduct))
        whenever(repository.save(any<Product>())).then(returnsFirstArg<Product>())

        service.increaseProductPrice(expectedProduct.id, 50.0)
        val expectedChangedProduct = Product("Robot", Price(PESOS, 150.0))
        assertThat(service.product(expectedProduct.id).price).isEqualTo(expectedChangedProduct.price)
    }

    @Test
    fun `Check Product increase price cannot be provided negative or zero percentage`() {
        whenever(repository.findById(any())).thenReturn(Optional.of(expectedProduct))
        whenever(repository.save(any<Product>())).then(returnsFirstArg<Product>())

        assertThrows<IllegalArgumentException> { service.increaseProductPrice(expectedProduct.id, -100.0) }
    }

    @Test
    fun `Check Product decrease price`() {
        whenever(repository.findById(any())).thenReturn(Optional.of(expectedProduct))
        whenever(repository.save(any<Product>())).then(returnsFirstArg<Product>())

        val expectedChangedProduct = Product("Robot", Price(PESOS, 50.0))
        service.decreaseProductPrice(expectedProduct.id, 50.0)
        assertThat(service.product(expectedProduct.id).price).isEqualTo(expectedChangedProduct.price)
    }

    @Test
    fun `Check Product decrease price cannot be provided negative or zero percentage`() {
        whenever(repository.findById(any())).thenReturn(Optional.of(expectedProduct))
        whenever(repository.save(any<Product>())).then(returnsFirstArg<Product>())

        assertThrows<IllegalArgumentException> { service.decreaseProductPrice(expectedProduct.id, -100.0) }
    }

    @Test
    fun `Check Product rename`() {
        whenever(repository.findById(any())).thenReturn(Optional.of(expectedProduct))
        whenever(repository.save(any<Product>())).then(returnsFirstArg<Product>())

        val expectedChangedProduct = Product("Mouse", Price(PESOS, 100.0))
        service.renameProduct(expectedProduct.id, "Mouse")
        assertThat(service.product(expectedProduct.id).name).isEqualTo(expectedChangedProduct.name)
    }

    @Test
    fun `Check Product rename cannot have empty value`() {
        whenever(repository.findById(any())).thenReturn(Optional.of(expectedProduct))
        whenever(repository.save(any<Product>())).then(returnsFirstArg<Product>())

        assertThrows<IllegalArgumentException> { service.renameProduct(expectedProduct.id, "") }
    }
}