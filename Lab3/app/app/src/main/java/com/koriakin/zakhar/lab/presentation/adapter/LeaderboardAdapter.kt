package com.koriakin.zakhar.lab.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.koriakin.zakhar.lab.R
import com.koriakin.zakhar.lab.databinding.ItemPlayerBinding
import com.koriakin.zakhar.lab.presentation.model.PlayerUiModel

class LeaderboardAdapter : ListAdapter<PlayerUiModel, LeaderboardAdapter.PlayerViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val binding = ItemPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlayerViewHolder(private val binding: ItemPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PlayerUiModel) {
            val context = binding.root.context
            binding.textRank.text = context.getString(R.string.label_rank_format, item.rank)
            binding.textPlayerName.text = item.name
            binding.textScore.text = item.score.toString()

            val rankColor = when (item.rank) {
                1 -> ContextCompat.getColor(context, R.color.rank_gold)
                2 -> ContextCompat.getColor(context, R.color.rank_silver)
                3 -> ContextCompat.getColor(context, R.color.rank_bronze)
                else -> ContextCompat.getColor(context, R.color.rank_default)
            }
            binding.textRank.setTextColor(rankColor)
            binding.textScore.setTextColor(rankColor)

            val bgColor = if (item.isMe) {
                ContextCompat.getColor(context, R.color.item_me_background)
            } else {
                ContextCompat.getColor(context, R.color.item_default_background)
            }
            binding.root.setBackgroundColor(bgColor)

            binding.textPlayerName.setTypeface(
                null,
                if (item.isMe) android.graphics.Typeface.BOLD else android.graphics.Typeface.NORMAL
            )
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PlayerUiModel>() {
        override fun areItemsTheSame(oldItem: PlayerUiModel, newItem: PlayerUiModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PlayerUiModel, newItem: PlayerUiModel) =
            oldItem == newItem
    }
}
