package ru.fav.petcare.home.ui.util

import androidx.recyclerview.widget.DiffUtil
import ru.fav.petcare.domain.model.FaqModel

class FaqDiffUtil(
    private var oldList: List<FaqModel>,
    private var newList: List<FaqModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].id == (newList[newItemPosition]).id)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}