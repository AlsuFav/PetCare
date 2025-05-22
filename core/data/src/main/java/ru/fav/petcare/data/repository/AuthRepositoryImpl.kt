package ru.fav.petcare.data.repository

import retrofit2.HttpException
import ru.fav.petcare.data.mapper.JwtMapper
import ru.fav.petcare.data.util.ErrorParser.parseProblemDetails
import ru.fav.petcare.data.util.HttpStatusCodes
import ru.fav.petcare.domain.exception.ClientAlreadyExistsException
import ru.fav.petcare.domain.exception.InvalidCredentialsException
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.model.JwtModel
import ru.fav.petcare.domain.repository.AuthRepository
import ru.fav.petcare.network.AuthApi
import ru.fav.petcare.network.pojo.request.LoginClientRequest
import ru.fav.petcare.network.pojo.request.RegisterClientRequest
import java.io.IOException
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val mapper: JwtMapper
) : AuthRepository {

    override suspend fun login(
        phone: String,
        password: String
    ): JwtModel {
        return try {
            val response = authApi.login(LoginClientRequest(phone, password))
            mapper.map(response)
        } catch (e: IOException) {
            throw NetworkException(null)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val problemDetails = parseProblemDetails(errorBody)

            when (e.code()) {
                HttpStatusCodes.UNAUTHORIZED -> throw InvalidCredentialsException(problemDetails?.detail)
                else -> throw ServerException(problemDetails?.detail)
            }
        }
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        phone: String,
        password: String,
        confirmPassword: String
    ): JwtModel {
        return try {
            val response = authApi.register(RegisterClientRequest(
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                password = password,
                confirmPassword = confirmPassword
            ))
            mapper.map(response)
        } catch (e: IOException) {
            throw NetworkException(null)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val problemDetails = parseProblemDetails(errorBody)

            when (e.code()) {
                HttpStatusCodes.CONFLICT -> throw ClientAlreadyExistsException(problemDetails?.detail)
                else -> throw ServerException(problemDetails?.detail)
            }
        }
    }
}