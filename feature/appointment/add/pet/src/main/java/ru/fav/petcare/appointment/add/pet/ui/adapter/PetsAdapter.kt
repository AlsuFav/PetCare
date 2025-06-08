package ru.fav.petcare.appointment.add.pet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.fav.petcare.domain.model.PetModel
import ru.fav.petcare.appointment.add.pet.databinding.ItemSelectPetBinding
import ru.fav.petcare.appointment.add.pet.ui.util.PetDiffUtil
import ru.fav.petcare.domain.model.ServiceModel
import ru.fav.petcare.presentation.R

class PetsAdapter (
    private val onPetClick: (PetModel) -> Unit
) : RecyclerView.Adapter<PetsAdapter.PetViewHolder>() {

    private var pets = mutableListOf<PetModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val binding = ItemSelectPetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PetViewHolder(binding)
    }

    override fun getItemCount(): Int = pets.size

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        holder.bind(pets[position])
    }

    inner class PetViewHolder(private val binding: ItemSelectPetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var currentPet: PetModel? = null

        init {
            binding.root.setOnClickListener {
                currentPet?.let { onPetClick(it) }
            }
        }


        fun bind(pet: PetModel) {
            currentPet = pet
            binding.textViewPetName.text = pet.name

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
        }
    }

    fun updateData(list: MutableList<PetModel>) {
        val diff = PetDiffUtil(oldList = pets, newList = list)
        val diffResult = DiffUtil.calculateDiff(diff)
        pets.clear()
        pets.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}
