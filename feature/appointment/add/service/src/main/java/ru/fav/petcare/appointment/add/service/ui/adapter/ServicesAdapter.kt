package ru.fav.petcare.appointment.add.service.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.fav.petcare.appointment.add.service.databinding.ItemSelectServiceBinding
import ru.fav.petcare.appointment.add.service.ui.util.ServiceDiffUtil
import ru.fav.petcare.domain.model.AppointmentModel
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

        private var currentService: ServiceModel? = null

        init {
            binding.root.setOnClickListener {
                currentService?.let { onServiceClick(it) }
            }
        }

        fun bind(service: ServiceModel) {
            currentService = service
            binding.textViewServiceName.text = service.name
            binding.textViewPrice.text = service.prices.first().price.toString()
        }
    }

    fun updateData(list: MutableList<ServiceModel>) {
        val diff = ServiceDiffUtil(oldList = services, newList = list)
        val diffResult = DiffUtil.calculateDiff(diff)
        services.clear()
        services.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}
