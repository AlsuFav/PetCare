package ru.fav.petcare.data.mapper

import ru.fav.petcare.domain.model.AppointmentModel
import ru.fav.petcare.network.pojo.response.AppointmentDataResponse
import javax.inject.Inject

class AppointmentMapper @Inject constructor(){
    fun map(input: AppointmentDataResponse?): AppointmentModel {
        return input?.let {
            AppointmentModel(
                id = it.id ?: 0,
                petName = it.petName.orEmpty(),
                groomerName = it.groomerName.orEmpty(),
                serviceName = it.serviceName.orEmpty(),
                price = it.price ?: 0,
                date = it.date.orEmpty(),
                upcoming = it.upcoming ?: false
                )
        } ?: AppointmentModel()
    }

    fun mapList(input: List<AppointmentDataResponse>?): List<AppointmentModel> {
        return input?.map { map(it) } ?: emptyList()
    }
}