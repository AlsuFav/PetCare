package ru.fav.petcare.service.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.fav.petcare.domain.model.ServiceModel
import ru.fav.petcare.service.databinding.ItemServiceBinding

class ServicesAdapter() :
    RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder>() {

    private var services = mutableListOf<ServiceModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ItemServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(binding)
    }

    override fun getItemCount(): Int = services.size

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(services[position])
    }

    inner class ServiceViewHolder(private val binding: ItemServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val servicePricesAdapter = ServicePricesAdapter()

        init {
            binding.servicePricesRecyclerView.apply {
                adapter = servicePricesAdapter
            }
        }

        fun bind(service: ServiceModel) {
            binding.textViewServiceName.text = service.name
            binding.textViewServiceDescription.text = service.description

            servicePricesAdapter.updateData(service.prices)
        }
    }

    fun updateData(list: List<ServiceModel>) {
        services.clear()
        services.addAll(list)
        notifyDataSetChanged()
    }
}