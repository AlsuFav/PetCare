package ru.fav.petcare.presentation.util

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.fav.petcare.presentation.R


object ErrorDialogUtil {
    fun showErrorDialog(
        context: Context,
        message: String,
        icon: Int = R.drawable.ic_error,
        title: String = context.getString(R.string.error_title),
        positiveAction: (() -> Unit)? = null
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setIcon(icon)
            .setPositiveButton(context.getString(R.string.ok)) { dialog, _ ->
                positiveAction?.invoke()
                dialog.dismiss()
            }
            .create()
            .show()
    }

    fun showConfirmationDialog(
        context: Context,
        message: String,
        icon: Int = R.drawable.ic_warning,
        title: String = context.getString(R.string.confirmation_title),
        positiveText: String = context.getString(R.string.yes),
        negativeText: String = context.getString(R.string.no),
        positiveAction: () -> Unit,
        negativeAction: (() -> Unit)? = null,
        cancelable: Boolean = true
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setIcon(icon)
            .setCancelable(cancelable)
            .setPositiveButton(positiveText) { dialog, _ ->
                positiveAction()
                dialog.dismiss()
            }
            .setNegativeButton(negativeText) { dialog, _ ->
                negativeAction?.invoke()
                dialog.dismiss()
            }
            .setOnCancelListener {
                negativeAction?.invoke()
            }
            .create()
            .show()
    }
}
