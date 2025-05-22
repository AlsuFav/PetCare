package ru.fav.petcare.util.extension

import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.fav.petcare.presentation.R

fun Fragment.showErrorDialog(
    message: String,
    icon: Int = R.drawable.ic_error,
    title: String = getString(R.string.error_title),
    positiveAction: (() -> Unit)? = null
) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setIcon(icon)
        .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
            positiveAction?.invoke()
            dialog.dismiss()
        }
        .create()
        .show()
}

fun Fragment.showConfirmationDialog(
    message: String,
    icon: Int = R.drawable.ic_warning,
    title: String = getString(R.string.confirmation_title),
    positiveText: String = getString(R.string.yes),
    negativeText: String = getString(R.string.no),
    positiveAction: () -> Unit,
    negativeAction: (() -> Unit)? = null,
    cancelable: Boolean = true
) {
    MaterialAlertDialogBuilder(requireContext())
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