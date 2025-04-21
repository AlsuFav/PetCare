package ru.fav.petcare.domain.exceptions

class InvalidCredentialsException(message: String?) : Exception(message)
class ClientAlreadyExistsException(message: String?) : Exception(message)

class NetworkException(message: String?) : Exception(message)
class ServerException(message: String?) : Exception(message)


