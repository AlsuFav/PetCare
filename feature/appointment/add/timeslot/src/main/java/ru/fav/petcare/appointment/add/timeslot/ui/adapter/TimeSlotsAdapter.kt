package ru.fav.petcare.appointment.add.timeslot.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.fav.petcare.appointment.add.timeslot.databinding.ItemTimeslotBinding
import ru.fav.petcare.appointment.add.timeslot.ui.util.TimeSlotDiffUtil
import ru.fav.petcare.domain.model.AppointmentModel
import ru.fav.petcare.domain.model.TimeSlotModel

class TimeSlotsAdapter(
    private val onTimeSlotClick: (TimeSlotModel) -> Unit
) : RecyclerView.Adapter<TimeSlotsAdapter.TimeSlotViewHolder>() {

    private var timeSlots = mutableListOf<TimeSlotModel>()

    inner class TimeSlotViewHolder(private val binding: ItemTimeslotBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var currentTimeSlot: TimeSlotModel? = null

        init {
            binding.root.setOnClickListener {
                currentTimeSlot?.let { onTimeSlotClick(it) }
            }
        }
        
        fun bind(timeSlot: TimeSlotModel) {
            currentTimeSlot = timeSlot
            binding.textViewTime.text = timeSlot.time
            binding.textViewGroomer.text = timeSlot.groomerName
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
        val diff = TimeSlotDiffUtil(oldList = timeSlots, newList = newTimeSlots)
        val diffResult = DiffUtil.calculateDiff(diff)
        timeSlots.clear()
        timeSlots.addAll(newTimeSlots)
        diffResult.dispatchUpdatesTo(this)
    }
}