package ru.fav.petcare.data.repository

import retrofit2.HttpException
import retrofit2.http.Query
import ru.fav.petcare.data.mapper.BreedMapper
import ru.fav.petcare.data.util.ErrorParser.parseProblemDetails
import ru.fav.petcare.domain.repository.BreedRepository
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.network.BreedApi
import java.io.IOException
import javax.inject.Inject

class BreedRepositoryImpl @Inject constructor(
    private val breedApi: BreedApi,
    private val mapper: BreedMapper,
): BreedRepository {
    override suspend fun getAllBreedsContainingQuery(query: String): List<String> {
        return try {
            val response = breedApi.getAllBreedsContainingQuery(query)
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
