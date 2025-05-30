package ru.fav.petcare.domain.exception


class InvalidPasswordException(message: String?) : Exception(message)
class InvalidCredentialsException(message: String?) : Exception(message)
class ClientAlreadyExistsException(message: String?) : Exception(message)
class UnauthorizedException(message: String?) : Exception(message)
class ForbiddenAccessException(message: String?) : Exception(message)
class NotFoundException(message: String?) : Exception(message)
class BadRequestException(message: String?) : Exception(message)

class NoJwtException(message: String?) : Exception(message)
class NoPetsException(message: String?) : Exception(message)
class NoAppointmentsException(message: String?) : Exception(message)
class NoTimeSlotsException(message: String?) : Exception(message)
class NoServicesException(message: String?) : Exception(message)

class NetworkException(message: String?) : Exception(message)
class ServerException(message: String?) : Exception(message)


