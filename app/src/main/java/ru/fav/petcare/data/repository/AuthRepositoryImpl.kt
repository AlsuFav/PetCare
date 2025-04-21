package ru.fav.petcare.data.repository

import retrofit2.HttpException
import ru.fav.petcare.data.mappers.JwtMapper
import ru.fav.petcare.data.remote.AuthApi
import ru.fav.petcare.data.remote.pojo.request.LoginClientRequest
import ru.fav.petcare.data.remote.pojo.response.JwtResponse
import ru.fav.petcare.data.utils.ErrorParser.parseProblemDetails
import ru.fav.petcare.domain.exceptions.AuthException
import ru.fav.petcare.domain.exceptions.NetworkException
import ru.fav.petcare.domain.exceptions.ServerException
import ru.fav.petcare.domain.models.JwtModel
import ru.fav.petcare.domain.repository.AuthRepository
import java.io.IOException
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val mapper: JwtMapper
) : AuthRepository {

    override suspend fun login(phone: String, password: String): JwtModel {
        return try {
            val response = authApi.login(LoginClientRequest(phone, password))
            mapper.map(response)
        } catch (e: IOException) {
            throw NetworkException(null)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val problemDetails = parseProblemDetails(errorBody)

            when (e.code()) {
                401 -> throw AuthException(problemDetails?.detail)
                else -> throw ServerException(problemDetails?.detail)
            }
        }
    }

}