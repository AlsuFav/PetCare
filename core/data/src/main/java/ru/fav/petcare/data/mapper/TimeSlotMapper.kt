package ru.fav.petcare.data.mapper

import ru.fav.petcare.domain.model.TimeSlotModel
import ru.fav.petcare.domain.provider.DateProvider
import ru.fav.petcare.network.AppointmentApi
import ru.fav.petcare.network.pojo.response.TimeSlotDataResponse
import javax.inject.Inject

class TimeSlotMapper @Inject constructor(
    private val dateProvider: DateProvider
){
    fun map(input: TimeSlotDataResponse?): TimeSlotModel {
        return input?.let {

            val dateTime = if (input.startTime != null) {
                dateProvider.parseDateTime(it.startTime!!)
            } else {
                dateProvider.getCurrentDate()
            }

            TimeSlotModel(
                id = it.id ?: 0,
                groomerName = it.groomerName.orEmpty(),
                date = dateProvider.formatDate(dateTime),
                time = dateProvider.formatTime(dateTime)
                )
        } ?: TimeSlotModel()
    }

    fun mapList(input: List<TimeSlotDataResponse>?): List<TimeSlotModel> {
        return input?.map { map(it) } ?: emptyList()
    }
}