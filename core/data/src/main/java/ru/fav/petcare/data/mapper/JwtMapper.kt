package ru.fav.petcare.data.mapper

import ru.fav.petcare.domain.model.JwtModel
import ru.fav.petcare.network.pojo.response.JwtResponse
import javax.inject.Inject

class JwtMapper @Inject constructor(){
    fun map(input: JwtResponse?): JwtModel {
        return input?.let {
            JwtModel(token = it.token ?: "")
        } ?: JwtModel()
    }
}