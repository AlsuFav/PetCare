package ru.fav.petcare.data.mapper

import ru.fav.petcare.domain.model.ServiceModel
import ru.fav.petcare.network.pojo.response.ServiceDataResponse
import javax.inject.Inject

class ServiceMapper @Inject constructor(){
    fun map(input: ServiceDataResponse?): ServiceModel {
        return input?.let {
            ServiceModel(
                id = it.id ?: 0,
                name = it.name.orEmpty(),
                description = it.description.orEmpty(),
                price = it.price ?: 0,
                )
        } ?: ServiceModel()
    }

    fun mapList(input: List<ServiceDataResponse>?): List<ServiceModel> {
        return input?.map { map(it) } ?: emptyList()
    }
}