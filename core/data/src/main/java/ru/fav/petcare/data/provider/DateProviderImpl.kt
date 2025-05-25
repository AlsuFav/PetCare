package ru.fav.petcare.data.provider

import ru.fav.petcare.domain.provider.DateProvider
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlin.apply

class DateProviderImpl @Inject constructor() : DateProvider {
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun getCurrentDate(): Calendar {
        return Calendar.getInstance()
    }

    override fun formatDate(calendar: Calendar): String {
        return dateFormat.format(calendar.time)
    }

    override fun parseDate(dateString: String): Calendar {
        val date = dateFormat.parse(dateString)
        return Calendar.getInstance().apply {
            if (date != null) {
                time = date
            }
        }
    }
}
