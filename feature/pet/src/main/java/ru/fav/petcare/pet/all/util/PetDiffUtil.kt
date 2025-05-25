package ru.fav.petcare.pet.all.util

import androidx.recyclerview.widget.DiffUtil
import ru.fav.petcare.domain.model.PetModel

class PetDiffUtil (
    private var oldList: List<PetModel>,
    private var newList: List<PetModel>
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
