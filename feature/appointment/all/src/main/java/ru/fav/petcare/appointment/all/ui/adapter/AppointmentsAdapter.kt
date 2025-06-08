package ru.fav.petcare.appointment.all.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.fav.petcare.domain.model.PetModel
import ru.fav.petcare.appointment.all.databinding.ItemAppointmentBinding
import ru.fav.petcare.appointment.all.ui.util.AppointmentDiffUtil
import ru.fav.petcare.domain.model.AppointmentModel

class AppointmentsAdapter(
    private val onAppointmentClick: (AppointmentModel) -> Unit
) : RecyclerView.Adapter<AppointmentsAdapter.AppointmentViewHolder>() {

    private var appointments = mutableListOf<AppointmentModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemAppointmentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AppointmentViewHolder(binding)
    }

    override fun getItemCount(): Int = appointments.size

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        holder.bind(appointments[position])
    }

    inner class AppointmentViewHolder(private val binding: ItemAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var currentAppointment: AppointmentModel? = null

        init {
            binding.root.setOnClickListener {
                currentAppointment?.let { onAppointmentClick(it) }
            }
        }

        fun bind(appointment: AppointmentModel) {
            currentAppointment = appointment
            binding.textViewAppointmentDate.text = appointment.date
            binding.textViewPetName.text = appointment.petName
        }
    }

    fun updateData(newList: List<AppointmentModel>) {
        appointments.clear()
        appointments.addAll(newList)
        notifyDataSetChanged()
    }
}