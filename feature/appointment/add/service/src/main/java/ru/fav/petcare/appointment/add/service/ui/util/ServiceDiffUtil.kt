package ru.fav.petcare.appointment.add.service.ui.util

import androidx.recyclerview.widget.DiffUtil
import ru.fav.petcare.domain.model.ServiceModel

class ServiceDiffUtil (
    private var oldList: List<ServiceModel>,
    private var newList: List<ServiceModel>
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
