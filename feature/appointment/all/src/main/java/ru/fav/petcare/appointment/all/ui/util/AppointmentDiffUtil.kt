package ru.fav.petcare.appointment.all.ui.util

import androidx.recyclerview.widget.DiffUtil
import ru.fav.petcare.domain.model.AppointmentModel

class AppointmentDiffUtil (
    private var oldList: List<AppointmentModel>,
    private var newList: List<AppointmentModel>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].id == (newList[newItemPosition]).id)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
