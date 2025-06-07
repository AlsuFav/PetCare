package ru.fav.petcare.domain.model

data class ServiceModel(
    val id: Long = 0,
    val name: String = "",
    val description: String = "",
    val prices: List<ServicePriceModel> = emptyList()
)

data class ServicePriceModel (
    val species: String = "",
    val breedType: String? = null,
    val price: Int = 0,
)