package ru.fav.petcare.data.mapper

import ru.fav.petcare.domain.model.PetModel
import ru.fav.petcare.network.pojo.response.PetDataResponse
import javax.inject.Inject

class PetMapper @Inject constructor(){
    fun map(input: PetDataResponse?): PetModel {
        return input?.let {
            PetModel(
                id = it.id ?: 0,
                name = it.name ?: "",
                species = it.species ?: "",
                breed = it.breed ?: "",
                birthDate = it.birthDate ?: "",
                )
        } ?: PetModel()
    }

    fun mapList(input: List<PetDataResponse>?): List<PetModel> {
        return input?.map { map(it) } ?: emptyList()
    }
}