package ru.fav.petcare.pet.all.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.fav.petcare.domain.model.PetModel
import ru.fav.petcare.pet.all.ui.util.PetDiffUtil
import ru.fav.petcare.pet.all.databinding.ItemPetBinding
import ru.fav.petcare.presentation.R

class PetsAdapter (
    private val onPetClick: (PetModel) -> Unit
) : RecyclerView.Adapter<PetsAdapter.PetViewHolder>() {

    private var pets = mutableListOf<PetModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val binding = ItemPetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PetViewHolder(binding)
    }

    override fun getItemCount(): Int = pets.size

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        holder.bind(pets[position])
    }

    inner class PetViewHolder(private val binding: ItemPetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pet: PetModel) {
            binding.textViewPetName.text = pet.name
            binding.textViewBirthDate.text = pet.birthDate

            val placeholder =
                if (pet.species.equals("кошка", ignoreCase = true)) {
                    R.drawable.ic_cat
                } else if (pet.species.equals("собака", ignoreCase = true)) {
                    R.drawable.ic_dog
                } else {
                    null
                }

            pet.imagePath.let { url ->
                Glide.with(binding.imageViewPetPhoto.context)
                    .load(url)
                    .error(placeholder)
                    .into(binding.imageViewPetPhoto)
            }

            itemView.setOnClickListener {
                onPetClick(pet)
            }
        }
    }

    fun updateData(list: MutableList<PetModel>) {
        val diff = PetDiffUtil(oldList = pets, newList = list)
        val diffResult = DiffUtil.calculateDiff(diff)
        pets.clear()
        pets.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun removeItem(pet: PetModel): Int? {
        val position = pets.indexOfFirst { it.id == pet.id }
        if (position != -1) {
            pets.removeAt(position)
            return position
        }
        return null
    }

    fun getItem(position: Int): PetModel? {
        return pets.getOrNull(position)
    }
}
