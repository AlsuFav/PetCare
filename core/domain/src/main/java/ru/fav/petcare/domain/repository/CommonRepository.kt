package ru.fav.petcare.domain.repository

import ru.fav.petcare.domain.model.FaqModel
import ru.fav.petcare.domain.model.MapModel

interface CommonRepository {
    suspend fun getMapData() : MapModel
    suspend fun getFaqData() : List<FaqModel>
}