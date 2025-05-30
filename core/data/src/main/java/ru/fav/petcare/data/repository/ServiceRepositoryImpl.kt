package ru.fav.petcare.data.repository

import retrofit2.HttpException
import ru.fav.petcare.data.mapper.ServiceMapper
import ru.fav.petcare.data.util.ErrorParser.parseProblemDetails
import ru.fav.petcare.data.util.HttpStatusCodes
import ru.fav.petcare.domain.exception.ForbiddenAccessException
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.NotFoundException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.exception.UnauthorizedException
import ru.fav.petcare.domain.model.ServiceModel
import ru.fav.petcare.domain.repository.ServiceRepository
import ru.fav.petcare.network.ServiceApi
import java.io.IOException
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val serviceApi: ServiceApi,
    private val mapper: ServiceMapper,
): ServiceRepository {
    override suspend fun getAllServicesDataForPet(id: Long): List<ServiceModel> {
        return try {
            val response = serviceApi.getAllServicesDataForPet(id)
            mapper.mapList(response)
        } catch (_: IOException) {
            throw NetworkException(null)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val problemDetails = parseProblemDetails(errorBody)

            when (e.code()) {
                HttpStatusCodes.UNAUTHORIZED -> throw UnauthorizedException(problemDetails?.detail)
                HttpStatusCodes.NOT_FOUND -> throw NotFoundException(problemDetails?.detail)
                HttpStatusCodes.FORBIDDEN_ACCESS -> throw ForbiddenAccessException(problemDetails?.detail)
                else -> throw ServerException(problemDetails?.detail)
            }
        }
    }
}
