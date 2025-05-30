package ru.fav.petcare.appointment.all.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.fav.petcare.appointment.all.R

class AppointmentsShimmerAdapter : RecyclerView.Adapter<AppointmentsShimmerAdapter.ShimmerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment_shimmer, parent, false)
        return ShimmerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) = Unit

    override fun getItemCount(): Int = 4

    class ShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
