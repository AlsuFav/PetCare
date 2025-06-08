package ru.fav.petcare.appointment.add.timeslot.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.fav.petcare.appointment.add.timeslot.databinding.ItemDateBinding
import ru.fav.petcare.appointment.add.timeslot.ui.util.DateDiffUtil
import ru.fav.petcare.domain.model.TimeSlotModel

class DatesAdapter(
    private val onTimeSlotClick: (TimeSlotModel) -> Unit
) : RecyclerView.Adapter<DatesAdapter.DateSlotViewHolder>() {

    private var dateGroups = mutableListOf<Pair<String, List<TimeSlotModel>>>()

    inner class DateSlotViewHolder(private val binding: ItemDateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val timeSlotsAdapter = TimeSlotsAdapter(onTimeSlotClick)

        init {
            binding.recyclerViewTimeSlots.apply {
                adapter = timeSlotsAdapter
            }
        }

        fun bind(dateGroup: Pair<String, List<TimeSlotModel>>) {
            binding.textViewDate.text = dateGroup.first
            timeSlotsAdapter.updateData(dateGroup.second)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateSlotViewHolder {
        val binding = ItemDateBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return DateSlotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateSlotViewHolder, position: Int) {
        holder.bind(dateGroups[position])
    }

    override fun getItemCount(): Int = dateGroups.size

    fun updateData(newGroups: MutableList<Pair<String, List<TimeSlotModel>>>) {
        val diff = DateDiffUtil(oldList = dateGroups, newList = newGroups)
        val diffResult = DiffUtil.calculateDiff(diff)
        dateGroups.clear()
        dateGroups.addAll(newGroups)
        diffResult.dispatchUpdatesTo(this)
    }
}