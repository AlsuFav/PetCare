package ru.fav.petcare.data.mapper

import ru.fav.petcare.domain.model.PetModel
import ru.fav.petcare.network.pojo.response.PetDataResponse
import javax.inject.Inject

class PetMapper @Inject constructor(){
    fun map(input: PetDataResponse?): PetModel {
        return input?.let {
            PetModel(
                id = it.id ?: 0,
                name = it.name.orEmpty(),
                species = it.species.orEmpty(),
                breed = it.breed.orEmpty(),
                birthDate = it.birthDate.orEmpty(),
                imagePath = it.imagePath.orEmpty()
                )
        } ?: PetModel()
    }

    fun mapList(input: List<PetDataResponse>?): List<PetModel> {
        return input?.map { map(it) } ?: emptyList()
    }
}