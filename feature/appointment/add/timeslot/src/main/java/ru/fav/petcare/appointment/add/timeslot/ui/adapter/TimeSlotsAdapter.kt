package ru.fav.petcare.appointment.add.timeslot.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.fav.petcare.appointment.add.timeslot.databinding.ItemTimeslotBinding
import ru.fav.petcare.domain.model.TimeSlotModel

class TimeSlotsAdapter(
    private val onTimeSlotClick: (TimeSlotModel) -> Unit
) : RecyclerView.Adapter<TimeSlotsAdapter.TimeSlotViewHolder>() {

    private var timeSlots = listOf<TimeSlotModel>()

    inner class TimeSlotViewHolder(private val binding: ItemTimeslotBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(timeSlot: TimeSlotModel) {
            binding.textViewTime.text = timeSlot.time
            binding.textViewGroomer.text = timeSlot.groomerName
            
            itemView.setOnClickListener {
                onTimeSlotClick(timeSlot)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        val binding = ItemTimeslotBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return TimeSlotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
        holder.bind(timeSlots[position])
    }

    override fun getItemCount() = timeSlots.size

    fun updateData(newTimeSlots: List<TimeSlotModel>) {
        timeSlots = newTimeSlots
        notifyDataSetChanged()
    }
}