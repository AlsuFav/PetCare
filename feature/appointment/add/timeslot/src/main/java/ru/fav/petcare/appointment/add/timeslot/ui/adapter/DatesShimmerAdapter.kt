package ru.fav.petcare.appointment.add.timeslot.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.fav.petcare.appointment.add.timeslot.R

class DatesShimmerAdapter : RecyclerView.Adapter<DatesShimmerAdapter.ShimmerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_date_shimmer, parent, false)
        return ShimmerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) = Unit

    override fun getItemCount(): Int = 4

    class ShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}