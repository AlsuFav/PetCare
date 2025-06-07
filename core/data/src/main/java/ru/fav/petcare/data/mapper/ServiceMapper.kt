package ru.fav.petcare.data.mapper

import ru.fav.petcare.domain.model.ServiceModel
import ru.fav.petcare.domain.model.ServicePriceModel
import ru.fav.petcare.network.pojo.response.ServiceDataResponse
import ru.fav.petcare.network.pojo.response.ServicePriceDataResponse
import javax.inject.Inject

class ServiceMapper @Inject constructor(){
    fun map(input: ServiceDataResponse?): ServiceModel {
        return input?.let {
            ServiceModel(
                id = it.id ?: 0,
                name = it.name.orEmpty(),
                description = it.description.orEmpty(),
                prices = mapPrices(it.prices)
                )
        } ?: ServiceModel()
    }

    fun mapList(input: List<ServiceDataResponse>?): List<ServiceModel> {
        return input?.map { map(it) } ?: emptyList()
    }


    private fun mapPrices(prices: List<ServicePriceDataResponse>?): List<ServicePriceModel> {
        return prices?.map { priceResponse ->
            ServicePriceModel(
                species = priceResponse.species.orEmpty(),
                breedType = priceResponse.breedType,
                price = priceResponse.price ?: 0
            )
        } ?: emptyList()
    }
}