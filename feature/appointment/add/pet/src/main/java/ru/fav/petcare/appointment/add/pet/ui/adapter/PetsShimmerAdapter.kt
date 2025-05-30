package ru.fav.petcare.appointment.add.pet.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.fav.petcare.appointment.add.pet.R

class PetsShimmerAdapter : RecyclerView.Adapter<PetsShimmerAdapter.ShimmerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_select_pet_shimmer, parent, false)
        return ShimmerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) = Unit

    override fun getItemCount(): Int = 4

    class ShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
