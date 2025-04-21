package ru.fav.petcare.data.mappers

import ru.fav.petcare.data.remote.pojo.response.JwtResponse
import ru.fav.petcare.domain.models.JwtModel
import javax.inject.Inject

class JwtMapper @Inject constructor(){
    fun map(input: JwtResponse?): JwtModel {
        return input?.let {
            JwtModel(token = it.token ?: "")
        } ?: JwtModel()
    }
}