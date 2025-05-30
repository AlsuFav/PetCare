package ru.fav.petcare.domain.repository

import ru.fav.petcare.domain.model.TimeSlotModel

interface TimeSlotRepository {
    suspend fun getAllTimeSlotsData(): List<TimeSlotModel>
}