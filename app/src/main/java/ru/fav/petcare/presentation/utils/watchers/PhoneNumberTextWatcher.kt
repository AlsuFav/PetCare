package ru.fav.petcare.presentation.utils.watchers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class PhoneNumberTextWatcher(private val editText: EditText) : TextWatcher {

    private var isFormatting: Boolean = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        if (isFormatting || s == null) return

        isFormatting = true

        val digits = s.replace(Regex("[^\\d]"), "")
        val builder = StringBuilder()

        if (digits.isNotEmpty() && !digits.startsWith("7") && !digits.startsWith("8")) {
            builder.append("7")
        } else {
            builder.append(digits.substring(0, 1))
        }

        if (digits.length > 1) {
            builder.append(digits.substring(1))
        }

        val formatted = formatPhone(builder.toString())
        editText.setText(formatted)
        editText.setSelection(formatted.length)

        isFormatting = false
    }

    private fun formatPhone(digits: String): String {
        val sb = StringBuilder()
        sb.append("+7 ")

        val cleaned = if (digits.startsWith("7")) digits.substring(1) else digits

        val length = cleaned.length

        if (length >= 1) sb.append("(")
        if (length >= 3) {
            sb.append(cleaned.substring(0, 3))
            sb.append(")")
        } else if (length >= 1) {
            sb.append(cleaned.substring(0, length))
        }

        if (length > 3) {
            sb.append(" ")
            if (length >= 6) {
                sb.append(cleaned.substring(3, 6))
            } else {
                sb.append(cleaned.substring(3))
            }
        }

        if (length > 6) {
            sb.append("-")
            if (length >= 8) {
                sb.append(cleaned.substring(6, 8))
            } else {
                sb.append(cleaned.substring(6))
            }
        }

        if (length > 8) {
            sb.append("-")
            if (length >= 10) {
                sb.append(cleaned.substring(8, 10))
            } else {
                sb.append(cleaned.substring(8))
            }
        }

        return sb.toString()
    }
}
