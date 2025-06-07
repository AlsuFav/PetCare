package ru.fav.petcare.appointment.add.service.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.fav.petcare.appointment.add.service.databinding.ItemSelectServiceBinding
import ru.fav.petcare.domain.model.ServiceModel

class ServicesAdapter (
    private val onServiceClick: (ServiceModel) -> Unit
) : RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder>() {

    private var services = mutableListOf<ServiceModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = ItemSelectServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(binding)
    }

    override fun getItemCount(): Int = services.size

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.bind(services[position])
    }

    inner class ServiceViewHolder(private val binding: ItemSelectServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(service: ServiceModel) {
            binding.textViewServiceName.text = service.name
            binding.textViewPrice.text = service.prices.first().price.toString()

            itemView.setOnClickListener {
                onServiceClick(service)
            }
        }
    }

    fun updateData(list: MutableList<ServiceModel>) {
        services.clear()
        services.addAll(list)
        notifyDataSetChanged()
    }
}
