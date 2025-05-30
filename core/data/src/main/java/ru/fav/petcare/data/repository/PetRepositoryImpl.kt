package ru.fav.petcare.data.repository

import retrofit2.HttpException
import ru.fav.petcare.data.mapper.PetMapper
import ru.fav.petcare.data.util.ErrorParser.parseProblemDetails
import ru.fav.petcare.data.util.HttpStatusCodes
import ru.fav.petcare.domain.exception.ForbiddenAccessException
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.NotFoundException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.exception.UnauthorizedException
import ru.fav.petcare.domain.model.PetModel
import ru.fav.petcare.domain.repository.PetRepository
import ru.fav.petcare.network.PetApi
import ru.fav.petcare.network.pojo.request.CreatePetRequest
import ru.fav.petcare.network.pojo.request.UpdatePetRequest
import java.io.IOException
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(
    private val petApi: PetApi,
    private val mapper: PetMapper,
): PetRepository {
    override suspend fun getAllPetsData(): List<PetModel> {
        return try {
            val response = petApi.getAllPetsData()
            mapper.mapList(response)
        } catch (_: IOException) {
            throw NetworkException(null)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val problemDetails = parseProblemDetails(errorBody)

            when (e.code()) {
                HttpStatusCodes.UNAUTHORIZED -> throw UnauthorizedException(problemDetails?.detail)
                else -> throw ServerException(problemDetails?.detail)
            }
        }
    }

    override suspend fun getPetData(id: Long): PetModel {
        return try {
            val response = petApi.getPetData(id)
            mapper.map(response)
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

    override suspend fun createPet(
        name: String,
        species: String,
        breed: String?,
        birthDate: String
    ) {
        try {
            petApi.createPet(CreatePetRequest(
                name = name,
                species = species,
                breed = breed,
                birthDate = birthDate
            ))
        } catch (_: IOException) {
            throw NetworkException(null)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val problemDetails = parseProblemDetails(errorBody)

            when (e.code()) {
                HttpStatusCodes.UNAUTHORIZED -> throw UnauthorizedException(problemDetails?.detail)
                else -> throw ServerException(problemDetails?.detail)
            }
        }
    }

    override suspend fun updatePet(
        id: Long,
        name: String,
        birthDate: String
    ) {
        try {
            petApi.updatePet(
                id,
                UpdatePetRequest(
                name = name,
                birthDate = birthDate
            ))
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

    override suspend fun deletePet(id: Long) {
        try {
            petApi.deletePet(id)
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
