package ru.fav.petcare.service.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.fav.petcare.service.R

class ServicesShimmerAdapter : RecyclerView.Adapter<ServicesShimmerAdapter.ShimmerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service_shimmer, parent, false)
        return ShimmerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) = Unit

    override fun getItemCount(): Int = 3

    class ShimmerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
