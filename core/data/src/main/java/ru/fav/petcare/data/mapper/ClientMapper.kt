package ru.fav.petcare.data.mapper

import ru.fav.petcare.domain.model.ClientModel
import ru.fav.petcare.network.pojo.response.ClientDataResponse
import javax.inject.Inject

class ClientMapper @Inject constructor(){
    fun map(input: ClientDataResponse?): ClientModel {
        return input?.let {
            ClientModel(
                firstName = it.firstName ?: "",
                lastName = it.lastName ?: "",
                phone = it.phone ?: "",
                )
        } ?: ClientModel()
    }
}