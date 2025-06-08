package ru.fav.petcare.data.mapper

import ru.fav.petcare.domain.model.AppointmentModel
import ru.fav.petcare.domain.model.FaqModel
import ru.fav.petcare.network.pojo.response.AppointmentDataResponse
import ru.fav.petcare.network.pojo.response.FaqDataResponse
import javax.inject.Inject

class FaqMapper @Inject constructor(){
    fun map(input: FaqDataResponse?): FaqModel {
        return input?.let {
            FaqModel(
                id = it.id ?: 0,
                question = it.question.orEmpty(),
                answer = it.answer.orEmpty(),
                )
        } ?: FaqModel()
    }

    fun mapList(input: List<FaqDataResponse>?): List<FaqModel> {
        return input?.map { map(it) } ?: emptyList()
    }
}