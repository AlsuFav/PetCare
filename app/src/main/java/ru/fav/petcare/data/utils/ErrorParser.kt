package ru.fav.petcare.data.utils

import com.google.gson.Gson
import ru.fav.petcare.domain.models.ProblemDetailsModel

object ErrorParser {

    fun parseProblemDetails(errorBody: String?): ProblemDetailsModel? {
        return try {
            errorBody?.let {
                Gson().fromJson(it, ProblemDetailsModel::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }
}
