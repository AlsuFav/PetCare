package ru.fav.petcare.data.mapper

import ru.fav.petcare.domain.model.MapModel
import ru.fav.petcare.network.pojo.response.MapDataResponse
import javax.inject.Inject

class MapMapper @Inject constructor(){
    fun map(input: MapDataResponse?): MapModel {
        return input?.let {
            MapModel(
                latitude = it.latitude ?: 0.0,
                longitude = it.longitude ?: 0.0,
                address = it.address.orEmpty(),
                )
        } ?: MapModel()
    }
}