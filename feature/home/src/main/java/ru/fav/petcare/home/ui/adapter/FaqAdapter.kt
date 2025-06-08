package ru.fav.petcare.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import ru.fav.petcare.domain.model.FaqModel
import ru.fav.petcare.home.databinding.ItemFaqBinding
import ru.fav.petcare.home.ui.util.FaqDiffUtil

class FaqAdapter() : RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {

    private var faqs = mutableListOf<FaqModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val binding = ItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FaqViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        holder.bind(faqs[position])
    }

    override fun getItemCount(): Int = faqs.size

    inner class FaqViewHolder(private val binding: ItemFaqBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(faqItem: FaqModel) {
            binding.tvQuestion.text = faqItem.question
            binding.tvAnswer.text = faqItem.answer

            binding.tvAnswer.isVisible = false
            binding.ivExpandIndicator.rotation = 0f

            binding.questionLayout.setOnClickListener {
                TransitionManager.beginDelayedTransition(
                    binding.root.parent as ViewGroup,
                    AutoTransition()
                )

                binding.tvAnswer.isVisible = ! binding.tvAnswer.isVisible
                binding.ivExpandIndicator.animate().rotation(if (binding.tvAnswer.isVisible) 180f else 0f).setDuration(200).start()
            }
        }
    }

    fun updateData(list: MutableList<FaqModel>) {
        val diff = FaqDiffUtil(oldList = faqs, newList = list)
        val diffResult = DiffUtil.calculateDiff(diff)
        faqs.clear()
        faqs.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}
