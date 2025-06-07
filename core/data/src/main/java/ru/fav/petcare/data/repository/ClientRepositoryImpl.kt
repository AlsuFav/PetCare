package ru.fav.petcare.data.repository

import retrofit2.HttpException
import ru.fav.petcare.data.mapper.ClientMapper
import ru.fav.petcare.data.util.ErrorParser.parseProblemDetails
import ru.fav.petcare.data.util.HttpStatusCodes
import ru.fav.petcare.database.dao.ClientDao
import ru.fav.petcare.domain.exception.ClientAlreadyExistsException
import ru.fav.petcare.domain.exception.InvalidPasswordException
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.NotFoundException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.exception.UnauthorizedException
import ru.fav.petcare.domain.model.ClientModel
import ru.fav.petcare.domain.repository.ClientRepository
import ru.fav.petcare.network.ClientApi
import ru.fav.petcare.network.pojo.request.ChangePasswordRequest
import ru.fav.petcare.network.pojo.request.UpdateClientRequest
import java.io.IOException
import javax.inject.Inject


class ClientRepositoryImpl @Inject constructor(
    private val clientApi: ClientApi,
    private val clientDao: ClientDao,
    private val mapper: ClientMapper
) : ClientRepository {

    override suspend fun getClientData(): ClientModel {
        return try {
            val response = clientApi.getClientData()
            val client = mapper.map(response)
            clientDao.deleteClient()
            clientDao.saveClient(mapper.mapToEntity(client))
            client
        } catch (_: IOException) {
            val client = clientDao.getClient()
            if (client != null) {
                return mapper.mapFromEntity(client)
            } else {
                throw NetworkException(null)
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val problemDetails = parseProblemDetails(errorBody)

            when (e.code()) {
                HttpStatusCodes.UNAUTHORIZED -> throw UnauthorizedException(problemDetails?.detail)
                HttpStatusCodes.NOT_FOUND -> throw NotFoundException(problemDetails?.detail)
                else -> throw ServerException(problemDetails?.detail)
            }
        }
    }

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ) {
        try {
            clientApi.changePassword(ChangePasswordRequest(
                currentPassword = currentPassword,
                newPassword = newPassword,
                confirmNewPassword = confirmNewPassword
            ))
        } catch (_: IOException) {
            throw NetworkException(null)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val problemDetails = parseProblemDetails(errorBody)

            when (e.code()) {
                HttpStatusCodes.BAD_REQUEST -> throw InvalidPasswordException(problemDetails?.detail)
                HttpStatusCodes.UNAUTHORIZED -> throw UnauthorizedException(problemDetails?.detail)
                HttpStatusCodes.NOT_FOUND -> throw NotFoundException(problemDetails?.detail)
                else -> throw ServerException(problemDetails?.detail)
            }
        }
    }

    override suspend fun updateClientData(
        firstName: String,
        lastName: String,
        phone: String
    ) {
        try {
            clientApi.updateClientData(UpdateClientRequest(
                firstName = firstName,
                lastName = lastName,
                phone = phone
            ))
            clientDao.deleteClient()
            clientDao.saveClient(mapper.mapToEntity(ClientModel(
                firstName = firstName,
                lastName = lastName,
                phone = phone
            )))
        } catch (_: IOException) {
            throw NetworkException(null)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val problemDetails = parseProblemDetails(errorBody)

            when (e.code()) {
                HttpStatusCodes.CONFLICT -> throw ClientAlreadyExistsException(problemDetails?.detail)
                HttpStatusCodes.UNAUTHORIZED -> throw UnauthorizedException(problemDetails?.detail)
                HttpStatusCodes.NOT_FOUND -> throw NotFoundException(problemDetails?.detail)
                else -> throw ServerException(problemDetails?.detail)
            }
        }
    }

    override suspend fun deleteClient() {
        try {
            clientApi.deleteClient()
            clientDao.deleteClient()
        } catch (_: IOException) {
            throw NetworkException(null)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val problemDetails = parseProblemDetails(errorBody)

            when (e.code()) {
                HttpStatusCodes.UNAUTHORIZED -> throw UnauthorizedException(problemDetails?.detail)
                HttpStatusCodes.NOT_FOUND -> throw NotFoundException(problemDetails?.detail)
                else -> throw ServerException(problemDetails?.detail)
            }
        }
    }
}