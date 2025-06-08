package ru.fav.petcare.data.mapper

import ru.fav.petcare.database.entity.ClientEntity
import ru.fav.petcare.domain.model.ClientModel
import ru.fav.petcare.network.pojo.response.ClientDataResponse
import javax.inject.Inject

class ClientMapper @Inject constructor(){
    fun map(input: ClientDataResponse?): ClientModel {
        return input?.let {
            ClientModel(
                firstName = it.firstName.orEmpty(),
                lastName = it.lastName.orEmpty(),
                phone = it.phone.orEmpty(),
                )
        } ?: ClientModel()
    }

    fun mapToEntity(clientModel: ClientModel): ClientEntity {
        return ClientEntity(
            phone = clientModel.phone,
            firstName = clientModel.firstName,
            lastName = clientModel.lastName
        )
    }

    fun mapFromEntity(entity: ClientEntity): ClientModel {
        return ClientModel(
            phone = entity.phone,
            firstName = entity.firstName,
            lastName = entity.lastName
        )
    }
}