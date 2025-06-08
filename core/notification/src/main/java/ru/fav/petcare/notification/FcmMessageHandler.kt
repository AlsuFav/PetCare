package ru.fav.petcare.notification

interface FcmMessageHandler {
    fun canHandle(category: String): Boolean
    fun handle(data: Map<String, String>)
}