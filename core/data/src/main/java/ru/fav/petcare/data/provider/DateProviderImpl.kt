package ru.fav.petcare.data.provider

import ru.fav.petcare.domain.provider.DateProvider
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlin.apply

class DateProviderImpl @Inject constructor() : DateProvider {
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun getCurrentDate(): Calendar {
        return Calendar.getInstance()
    }

    override fun formatDate(calendar: Calendar): String {
        return dateFormat.format(calendar.time)
    }

    override fun formatTime(calendar: Calendar): String {
        return timeFormat.format(calendar.time)
    }

    override fun parseDate(dateString: String): Calendar {
        val date = dateFormat.parse(dateString)
        return Calendar.getInstance().apply {
            if (date != null) {
                time = date
            }
        }
    }

    override fun parseDateTime(dateTimeString: String): Calendar {
        val date = dateTimeFormat.parse(dateTimeString)
        return Calendar.getInstance().apply {
            if (date != null) {
                time = date
            }
        }
    }
}
