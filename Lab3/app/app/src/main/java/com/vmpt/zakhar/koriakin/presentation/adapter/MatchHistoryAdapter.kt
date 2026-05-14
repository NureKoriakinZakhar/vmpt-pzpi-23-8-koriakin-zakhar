package com.vmpt.zakhar.koriakin.presentation.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vmpt.zakhar.koriakin.R
import com.vmpt.zakhar.koriakin.databinding.ItemMatchHistoryBinding
import com.vmpt.zakhar.koriakin.domain.model.MatchOutcome
import com.vmpt.zakhar.koriakin.domain.model.MatchRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MatchHistoryAdapter : ListAdapter<MatchRecord, MatchHistoryAdapter.MatchViewHolder>(Diff) {

    private val formatter = SimpleDateFormat("d MMM yyyy, HH:mm", Locale.forLanguageTag("uk-UA"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMatchHistoryBinding.inflate(inflater, parent, false)
        return MatchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MatchViewHolder(
        private val binding: ItemMatchHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MatchRecord) {
            val ctx = binding.root.context
            val line = formatter.format(Date(item.finishedAtMillis))
            binding.textDate.text = line
            when (item.outcome) {
                MatchOutcome.X_WINS -> {
                    binding.textResult.text = ctx.getString(R.string.match_line_x)
                    binding.chipResult.text = ctx.getString(R.string.match_chip_x)
                    binding.chipResult.setTextColor(ContextCompat.getColor(ctx, R.color.mark_x))
                    binding.chipResult.setChipBackgroundColor(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(ctx, R.color.brand_primary_container)
                        )
                    )
                }
                MatchOutcome.O_WINS -> {
                    binding.textResult.text = ctx.getString(R.string.match_line_o)
                    binding.chipResult.text = ctx.getString(R.string.match_chip_o)
                    binding.chipResult.setTextColor(ContextCompat.getColor(ctx, R.color.mark_o))
                    binding.chipResult.setChipBackgroundColor(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(ctx, R.color.brand_primary_container)
                        )
                    )
                }
                MatchOutcome.DRAW -> {
                    binding.textResult.text = ctx.getString(R.string.match_line_draw)
                    binding.chipResult.text = ctx.getString(R.string.match_chip_draw)
                    binding.chipResult.setTextColor(ContextCompat.getColor(ctx, R.color.text_secondary))
                    binding.chipResult.setChipBackgroundColor(
                        ColorStateList.valueOf(
                            ContextCompat.getColor(ctx, R.color.chip_draw_bg)
                        )
                    )
                }
            }
        }
    }

    private object Diff : DiffUtil.ItemCallback<MatchRecord>() {
        override fun areItemsTheSame(oldItem: MatchRecord, newItem: MatchRecord): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MatchRecord, newItem: MatchRecord): Boolean {
            return oldItem == newItem
        }
    }
}
