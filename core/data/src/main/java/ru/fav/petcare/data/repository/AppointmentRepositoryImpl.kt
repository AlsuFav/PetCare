package ru.fav.petcare.data.repository

import retrofit2.HttpException
import ru.fav.petcare.data.mapper.AppointmentMapper
import ru.fav.petcare.data.util.ErrorParser.parseProblemDetails
import ru.fav.petcare.data.util.HttpStatusCodes
import ru.fav.petcare.domain.exception.BadRequestException
import ru.fav.petcare.domain.exception.ForbiddenAccessException
import ru.fav.petcare.domain.exception.NetworkException
import ru.fav.petcare.domain.exception.NotFoundException
import ru.fav.petcare.domain.exception.ServerException
import ru.fav.petcare.domain.exception.UnauthorizedException
import ru.fav.petcare.domain.model.AppointmentModel
import ru.fav.petcare.domain.model.PetModel
import ru.fav.petcare.domain.repository.AppointmentRepository
import ru.fav.petcare.network.AppointmentApi
import ru.fav.petcare.network.pojo.request.CreateAppointmentRequest
import ru.fav.petcare.network.pojo.request.CreatePetRequest
import java.io.IOException
import javax.inject.Inject

class AppointmentRepositoryImpl @Inject constructor(
    private val appointmentApi: AppointmentApi,
    private val mapper: AppointmentMapper,
): AppointmentRepository {
    override suspend fun getAllUpcomingAppointmentsData(): List<AppointmentModel> {
        return try {
            val response = appointmentApi.getAllUpcomingAppointmentsData()
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

    override suspend fun getAllPassedAppointmentsData(): List<AppointmentModel> {
        return try {
            val response = appointmentApi.getAllPassedAppointmentsData()
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


    override suspend fun getAppointmentData(id: Long): AppointmentModel {
        return try {
            val response = appointmentApi.getAppointmentData(id)
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

    override suspend fun cancelAppointment(id: Long) {
        try {
            appointmentApi.cancelAppointment(id)
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

    override suspend fun createAppointment(
        petId: Long,
        serviceId: Long,
        timeSlotId: Long,
    ) {
        try {
            appointmentApi.createAppointment(CreateAppointmentRequest(
                petId = petId,
                serviceId = serviceId,
                timeSlotId = timeSlotId,
            ))
        } catch (_: IOException) {
            throw NetworkException(null)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val problemDetails = parseProblemDetails(errorBody)

            when (e.code()) {
                HttpStatusCodes.UNAUTHORIZED -> throw UnauthorizedException(problemDetails?.detail)
                HttpStatusCodes.BAD_REQUEST -> throw BadRequestException(problemDetails?.detail)
                HttpStatusCodes.NOT_FOUND -> throw NotFoundException(problemDetails?.detail)
                HttpStatusCodes.FORBIDDEN_ACCESS -> throw ForbiddenAccessException(problemDetails?.detail)
                else -> throw ServerException(problemDetails?.detail)
            }
        }
    }

    override suspend fun getAppointmentConfirmationData(
        petId: Long,
        serviceId: Long,
        timeSlotId: Long,
    ) : AppointmentModel {
        return try {
            val response = appointmentApi.getAppointmentConfirmationData(CreateAppointmentRequest(
                petId = petId,
                serviceId = serviceId,
                timeSlotId = timeSlotId,
            ))
            mapper.map(response)
        } catch (_: IOException) {
            throw NetworkException(null)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val problemDetails = parseProblemDetails(errorBody)

            when (e.code()) {
                HttpStatusCodes.UNAUTHORIZED -> throw UnauthorizedException(problemDetails?.detail)
                HttpStatusCodes.BAD_REQUEST -> throw BadRequestException(problemDetails?.detail)
                HttpStatusCodes.NOT_FOUND -> throw NotFoundException(problemDetails?.detail)
                HttpStatusCodes.FORBIDDEN_ACCESS -> throw ForbiddenAccessException(problemDetails?.detail)
                else -> throw ServerException(problemDetails?.detail)
            }
        }
    }
}
