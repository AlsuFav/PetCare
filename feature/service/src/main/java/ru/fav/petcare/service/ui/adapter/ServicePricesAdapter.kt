package ru.fav.petcare.service.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.fav.petcare.domain.model.ServicePriceModel
import ru.fav.petcare.service.R
import ru.fav.petcare.service.databinding.ItemServicePriceBinding

class ServicePricesAdapter() :
    RecyclerView.Adapter<ServicePricesAdapter.PriceViewHolder>() {

    private var servicePrices = listOf<ServicePriceModel>()

    class PriceViewHolder(private val binding: ItemServicePriceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(price: ServicePriceModel) {
            val context = binding.root.context
            val speciesText = if (price.breedType.isNullOrEmpty()) {
                context.getString(R.string.service_price_species_only_format, price.species)
            } else {
                context.getString(R.string.service_price_species_breed_format, price.species, price.breedType)
            }

            binding.textViewSpeciesBreed.text = speciesText
            binding.textViewPriceValue.text = price.price.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        val binding = ItemServicePriceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return PriceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        holder.bind(servicePrices[position])
    }

    override fun getItemCount(): Int = servicePrices.size

    fun updateData(newServicePrices: List<ServicePriceModel>) {
        servicePrices = newServicePrices
        notifyDataSetChanged()
    }
}