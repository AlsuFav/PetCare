package ru.fav.petcare.data.repository

import retrofit2.HttpException
import ru.fav.petcare.data.mapper.TimeSlotMapper
import ru.fav.petcare.data.util.ErrorParser.parseProblemDetails
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.model.TimeSlotModel
import ru.fav.petcare.domain.repository.TimeSlotRepository
import ru.fav.petcare.network.TimeSlotApi
import java.io.IOException
import javax.inject.Inject

class TimeSlotRepositoryImpl @Inject constructor(
    private val timeSlotApi: TimeSlotApi,
    private val mapper: TimeSlotMapper,
): TimeSlotRepository {
    override suspend fun getAllTimeSlotsData(): List<TimeSlotModel> {
        return try {
            val response = timeSlotApi.getAllTimeSlotsData()
            mapper.mapList(response)
        } catch (_: IOException) {
            throw NetworkException(null)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val problemDetails = parseProblemDetails(errorBody)

            when (e.code()) {
                else -> throw ServerException(problemDetails?.detail)
            }
        }
    }
}
