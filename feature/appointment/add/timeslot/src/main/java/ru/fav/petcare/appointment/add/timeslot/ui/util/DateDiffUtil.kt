package ru.fav.petcare.appointment.add.timeslot.ui.util

import androidx.recyclerview.widget.DiffUtil
import ru.fav.petcare.domain.model.TimeSlotModel

class DateDiffUtil (
    private var oldList: List<Pair<String, List<TimeSlotModel>>>,
    private var newList: List<Pair<String, List<TimeSlotModel>>>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].first == (newList[newItemPosition]).first)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
