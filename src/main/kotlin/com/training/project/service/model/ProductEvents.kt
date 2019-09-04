package com.training.project.service.model

import java.util.UUID

interface ProductEvent

data class ProductRegistered(val id: UUID, val name: String, val price: Price) : ProductEvent
data class ProductDeregistered(val id: UUID) : ProductEvent
data class ProductPriceIncreased(val id: UUID, val price: Price, val oldPrice: Price) : ProductEvent
data class ProductPriceDecreased(val id: UUID, val price: Price, val oldPrice: Price) : ProductEvent
data class ProductRenamed(val id: UUID, val name: String, val oldName: String) : ProductEvent
data class ProductPriceSet(val id: UUID, val price: Price, val oldPrice: Price) : ProductEvent



