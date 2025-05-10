package ru.fav.petcare.presentation.util.validators

object PhoneValidator {

    private val phoneRegex = Regex("""^\+7 \(\d{3}\) \d{3}-\d{2}-\d{2}$""")

    fun isValid(phone: String): Boolean {
        return phoneRegex.matches(phone)
    }
}
