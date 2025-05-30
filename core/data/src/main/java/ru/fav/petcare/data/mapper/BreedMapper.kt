package ru.fav.petcare.data.mapper

import ru.fav.petcare.network.pojo.response.BreedResponse
import javax.inject.Inject

class BreedMapper @Inject constructor(){

    fun mapList(input: List<BreedResponse>?): List<String> {
        return input?.mapNotNull { it.name } ?: emptyList()
    }
}