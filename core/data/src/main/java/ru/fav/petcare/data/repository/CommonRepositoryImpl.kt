package ru.fav.petcare.data.repository

import retrofit2.HttpException
import ru.fav.petcare.data.mapper.FaqMapper
import ru.fav.petcare.data.mapper.MapMapper
import ru.fav.petcare.data.util.ErrorParser.parseProblemDetails
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.model.FaqModel
import ru.fav.petcare.domain.model.MapModel
import ru.fav.petcare.domain.repository.CommonRepository
import ru.fav.petcare.network.CommonApi
import java.io.IOException
import javax.inject.Inject

class CommonRepositoryImpl @Inject constructor(
    private val commonApi: CommonApi,
    private val mapMapper: MapMapper,
    private val faqMapper: FaqMapper
): CommonRepository {

    override suspend fun getMapData(): MapModel {
        return try {
            val response = commonApi.getMapData()
            mapMapper.map(response)
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

    override suspend fun getFaqData(): List<FaqModel> {
        return try {
            val response = commonApi.getFaqData()
            faqMapper.mapList(response)
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
